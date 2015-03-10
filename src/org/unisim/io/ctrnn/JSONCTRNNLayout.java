/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.io.ctrnn;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.unisim.simulation.robot.ctrnn.LayoutBuilder;
import org.unisim.simulation.robot.ctrnn.LayoutNeuron;
import org.unisim.simulation.robot.ctrnn.LayoutNeuron.NeuronParameter;
import org.unisim.util.Range;

/**
 *
 * @author miles
 */
public class JSONCTRNNLayout {

    private String filename = "";

    public JSONCTRNNLayout() {
    }

    public void saveToFile(File file) throws IOException {
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file,
                this);
    }

    public static JSONCTRNNLayout fromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JSONCTRNNLayout layout = mapper.readValue(file, JSONCTRNNLayout.class);
        layout.filename = file.getAbsolutePath();
        return layout;
    }

    public CTRNNLayout toCTRNNLayout() {
        LayoutBuilder lb = new LayoutBuilder();
        lb.setFilename(filename);
        HashMap<LayoutNeuron, ArrayList<String>> conns
                = new HashMap<>();
        int idCount = 0;
        List<String> neuronNames = new ArrayList<>();
        for(Layer l : layers)
            for(Neuron n : l.getNeurons())
                neuronNames.add(n.getName());        
        
        for (Layer l : layers) {
            lb.addNewLayer();
            String p = l.getParams();
            ParamRanges pr = findParamRange(p);
            if (pr == null) {
                throw new IllegalArgumentException("Parameter string '" + p
                        + "' not found in defined parameter ranges");
            }
            for (Neuron n : l.getNeurons()) {

                String name = n.getName();
                int ID = idCount;
                idCount++;
                Map<LayoutNeuron.CTRNNParameterType, Range> paramRanges
                        = new HashMap<>();
                paramRanges.put(LayoutNeuron.CTRNNParameterType.TAU, new Range(
                        pr.getTauR()[0], pr.getTauR()[1]));
                paramRanges.put(LayoutNeuron.CTRNNParameterType.BIAS, new Range(
                        pr.getBiasR()[0], pr.getBiasR()[1]));
                paramRanges.put(LayoutNeuron.CTRNNParameterType.GAIN, new Range(
                        pr.getGainR()[0], pr.getGainR()[1]));
                paramRanges.put(LayoutNeuron.CTRNNParameterType.WEIGHT,
                        new Range(pr.getWeightsR()[0], pr.getWeightsR()[1]));

                if (n.getName().startsWith("s")) {
                    lb.addSensorInput(Integer.parseInt(letter2(n.getName())));
                }

                if (n.getName().startsWith("m")) {
                    if (n.getName().endsWith("L")) {
                        lb.setLeftMotorID(ID);
                    } else if (n.getName().endsWith("R")) {
                        lb.setRightMotorID(ID);
                    }
                }

                NeuronParameter gainParam, tauParam, biasParam;

                if (n.getTau().startsWith("g")) {
                    tauParam = NeuronParameter.withGeneIndex(Integer.parseInt(n.
                            getTau().substring(1)));
                } else {
                    tauParam = NeuronParameter.withActualValue(Float.parseFloat(
                            n.getTau()));
                }

                if (n.getBias().startsWith("g")) {
                    gainParam = NeuronParameter.withGeneIndex(Integer.parseInt(
                            n.getBias().substring(1)));
                } else {
                    gainParam = NeuronParameter.withActualValue(Float.
                            parseFloat(n.getBias()));
                }

                if (n.getGain().startsWith("g")) {
                    biasParam = NeuronParameter.withGeneIndex(Integer.parseInt(
                            n.getGain().substring(1)));
                } else {
                    biasParam = NeuronParameter.withActualValue(Float.
                            parseFloat(n.getGain()));
                }

                if (n.getConns().length != n.getWeights().length) {
                    throw new ArrayIndexOutOfBoundsException(String.format(
                            "Mismatch: Neuron named %s has %d conns defined and %d weights",
                            n.getName(), n.getConns().length,
                            n.getWeights().length));
                }
                List<NeuronParameter> weights = new ArrayList<>();
                List<Integer> connectionIDs = new ArrayList<>();
                for (int i = 0; i < n.getConns().length; i++) {
                    
                    int index = neuronNames.indexOf(n.getConns()[i]);
                    if(index == -1)
                        throw new IndexOutOfBoundsException("Neuron name " + n.getConns()[i] + " not found in file.");
                    connectionIDs.add(index);
                    String w = n.getWeights()[i];
                    if (letter1(w).equals("g")) {
                        weights.add(NeuronParameter.withGeneIndex(Integer.
                                parseInt(w.substring(1))));
                    } else {
                        weights.add(NeuronParameter.withActualValue(Float.parseFloat(w)));
                    }
                }
                Map<LayoutNeuron.CTRNNParameterType, NeuronParameter> parameters
                        = new HashMap<>();
                parameters.put(LayoutNeuron.CTRNNParameterType.TAU, tauParam);
                parameters.put(LayoutNeuron.CTRNNParameterType.BIAS, biasParam);
                parameters.put(LayoutNeuron.CTRNNParameterType.GAIN, gainParam);

                lb.addNeuronToLastLayer(new LayoutNeuron(ID, name, parameters,
                        paramRanges, weights, connectionIDs));
            }
        }


        return lb.build();
    }

    private String letter1(String str) {
        return str.substring(0, 1);
    }

    private String letter2(String str) {

        return str.substring(1);
    }

    private ParamRanges findParamRange(String name) {
        for (ParamRanges pr : paramranges) {
            if (pr.getName() == null ? name == null : pr.getName().equals(name)) {
                return pr;
            }
        }
        return null;
    }

    private Metadata metadata;
    private ParamRanges[] paramranges;
    private Layer[] layers;

    @Override
    public String toString() {
        return "JSONCTRNNLayout{" + "metadata=" + metadata + ", paramranges="
                + Arrays.toString(paramranges) + ", layers=" + Arrays.toString(
                        layers) + '}';
    }

    public org.unisim.io.ctrnn.Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(org.unisim.io.ctrnn.Metadata metadata) {
        this.metadata = metadata;
    }

    public ParamRanges[] getParamranges() {
        return paramranges;
    }

    public void setParamranges(ParamRanges[] paramranges) {
        this.paramranges = paramranges;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

}
