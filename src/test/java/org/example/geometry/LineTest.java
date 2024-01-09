package org.example.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    void testEquals(){
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(3,4), new Point(1,2)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));
        assertEquals(new Line(new Point(1,2), new Point(3,4)), new Line(new Point(1,2), new Point(3,4)));


    }

    @Test
    void testClone(){
        Line line = new Line(new Point(1,2), new Point(3,4));
        Line clone = line.clone();
        assertEquals(line, clone);
        assertNotSame(line, clone);
        assertNotSame(line.getStart(), clone.getStart());
        assertNotSame(line.getEnd(), clone.getEnd());
    }

    @Test
    void testHashCode(){
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(3,4), new Point(1,2)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
        assertEquals(new Line(new Point(1,2), new Point(3,4)).hashCode(), new Line(new Point(1,2), new Point(3,4)).hashCode());
    }

}