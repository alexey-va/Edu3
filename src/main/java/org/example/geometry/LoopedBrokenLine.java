package org.example.geometry;

import org.example.geometry.BrokenLine;
import org.example.geometry.Measurable;
import org.example.geometry.Point;

class LoopedBrokenLine extends BrokenLine implements Measurable {
    public LoopedBrokenLine(Point... points) {
        super(points);
    }

    @Override
    public double length() {
        return super.length() +
                this.getPoints().get(0)
                        .distanceTo(this.getPoints().get(getPoints().size() - 1));
    }


}
