/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot.ctrnn;

import java.util.ArrayList;
import java.util.List;
import org.evors.rs.sim.robot.ctrnn.LayoutNeuron.NeuronParameter;

/**
 *
 * @author miles
 */
public class LayoutBuilder {

    private String filename = "";
    private int genomeLength = 0;

    private final List<List<LayoutNeuron>> layers = new ArrayList<>();

    private int leftMotorID = 0, rightMotorID = 0;
    private final List<Integer> sensorInputs = new ArrayList<>();
    private final GeneMapping geneMapping = new GeneMapping();

    public LayoutBuilder() {
    }

    public LayoutBuilder addNewLayer() {
        layers.add(new ArrayList());
        return this;
    }

    public LayoutBuilder addNeuronToLastLayer(LayoutNeuron neuron) {
        layers.get(layers.size() - 1).add(neuron);

        LayoutNeuron.NeuronParameter gainParam = neuron.getParameter(
                LayoutNeuron.CTRNNParameterType.GAIN),
                tauParam = neuron.getParameter(
                        LayoutNeuron.CTRNNParameterType.TAU),
                biasParam = neuron.getParameter(
                        LayoutNeuron.CTRNNParameterType.BIAS);

        if (tauParam.usesGene()) {
            geneMapping.add(tauParam.getGeneIndex(), neuron.getID(),
                    GeneMapping.Parameter.TAU);
        }
        if (biasParam.usesGene()) {
            geneMapping.add(biasParam.getGeneIndex(), neuron.getID(),
                    GeneMapping.Parameter.BIAS);
        }
        if (gainParam.usesGene()) {
            geneMapping.add(gainParam.getGeneIndex(), neuron.getID(),
                    GeneMapping.Parameter.GAIN);
        }
        for (NeuronParameter weight : neuron.getWeights()) {
            if (weight.usesGene()) {
                geneMapping.add(weight.getGeneIndex(), neuron.getID(),
                        GeneMapping.Parameter.WEIGHT);
            }
        }
        return this;
    }

    public void setLeftMotorID(int leftMotorID) {
        this.leftMotorID = leftMotorID;
    }

    public void setRightMotorID(int rightMotorID) {
        this.rightMotorID = rightMotorID;
    }
    
    public LayoutBuilder addSensorInput(int neuronID) {
        sensorInputs.add(neuronID);
        return this;
    }

    public LayoutBuilder setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public CTRNNLayout build() {
        return new CTRNNLayout(layers, sensorInputs, leftMotorID, rightMotorID,
                geneMapping, 0.1f);
    }

    public LayoutNeuron getNeuronByName(String name) {
        for (List<LayoutNeuron> layer : layers) {
            for (LayoutNeuron n : layer) {
                if (n.getName().equals(name)) {
                    return n;
                }
            }
        }
        return null;
    }

}
