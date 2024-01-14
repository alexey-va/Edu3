package org.example.geometry;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BrokenLine implements Measurable, Cloneable {

    private List<Point> points;

    public BrokenLine(Point... points) {
        this.points = Arrays.stream(points).collect(Collectors.toList());
    }

    public void add(Point... points) {
        this.points.addAll(Arrays.stream(points).toList());
    }

    public double length() {
        double sum = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            sum += points.get(i).distanceTo(points.get(i + 1));
        }

        return sum;
    }

    @Override
    public String toString() {
        if (points == null || points.isEmpty()) return "Line with no points!";
        return points.stream()
                .map(Point::toString)
                .collect(Collectors.joining(",", "Line [", "]"));
    }

}
