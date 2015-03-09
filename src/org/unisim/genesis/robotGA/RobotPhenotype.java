/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis.robotGA;

import org.unisim.exp.Experiment;
import org.unisim.exp.Trial;
import org.unisim.exp.params.Parameters;
import org.unisim.genesis.Phenotype;
import org.unisim.reality.RunController;
import org.unisim.simulation.core.SimulationBuilder;
import org.unisim.simulation.core.SimulationWorld;
import org.unisim.simulation.robot.ctrnn.CTRNNController;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;

public final class RobotPhenotype implements Phenotype {

    private final CTRNNLayout layout;
    private final Parameters params;
    private final SimulationWorld world;
    
    public RobotPhenotype(Experiment exp) {
        layout = exp.getLayout();
        params = exp.getParam();
        world = exp.getWorld();
    }

    @Override
    public int getGenomeLength() {
        return layout.getGenomeLength();
    }

    @Override
    public float calculateFitness(float[] genes) {
        float sum = 0.0f;

        //float[] angs = {1,-3,-2,-4,4,-6,5,-5,-1,2,6,3};
        RunController sc
                = new SimulationBuilder(new CTRNNController(layout, params.
                                getController_timestep())).setWorld(world).
                build();

            Trial t = new Trial(sc, 60, false, true);
            double fitness = t.run();
            sum += fitness;

        return (sum);
    }

}
