package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Line {

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
}
