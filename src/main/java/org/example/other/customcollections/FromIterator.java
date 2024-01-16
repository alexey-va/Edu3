package org.example.other.customcollections;

import java.util.AbstractSet;
import java.util.Iterator;

public class FromIterator<T> extends AbstractSet<T> {

    int size;
    T value;

    public FromIterator(T value,  int size) {
        this.size = size;
        this.value = value;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                if (index >= size) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
                }
                index++;
                return value;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }
}
