package org.example.generics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HolderTest {

    @Test
    void testHolder() {
        Holder<String> holder = new Holder<>();
        holder.setValue("Hello");
        assertEquals("Hello", holder.getValue());
    }

    @Test
    void testHolder2() {
        Holder<String> holder = new Holder<>();
        holder.setValue("Hello");
        assertEquals("Hello", holder.getValue());
        holder.setValue("Hello");
        assertThrows(IllegalStateException.class, () -> holder.setValue("Hello"));
    }

    @Test
    void testHolder3() {
        Holder<String> holder = new Holder<>();
        holder.setValue("Hello");
        assertEquals("Hello", holder.getValue());
        holder.setValue("Hello");
        assertThrows(IllegalStateException.class, () -> holder.setValue("Hello"));
        holder.getValue();
        assertNull(holder.getValue());
    }

}