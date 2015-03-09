/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.unisim.exp.Experiment;
import org.unisim.exp.ExperimentController;
import org.unisim.exp.params.Parameters;
import org.unisim.util.DataFile;

/**
 * 
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Population<I extends AbstractIndividual> {

    ArrayList<Float> getFitnesses() {
        ArrayList<Float> ar = new ArrayList<>();
        for(AbstractIndividual ind : population) {
            ar.add(ind.getRawFitness());
        }
        return ar;
    }

    ArrayList<String> getGenotypes() {
        ArrayList<String> ar = new ArrayList<>();
        for(AbstractIndividual ind : population) {
            ar.add(ind.getGenotype().toString());
        }
        return ar;
    }
    
    
    public ArrayList<AbstractIndividual> population = new ArrayList<>();
    protected float p_mutation; protected float pCross;
    protected Random rand = new Random();
    Parameters param;
    
    Population() {     }
    
    Population(Experiment exp) {
        param = exp.getParam();
        population = new ArrayList<>(param.getGa_population());
        for(int i = 0; i < param.getGa_population();i++) {
            population.add(new RobotIndividual(exp));
        }
        
        this.p_mutation = (float) param.getGa_mutrate(); this.pCross = (float) param.getGa_crossrate();
    }
    
    
    public int getRandIndex() {
        return rand.nextInt(size());
    }
    
    public AbstractIndividual getIndividual(int index) {
        return population.get(index);
    }

    public int size() {
        return population.size();
    }
    public boolean isABetterThanB(int a, int b)  {
        float aFit = population.get(a).getRawFitness();
        float bFit = population.get(b).getRawFitness();
        if(aFit == bFit)
            return false;
        return aFit > bFit;
    }

    public void crossover(int a, int b) {
        population.get(a).crossover(pCross, population.get(b).getGenotype());
    }

    public void mutate(int a)  {
        population.get(a).mutate(p_mutation);
    }
    
    public DataFile asDataFile() {
        DataFile df = new DataFile();
        df.addHeaders("ID","Fitness");
        int i = 0;
        for(AbstractIndividual ind : population) {
            Object[] data = new Object[ind.getGenotype().getLength() + 2];
            data[0] = i; data[1] = ind.getRawFitness();
            for(int g = 0; g < ind.getGenotype().getLength(); g++) {
                df.addHeaders("gene " + g);
                data[g + 2] = ind.getGenotype().getGenes()[g];
            }
            i++;
        }
        return df;
    }
    
    public void saveToCSV(String filename) {
        try {
            Files.createDirectories(Paths.get(filename).getParent());
            File statsFile = new File(filename);
            try (FileWriter out = new FileWriter(statsFile)) {
                out.append("ID,Fitness,");
                int nGenes = population.get(0).getGenotype().getLength();
                
                for(int i = 0; i < nGenes; i++) {
                    out.append("gene " + i);
                    if(i < (nGenes - 1)) {
                        out.append(",");
                    }
                }
                out.append("\n");
                int i = 0;
                for(AbstractIndividual ind : population) {
                    out.append(i + ",");
                    out.append(ind.getRawFitness() + ",");
                    for(int j = 0; j < nGenes; j++) {
                        out.append(String.valueOf(ind.getGenotype().getGenes()[j]));
                        if(j < (nGenes - 1))
                            out.append(",");
                    }
                    out.append("\n");
                    i++;
                }                
                                
                out.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString() {
        ArrayList<RobotIndividual> sortedPop = (ArrayList<RobotIndividual>) population.clone();
        Collections.sort(sortedPop);
        StringBuilder r = new StringBuilder("Fitness\tGenotype\n");
        for(RobotIndividual ind : sortedPop) {
            r.append(String.format("%.6f\t%s\n", ind.getRawFitness(), ind.getGenotype().toString()));
        }
        return r.toString();
    }
        
        
}
