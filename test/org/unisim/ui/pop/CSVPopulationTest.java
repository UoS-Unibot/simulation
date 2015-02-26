/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui.pop;

import org.unisim.io.CSVPopulation;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class CSVPopulationTest {
    
    public CSVPopulationTest() {
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
    public void loadCSVFile() throws IOException {
        CSVPopulation pop = CSVPopulation.fromFile(new File("/home/miles/NetBeansProjects/UnibotSim/user/experiments/25-02-2015/23-42-55/population-1.csv"));
        System.out.println(pop.getFitnessAt(0));
    }
    
}
