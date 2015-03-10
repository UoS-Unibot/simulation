/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util;

import org.evors.core.util.Range;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class RangeTest {
    
    public RangeTest() {
    }

    @Test
    public void mapRangeOf5_10() {
        Range r = new Range(5,10);
        assertEquals(5,r.map(-1),0.001);
        assertEquals(10,r.map(1),0.001);
        assertEquals(7.5,r.map(0),0.001);
        assertEquals(8.75,r.map(0.5f),0.001);
        assertEquals(6.25,r.map(-0.5f),0.001);
    }
    
}
