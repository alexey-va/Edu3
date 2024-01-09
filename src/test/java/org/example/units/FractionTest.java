package org.example.units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FractionTest {
    @Test
    public void testEquals(){
        assertEquals(new Fraction(1,2), new Fraction(1,2));
        assertEquals(new Fraction(1,2), new Fraction(2,4));
        assertEquals(new Fraction(1,2), new Fraction(-1,-2));
        assertEquals(new Fraction(-1,2), new Fraction(1,-2));
        assertEquals(new Fraction(1,2), new Fraction(-2,-4));
        assertEquals(new Fraction(1,2), new Fraction(2,4));
        assertEquals(new Fraction(1,2), new Fraction(2,4));
        assertEquals(new Fraction(1,2), new Fraction(2, 4));
    }

    @Test
    void testClone(){
        Fraction f = new Fraction(1,2);
        Fraction f2 = f.clone();
        assertEquals(f, f2);
        assertNotSame(f, f2);
    }
}