/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.genesis;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.evors.genesis.Phenotype;
import org.evors.core.RunController;
import org.evors.rs.sim.core.SimulationBuilder;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.SimulatedRobotBody;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

public final class RobotPhenotype implements Phenotype {

    private final CTRNNLayout layout;
    private final SimulationWorld world;
    private final SimulatedRobotBody robot;

    public RobotPhenotype(RobotExperiment exp) {
        layout = exp.getLayout();
        world = exp.getWorld();
        robot = exp.getRobot();
    }

    @Override
    public int getGenomeLength() {
        return layout.getGenotypeLength();
    }

    @Override
    public float calculateFitness(float[] genes) {
        /**
         * Implements the obstacle avoidance fitness function in Jakobi et al.
         * (1995)
         */

        RunController sc
                = new SimulationBuilder(layout.getCTRNNController(genes), robot).
                setWorld(world).
                build();

        float totalTrialLength = (float) (30000f * sc.getTimeStep());
        float i;
        float sumV = 0, sumD = 0, sumI = 0;
        
        for (i = 0; sc.isLive() & i < totalTrialLength; i += sc.getTimeStep()) {
            double mL, mR;
            mR = sc.getController().getVelocity() - sc.getController().
                    getAngularVelocity() * 10 * 0.5;
            mL = mR - 2 * sc.getController().getVelocity();
            
            sumV += mL+mR;
            sumD += Math.abs(mR - mL);
            
            sc.step();
        }
        
        sumV /= 30000f;
        sumD /= 30000f;
        
        
        return (float) (sumV * (1 - Math.sqrt(sumD)));
        
    }
    private static final Variance var = new Variance();
}
