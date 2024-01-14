package org.example.other.concur.collections;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class AllCollsTest {

    private static AllColls allColls = new AllColls();

    @Test
    public void bench() throws IOException {
        org.openjdk.jmh.Main.main(new String[]{});
    }

    public static Stream<Map<String, String>> getMaps(){
        return Stream.of(
                new HashMap<>(),
                allColls.concurrentHashMap,
                allColls.concurrentSkipListMap
        );
    }

    public static Stream<List<String>> getArrs(){
        return Stream.of(
                new ArrayList<>(),
                allColls.copyOnWriteArrayList
        );
    }

    @ParameterizedTest
    @MethodSource("getMaps")
    public void testConcurHashMap(Map<String, String> map){
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();


        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            int finalI = i;
            futures.add(service.submit(() -> {
                map.put(finalI +"", "value");
            }));
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("Impl: "+map.getClass().getSimpleName()+" "+map.size()+"/1000000");
    }

    @ParameterizedTest
    @MethodSource("getArrs")
    public void testCopyArray(List<String> list){
        List<Future> futures = new ArrayList<>();
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            futures.add(service.submit(() -> {
                list.add(finalI +"");
            }));
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("Impl: "+list.getClass().getSimpleName()+" "+list.size()+"/10000");
    }


}