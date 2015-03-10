/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import mockit.Expectations;
import mockit.Mocked;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author miles
 */
public class GATest {

    public GATest() {
    }

    GA ga;
    @Mocked GAParameters gaparams;
    @Mocked Phenotype phenotype;
    @Mocked Population population;
    @Mocked Stats stats;

    @Before
    public void setUp() {
        ga = new GA(phenotype,gaparams);
    }

    @Test
    public void gAIsNotFinishedUntilGenerationsNumberReached() {
        new Expectations() {
            {
                gaparams.getGenerations();
                result = 10;
            }
        };
        assertFalse(ga.isFinished());
        for (int i = 0; i < 8; i++) {
            ga.doNextGeneration();
        }
        assertFalse(ga.isFinished());
    }
    
    @Test
    public void gAIsFinishedWhenGenerationsAreRun() {
        new Expectations() {
            {
                gaparams.getGenerations();
                result = 10;
            }
        };
        for (int i = 0; i < 10; i++) {
            ga.doNextGeneration();
        }
        assertTrue(ga.isFinished());
    }
    
    @Test(expected = IllegalStateException.class)
    public void stepThrowsStateExceptionWhenCalledAfterGAIsFinished() {
        for (int i = 0; i < 11; i++) {
            ga.doNextGeneration();
        }
    }
    
}
