package org.example.entities;

import org.junit.jupiter.api.Test;

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




}