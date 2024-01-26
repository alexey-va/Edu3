package org.example.other.test;

import lombok.Builder;
import org.example.other.network.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class RequestManager {

    static Map<UUID, DataRequest> requestMap = new ConcurrentHashMap<>();
    public static MessageProducer reqProducer;


    public static void initCleanTask(){
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            requestMap.entrySet().removeIf(e -> e.getValue().future.isDone());
        }, 100, TimeUnit.MILLISECONDS);
    }


    public static CompletableFuture<List<Integer>> makeRequest(int bound){
        String data = JsonParser.toJson(Map.of("number", bound));
        CompletableFuture<List<Integer>> future = new CompletableFuture<>();

        int count = 10;

        DataRequest request = DataRequest.builder()
                .future(future)
                .tasks(count)
                .uuid(UUID.randomUUID())
                .timeout(10000)
                .build();
        requestMap.put(request.uuid, request);
        for(int i=0;i<count;i++) reqProducer.send(request.uuid.toString(), data);

        return future;
    }

    public static void processResponse(UUID uuid, int number){
        if(!requestMap.containsKey(uuid)) return;
        DataRequest request = requestMap.get(uuid);
        request.responses.add(number);
        if(request.responses.size() == request.tasks) {
            request.future.complete(request.responses);
            requestMap.remove(uuid);
        }
    }



    @Builder
    static class DataRequest{
        UUID uuid;
        int tasks;
        long timeout;
        @Builder.Default
        long timestamp = System.currentTimeMillis();
        CompletableFuture<List<Integer>> future;
        @Builder.Default
        List<Integer> responses = new CopyOnWriteArrayList<>();
    }
}
