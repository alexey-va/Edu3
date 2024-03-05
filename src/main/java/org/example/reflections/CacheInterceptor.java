package org.example.reflections;

import org.example.reflections.annotations.Cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CacheInterceptor implements InvocationHandler {
    Map<CacheEntry, Object> cacheEntryMap = new HashMap<>();
    private final Object targetObject;
    private Set<String> methodNames;

    public CacheInterceptor(Object targetObject) {
        this.targetObject = targetObject;
        if (targetObject.getClass().isAnnotationPresent(Cache.class)) {
            String[] methods = targetObject.getClass().getAnnotation(Cache.class).value();
            if (methods.length != 0) {
                methodNames = new HashSet<>();
                Collections.addAll(methodNames, methods);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.setAccessible(true);
        if (method.getReturnType() == void.class || (methodNames != null && !methodNames.contains(method.getName()))) {
            return method.invoke(targetObject, args);
        }
        CacheEntry cacheEntry = new CacheEntry(method.getName(), readFields(), args);

        if (cacheEntryMap.containsKey(cacheEntry)) return cacheEntryMap.get(cacheEntry);
        Object result = method.invoke(targetObject, args);
        cacheEntryMap.put(cacheEntry, result);
        return result;
    }

    private Map<String, Object> readFields() {
        record FieldData(String fieldName, Object value) {
        }
        var list = Utils.fieldsOf(targetObject.getClass()).stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .map(f -> {
                    try {
                        f.setAccessible(true);
                        return new FieldData(f.getName(), f.get(targetObject));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).toList();
        Map<String, Object> map = new HashMap<>();
        list.forEach(fd -> map.put(fd.fieldName, fd.value));
        return map;
    }

    record CacheEntry(String methodName, Map<String, Object> fieldContext, Object[] args) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheEntry that)) return false;

            if (!Objects.equals(methodName, that.methodName)) return false;
            if (!Objects.equals(fieldContext, that.fieldContext))
                return false;
            return Arrays.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            int result = methodName != null ? methodName.hashCode() : 0;
            result = 31 * result + (fieldContext != null ? fieldContext.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }
}
