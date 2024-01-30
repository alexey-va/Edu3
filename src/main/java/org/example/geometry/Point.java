package org.example.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Data
@AllArgsConstructor
public class Point implements Cloneable, Moveable {

    double x, y;
    private static final DecimalFormat format = new DecimalFormat("##.#") {{
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        setDecimalFormatSymbols(symbols);
    }};


    public double distanceTo(Point point) {
        return Math.sqrt((this.x - point.x) * (this.x - point.x) + (this.y - point.y) * (this.y - point.y));
    }

    @Override
    public void shift(Shift shift) {
        x+=shift.axis(0);
        y+=shift.axis(1);
    }

    @Override
    public String toString() {
        return "{" +
                format.format(x) +
                "," + format.format(y) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        return Math.abs(this.x - ((Point) o).x) < 1e-14
                && Math.abs(this.y - ((Point) o).y) < 1e-14;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }



}
