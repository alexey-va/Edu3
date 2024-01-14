package org.example.entities;

import com.sun.javafx.collections.UnmodifiableListSet;
import org.junit.jupiter.api.Test;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void testEquals(){
        assertEquals(new City("A"), new City("A"));
        assertEquals(new City("A", new City.Route(new City("B"), 1)), new City("A", new City.Route(new City("B"), 1)));
        assertNotEquals(new City("A", new City.Route(new City("B"), 1)), new City("A", new City.Route(new City("B"), 2)));
        assertNotEquals(new City("A", new City.Route(new City("B"), 1)), new City("A", new City.Route(new City("C"), 1)));

        SmartCity smartCity = new SmartCity("A", new City.Route(new City("B"), 1));
        assertEquals(new City("A", new City.Route(new City("B"), 1)), smartCity);
    }

    @Test
    void testHash(){
        assertEquals(new City("A").hashCode(), new City("A").hashCode());
        assertEquals(new City("A", new City.Route(new City("B"), 1)).hashCode(), new City("A", new City.Route(new City("B"), 1)).hashCode());
        assertNotEquals(new City("A", new City.Route(new City("B"), 1)).hashCode(), new City("A", new City.Route(new City("B"), 2)).hashCode());
        assertNotEquals(new City("A", new City.Route(new City("B"), 1)).hashCode(), new City("A", new City.Route(new City("C"), 1)).hashCode());

        SmartCity smartCity = new SmartCity("A", new City.Route(new City("B"), 1));
        assertEquals(new City("A", new City.Route(new City("B"), 1)).hashCode(), smartCity.hashCode());
    }

    @Test
    void testHashWithMap(){
        Map<City, String> map = new HashMap<>();
        map.put(new City("A"), "A");
        map.put(new City("A"), "B");
        assertEquals(1, map.size());

        map.clear();

        map.put(new City("A", new City.Route(new City("B"), 1)), "A");
        map.put(new City("A", new City.Route(new City("B"), 1)), "B");
        assertEquals(1, map.size());

        A a = new A();
        A.B b = a.new B();
        A.B.B b2 = b.new B();
        System.out.println(b2.getClass().getSuperclass());

    }



    class A{
        class B extends A{
            public void test(){
                System.out.println("test");
            }
        }
    }

    class D extends A{
        class B extends A.B{
            public void test2(){
                System.out.println("test2");
            }
        }
    }






}