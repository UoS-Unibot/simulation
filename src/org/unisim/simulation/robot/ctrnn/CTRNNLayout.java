/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot.ctrnn;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.List;
import org.unisim.simulation.robot.ctrnn.CTRNN.CTRNNNeuron;

/**
 *
 * @author miles
 */
public class CTRNNLayout {

    int getNumberOfNeurons() {
        return allNeurons.size();
    }
    
    private final List<List<LayoutNeuron>> layers;
    private final List<LayoutNeuron> allNeurons = new ArrayList<>();
    private final List<Integer> sensorIDs;
    private final int leftMotorID,rightMotorID;
    private final String filename;
    private final GeneMapping geneMapping;
    private final float integrationStepSize;
    
    public CTRNNLayout(List<List<LayoutNeuron>> layers, List<Integer> sensorIDs,
            int leftMotorID, int rightMotorID, GeneMapping geneMapping,
            float integrationStepSize) {
        this("",layers, sensorIDs, leftMotorID, rightMotorID, geneMapping,
                integrationStepSize);
    }

    public CTRNNLayout(
            String filename,
            List<List<LayoutNeuron>> layers,
            List<Integer> sensorIDs,
            int leftMotorID, int rightMotorID, 
            GeneMapping geneMapping,
            float integrationStepSize
    ) {
        for(List<LayoutNeuron> layer : layers)
            allNeurons.addAll(layer);
        this.layers = layers;
        this.sensorIDs = sensorIDs;
        this.leftMotorID = leftMotorID;
        this.rightMotorID = rightMotorID;
        this.geneMapping = geneMapping;
        this.integrationStepSize = integrationStepSize;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    
    
    
    public int getGenotypeLength() {
        return geneMapping.getHighestGeneID() + 1;
    }
    
    public CTRNNController getCTRNNController(float[] genes) {
        CTRNNNeuron[] realNeurons = new CTRNNNeuron[allNeurons.size()];
        for (int i = 0; i < allNeurons.size(); i++) {
            realNeurons[i] = allNeurons.get(i).getCTRNNNeuron(allNeurons.size(), genes);
        }
        return new CTRNNController(realNeurons, Ints.toArray(sensorIDs), integrationStepSize, 0.6f,leftMotorID,rightMotorID);
    }

    @Override
    public String toString() {
        return "CTRNNLayout{" + "layers=" + layers + ", allNeurons=" + allNeurons +
                ", sensorIDs=" + sensorIDs + ", leftMotorID=" + leftMotorID +
                ", rightMotorID=" + rightMotorID + ", geneMapping=" + geneMapping +
                ", integrationStepSize=" + integrationStepSize + '}';
    }

    
    
}
