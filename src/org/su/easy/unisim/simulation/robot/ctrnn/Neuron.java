/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Miles
 */
public class Neuron {
    public int ID = -1;
    public ArrayList<Integer> conns = new ArrayList<>(); //stores IDs of neurons this connects to
    public CTRNNParamRanges ParamRanges;
    
    public float tauG,gainG,biasG;
    public int tauGID,gainGID,biasGID;
    public ArrayList<Integer> weightsGID = new ArrayList<>();
    public ArrayList<Float> weightsG = new ArrayList<>();
    public String name = "";

    public Neuron(int ID) {
        this.ID = ID;
    }
    public Neuron(int ID, CTRNNParamRanges params) {
        this.ID = ID; this.ParamRanges = params;
    }
    public Neuron(int ID, CTRNNParamRanges params,ArrayList<Integer> conns) {
        if(conns != null)
            this.conns = conns;
        this.ID = ID; this.ParamRanges = params;
    }
    
    public void setGeneLoci(int tau, int gain, int bias, ArrayList<Integer> weights) {
        tauGID = tau; gainGID= gain; biasGID = bias;
        if(weights != null)
            weightsGID = (ArrayList<Integer>)weights.clone();
    }
    
    public void setGenes(float tau, float bias, float gain, ArrayList<Float> weights) {
        if(weights != null)
            this.weightsG = (ArrayList<Float>) weights.clone();
        this.tauG=tau;this.biasG=bias;this.gainG=gain;
    }
    
    public float getMappedTau() {
        return ParamRanges.tau.map(tauG);
    }
    public float getMappedGain() {
        return ParamRanges.gain.map(gainG);
    }
    public float getMappedBias() {
        return ParamRanges.bias.map(biasG);
    }
    public ArrayList<Float> getMappedWeights() {
        return ParamRanges.weights.map(weightsG);
    }

    @Override
    public String toString() {
        return "\n\tNeuron{" + "ID=" + ID + ", conns=" + conns + ", ParamRanges=" + ParamRanges + ", tauG=" + tauG + ", gainG=" + gainG + ", biasG=" + biasG + ", tauGID=" + tauGID + ", gainGID=" + gainGID + ", biasGID=" + biasGID + ", weightsGID=" + weightsGID + ", weightsG=" + weightsG + ", name=" + name + '}';
    }
    
    
}
