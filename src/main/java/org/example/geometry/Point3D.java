package org.example.geometry;

import org.example.geometry.Point;

public class Point3D extends Point {

    double z;

    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public Point3D(double x, double y) {
        this(x, y, 0);
    }


    @Override
    public String toString() {
        return "Point3D{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
