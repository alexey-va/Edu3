package org.example.other.concur.collections;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    @Test
    void testCache() throws InterruptedException {
        CacheManager manager = CacheManager.instance();
        manager.createCache("cache", 1000);
        manager.put("cache", "1", "asd");
        assertEquals("asd", manager.get("cache", "1"));
        manager.put("cache", "1", "asd2");
        assertEquals("asd2", manager.get("cache", "1"));

        Thread.sleep(2000);
        assertNull(manager.get("cache", "1"));

    }

    @Test
    void testCache2(){
        CacheManager manager = CacheManager.instance();
        manager.createCache("cache", 1000);
        manager.put("cache", "1", "asd");
        assertEquals("asd", manager.get("cache", "1"));
        manager.put("cache", "1", "asd2");
        assertEquals("asd2", manager.get("cache", "1"));
        manager.remove("cache", "1");
        assertNull(manager.get("cache", "1"));
    }

    @Test
    void testCache3(){
        ExecutorService service = Executors.newFixedThreadPool(10);
        CacheManager manager = CacheManager.instance();
        manager.createCache("cache", 1000);

        for(int i = 0; i < 100; i++){
            service.submit(() -> {
                manager.put("cache", "1", "asd");
                assertEquals("asd", manager.get("cache", "1"));
                manager.put("cache", "1", "asd2");
                assertEquals("asd2", manager.get("cache", "1"));
                manager.remove("cache", "1");
                assertNull(manager.get("cache", "1"));
            });
        }
    }

}