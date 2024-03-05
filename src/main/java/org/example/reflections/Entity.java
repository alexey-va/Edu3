package org.example.reflections;

import org.example.reflections.annotations.ToString;
import org.example.reflections.annotations.Type;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

import static org.example.reflections.Utils.fieldsOf;

public class Entity {
    @Override
    public final String toString() {
        record FieldData(String name, String value) {
        }
        return this.getClass().getSimpleName() + fieldsOf(this.getClass()).stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .filter(f -> {
                    if (f.isAnnotationPresent(ToString.class))
                        return f.getAnnotation(ToString.class).value() == Type.YES;
                    return true;
                }).filter(f -> {
                    if (f.getDeclaringClass().isAnnotationPresent(ToString.class))
                        return f.getDeclaringClass().getAnnotation(ToString.class).value() == Type.YES;
                    return true;
                }).map(f -> {
                    try {
                        f.setAccessible(true);
                        return new FieldData(f.getName(), f.get(this).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).map(fd -> fd.name + "=" + fd.value)
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
