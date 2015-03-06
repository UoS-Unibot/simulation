/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot;

/**
 *
 * @author mb459
 */
public interface IRobotBody {
    
    
    public double getRange();
    public double[] getSonars();
    public boolean isLive();

    void step(double velocity, double angularVelocity);
}
