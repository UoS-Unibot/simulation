/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.io.pop;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.unisim.genesis.Phenotype;
import org.unisim.genesis.Population;

/**
 *
 * @author miles
 */
public class JSONPopulationTest {
    
    public JSONPopulationTest() {
    }

    @Test
    public void testJSONPopulationStringOutput() throws IOException {
        Population pop = new Population(new Phenotype() {

            @Override
            public int getGenomeLength() {
                return 10;
            }

            @Override
            public float calculateFitness(float[] genes) {
                float sum = 0;
                for(Float g : genes)
                    sum += g;
                return sum;
            }
        }, 10);
        JSONPopulation jpop = JSONPopulation.fromPopulation(pop);
        System.out.println(jpop.toJSONString());
    }
    
}
