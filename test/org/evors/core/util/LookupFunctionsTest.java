/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class LookupFunctionsTest {

    public LookupFunctionsTest() {
    }

    @Test
    public void sinShouldReturnSameAsMathSin() {
        for (double i = -4 * Math.PI; i < 4 * Math.PI; i += 0.1) {

            assertEquals("Failed for i = " + i,Math.sin(i), LookupFunctions.sin(i), 0.001);
        }
    }

    @Test
    public void cosShouldReturnSameAsMathCos() {
        for (double i = -4 * Math.PI; i < 4 * Math.PI; i += 0.1) {

            assertEquals("Failed for i = " + i,Math.cos(i), LookupFunctions.cos(i), 0.001);
        }
    }

    @Test
    public void speedComp100000MathSins() {
        for (long i = 0; i < 1000000; i++) {
            Math.sin(i);
        }
    }

    @Test
    public void speedComp100000SinLookups() {
        for (long i = 0; i < 1000000; i++) {
            LookupFunctions.sin(i);
        }
    }

    @Test
    public void sin2PiIs0() {
        assertEquals(0, LookupFunctions.sin(2 * Math.PI), 0.001);
    }

}
