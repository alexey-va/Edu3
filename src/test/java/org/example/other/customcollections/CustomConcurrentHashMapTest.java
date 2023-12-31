package org.example.other.customcollections;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class CustomConcurrentHashMapTest {

    static CustomConcurrentHashMap<String, String> map = new CustomConcurrentHashMap<>();
    static ConcurrentHashMap<String, String> map2 = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        map = new CustomConcurrentHashMap<>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        map2 = new ConcurrentHashMap<>();
        map2.put("1", "one");
        map2.put("2", "two");
        map2.put("3", "three");
    }

    @Test
    void put() {
        map.put("4", "four");
        assertEquals(4, map.size());
    }

    @Test
    void getCustom() {
        for(int i =0;i<100000000;i++){
            map.get("1");
        }
    }

    @Test
    void getConcurrent() {
        for(int i =0;i<100000000;i++){
            map2.get("1");
        }
    }

    @Test
    void remove() {
        map.remove("1");
        assertEquals(2, map.size());
    }

    @Test
    void size() {
        assertEquals(3, map.size());
    }

    @Test
    void isEmpty() {
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    void containsKey() {
        assertTrue(map.containsKey("1"));
        assertFalse(map.containsKey("4"));
    }

    @Test
    void containsValue() {
        assertTrue(map.containsValue("one"));
        assertFalse(map.containsValue("four"));
    }

    @Test
    void clear() {
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    void keySet() {
        log.info(map.keySet());
        assertEquals(3, map.keySet().size());
    }

    @Test
    void values() {
        log.info(map.values());
        assertEquals(3, map.values().size());
    }

    @Test
    void entrySet() {
        map.clear();
        for(int i =0;i<100;i++){
            map.put(String.valueOf(i), String.valueOf(i));
        }
        log.info(map.entrySet());
        assertEquals(100, map.entrySet().size());
    }

    @Test
    void putAll() {
        CustomConcurrentHashMap<String, String> map2 = new CustomConcurrentHashMap<>();
        map2.put("4", "four");
        map2.put("5", "five");
        map2.put("6", "six");
        map.putAll(map2);
        assertEquals(6, map.size());
    }

    @Test
    void putIfAbsent() {
        map.putIfAbsent("4", "four");
        assertEquals(4, map.size());
        map.putIfAbsent("1", "one");
        assertEquals(4, map.size());
    }


    @Test
    void concurrentThreadTest(){
        map2.clear();

        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();
        int count = 1000000;
        for(int i =0;i<count;i++){
            int finalI = i;
            futures.add(service.submit(()->{
                map2.put(String.valueOf(finalI), String.valueOf(finalI));
            }));
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                log.error(e);
            }
        }
        assertEquals(count, map2.size());
    }

    @Test
    void threadTest(){
        map.clear();

        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();
        int count = 1000000;
        for(int i =0;i<count;i++){
            int finalI = i;
            futures.add(service.submit(()->{
                map.put(String.valueOf(finalI), String.valueOf(finalI));
            }));
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                log.error(e);
            }
        }
        assertEquals(count, map.size());

    }

}