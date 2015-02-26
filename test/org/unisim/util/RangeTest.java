/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.util;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class RangeTest {

    Range tRange;

    @Before
    public void setUp() {
        tRange = new Range(-10, 10);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void map0ShouldReturn0() {
        assertEquals(0, tRange.map(0), 0.0001);
    }

    @Test
    public void map0p5ShouldReturn5() {
        assertEquals(5, tRange.map(0.5f), 0.0001);
    }

    @Test
    public void mapArray_n0p8_n0p4_0p3_0p7_ShouldReturn_n8_n4_3_7() {
        assertArrayEquals(
                new float[]{-8, -4, 3, 7},
                tRange.map(new float[]{-0.8f,-0.4f,0.3f,0.7f}),
                0.0001f
        );
    }
    
    @Test
    public void mapArrayListWith_n0p8_n0p4_0p3_0p7_ShouldReturn_n8_n4_3_7() {
        Collection<Float> expected = new ArrayList<>();
        Collection<Float> parameters = new ArrayList<>();
        parameters.add(-0.8f);parameters.add(-0.4f);parameters.add(0.3f);parameters.add(0.7f);        
        expected.add(-8f);expected.add(-4f);expected.add(3f);expected.add(7f);
        assertEquals(expected,tRange.map(parameters));
    }

}
