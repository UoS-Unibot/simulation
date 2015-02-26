/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class RangeTest {
    
    public RangeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
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
        assertEquals(0, tRange.map(0),0.0001);
    }
    
    @Test
    public void map0p5ShouldReturn5() {
        assertEquals(5, tRange.map(0.5f),0.0001);
    }
    
}
