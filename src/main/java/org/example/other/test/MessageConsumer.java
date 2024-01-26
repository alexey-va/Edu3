package org.example.other.test;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.other.events.base.EventManager;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class MessageConsumer {

    public final String topic;
    Thread thread;

    Consumer<String, String> consumer;

    public void init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "rus-crafting.ru:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test");

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));

        thread = new Thread(() -> {
            while (true) {
                StreamSupport.stream(consumer.poll(Duration.ofMillis(1000)).spliterator(), false)
                        .map(record -> new MessageEvent(record.topic(), record.key(), record.value()))
                        .forEach(EventManager.getInstance()::run);
                System.out.println("Listening...");
            }
        });
        thread.start();
    }
}
