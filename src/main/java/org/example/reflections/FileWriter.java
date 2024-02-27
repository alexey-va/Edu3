package org.example.reflections;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.other.network.JsonParser;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.reflections.Utils.fieldsOf;

@AllArgsConstructor
public class FileWriter {

    private final File file;

    @SneakyThrows
    public void write(Object o) {
        Map<String, Object> map = objectToMap(o);
        String json = JsonParser.toJson(map);
        if(!file.exists())file.createNewFile();
        try(var writer = new BufferedWriter(new java.io.FileWriter(file))){
            writer.write(json);
        }
    }

    private Map<String, Object> objectToMap(Object o) {
        return Arrays.stream(o.getClass().getDeclaredFields())
                .map(f -> {
                    try {
                        f.setAccessible(true);
                        return Map.entry(f.getName(), f.get(o) == null ? "null" : f.get(o));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
