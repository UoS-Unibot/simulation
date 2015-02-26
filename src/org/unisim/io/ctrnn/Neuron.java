package org.unisim.io.ctrnn;

import java.util.Arrays;

/**
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class Neuron {

    private String name;
    private String tau;
    private String gain;
    private String bias;
    private String[] conns;
    private String[] weights;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTau() {
        return tau;
    }

    public void setTau(String tau) {
        this.tau = tau;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public String getBias() {
        return bias;
    }

    public void setBias(String bias) {
        this.bias = bias;
    }

    public String[] getConns() {
        return conns;
    }

    public void setConns(String[] conns) {
        this.conns = conns;
    }

    public String[] getWeights() {
        return weights;
    }

    public void setWeights(String[] weights) {
        this.weights = weights;
    }

    @Override
    public String toString() {
        return "Neuron{" + "name=" + name + ", tau=" + tau + ", gain=" + gain + ", bias=" + bias + ", conns=" + Arrays.toString(conns) + ", weights=" + Arrays.toString(weights) + '}';
    }

}
