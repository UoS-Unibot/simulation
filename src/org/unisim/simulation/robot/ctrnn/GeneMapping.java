package org.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Maintains a map of which genes are related to which parameters. Used for
 * loading a layout from a JSON file, but also intended for future use in e.g. a
 * UI or output log showing the genotype mappings.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class GeneMapping {

    HashMap<Integer, Gene> genes = new HashMap<>();
    private int highestGeneID = 0;

    public void add(int gID, int neuronID, Parameter type) {
        Gene gene = getGene(gID);
        Mapping mapping = new Mapping();
        mapping.neuronID = neuronID;
        mapping.type = type;
        gene.mappings.add(mapping);
        if (gID > highestGeneID) {
            highestGeneID = gID;
        }
    }

    public int getHighestGeneID() {
        return highestGeneID;
    }

    public void addWeight(int gID, int neuronID1, int neuronID2) {
        Gene gene = getGene(gID);
        Mapping mapping = new Mapping();
        mapping.neuronID = neuronID1;
        mapping.neuronID2 = neuronID2;
        mapping.type = Parameter.WEIGHT;
        gene.mappings.add(mapping);
        if (gID > highestGeneID) {
            highestGeneID = gID;
        }
    }

    public int getGeneCount() {
        return genes.size();
    }

    public Gene getGene(int gID) {
        Gene gene;
        if (genes.containsKey(gID)) {
            return genes.get(gID);
        } else {
            gene = new Gene();
            genes.put(gID, gene);
            return gene;
        }
    }

    public static enum Parameter {

        TAU, BIAS, GAIN, WEIGHT
    }

    public class Gene {

        ArrayList<Mapping> mappings = new ArrayList<>();
    }

    public class Mapping {

        int neuronID;
        Parameter type;
        int neuronID2;
    }
}
