package org.unisim.genesis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a population of individuals and provides selection methods and
 * ability to reproduce.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Population {

    private final List<Individual> pop;

    /**
     * Creates a new population with a given number of individuals initialised
     * to random genotypes.
     *
     * @param phenotype phenotype specifying the genotype.
     * @param populationSize number of individuals to create in this population.
     */
    public Population(Phenotype phenotype, int populationSize) {
        pop = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            pop.add(Individual.withRandomGenes(phenotype));
        }
    }

    /**
     * Gets the index of a random individual within this population.
     *
     * @return index of Individual.
     */
    public int getRandomIndex() {
        return ThreadLocalRandom.current().nextInt(pop.size());
    }

    /**
     * Selects a random index with geographical selection relative to the index
     * given. That is, the index returned will always be within the demeSize
     * value of the first index. The index is wrapped as if it were a one
     * dimensional ring, so that the last individual is next to the first one.
     *
     * @param index1 index of first Individual.
     * @param demeSize size of area around first index to select from.
     * @return an index of an individual within the deme of the first index
     */
    public int getRandomWithinDeme(int index1, int demeSize) {
        int result = index1;
        while (result == index1) {
            result = ThreadLocalRandom.current().nextInt(demeSize * 2)
                    - demeSize;
            result += index1;
            if (result < 0) {
                result += pop.size();
            } else if (result > pop.size() - 1) {
                result -= pop.size();
            }
        }
        return result;
    }

    /**
     * Performs reproduction with the two selected individuals; the one with
     * lower fitness is mutated and the genes crossed over from the one with
     * higher fitness. If fitnesses are exactly equal (e.g. both zero) then the
     * first individual is selected as the loser.
     *
     * @param index1 First individual to reproduce.
     * @param index2 Second individual to reproduce.
     * @param pCross Probability of crossover; must be between 0 and 1.
     * @param pMut Variance added to genes to mutate them.
     */
    public void doReproduction(int index1, int index2, float pCross,
            float pMut) {
        int loserIndex, winnerIndex;
        if (pop.get(index1).getFitness() > pop.get(index2).getFitness()) {
            loserIndex = index2;
            winnerIndex = index1;
        } else {
            loserIndex = index1;
            winnerIndex = index2;
        }
        pop.set(
                loserIndex,
                pop.get(loserIndex).reproduce(pCross, pMut,
                        pop.get(winnerIndex).getGenotype().getGenes())
        );
    }
    
    public List<Float> getFitnesses() {
        List<Float> fitnesses = new LinkedList<>();
        for(Individual ind : pop)
            fitnesses.add(ind.getFitness());
        return fitnesses;
    }

    /**
     * Gets the number of individuals in this population.
     *
     * @return number of individuals.
     */
    public int getSize() {
        return pop.size();
    }

    public Individual getIndividual(int index) {
        return pop.get(index);
    }
}
