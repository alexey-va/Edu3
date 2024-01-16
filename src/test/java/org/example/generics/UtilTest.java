package org.example.generics;

import org.checkerframework.checker.units.qual.A;
import org.example.geometry.Line;
import org.example.geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void testShiftX() {
        Line<Point> line = new Line<>(new Point(1, 1), new Point(2, 2));
        Util.shiftX(line, 1);
        assertEquals(2, line.getStart().getX());
    }

    @Test
    void testMax() {
        MyOptional<Integer> optional = MyOptional.of(1);
        MyOptional<Integer> optional2 = MyOptional.of(2);
        MyOptional<Integer> optional3 = MyOptional.of(3);
        MyOptional<Integer> optional4 = MyOptional.of(4);
        MyOptional<Integer> optional5 = MyOptional.of(5);

        assertEquals(5, Util.max(List.of(optional, optional2, optional3, optional4, optional5)).getAsDouble());
    }

    @Test
    void testPutPoint() {
        Holder<Point> holder = new Holder<>();
        Util.putPoint(holder);
        assertNotNull(holder.getValue());
    }



    @Test
    void testPutNumbers() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Util.putNumbers(list, 6, 10);
        System.out.println(list);
        assertEquals(10, list.size());
    }

    @Test
    void testCollect() {
        Set<Integer> list = Util.collect(List.of(1, 2, 3, 4, 5, 5), HashSet::new, HashSet::add);
        assertEquals(5, list.size());
    }

    @Test
    void testReduce() {
        Integer reduce = Util.reduce(List.of(1, 2, 3, 4, 5), 0, Integer::sum);
        assertEquals(15, reduce);
    }

    @Test
    void testTrim() {
        List<Integer> trim = Util.trim(List.of(1, 2, 3, 4, 5), (i) -> i > 3);
        assertEquals(2, trim.size());
    }

    @Test
    void testApplyToAll() {
        List<Integer> applyToAll = Util.applyToAll(List.of(1, 2, 3, 4, 5), (i) -> i * 2);
        assertEquals(5, applyToAll.size());
        assertEquals(2, applyToAll.get(0));
        assertEquals(4, applyToAll.get(1));
        assertEquals(6, applyToAll.get(2));
        assertEquals(8, applyToAll.get(3));
        assertEquals(10, applyToAll.get(4));
    }

    @Test
    void countAll() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        List<Integer> list2 = List.of(1, 2);
        Integer result = Util.reduce(
                Util.applyToAll(List.of(list, list2), List::size),
                0,
                Integer::sum
        );
        System.out.println(result);
    }


}