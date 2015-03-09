/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

/**
 *
 * @author miles
 */
public abstract class AbstractIndividual implements Comparable<AbstractIndividual> {
    
    protected final Genotype genotype;
    protected final Phenotype phenotype;
    protected float rawFitness = 0.0F;
    protected boolean rawFitnessSet = false;

    public AbstractIndividual(Phenotype phenotype, Genotype genotype) {
        this.genotype = genotype;
        this.phenotype = phenotype;
    }
    
    @Override
    public int compareTo(AbstractIndividual o) {
        return Float.valueOf(this.getRawFitness()).compareTo(o.getRawFitness());
    }

    public Genotype getGenotype() {
        return genotype;
    }


    public void crossover(float pCross, Genotype g) {
        genotype.crossover(pCross, g);
        rawFitness = calcRawFitness();
        phenotype.updateGenes(genotype.getGenes());
    }

    public float getRawFitness() {
        if (!rawFitnessSet) {
            rawFitness = calcRawFitness();
            rawFitnessSet = true;
        }
        return rawFitness;
    }

    public void mutate(float p_mutation) {
        genotype.mutate(p_mutation);
        rawFitness = calcRawFitness();
        phenotype.updateGenes(genotype.getGenes());
    }
    
    
    public abstract float calcRawFitness();
}
