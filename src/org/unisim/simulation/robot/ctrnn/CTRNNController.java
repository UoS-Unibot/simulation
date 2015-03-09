/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;
import java.util.Collection;
import org.unisim.genesis.Genotype;
import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.RobotInput;

/**
 * Simulates a CTRNN. Loads neurons into an array from a CTRNNLayout.
 * @author Miles
 */
public class CTRNNController implements IRobotController{
    
    int n; //number of neurons
    int nSensors; //number of sensory inputs in the agent. The first nSensor neurons will receive input
    public Neuron[] neurons; //array of neurons
    public Collection<Collection<Neuron>> neuronLayers; //stores layers of neurons
    public double[] taus,biases,gains,states,outputs;
    public double[][] weights;
    public int outIndL,outIndR;
    public ArrayList<Integer> sensNeurIndices;
    private final double axleWidth;
    private final double timeStep;
    
    public CTRNNController(CTRNNLayout layout, double timeStep) {
        this(layout,timeStep,0.6f);
    }
    
    /**
     * Creates a new CTRNN with specified number of sensors and genotype
     * @param nSensors
     * @param g 
     */
    public CTRNNController(CTRNNLayout layout, double timeStep,double axleWidth) {
        this.timeStep = timeStep;
        this.n = layout.getTotalN(); //get number of neurons
        this.axleWidth = axleWidth;
        taus = new double[n];
        biases = new double[n];
        gains = new double[n];
        states = new double[n];
        outputs = new double[n];
        weights = new double[n][n];
        
        nSensors = layout.sensorInputs.size();
        
        
        sensNeurIndices = layout.sensorInputs;
        
        ArrayList<org.unisim.simulation.robot.ctrnn.Neuron> neurs = layout.getAllNeurons();
        for(int i = 0; i < n; i++) {
            org.unisim.simulation.robot.ctrnn.Neuron neuron = neurs.get(i);
            taus[i] = neuron.getMappedTau();
            gains[i] = neuron.getMappedGain();
            biases[i] = neuron.getMappedBias();
            for(int j = 0; j < n; j++) {
                if(neuron.conns.contains(j)) {
                    weights[i][j] = neuron.getMappedWeights().get(neuron.conns.indexOf(j));
                } else
                    weights[i][j] = 0;
            }
            states[i] = 0.5f;
            outputs[i] = 0f;
        }
        outIndL = layout.getMotorNeuron(true).ID;
        outIndR = layout.getMotorNeuron(false).ID;
        
    }
    
    
    /**
     * Integrates one time step
     * @param inputs 
     */
    public void step(double[] inputs) {
        EulerStep(timeStep,inputs);
    }
    
    /**
     * Integrates one step using Euler's method (see any CTRNN resource)
     * @param stepSize
     * @param inputs 
     */
    public void EulerStep(double stepSize, double[] inputs) {
        for(int i = 0; i < n; i++) {
            double input;
            int sensInd = sensNeurIndices.indexOf(i);
            if(sensInd != -1)
                input = inputs[sensInd];
            else
                input = 0;
            for(int j = 0; j < n; j++)
                input += weights[j][i] * outputs[j];
            states[i] += stepSize * taus[i] * (input - states[i]);
            outputs[i] = sigmoid(gains[i] * (states[i] + biases[i]));
        }
    }
    /**
     * @return motor output: relative difference between motor neurons (last two neurons)
     */
    public double getMotorLOutput() {
        return outputs[outIndL];
    }
    /**
     * @return motor output: relative difference between motor neurons (last two neurons)
     */
    public double getMotorROutput() {
        return outputs[outIndR];
    }
    /**
     * Standard sigmoid function.
     * @param x
     * @return 
     */
    protected double sigmoid(double x) {
        return 1.0f / (1 + (double)Math.exp(-x));
    }
    /**
     * Inverse standard sigmoid function.
     * @param x
     * @return 
     */
    private double invSigmoid(double x) {
        return (double)Math.log(x / (1 - x));
    }

    @Override
    public void step(RobotInput input) {
        step(new double[]{input.getRange()});
    }

    @Override
    public double getVelocity() {
        return (getMotorLOutput() + getMotorROutput()) / 2;
    }

    @Override
    public double getAngularVelocity() {
        return (getMotorROutput() - getMotorLOutput()) / axleWidth;
    }
    
}
