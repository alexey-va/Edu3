package org.example.generics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyOptionalTest {
    @Test
    void testMyOptional(){
        MyOptional<String> optional = MyOptional.of("Hello");
        assertEquals("Hello", optional.getOrDefault("World"));

        MyOptional<String> empty = MyOptional.of(null);
        assertEquals("World", empty.getOrDefault("World"));

        MyOptional<String> optional2 = MyOptional.of("Hello");
        assertEquals("Hello", optional2.getOrThrow());

        MyOptional<String> empty2 = MyOptional.of(null);
        assertThrows(IllegalStateException.class, () -> empty2.getOrThrow());

        MyOptional<String> optional3 = MyOptional.of("Hello");
        assertEquals("Hello", optional3.getOrSupply(() -> "World"));

        MyOptional<String> empty3 = MyOptional.of(null);
        assertEquals("World", empty3.getOrSupply(() -> "World"));

        MyOptional<String> optional4 = MyOptional.of("Hello");
        assertTrue(optional4.isPresent());

        MyOptional<String> empty4 = MyOptional.of(null);
        assertFalse(empty4.isPresent());

        MyOptional<String> optional5 = MyOptional.of("Hello");
        assertFalse(optional5.isEmpty());

        MyOptional<String> empty5 = MyOptional.of(null);
        assertTrue(empty5.isEmpty());

        MyOptional<String> optional6 = MyOptional.of("Hello");
        optional6.ifPresentOrElse(() -> System.out.println("Present"), () -> System.out.println("Empty"));

        MyOptional<String> empty6 = MyOptional.of(null);
        empty6.ifPresentOrElse(() -> System.out.println("Present"), () -> System.out.println("Empty"));
    }
}