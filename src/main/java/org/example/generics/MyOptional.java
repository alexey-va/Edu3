package org.example.generics;

import java.util.function.Supplier;

public class MyOptional<T> {

    private final T value;

    private MyOptional(T value) {
        this.value = value;
    }

    public static <T> MyOptional<T> of(T value) {
        return new MyOptional<>(value);
    }

    public T getOrDefault(T defaultValue) {
        if(value == null) return defaultValue;
        return value;
    }

    public T getOrThrow() {
        if(value == null) throw new IllegalStateException("No value present");
        return value;
    }

    public T getOrSupply(Supplier<T> supplier) {
        if(value == null) return supplier.get();
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public void ifPresentOrElse(Runnable present, Runnable empty) {
        if(value != null) present.run();
        else empty.run();
    }


}
