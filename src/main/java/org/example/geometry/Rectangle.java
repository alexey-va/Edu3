package org.example.geometry;

import lombok.ToString;

@ToString
public class Rectangle extends Figure implements Chainable {

    double a, b;

    public Rectangle(Point leftUp, double a, double b) {
        super.getPoints().add(leftUp);
        this.a = a;
        this.b = b;
    }

    protected Rectangle() {
    }


    @Override
    public double area() {
        return a * b;
    }

    @Override
    public BrokenLine toLine() {
        Point leftUpperCorner = getPoints().get(0);
        return new LoopedBrokenLine(
                new Point(leftUpperCorner.x, leftUpperCorner.y),
                new Point(leftUpperCorner.x + a, leftUpperCorner.y),
                new Point(leftUpperCorner.x + a, leftUpperCorner.y - b),
                new Point(leftUpperCorner.x, leftUpperCorner.y - b)
        );
    }
}
