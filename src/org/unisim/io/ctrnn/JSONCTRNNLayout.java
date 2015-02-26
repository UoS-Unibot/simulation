package org.unisim.io.ctrnn;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.unisim.simulation.robot.ctrnn.CTRNNParamRanges;
import org.unisim.simulation.robot.ctrnn.LayoutBuilder;
import org.unisim.util.Range;

/**
 * Provides a POJO representation of CTRNNLayout, used for loading and saving
 * JSON files
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class JSONCTRNNLayout {

    private String filename = "";

    public JSONCTRNNLayout() {
    }

    public void saveToFile(File file) throws IOException {
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, this);
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
        HashMap<org.unisim.simulation.robot.ctrnn.Neuron, ArrayList<String>> conns = new HashMap<>();
        int idCount = 0;
        for (Layer l : layers) {
            lb.addNewLayer();
            String p = l.getParams();
            ParamRanges pr = findParamRange(p);
            if (pr == null) {
                throw new IllegalArgumentException("Parameter string '" + p + "' not found in defined parameter ranges");
            }
            for (Neuron n : l.getNeurons()) {
                org.unisim.simulation.robot.ctrnn.Neuron neuron = new org.unisim.simulation.robot.ctrnn.Neuron(idCount);

                neuron.name = n.getName();
                neuron.ID = idCount;
                idCount++;
                neuron.ParamRanges = new CTRNNParamRanges(
                        new Range(pr.getTauR()[0], pr.getTauR()[1]),
                        new Range(pr.getBiasR()[0], pr.getBiasR()[1]),
                        new Range(pr.getGainR()[0], pr.getGainR()[1]),
                        new Range(pr.getWeightsR()[0], pr.getWeightsR()[1])
                );
                if (letter1(n.getName()).equals("s")) {
                    lb.addSensorInput(Integer.parseInt(letter2(n.getName())));
                }
                if (letter1(n.getName()).equals("m")) {
                    lb.addMotorOutput(Integer.parseInt(letter2(n.getName())));
                }
                if (letter1(n.getTau()).equals("g")) {
                    neuron.tauG = 0;
                    neuron.tauGID = Integer.parseInt(n.getTau().substring(1));
                } else {
                    neuron.tauG = Float.parseFloat(n.getTau());
                    neuron.tauGID = -1;
                }
                if (letter1(n.getBias()).equals("g")) {
                    neuron.biasG = 0;
                    neuron.biasGID = Integer.parseInt(n.getBias().substring(1));
                } else {
                    neuron.biasG = Float.parseFloat(n.getBias());
                    neuron.biasGID = -1;
                }
                if (neuron.name.equals("m0")) {
                    if (letter1(n.getGain()).equals("g")) {
                        neuron.gainG = 0;
                        neuron.gainGID = Integer.parseInt(n.getGain().substring(1));
                    } else {
                        neuron.gainG = Float.parseFloat(n.getGain());
                        neuron.gainGID = -1;
                    }
                }

                if (n.getConns().length != n.getWeights().length) {
                    throw new ArrayIndexOutOfBoundsException(String.format("Mismatch: Neuron named %s has %d conns defined and %d weights", n.getName(), n.getConns().length, n.getWeights().length));
                }

                CTRNNParamRanges cpr = new CTRNNParamRanges();
                cpr.bias = new Range(pr.getTauR()[0], pr.getTauR()[1]);
                conns.put(neuron, new ArrayList<String>());
                for (int i = 0; i < n.getConns().length; i++) {
                    conns.get(neuron).add(n.getConns()[i]);
                    String w = n.getWeights()[i];
                    if (letter1(w).equals("g")) {
                        neuron.weightsG.add(0f);
                        neuron.weightsGID.add(Integer.parseInt(w.substring(1)));
                    } else {
                        neuron.weightsG.add(Float.parseFloat(w));
                        neuron.weightsGID.add(-1);
                    }
                }

                lb.addNeuronToLastLayer(neuron);
            }
        }

        for (org.unisim.simulation.robot.ctrnn.Neuron neuron : conns.keySet()) {
            for (String conn : conns.get(neuron)) {
                org.unisim.simulation.robot.ctrnn.Neuron neuron2 = lb.getNeuronByName(conn);
                if (neuron2 == null) {
                    throw new IllegalArgumentException("Neuron named " + conn + "not found in layout file");
                }
                neuron.conns.add(neuron2.ID);
            }
        }

        return new CTRNNLayout(lb.build());
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
        return "JSONCTRNNLayout{" + "metadata=" + metadata + ", paramranges=" + Arrays.toString(paramranges) + ", layers=" + Arrays.toString(layers) + '}';
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
