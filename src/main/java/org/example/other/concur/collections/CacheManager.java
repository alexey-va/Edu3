package org.example.other.concur.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class CacheManager {

    private Map<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private static volatile CacheManager instance;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(4);

    private CacheManager() {
    }

    public static CacheManager instance(){
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void put(String cache, String id, Object o){
        Cache c = cacheMap.get(cache);
        if(c == null) throw new IllegalArgumentException("No cache "+cache+" found!");
        c.put(id, o);
    }

    public void remove(String cache, String id){
        Cache c = cacheMap.get(cache);
        if(c == null) throw new IllegalArgumentException("No cache "+cache+" found!");
        c.remove(id);
    }

    public Object get(String cache, String id){
        Cache c = cacheMap.get(cache);
        if(c == null) throw new IllegalArgumentException("No cache "+cache+" found!");
        return c.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String cache, String id, Class<T> clazz){
        Cache c = cacheMap.get(cache);
        if(c == null) throw new IllegalArgumentException("No cache "+cache+" found!");
        return (T) c.get(id);
    }

    public <T> T getOrElse(String cache, String id, Supplier<T> defaultValue){
        Cache c = cacheMap.get(cache);
        if(c == null) throw new IllegalArgumentException("No cache "+cache+" found!");
        Object o = c.get(id);
        //System.out.println("o = " + o);
        if(o == null){
            T t = defaultValue.get();
            c.put(id, t);
            return t;
        }
        return (T) o;
    }

    public void createCache(String name, long ttl){
        Cache cache = Cache.builder()
                .ttl(ttl)
                .name(name)
                .preciseTtl(true)
                .service(service)
                .build();
        cacheMap.put(name, cache);
    }


}
