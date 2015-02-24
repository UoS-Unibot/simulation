/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;

/**
 * Specifies a layout for a CTRNN, with multiple layers of neurons.
 *
 * @author Miles
 */
public class CTRNNLayout {

    public CTRNNLayout(LayoutBuilder builder) {
        filename = builder.getFilename();
        layers = builder.getLayers();
        geneMapping = builder.getGeneMapping();
        genomeLength = builder.getGenomeLength();
        sensorInputs = builder.getSensorInputs();
        motorOutputs = builder.getMotorOutputs();
    }

    public String filename = "";
    public ArrayList<Layer> layers = new ArrayList<>();
    public int genomeLength = 0;
    public ArrayList<Integer> sensorInputs = new ArrayList<>();
    public ArrayList<Integer> motorOutputs = new ArrayList<>();
    public GeneMapping geneMapping = new GeneMapping();

    public CTRNNLayout() {
    }

    public int getTotalN() {
        int t = 0;
        for (Layer l : layers) {
            t += l.neurons.size();
        }
        return t;
    }

    public Neuron getFromID(int ID) {
        for (Layer l : layers) {
            for (Neuron n : l.neurons) {
                if (ID == n.ID) {
                    return n;
                }
            }
        }
        return null;
    }

    public ArrayList<Neuron> getAllNeurons() {
        ArrayList<Neuron> neurons = new ArrayList<>(getTotalN());
        for (Layer l : layers) {
            for (Neuron n : l.neurons) {
                neurons.add(n);
            }
        }
        return neurons;
    }

    public Neuron getMotorNeuron(boolean getLeft) {
        if (getLeft) {
            return getFromID(motorOutputs.get(0));
        } else {
            return getFromID(motorOutputs.get(1));
        }
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public void update(float[] genes) {
        for (Layer layer : layers) {
            for (Neuron neuron : layer.neurons) {
                float tauG, biasG, gainG;
                ArrayList<Float> weightsG = new ArrayList<>(neuron.weightsG.size());

                if (neuron.tauGID == -1) {
                    tauG = neuron.tauG;
                } else {
                    tauG = genes[neuron.tauGID];
                }

                if (neuron.biasGID == -1) {
                    biasG = neuron.biasG;
                } else {
                    biasG = genes[neuron.biasGID];
                }

                if (neuron.gainGID == -1) {
                    gainG = neuron.gainG;
                } else {
                    gainG = genes[neuron.gainGID];
                }

                for (int i = 0; i < neuron.weightsGID.size(); i++) {
                    if (neuron.weightsGID.get(i) == -1) {
                        weightsG.add(neuron.weightsG.get(i));
                    } else {
                        weightsG.add(genes[neuron.weightsGID.get(i)]);
                    }
                }

                neuron.setGenes(tauG, biasG, gainG, weightsG);
            }
        }
    }

    @Override
    public String toString() {
        return "CTRNNLayout{" + "filename=" + filename + ", layers=" + layers + ", genomeLength=" + genomeLength + ", sensorInputs=" + sensorInputs + ", motorOutputs=" + motorOutputs + ", geneMapping=" + geneMapping + '}';
    }

    

}
