package org.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;

/**
 *
 * @author miles
 */
public class LayoutBuilder {

    private String filename = "";
    private int genomeLength = 0;

    private final ArrayList<Layer> layers = new ArrayList<>();

    private final ArrayList<Integer> motorOutputs = new ArrayList<>();
    private final ArrayList<Integer> sensorInputs = new ArrayList<>();
    private final GeneMapping geneMapping = new GeneMapping();

    public LayoutBuilder() {
    }

    public LayoutBuilder addNewLayer() {
        getLayers().add(new Layer());
        return this;
    }

    public LayoutBuilder addNeuronToLastLayer(Neuron neuron) {
        getLayers().get(getLayers().size() - 1).neurons.add(neuron);
        if (neuron.tauGID != -1) {
            getGeneMapping().add(neuron.tauGID, neuron.ID, GeneMapping.Parameter.TAU);
        }
        if (neuron.biasGID != -1) {
            getGeneMapping().add(neuron.biasGID, neuron.ID, GeneMapping.Parameter.BIAS);
        }
        if (neuron.gainGID != -1) {
            getGeneMapping().add(neuron.gainGID, neuron.ID, GeneMapping.Parameter.GAIN);
        }
        for (int i : neuron.weightsGID) {
            getGeneMapping().add(i, neuron.ID, GeneMapping.Parameter.WEIGHT);
        }
        return this;
    }

    public LayoutBuilder addSensorInput(int neuronID) {
        getSensorInputs().add(neuronID);
        return this;
    }

    public LayoutBuilder addMotorOutput(int neuronID) {
        getMotorOutputs().add(neuronID);
        return this;
    }

    public LayoutBuilder setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public LayoutBuilder build() {
        genomeLength = getGeneMapping().getHighestGeneID() + 1;
        return this;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return the genomeLength
     */
    public int getGenomeLength() {
        return genomeLength;
    }

    /**
     * @return the layers
     */
    public ArrayList<Layer> getLayers() {
        return layers;
    }

    /**
     * @return the motorOutputs
     */
    public ArrayList<Integer> getMotorOutputs() {
        return motorOutputs;
    }

    /**
     * @return the sensorInputs
     */
    public ArrayList<Integer> getSensorInputs() {
        return sensorInputs;
    }

    /**
     * @return the geneMapping
     */
    public GeneMapping getGeneMapping() {
        return geneMapping;
    }
    
    public Neuron getNeuronByName(String name) {
        for(Layer l : layers) {
            for(Neuron n : l.neurons) {
                if(n.name.equals(name))
                    return n;
            }
        }
        return null;
    }

}
