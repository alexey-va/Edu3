package org.example.other.test;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

@NoArgsConstructor
@Log4j2
public class KafkaMessageBroker {


    private Properties properties;
    private Producer<String, Data> producer;
    private Consumer<String, Data> consumer;
    private final String topic = "test";


    public void init() {
        properties = new Properties();
        properties.put("bootstrap.servers", ".ru:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", KafkaMessageBroker.DataSerializer.class.getName());
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", KafkaMessageBroker.DataDeserializer.class.getName());
        properties.put("group.id", "test");
        log.info("Initializing KafkaMessageBroker");
        producer = new KafkaProducer<>(properties);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(topic));
    }

    public void produce(int counter) {
        ProducerRecord<String, Data> record = new ProducerRecord<>(topic, new Random().nextInt(0, 4) + "",
                new Data(
                        "" + new Random().nextInt(),
                        "" + new Random().nextInt()
                ));
        producer.send(record, (recordMetadata, e) -> {
            System.out.println("producing: " + recordMetadata + " " + e);
        });
    }

    public void consume(int counter) {
        consumer.poll(Duration.of(200, ChronoUnit.MILLIS)).forEach(record -> {
            System.out.println("got: " + record.key() + " " + record.value() + " " + record.partition() + " " + counter);
        });
    }

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String key;
        private String value;
        //private Date date;
    }

    public static class DataSerializer implements Serializer<Data> {

        @Override
        public byte[] serialize(String s, Data data) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                outputStream.write(data.getKey().getBytes());
                outputStream.write(-1);
                outputStream.write(data.getValue().getBytes());
                System.out.println(Arrays.toString(outputStream.toByteArray()));
                return outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class DataDeserializer implements Deserializer<Data> {

        @Override
        public Data deserialize(String s, byte[] bytes) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
                Data data = new Data();
                StringBuilder builder = new StringBuilder();
                for (byte b : inputStream.readAllBytes()) {
                    if (b == -1) {
                        data.setKey(builder.toString());
                        builder = new StringBuilder();
                        continue;
                    }
                    builder.append((char) b);
                }
                data.setValue(builder.toString());
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
