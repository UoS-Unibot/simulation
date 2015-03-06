/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.exp;

import org.unisim.reality.RunController;
import org.unisim.simulation.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class Trial {

    private final RunController simulation;
    private final double trialLength;
    private final boolean terminateOnCollision;
    private volatile  boolean terminateTrial = false;

    public Trial(RunController simulation, double trialLength, boolean loggingEnabled, boolean terminateOnCollision) {
        this.simulation = simulation;
        this.trialLength = trialLength;
        this.terminateOnCollision = terminateOnCollision;
    }

    public synchronized boolean trialTerminated() {
        return terminateTrial;
    }
    
    

    public double run() {
        double trialLengthMsec = trialLength * 60 * simulation.getTimeStep();
        double totalT = 0;
        for (float t = 0; t < trialLengthMsec; t += simulation.getTimeStep()) {
            if (trialTerminated()) {
                return t/trialLengthMsec;
            }
            simulation.step();
        }
        return 1;
    }

}
