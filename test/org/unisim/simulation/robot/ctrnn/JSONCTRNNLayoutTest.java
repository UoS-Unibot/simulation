/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot.ctrnn;

import org.unisim.io.ctrnn.JSONCTRNNLayout;
import java.io.File;
import java.io.IOException;
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
public class JSONCTRNNLayoutTest {
    
    public JSONCTRNNLayoutTest() {
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
    public void loadSimple5Neuron3LayerController() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json");
        JSONCTRNNLayout layout = JSONCTRNNLayout.fromFile(file);
        System.out.println(layout);
        System.out.println(layout.toCTRNNLayout());
    }
    
}
