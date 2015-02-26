/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.exp;

import org.unisim.exp.Trial;
import org.unisim.exp.ExpParam;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unisim.genesis.RobotIndividual;
import org.unisim.simulation.core.SimulationController;
import org.unisim.simulation.robot.ctrnn.CTRNN;
import org.unisim.io.ctrnn.JSONCTRNNLayout;

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

    SimulationController sc;

    @Before
    public void setUp() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json");
        JSONCTRNNLayout layout = JSONCTRNNLayout.fromFile(file);
        RobotIndividual ind = new RobotIndividual(layout.toCTRNNLayout(), new ExpParam());
        sc
                = new SimulationController.SimulationBuilder(new CTRNN(ind.getGenotype(), new ExpParam())).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void runTrialFor10Sec() {
        Trial trial = new Trial(sc, 10, true, true);
        System.out.println(trial.run());
    }

    @Test
    public void run10TrialsFor10Sec() {
        for (int i = 0; i < 10; i++) {
            Trial trial = new Trial(sc, 10, true, true);
            System.out.println(trial.run());
        }
    }

}
