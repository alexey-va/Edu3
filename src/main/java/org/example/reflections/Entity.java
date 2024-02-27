package org.example.reflections;

import org.example.entrypoints.Main;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

import static org.example.reflections.Utils.fieldsOf;

public class Entity {
    @Override
    public final String toString() {
        record FieldData(String name, String value) {
        }
        return this.getClass().getSimpleName() + fieldsOf(this.getClass()).stream()
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .map(f -> {
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
