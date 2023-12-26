package org.example.geometry;

import lombok.ToString;

import java.util.List;

@ToString
public class Triangle extends Figure implements Chainable {
    public Triangle(Point p1, Point p2, Point p3) {
        super.getPoints().addAll(List.of(p1, p2, p3));
    }

    @Override
    public double area() {
        Point p1 = getPoints().get(0);
        Point p2 = getPoints().get(1);
        Point p3 = getPoints().get(2);
        return Math.abs((1.0 / 2) * (
                (p2.y + p1.y) * (p1.x - p2.x) +
                        (p1.y + p3.y) * (p3.x - p1.x) -
                        (p2.y + p3.y) * (p3.x - p2.x))
        );
    }

    @Override
    public BrokenLine toLine() {
        Point p1 = getPoints().get(0);
        Point p2 = getPoints().get(1);
        Point p3 = getPoints().get(2);
        return new LoopedBrokenLine(
                new Point(p1.getX(), p1.getY()),
                new Point(p2.getX(), p2.getY()),
                new Point(p3.getX(), p3.getY())
        );
    }
}
