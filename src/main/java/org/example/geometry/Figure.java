package org.example.geometry;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Figure implements Moveable {

    private List<Point> points = new ArrayList<>();

    public abstract double area();

    @Override
    public void shift(Shift shift){
        points.forEach(p -> p.shift(shift));
    }

}

