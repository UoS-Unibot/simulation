/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import org.unisim.simulation.robot.ctrnn.CTRNNLayout;
import java.util.Random;
import org.unisim.exp.Experiment;
import org.unisim.exp.Trial;
import org.unisim.exp.params.Parameters;
import org.unisim.simulation.core.SimulationController;
import org.unisim.simulation.core.SimulationWorld;
import org.unisim.simulation.robot.ctrnn.CTRNN;

/**
 *
 * @author Miles
 */
public class RobotIndividual implements Comparable<RobotIndividual> {

    protected RobotGenotype genotype;
    protected Parameters params;
    protected SimulationWorld world;
    protected int nTrials;

    public RobotIndividual(Experiment exp) {
        genotype = new RobotGenotype(exp.getLayout());
        params = exp.getParam();
        world = exp.getWorld();
        nTrials = params.getFitness_n_trials();
    }
    
    

    public RobotGenotype getGenotype() {
        return genotype;
    }

    void mutate(float p_mutation) {
        genotype.mutate(p_mutation);
        rawFitness = calcRawFitness();
    }

    private float rawFitness = 0.0f;
    private boolean rawFitnessSet = false;

    public float rawFitness() {
        if (!rawFitnessSet) {
            rawFitness = calcRawFitness();
            rawFitnessSet = true;
        }
        return rawFitness;
    }

    public float calcRawFitness() {
        float sum = 0.0f;

        int nTrials = params.getFitness_n_trials();

        //float[] angs = {1,-3,-2,-4,4,-6,5,-5,-1,2,6,3};
        SimulationController sc
                = new SimulationController.SimulationBuilder(new CTRNN(genotype, params)).setWorld(world).build();

        for (int i = 0; i < nTrials; i++) {

            Trial t = new Trial(sc, 60, false, true);
            double fitness = t.run();
            sum += fitness;
        }

        return (sum / nTrials);
    }

    public void crossover(float pCross, RobotIndividual ind) {

        Random rand = new Random();
        for (int i = 0; i < genotype.genes.length; i++) {
            if (rand.nextFloat() < pCross) {
                genotype.genes[i] = ind.genotype.genes[i];
            }
        }

        rawFitness = calcRawFitness();
    }

    @Override
    public int compareTo(RobotIndividual o) {
        return Float.valueOf(this.rawFitness()).compareTo(o.rawFitness());
    }

}
