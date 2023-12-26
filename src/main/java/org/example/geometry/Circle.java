package org.example.geometry;

import lombok.ToString;

@ToString
public class Circle extends Figure {
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
