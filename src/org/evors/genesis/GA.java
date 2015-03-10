package org.evors.genesis;

import com.google.common.collect.Lists;
import java.util.List;
import org.evors.core.io.Loggable;

/**
 * The GA class performs genetic algorithm optimisation. Parameters for the GA
 * and a phenotype (specifying the genotype and fitness function) are given;
 * doNextGeneration() is called for each step, performing reproduction events
 * for the population size.
 *
 * @author Miles Bryant
 */
public class GA implements Loggable<Float> {

    
    private final GAParameters parameters;
    private final Population population;
    private volatile int currentGen = 0;
    private Stats lastStats;

    /**
     * Creates a new GA with specified phenotype and default parameters.
     *
     * @param phenotype phenotype specifying genotype and fitness function.
     */
    public GA(Phenotype phenotype) {
        this(phenotype, new GAParameters.GABuilder().build());
    }

    /**
     * Creates a new GA with specified phenotype and specified parameters.
     *
     * @param phenotype phenotype specifying genotype and fitness function.
     * @param parameters parameters specifying GA parameters.
     */
    public GA(Phenotype phenotype, GAParameters parameters) {
        this.parameters = parameters;
        population = new Population(phenotype, parameters.getPopulationSize());
        lastStats = new Stats(0, population);
    }

    /**
     * Returns whether this GA has reached the number of generations specified
     * in the parameters.
     *
     * @return truth value whether GA is finished.
     */
    public boolean isFinished() {
        return currentGen == parameters.getGenerations();
    }

    public int getCurrentGen() {
        return currentGen;
    }

    public Population getPopulation() {
        return population;
    }
    
    

    /**
     * Iterates this GA one step, performing a reproduction event for every
     * individual in the population.
     */
    public void doNextGeneration() {
        if (isFinished()) {
            throw new IllegalStateException(
                    "GA is already finished; cannot do next generation");
        }
        for (int i = 0; i < population.getSize(); i++) {
            int ind1 = population.getRandomIndex(),
                    ind2 = population.getRandomWithinDeme(ind1, parameters.
                            getDemesize());
            population.doReproduction(ind1, ind2, parameters.getCrossrate(),
                    parameters.getMutrate());
        }
        currentGen++;
        lastStats = new Stats(currentGen, population);
    }

    @Override
    public List getHeaders() {
        return Lists.newArrayList("Gen", "MaxFit", "AvgFit", "MinFit",
                "Variance");
    }

    @Override
    public List getDataRow() {
        return Lists.newArrayList(
                lastStats.getGenerationN(),
                lastStats.getMaxFit(),
                lastStats.getAvgFit(),
                lastStats.getMinFit(),
                lastStats.getVarFit()
        );
    }
}
