package org.example.other.test;

import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class KafkaMessageBrokerTest {


    @Test
    void testKafka() throws ExecutionException, InterruptedException {
        KafkaMessageBroker broker = new KafkaMessageBroker();
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
        broker.init();


        Future<?> future =  service.submit(() -> {
            for(int i=0;i<1000;i++) {
                String message = LoremIpsum.getInstance().getCountry();
                broker.send("origin", message);
                System.out.println("Sent message: "+message);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.submit(() -> broker.echo());
        future.get();
    }

}