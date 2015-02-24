/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.genesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.su.easy.unisim.exp.ExpParam;
import org.su.easy.unisim.exp.ExperimentController;

/**
 *
 * @author Miles
 */
public class Population {

    ArrayList<Float> getFitnesses() {
        ArrayList<Float> ar = new ArrayList<>();
        for(RobotIndividual ind : population) {
            ar.add(ind.rawFitness());
        }
        return ar;
    }

    ArrayList<String> getGenotypes() {
        ArrayList<String> ar = new ArrayList<>();
        for(RobotIndividual ind : population) {
            ar.add(ind.genotype.toString());
        }
        return ar;
    }
    
    
    public ArrayList<RobotIndividual> population = new ArrayList<>();
    protected float p_mutation; protected float pCross;
    protected Random rand = new Random();
    ExpParam param;
    
    Population() {     }
    
    Population(int n_pop, float p_mutation, float pCross, ExpParam param,CTRNNLayout layout) {
        population = new ArrayList<>(n_pop);
        for(int i = 0; i < n_pop;i++) {
            population.add(new RobotIndividual(layout,param));
        }
        
        this.p_mutation = p_mutation; this.pCross = pCross;
        this.param = param;
    }
    
    
    public int getRandIndex() {
        return rand.nextInt(size());
    }
    
    public RobotIndividual getIndividual(int index) {
        return population.get(index);
    }

    public int size() {
        return population.size();
    }
    public boolean isABetterThanB(int a, int b)  {
        float aFit = population.get(a).rawFitness();
        float bFit = population.get(b).rawFitness();
        if(aFit == bFit)
            return false;
        return aFit > bFit;
    }

    public void crossover(int a, int b) {
        population.get(a).crossover(pCross, population.get(b));
    }

    public void mutate(int a)  {
        population.get(a).mutate(p_mutation);
    }
    
    public void saveToCSV(String filename) {
        try {
            Files.createDirectories(Paths.get(filename).getParent());
            File statsFile = new File(filename);
            try (FileWriter out = new FileWriter(statsFile)) {
                out.append("ID,Fitness,");
                int nGenes = population.get(0).genotype.len;
                
                for(int i = 0; i < nGenes; i++) {
                    out.append("gene " + i);
                    if(i < (nGenes - 1)) {
                        out.append(",");
                    }
                }
                out.append("\n");
                int i = 0;
                for(RobotIndividual ind : population) {
                    out.append(i + ",");
                    out.append(ind.rawFitness() + ",");
                    for(int j = 0; j < nGenes; j++) {
                        out.append(String.valueOf(ind.genotype.genes[j]));
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
            r.append(String.format("%.6f\t%s\n", ind.rawFitness(), ind.genotype.toString()));
        }
        return r.toString();
    }
        
        
}
