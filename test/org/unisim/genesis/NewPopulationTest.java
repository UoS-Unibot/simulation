/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import java.util.Random;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class NewPopulationTest {
    
    public NewPopulationTest() {
    }
    
    @Mocked Random rand;
    @Mocked Phenotype phenotype;
    @Mocked Individual ind;
    //@Injectable Individual newInd;
    Population pop;

    @Test
    public void populationSelectsRandomIndexWithinPopulation() {
        pop = new Population(phenotype, 10);
        new Expectations() {{
            rand.nextInt(10);
        }};
        pop.getRandomIndex();
    }
    
    @Test
    public void populationSelectsSecondIndWithinDemeSize() {
        pop = new Population(phenotype, 10);
        new Expectations() {{
            rand.nextInt(anyInt); result = 2;
        }};
        assertEquals(3,pop.getRandomWithinDeme(5,4));
        
    }
    
    @Test
    public void populationWrapsSecondIndIfOutOfBounds() {
        pop = new Population(phenotype, 10);
        new Expectations() {{
            rand.nextInt(anyInt); result = 14;
        }};
        assertEquals(1,pop.getRandomWithinDeme(5,8));
    }
    
    @Test
    public void getRandomWithinDemeLoopsUntilDifferentIndividualsFound() {
         pop = new Population(phenotype, 10);
        new Expectations() {{
            rand.nextInt(anyInt); returns(4,4,4,2);
        }};
        assertEquals(3,pop.getRandomWithinDeme(5,4));
    }
    
    @Test
    public void reproduceMutatesIndividualWithLowerFitness() {
        pop = new Population(phenotype, 2);
        new Expectations() {{
            ind.getFitness(); returns(2f,1f);
            ind.reproduce(anyFloat, anyFloat, (float[])any);result = ind;
            ind.getFitness(); returns(2f,4f);
        }};
        pop.doReproduction(0, 1, 0.5f, 0.5f);
        assertEquals(4f,pop.getIndividual(1).getFitness(),0.001);
    }
}
