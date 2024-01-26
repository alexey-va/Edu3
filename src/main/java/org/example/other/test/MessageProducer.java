package org.example.other.test;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class MessageProducer {

    public final String topic;

    Producer<String, String> producer;

    public void init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "rus-crafting.ru:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        producer = new KafkaProducer<>(properties);
    }

    public void send(String key, String message){
        System.out.println("Sending messsage: "+key+" "+message);
        producer.send(new ProducerRecord<>(topic, key, message));
    }

}
