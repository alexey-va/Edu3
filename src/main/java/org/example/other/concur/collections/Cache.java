package org.example.other.concur.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Builder
@AllArgsConstructor
public class Cache {

    @Builder.Default
    private Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    @Builder.Default
    long ttl = 10000;
    String name;
    ScheduledExecutorService service;
    Future<?> cleanupTask = null;
    @Builder.Default
    boolean preciseTtl = false;

    public Cache(String name, long ttl, ScheduledExecutorService service, boolean preciseTtl) {
        this.name = name;
        this.ttl = ttl;
        this.service=service;
        this.preciseTtl = preciseTtl;

        initCleanupTask();
    }

    void put(String name, Object o){
        Future<?> removeTask = null;
        if(preciseTtl) removeTask = service.schedule(() -> cache.remove(name), ttl, TimeUnit.MILLISECONDS);
        cache.put(name, new CacheEntry(System.currentTimeMillis(), o, removeTask));
    }

    void remove(String name){
        CacheEntry cacheEntry = cache.get(name);
        if(cacheEntry == null) return;
        if(cacheEntry.removeTask != null && !cacheEntry.removeTask.isCancelled())
            cacheEntry.removeTask.cancel(false);
        cache.remove(name);
    }

    Object get(String name){
        CacheEntry cacheEntry = cache.get(name);
        return cacheEntry == null ? null : cacheEntry.value;
    }

    @SuppressWarnings("unchecked")
    <T> T get(String name, Class<T> clazz){
        CacheEntry cacheEntry = cache.get(name);
        return cacheEntry == null ? null : (T)cacheEntry.value;
    }

    private void initCleanupTask(){
        cleanupTask = service.scheduleAtFixedRate(this::clean, 1000L, 1000L, TimeUnit.MILLISECONDS);
    }

    void cancelCleanupTask(){
        cleanupTask.cancel(false);
    }

    private void clean(){
        cache.entrySet().removeIf(e -> e.getValue().timestamp - System.currentTimeMillis() >= ttl);
    }

    @Getter @AllArgsConstructor
    static class CacheEntry{
        long timestamp;
        Object value;
        Future<?> removeTask;
    }


}
