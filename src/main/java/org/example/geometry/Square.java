package org.example.geometry;

import lombok.Getter;

public class Square extends Figure implements Chainable{

    @Getter
    double side;


    public Square(Point point, double side){
        if(side <0) throw new IllegalArgumentException("side < 0");
        getPoints().add(point);
        this.side=side;
    }
    public Square(double x, double y, double side){

        this(new Point(x,y), side);
    }

    public BrokenLine toLine(){
        return new LoopedBrokenLine(
                new Point(point().x, point().y),
                new Point(point().x+side, point().y),
                new Point(point().x+side, point().y-side),
                new Point(point().x, point().y-side)
        );
    }

    public void setSide(double side) {
        if(side < 0) throw new IllegalArgumentException("side < 0");
        this.side = side;
    }

    private Point point(){
        return getPoints().getFirst();
    }

    @Override
    public String toString() {
        return "Square{" +
                "leftUpperCorner=" + point() +
                ", side=" + side +
                '}';
    }

    @Override
    public double area() {
        return side*side;
    }
}
