package org.example.other.test;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

@NoArgsConstructor
@Log4j2
public class KafkaMessageBroker {


    private Properties properties;
    private Producer<String, String> producer;
    private Consumer<String, String> consumer;
    private final String topic = "test";


    public void init() {
        properties = new Properties();
        properties.put("bootstrap.servers", "rus-crafting.ru:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        log.info("Initializing KafkaMessageBroker");
        producer = new KafkaProducer<>(properties);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(topic));
        System.out.println("Done init");
    }

    public void produce(int counter) {
        for (int i = 0; i < counter; i++) {
            String message = "Message " + i;
            try {
                producer.send(new ProducerRecord<>(topic, message)).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("Sent message: "+message);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(String key, String message) {
        producer.send(new ProducerRecord<>(topic, key, message));
    }

    public void consume(int counter) {
        while (true) {
            consumer.poll(Duration.ofSeconds(1))
                    .forEach(System.out::println);
            System.out.println("Done listening");
        }

    }

    public void echo(){
        while (true) {
            StreamSupport.stream(consumer.poll(Duration.ofSeconds(1)).spliterator(), false)
                    //.peek(System.out::println)
                    .filter(record -> !record.key().equals("echo"))
                    .map(ConsumerRecord::value)
                    .peek(System.out::println)
                    .map(msg -> msg+" echo")
                    .forEach(msg -> send("echo", msg));
        }
    }


}
