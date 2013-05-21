package de.stock.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testRandomDouble() {
        assertEquals(new Double(0), Utils.random(0.0, 0.0));
        assertEquals(new Double(1.0), Utils.random(1.0, 1.0));
        assertEquals(new Double(0), Utils.random(1.0, 0.0), 1.0);
    }

    @Test
    public void testRandomInt() {
        assertEquals(new Integer(0), Utils.random(0, 0));
        assertEquals(new Integer(1), Utils.random(1, 1));
        assertEquals(new Integer(0), Utils.random(1, 0), 1);
    }
}
