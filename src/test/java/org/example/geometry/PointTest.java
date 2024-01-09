package org.example.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testEquals(){
        assertEquals(new Point(1,2), new Point(1,2));
        assertEquals(new Point(-1,2), new Point(-1,2));
        assertNotEquals(new Point(1.000000001,2), new Point(1,2));
        assertEquals(new Point(1.0+2.0,2), new Point(3.0,2));
    }

    @Test
    void testClone(){
        Point p = new Point(1,2);
        Point p2 = p.clone();
        assertEquals(p, p2);
        assertNotSame(p, p2);
    }
}