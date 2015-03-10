/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.genesis;

import org.evors.genesis.GA;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.evors.rs.genesis.RobotExperiment;
import org.evors.rs.genesis.RobotPhenotype;
import org.evors.rs.sim.robot.ctrnn.io.JSONCTRNNLayout;
import org.evors.rs.sim.core.worldio.JSONWorld;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author miles
 */
public class GAEndToEnd {

    GA ga;
    RobotExperiment exp;

    @Test
    public void GAEndToEnd() throws IOException {
        File file = new File(System.getProperty("user.dir")
                + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json");
        CTRNNLayout layout = JSONCTRNNLayout.fromFile(file).toCTRNNLayout();
        SimulationWorld world = JSONWorld.fromFile(new File(System.getProperty(
                "user.dir") + "/user/Worlds/5x5_Two_Objects.json"));
        exp = new RobotExperiment(layout, world);
        ga = new GA(new RobotPhenotype(exp));
        while (!ga.isFinished()) {
            ga.doNextGeneration();
            System.out.println(ga.getDataRow().toString());
        }
    }

}
