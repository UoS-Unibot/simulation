/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.robot.ctrnn.jsonIO;

import java.util.Arrays;

/**
 *
 * @author miles
 */
public class ParamRanges {

    private String name;
    private int[] tauR;
    private int[] gainR;
    private int[] biasR;
    private int[] weightsR;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getTauR() {
        return tauR;
    }

    public void setTauR(int[] tauR) {
        if(tauR.length != 2)
            throw new IllegalArgumentException("Tau parameter ranges must have 2 elements: [low, high], instead got " + tauR.length);
        this.tauR = tauR;
    }

    public int[] getGainR() {
        return gainR;
    }

    public void setGainR(int[] gainR) {
        if(gainR.length != 2)
            throw new IllegalArgumentException("Tau parameter ranges must have 2 elements: [low, high], instead got " + gainR.length);
        this.gainR = gainR;
    }

    public int[] getBiasR() {
        return biasR;
    }

    public void setBiasR(int[] biasR) {
        if(biasR.length != 2)
            throw new IllegalArgumentException("Tau parameter ranges must have 2 elements: [low, high], instead got " + biasR.length);
        this.biasR = biasR;
    }

    public int[] getWeightsR() {
        return weightsR;
    }

    public void setWeightsR(int[] weightsR) {
        this.weightsR = weightsR;
    }

    @Override
    public String toString() {
        return "ParamRanges{" + "name=" + name + ", tauR=" + Arrays.toString(tauR) + ", gainR=" + Arrays.toString(gainR) + ", biasR=" + Arrays.toString(biasR) + ", weightsR=" + Arrays.toString(weightsR) + '}';
    }

}
