/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.genesis;

import com.google.common.primitives.Doubles;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.evors.genesis.Phenotype;
import org.evors.core.RunController;
import org.evors.rs.sim.core.SimulationBuilder;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

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
        
        float totalTrialLength = (float) (30000f *sc.getTimeStep());
        float i;
        float sum = 0;
        List<Double> angularVelos = new LinkedList<>();
        for(i = 0; sc.isLive() & i < totalTrialLength;i += sc.getTimeStep()) {
            sc.step();
            angularVelos.add(sc.getController().getAngularVelocity());
        }
        double angVariance = var.evaluate(Doubles.toArray(
                angularVelos));
        return (float) (i / (totalTrialLength) * angVariance);
    }
    private static final Variance var = new Variance();
}
