package org.example.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeometryGroupTest {


    @Test
    void testGroup(){
        Triangle triangle = new Triangle(new Point(0,2), new Point(2,3), new Point(3,4));
        Square square = new Square(0,1,5);
        Rectangle rectangle = new Rectangle(new Point(0,0), 3,5);
        GeometryGroup group = GeometryGroup.of(triangle, square, rectangle);
        System.out.println(group);
        group.shift(Moveable.Shift.of(10));
        System.out.println(group);

        Square square1 = new Square(5,5, 100);
        GeometryGroup group1 = GeometryGroup.of(square1, group);
        System.out.println(group1);
        group1.shift(Moveable.Shift.of(0, -100));
        System.out.println(group1);
    }

}