/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot.ctrnn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.evors.core.util.Range;

/**
 *
 * @author miles
 */
public class LayoutNeuron {

    public static enum CTRNNParameterType {

        TAU, BIAS, GAIN, WEIGHT
    }

    public static class NeuronParameter {

        private final float actualValue;
        private final int geneIndex;
        private final boolean usesGene;

        public static NeuronParameter withActualValue(float actualValue) {
            return new NeuronParameter(actualValue, -1, false);
        }

        public static NeuronParameter withGeneIndex(int geneIndex) {
            return new NeuronParameter(0, geneIndex, true);
        }

        private NeuronParameter(float actualValue, int geneIndex,
                boolean usesGene) {
            this.actualValue = actualValue;
            this.geneIndex = geneIndex;
            this.usesGene = usesGene;
        }

        public float getActualValue() {
            return actualValue;
        }

        public int getGeneIndex() {
            return geneIndex;
        }

        public boolean usesGene() {
            return usesGene;
        }

        @Override
        public String toString() {
            if(usesGene)
                return "{gene index " + String.valueOf(geneIndex) + "}";
            return "{value " + String.valueOf(actualValue) + "}";
        }
        
        

    }
    private final Map<CTRNNParameterType, NeuronParameter> parameters;
    private final Map<CTRNNParameterType, Range> paramRanges;
    private final List<NeuronParameter> weights;
    private final List<Integer> connectionIDs;
    private final String name;
    private final int ID;

    public LayoutNeuron(
            int ID,
            String name,
            Map<CTRNNParameterType, NeuronParameter> parameters,
            Map<CTRNNParameterType, Range> paramRanges,
            List<NeuronParameter> weights,
            List<Integer> connectionIDs) {
        this.parameters = parameters;
        this.paramRanges = paramRanges;
        this.weights = weights;
        this.connectionIDs = connectionIDs;
        this.name = name;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public List<NeuronParameter> getWeights() {
        return weights;
    }
    
    

    public String getName() {
        return name;
    }
    
    public NeuronParameter getParameter(CTRNNParameterType type) {
        return parameters.get(type);
    }

    public CTRNN.CTRNNNeuron getCTRNNNeuron(int totalN, float[] genes) {
        float gain, tau, bias;
        NeuronParameter gainParam = parameters.get(
                CTRNNParameterType.GAIN),
                tauParam = parameters.get(CTRNNParameterType.TAU),
                biasParam = parameters.get(CTRNNParameterType.BIAS);

        if (gainParam.usesGene()) {
            gain = paramRanges.get(CTRNNParameterType.GAIN).map(
                    genes[gainParam.getGeneIndex()]);
        } else {
            gain = gainParam.getActualValue();
        }

        if (tauParam.usesGene()) {
            tau = paramRanges.get(CTRNNParameterType.TAU).map(
                    genes[tauParam.getGeneIndex()]);
        } else {
            tau = tauParam.getActualValue();
        }

        if (biasParam.usesGene()) {
            bias = paramRanges.get(CTRNNParameterType.BIAS).map(
                    genes[biasParam.getGeneIndex()]);
        } else {
            bias = biasParam.getActualValue();
        }

        float[] weightsArr = new float[totalN];
        Arrays.fill(weightsArr, 0);
        for (int i = 0; i < connectionIDs.size(); i++) {
            int connID = connectionIDs.get(i);
            NeuronParameter weight = weights.get(i);
            if (weight.usesGene()) {
                weightsArr[connID] = paramRanges.get(
                        CTRNNParameterType.WEIGHT).map(genes[weight.
                                getGeneIndex()]);
            } else {
                weightsArr[connID] = weight.getActualValue();
            }
        }

        return new CTRNN.CTRNNNeuron(gain, bias, tau, weightsArr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LayoutNeuron{\n\tID=");
        sb.append(ID);
        sb.append("\n\tname=");sb.append(name);
        sb.append("\n\tParamRanges={");
        for (CTRNNParameterType p : paramRanges.keySet()) {
            sb.append("\n\t\t");sb.append(p.name());sb.append("=");
            sb.append(paramRanges.get(p).toString());
        }
        
        sb.append("\n\tParameters={");
        for (CTRNNParameterType p : parameters.keySet()) {
            sb.append("\n\t\t");sb.append(p.name());sb.append("=");
            sb.append(parameters.get(p).toString());
        }
        
        sb.append("\n}");
        return sb.toString();
    }
    
    
}
