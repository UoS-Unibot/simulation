/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.io.world;

import org.unisim.io.world.JSONWorld;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisim.simulation.core.SimulationWorld;

/**
 *
 * @author miles
 */
public class JSONWorldTest {
    
    public JSONWorldTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void loadAWorld() throws IOException {
        SimulationWorld world = JSONWorld.fromFile(new File(System.getProperty("user.dir") + "/user/Worlds/5x5_Two_Objects.json"));
        System.out.println(world.getObjects().size());
        System.out.println(world.toString());
    }
    
}
