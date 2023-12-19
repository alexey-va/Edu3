package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Square implements Chainable{

    Point leftUpperCorner;
    @Getter
    double side;


    public Square(Point point, double side){
        if(side <0) throw new IllegalArgumentException("side < 0");
        this.leftUpperCorner = point;
        this.side=side;
    }
    public Square(double x, double y, double side){

        this(new Point(x,y), side);
    }

    public BrokenLine toLine(){
        return new Main.LoopedBrokenLine(
                new Point(leftUpperCorner.x, leftUpperCorner.y),
                new Point(leftUpperCorner.x+side, leftUpperCorner.y),
                new Point(leftUpperCorner.x+side, leftUpperCorner.y-side),
                new Point(leftUpperCorner.x, leftUpperCorner.y-side)
        );
    }

    public void setSide(double side) {
        if(side < 0) throw new IllegalArgumentException("side < 0");
        this.side = side;
    }

    @Override
    public String toString() {
        return "Square{" +
                "leftUpperCorner=" + leftUpperCorner +
                ", side=" + side +
                '}';
    }
}
