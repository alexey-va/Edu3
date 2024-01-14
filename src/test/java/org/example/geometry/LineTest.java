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

    @Test
    void testGeneric(){
        Line<Point> line = new Line<>(new Point(1,2), new Point(3,4));
        System.out.println(line);
        line.setEnd(new Point(5,6));
        System.out.println(line);
    }

    @Test
    void test3D(){
        Line<Point3D> line = new Line<>(new Point3D(1,2,3), new Point3D(4,5,6));
        System.out.println(line);
        line.setEnd(new Point3D(7,8,9));
        System.out.println(line);
    }

}