package org.evors.genesis;

/**
 * The Phenotype interface calculates the fitness of an array of genes, also
 * providing the number of genes that should be evolved.
 *
 * @author miles
 */
public interface Phenotype {

    int getGenomeLength();

    public float calculateFitness(float[] genes);

}
