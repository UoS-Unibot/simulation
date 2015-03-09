/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import org.unisim.exp.Experiment;
import org.unisim.exp.Trial;
import org.unisim.exp.params.Parameters;
import org.unisim.reality.RunController;
import org.unisim.simulation.core.SimulationBuilder;
import org.unisim.simulation.core.SimulationWorld;
import org.unisim.simulation.robot.ctrnn.CTRNNController;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author Miles
 */
public class RobotIndividual extends AbstractIndividual {

    protected CTRNNLayout layout;
    protected Parameters params;
    protected SimulationWorld world;
    protected int nTrials;

    public RobotIndividual(Experiment exp) {
        this(exp, Genotype.withRandomGenome(exp.getLayout().getGenomeLength()));
    }    

    public RobotIndividual(Experiment exp, Genotype genotype) {
        super(exp.getLayout(),genotype);
        layout = exp.getLayout();
        params = exp.getParam();
        world = exp.getWorld();
        nTrials = params.getFitness_n_trials();
    }
    
    @Override
    public float calcRawFitness() {
        float sum = 0.0f;

        int nTrials = params.getFitness_n_trials();

        //float[] angs = {1,-3,-2,-4,4,-6,5,-5,-1,2,6,3};
        RunController sc
                = new SimulationBuilder(new CTRNNController(layout, params.
                                getController_timestep())).setWorld(world).
                build();

        for (int i = 0; i < nTrials; i++) {

            Trial t = new Trial(sc, 60, false, true);
            double fitness = t.run();
            sum += fitness;
        }

        return (sum / nTrials);
    }



}
