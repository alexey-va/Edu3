package org.example.geometry;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class Line implements Measurable, Cloneable{

    private Point start, end;

    public Line(Point point1, Point point2){
        this(point1.x, point1.y, point2.x, point2.y);
    }

    public Line(double x1, double y1, double x2, double y2){
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    public Point getEnd(){
        return new Point(end.x, end.y);
    }

    public Point getStart(){
        return new Point(start.x, start.y);
    }

    public void setStartCoordinates(int x, int y){
        this.start.x=x;
        this.start.y=y;
    }

    public void setEndCoordinates(int x, int y){
        this.end.x=x;
        this.end.y=y;
    }


    public double length(){
        return start.distanceTo(end);
    }

    @Override
    public String toString(){
        return "Line from "+start+" to "+end;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Line line = (Line) object;

        if(Objects.equals(start, line.start) && Objects.equals(end, line.end)) return true;
        return Objects.equals(start, line.end) && Objects.equals(end, line.start);
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public Line clone() {
        try {
            Line clone = (Line) super.clone();
            clone.start = start.clone();
            clone.end = end.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
