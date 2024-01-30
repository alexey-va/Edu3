package org.example.geometry;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
public class GeometryGroup implements Moveable {

    List<Moveable> moveables = new ArrayList<>();

    private GeometryGroup(){}

    public static GeometryGroup of(Moveable... moveables){
        GeometryGroup group = new GeometryGroup();
        group.moveables.addAll(Arrays.asList(moveables));
        return group;
    }

    @Override
    public void shift(Shift shift) {
        moveables.forEach(m -> m.shift(shift));
    }
}
