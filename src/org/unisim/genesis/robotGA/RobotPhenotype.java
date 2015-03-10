/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis.robotGA;

import org.unisim.genesis.Phenotype;
import org.unisim.reality.RunController;
import org.unisim.simulation.core.SimulationBuilder;
import org.unisim.simulation.core.SimulationWorld;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;

public final class RobotPhenotype implements Phenotype {

    private final CTRNNLayout layout;
    private final SimulationWorld world;
    
    public RobotPhenotype(RobotExperiment exp) {
        layout = exp.getLayout();
        world = exp.getWorld();
    }

    @Override
    public int getGenomeLength() {
        return layout.getGenotypeLength();
    }

    @Override
    public float calculateFitness(float[] genes) {

        RunController sc
                = new SimulationBuilder(layout.getCTRNNController(genes)).setWorld(world).
                build();
        
        int trialLength = 10000;
        float i;
        for(i = 0; i< (float)trialLength*sc.getTimeStep() && sc.isLive();i += sc.getTimeStep()) {
            sc.step();
        }
        return i / trialLength;
    }

}
