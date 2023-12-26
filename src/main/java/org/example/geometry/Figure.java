package org.example.geometry;

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

