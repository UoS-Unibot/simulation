/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.exp;

import org.su.easy.unisim.simulation.core.SimulationController;
import org.su.easy.unisim.simulation.core.SimulationEventListener;

/**
 *
 * @author miles
 */
public class Trial implements SimulationEventListener {

    private final SimulationController simulation;
    private final double trialLength;
    private final boolean terminateOnCollision;
    private volatile  boolean terminateTrial = false;

    public Trial(SimulationController simulation, double trialLength, boolean loggingEnabled, boolean terminateOnCollision) {
        this.simulation = simulation;
        this.trialLength = trialLength;
        this.terminateOnCollision = terminateOnCollision;
        simulation.addListener(this);
    }

    public synchronized boolean trialTerminated() {
        return terminateTrial;
    }
    
    @Override
    public boolean collisionOccured() {
        if (terminateOnCollision) {
            terminateTrial = true;
            return true;
        }
        return false;
    }
    

    public double run() {
        float velocitySum = 0;
        double trialLengthMsec = trialLength * 60 * simulation.getTimeStep();
        for (float t = 0; t < trialLengthMsec; t += simulation.getTimeStep()) {
            if (trialTerminated()) {
                return 0;
            }
            simulation.step();
            velocitySum += simulation.getRobot().getVelocity();
        }
        return velocitySum / trialLengthMsec;
    }

}
