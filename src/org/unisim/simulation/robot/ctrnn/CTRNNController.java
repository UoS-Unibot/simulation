/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot.ctrnn;

import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.RobotInput;

/**
 * Simulates a CTRNN. Loads neurons into an array from a CTRNNLayout.
 * @author Miles
 */
public class CTRNNController extends CTRNN implements IRobotController{

    private final float axleWidth;
    private final int leftMotorID,rightMotorID;
    
    public CTRNNController(CTRNNNeuron[] neurons, int[] sensorIndices,
            float stepSize,float axleWidth,int leftMotorID,int rightMotorID) {
        super(neurons, sensorIndices, stepSize);
        this.axleWidth = axleWidth;
        this.leftMotorID = leftMotorID;
        this.rightMotorID = rightMotorID;
    }

    @Override
    public void step(RobotInput input) {
        super.integrate(new float[]{(float)input.getRange()});
    }

    @Override
    public double getVelocity() {
        return (getNeurons()[leftMotorID].activation + getNeurons()[rightMotorID].activation) / 2;
    }

    @Override
    public double getAngularVelocity() {
        return (getNeurons()[rightMotorID].activation - getNeurons()[leftMotorID].activation) / axleWidth;
    }
    
   
    
}
