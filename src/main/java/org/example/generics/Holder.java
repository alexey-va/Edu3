package org.example.generics;

import lombok.Getter;
import lombok.Setter;

public class Holder<T> {

    T value;

    public T getValue() {
        T t = value;
        value = null;
        return t;
    }

    public void setValue(T value) {
        if(this.value != null) throw new IllegalStateException("Holder is full");
        this.value = value;
    }
}
