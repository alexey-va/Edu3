package org.example.geometry;

import java.util.HashMap;
import java.util.Map;

public interface Moveable {

    void shift(Shift shift);


    static class Shift{
        private Map<Integer, Double> perAxisShift = new HashMap<>();

        private Shift(){}

        public static Shift of(double x){
            Shift shift = new Shift();
            shift.shiftOnAxis(0, x);
            return shift;
        }

        public static Shift of(double x,double y){
            Shift shift = new Shift();
            shift.shiftOnAxis(0, x);
            shift.shiftOnAxis(1, y);
            return shift;
        }

        public static Shift of(double x,double y,double z){
            Shift shift = new Shift();
            shift.shiftOnAxis(0, x);
            shift.shiftOnAxis(1, y);
            shift.shiftOnAxis(2, z);
            return shift;
        }

        public void shiftOnAxis(int axis, double shift){
            if(axis < 0) throw new IllegalArgumentException("Axis id should be >= 0");
            perAxisShift.compute(axis, (key, old) -> old == null ? shift : shift + old);
        }

        public double axis(int axis){
            return perAxisShift.getOrDefault(axis, 0.0);
        }
    }
}
