package org.example.geometry;

import lombok.ToString;

@ToString
public class Squar extends Rectangle implements Chainable {

    public Squar(Point leftUp, double side) {
        super.getPoints().add(leftUp);
        this.a = side;
        this.b = side;
    }
}
