package org.example;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public abstract class Figure {

    private List<Point> points = new ArrayList<>();

    public abstract double area();

    public List<Point> getPoints() {
        return points;
    }

}
@ToString
class Circle extends Figure {
    double radius;

    public Circle(Point center, double radius) {
        super.getPoints().add(center);
        this.radius = radius;
    }


    @Override
    public double area() {
        return radius * radius * Math.PI;
    }

}
@ToString
class Rectangle extends Figure implements Chainable {

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
        return new Main.LoopedBrokenLine(
                new Point(leftUpperCorner.x, leftUpperCorner.y),
                new Point(leftUpperCorner.x+a, leftUpperCorner.y),
                new Point(leftUpperCorner.x+a, leftUpperCorner.y-b),
                new Point(leftUpperCorner.x, leftUpperCorner.y-b)
        );
    }
}

@ToString
class Squar extends Rectangle implements Chainable {

    public Squar(Point leftUp, double side) {
        super.getPoints().add(leftUp);
        this.a = side;
        this.b = side;
    }
}

@ToString
class Triangle extends Figure implements Chainable {
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
        return new Main.LoopedBrokenLine(
               new Point(p1.getX(), p1.getY()),
                new Point(p2.getX(), p2.getY()),
                new Point(p3.getX(), p3.getY())
        );
    }
}

