package org.example.reflections;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.other.network.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FileReader {

    private final File file;


    public <T> T read(Class<T> clazz){
        var map = readFile();
        try {
            T t = clazz.getConstructor().newInstance();
            for(Field f : clazz.getDeclaredFields()){
                f.setAccessible(true);
                if(map.containsKey(f.getName())){
                    f.set(t, map.get(f.getName()));
                }
            }
            return t;
        } catch (Exception e){
            throw  new RuntimeException("Could not find no-args constructor for "+clazz.getSimpleName());
        }
    }

    @SneakyThrows
    private Map<String, Object> readFile(){
        try(var reader = new BufferedReader(new java.io.FileReader(file))){
            return JsonParser.parse(reader.lines().collect(Collectors.joining()));
        }
    }

}
