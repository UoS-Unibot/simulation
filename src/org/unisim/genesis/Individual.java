package org.unisim.genesis;

import java.util.Random;

/**
 * Represents an individual in a population, with a genotype. Can be compared to
 * other individuals, mutated, crossed over and fitness calculated. Fitness
 * values are cached, and only calculated when the genotype changes.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public abstract class Individual implements Comparable<Individual> {

    //protected Genotype genotype;
    private final Random rand = new Random();

//    public Object getGene(int index) {
//        return genotype.getGene(index);
//    }
//    public void setGene(int index, Object value)  {
//        genotype.setGene(index, value);
//    }
//    public Genotype getGenotype() {
//        return genotype;
//    }
//    
//    Individual() {}
//        
//    Individual(Genotype prototype) {
//        genotype = prototype.getInstanceOf();
//    }
    /**
     * Calculates HammingDistance between this instance and another.
     *
     * Calculates the HammingDistance between this instance and BinaryIndividual
     * ind. http://en.wikipedia.org/wiki/Hamming_distance.
     *
     * @param ind Other individual to compare this instance with.
     * @return Int value representing the Hamming Distance, or difference.
     */
    //int HammingDistance(Individual ind)
    /**
     * Fitness function for an individual - it should unambiguously provide a
     * quantitative measure of success from an individual's genotype.
     *
     * @return Fitness value.
     */
    public abstract float fitness();

    /**
     * Simulates biological gene crossover between this and another individual.
     *
     * Simulates biological gene crossover by replacing each gene with the copy
     * in another individual with probability p_crossover.
     *
     * @param p_crossover Probability for each gene that it will be crossed
     * over.
     * @param ind Individual to cross over genes from.
     */
    //void crossover(float p_crossover, Individual ind);
    /**
     * Mutates each gene with p_mutation probability.
     *
     * @param p_mutation Probability of mutation for each gene between 0 and 1.
     */
    //void mutate(float p_mutation);
    /**
     * Returns size of the genotype in bits.
     *
     * @return size of the genotype in bits. size([0000]) -> 4
     */
    //int size();
    /**
     * Provides a string representation of the individual, by default genotype
     * and fitness.
     *
     * @return String to be outputted.
     */
    @Override
    public String toString() {
        return "";
    }

    @Override
    public int compareTo(Individual ind) {
        if (this.fitness() > ind.fitness()) {
            return 1;
        } else if (this.fitness() < ind.fitness()) {
            return -1;
        } else {
            return 0;
        }
    }

    public abstract void crossover(float pCross, Individual ind);
//    {
//        for(int i = 0; i < genotype.size(); i++) {
//            if(rand.nextFloat() < p_crossover)
//                setGene(i, ind.getGene(i)); //probability matched, so crossover
//        }
//    }

    public abstract Individual getInstanceOf();

    abstract void mutate(float p_mutation);

}
