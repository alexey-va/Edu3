package org.example.other.customcollections;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CustomListTest {

    @Test
    void testCustomList(){
        CustomList<String> customList = new CustomList<>("Hello", 5);
        assertEquals("Hello", customList.get(0));
        assertEquals("Hello", customList.get(4));
        assertThrows(IndexOutOfBoundsException.class, () -> customList.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> customList.get(-1));
        assertEquals(5, customList.size());
    }

    @Test
    void testSet(){
        FromIterator<Integer> fromIterator = new FromIterator<>(5, 5);
        Iterator<Integer> iterator = fromIterator.iterator();
        assertEquals(5, iterator.next());
        assertEquals(5, iterator.next());
        assertEquals(5, iterator.next());
        assertEquals(5, iterator.next());
        assertEquals(5, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(IndexOutOfBoundsException.class, iterator::next);
    }

}