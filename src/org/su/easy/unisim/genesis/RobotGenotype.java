/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.genesis;

import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Random;
/**
 *
 * @author Miles
 */
public class RobotGenotype implements Serializable{
    
    public CTRNNLayout layout = new CTRNNLayout();
    boolean GENOME_CHANGED = false;
    
    //private int nWeights;
    public int len;
    public float[] genes;
    int n;
    private float fitness = 0.0f;
    private String name = "";
    private String filename = "";
    private Random rand = new Random();
    

    public RobotGenotype(CTRNNLayout layout) {
        this.layout = layout;
        len = layout.getGenomeLength();
        n = layout.getTotalN();
        randomiseGenome();
        updateLayout();
    }
    
    
    public RobotGenotype(CTRNNLayout layout, float[] genes) {
        this(layout);
        if(layout.genomeLength != genes.length)
            throw new IllegalArgumentException(String.format("Genes provided (n=%d) do not match specified layout (n=%d)",genes.length, layout.genomeLength));
        this.genes = genes;
        updateLayout();
    }
    
    private void randomiseGenome() {
        genes = new float[len];
        for(int i = 0; i < len; i++)
            genes[i] = rand();
    }
    
    public void updateLayout() {
        layout.update(genes);
    }
    
    
    
    public RobotGenotype(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fs = new FileInputStream(filename);
        ObjectInputStream os = new ObjectInputStream(fs);
        RobotGenotype g = (RobotGenotype)os.readObject();
        os.close();fs.close();
        copyFrom(g);
        this.filename = filename;
    }
    
    
    private final void copyFrom(RobotGenotype g) {
        genes = g.genes;
        setFitness(g.getFitness());
        setName(g.getName());
    }
    
    
    private float rand() {
        return (float)rand.nextDouble() * 2 - 1;
    }
    
    
    
    private float randCreep(float mutVariance) {
        return (float)rand.nextGaussian() * mutVariance;
    }
    
    
    public void mutate(float mutVariance) {
        for(float g : genes) {
            float mut,dm;
            dm = randCreep(mutVariance);
            mut = g + dm;
            if(mut < -1.0)
                g = -2 - mut;
            else if(mut > 1.0)
                g = 2 - mut;
            else
                g = mut;
        }
        updateLayout();
    }
    
    public void saveToFile(String filename) throws FileNotFoundException, IOException {
        FileOutputStream fs = new FileOutputStream(filename);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(this);
        os.close();fs.close();
    }
    
    public int getNSensors() {
        return layout.sensorInputs.size();
    }

    /**
     * @return the fitness
     */
    public float getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    
    @Override
    public String toString() {
        String str = "";
        for(float gene : genes) {
            String prefix = gene > 0 ? "+" : "";
            str += prefix + String.format("%f\t", gene);
        }
        return str;
    }

}
