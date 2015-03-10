/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.genesis;

import java.util.Objects;

/**
 * Represents an individual in a population, with a genotype. It is immutable;
 * fitness is calculated via phenotype.calculateFitness with the genotype's
 * genes, or can be
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public final class Individual {

    private final Genotype genotype;
    private final float fitness;
    private final Phenotype phenotype;

    /**
     * Creates a new Individual based on the phenotype given with random genes.
     *
     * @param phenotype phenotype specifying the genotype.
     * @return a new Individual with random genes.
     */
    public static Individual withRandomGenes(Phenotype phenotype) {
        return new Individual(Genotype.withRandomGenome(phenotype.
                getGenomeLength()), phenotype);
    }

    /**
     * Creates a new Individual with the specified genotype, phenotype and
     * fitness.
     *
     * @param genotype genotype with specified genes.
     * @param phenotype phenotype to specify
     * @param fitness measured fitness of this individual
     */
    protected Individual(Genotype genotype, Phenotype phenotype, float fitness) {
        this.genotype = genotype;
        this.fitness = fitness;
        this.phenotype = phenotype;
    }

    /**
     * Creates a new Individual with the specified genotype and phenotype.
     * Fitness is then calculated via the phenotype.
     *
     * @param genotype genotype with specified genes.
     * @param phenotype measured fitness of this individual
     */
    protected Individual(Genotype genotype, Phenotype phenotype) {
        this(genotype, phenotype, phenotype.
                calculateFitness(genotype.getGenes()));
    }

    /**
     * Gets the fitness of this Individual.
     *
     * @return fitness
     */
    public float getFitness() {
        return fitness;
    }

    /**
     * Gets the genotype of this Individual.
     *
     * @return the genotype.
     */
    public Genotype getGenotype() {
        return genotype;
    }

    /**
     * Produces a new individual, mutating the genes and crossing them over from
     * another array of genes.
     *
     * @param pCross probability that a given gene is copied from the second
     * gene array; must be between 0 and 1.
     * @param pMut variance of random Gaussian variable to be added to the
     * genes. May be any value, but if it is over 1 the creep is likely to often
     * take genes out of range causing a lot of reflections, and the genotype
     * will be extremely unstable.
     * @param crossOverGenes array of genes to copy from.
     * @return a new Individual.
     */
    public Individual reproduce(float pCross, float pMut, float[] crossOverGenes) {
        Genotype newGenotype = genotype.crossover(pCross, crossOverGenes).
                mutate(pMut);
        return new Individual(newGenotype, phenotype);
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        if (!Objects.equals(this.genotype, other.genotype)) {
            return false;
        }
        if (Float.floatToIntBits(this.fitness) != Float.floatToIntBits(
                other.fitness)) {
            return false;
        }
        return Objects.equals(this.phenotype, other.phenotype);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.genotype);
        hash = 53 * hash + Float.floatToIntBits(this.fitness);
        hash = 53 * hash + Objects.hashCode(this.phenotype);
        return hash;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Individual{" + "fitness=" + fitness + ", phenotype=" + phenotype
                + '}';
    }

}
