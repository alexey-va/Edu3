package org.example.other.customcollections;

import java.util.AbstractList;

public class CustomList<T> extends AbstractList<T> {

    int amount;
    T value;

    public CustomList(T value, int amount) {
        this.amount = amount;
        this.value = value;
    }

    @Override
    public T get(int index) {
        if(index >= amount || index < 0){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + amount);
        }
        return value;
    }

    @Override
    public int size() {
        return amount;
    }
}
