/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mb459.easy.premca.exp;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mb459.easy.premca.genesis.RobotIndividual;
import org.su.easy.unisim.simulation.core.SimulationController;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNN;
import org.su.easy.unisim.simulation.robot.ctrnn.jsonIO.JSONCTRNNLayout;

/**
 *
 * @author miles
 */
public class TrialTest {
    
    public TrialTest() {
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
    public void runTrialFor10Sec() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json");
        JSONCTRNNLayout layout = JSONCTRNNLayout.fromFile(file);
        RobotIndividual ind = new RobotIndividual(layout.toCTRNNLayout(), new ExpParam());
        SimulationController sc
                = new SimulationController.SimulationBuilder(new CTRNN(ind.getGenotype(), new ExpParam())).build();
        Trial trial = new Trial(sc, 10, true, true);
        System.out.println(trial.run());
    }
    
}
