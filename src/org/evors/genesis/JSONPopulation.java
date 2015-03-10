/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.genesis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.evors.genesis.Population;

/**
 *
 * @author miles
 */
public class JSONPopulation {
    
    public static JSONPopulation fromPopulation(Population pop) {
        JSONPopulation jpop = new JSONPopulation();
        jpop.setIndividuals(new Individual[pop.getSize()]);
        for (int i = 0; i < pop.getSize(); i++) {
            jpop.individuals[i] = new Individual(
                    pop.getIndividual(i).getFitness(),
                    pop.getIndividual(i).getGenotype().getGenes());
        }
        return jpop;
    }
    
    public static JSONPopulation fromJSONFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JSONPopulation jpop = mapper.readValue(file, JSONPopulation.class);
        return jpop;
    }
    
    private Individual[] individuals;
    
    public static class Individual {
        private float fitness;
        private float[] genes;

        public Individual(float fitness, float[] genes) {
            this.fitness = fitness;
            this.genes = genes;
        }

        public Individual() {
        }
        
        

        public float getFitness() {
            return fitness;
        }

        public void setFitness(float fitness) {
            this.fitness = fitness;
        }

        public float[] getGenes() {
            return genes;
        }

        public void setGenes(float[] genes) {
            this.genes = genes;
        }
        
        
    }

    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }
    
    public String toJSONString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        mapper.writeValue(bout, this);
        return bout.toString("utf8");
    }
    
}
