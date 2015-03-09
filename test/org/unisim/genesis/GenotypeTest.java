/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import java.util.Random;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class GenotypeTest {

    public GenotypeTest() {
    }

    @Mocked Random rand;

    @Test
    public void withGenesCreatesGenomeWithGenes() {
        float[] genes = new float[]{-0.8f, -0.6f, -0.4f, -0.2f};
        Genotype g = Genotype.withGenes(genes);
        Assert.assertArrayEquals(genes, g.getGenes(), 0.001f);
    }

    @Test
    public void withRandomizedGenomeCreatesRandomGenome() {
        new Expectations() {
            {
                rand.nextFloat();
                returns(0.1f, 0.2f, 0.3f, 0.4f);
            }
        };
        Genotype g = Genotype.withRandomGenome(4);
        float[] expected = new float[]{-0.8f, -0.6f, -0.4f, -0.2f};
        Assert.assertArrayEquals(expected, g.getGenes(), 0.001f);
    }

    @Test
    public void mutateCreatesNewMutatedGenomeWithMutatedValues() {
        float[] genes = new float[]{-0.8f, -0.6f, -0.4f, -0.2f};
        float[] expected = new float[]{-0.7f, -0.7f, -0.2f, -0.4f};
        new Expectations() {
            {
                rand.nextGaussian();
                returns(0.1, -0.1, 0.2, -0.2);
            }
        };
        Genotype g = Genotype.withGenes(genes).mutate(1.0f);
        Assert.assertArrayEquals(expected, g.getGenes(), 0.001f);
    }

    @Test
    public void crossOverCreatesNewCrossedOverGenome() {
        float[] genes1 = new float[]{-0.8f, -0.6f, -0.4f, -0.2f};
        float[] genes2 = new float[]{0.8f, 0.6f, 0.4f, 0.2f};
        float[] expected = new float[]{0.8f, -0.6f, 0.4f, -0.2f};

        new Expectations() {
            {
                rand.nextFloat();
                returns(0.1f, 0.8f, 0.3f, 0.7f);
            }
        };

        Assert.assertArrayEquals(
                expected, 
                Genotype.withGenes(genes1).crossover(0.5f,Genotype.withGenes(genes2).getGenes()).getGenes(),
                0.001f
        );

    }
    
    @Test
    public void toStringTest() {
        float[] genes = new float[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        System.out.println(Genotype.withGenes(genes).toString());
    }

}
