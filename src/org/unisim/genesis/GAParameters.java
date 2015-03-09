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
public class GAParameters {

    private final float mutrate;
    private final float crossrate;
    private final int populationSize;
    private final int generations;
    private final int demesize;

    private GAParameters(float mutrate, float crossrate, int populationSize,
            int generations, int demesize) {
        this.mutrate = mutrate;
        this.crossrate = crossrate;
        this.populationSize = populationSize;
        this.generations = generations;
        this.demesize = demesize;
    }

    public float getMutrate() {
        return mutrate;
    }

    public float getCrossrate() {
        return crossrate;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerations() {
        return generations;
    }

    public int getDemesize() {
        return demesize;
    }

    public static class GABuilder {

        private float mutrate = 0.4f;
        private float crossrate = 0.05f;
        private int populationSize = 100;
        private int generations = 200;
        private int demesize = 5;

        public GABuilder() {
        }

        private void checkParamIsProbability(float param) {
            if (param < 0 || param > 1) {
                throw new IllegalArgumentException("parameter (value "
                        + String.
                        valueOf(param)
                        + ") must be a probability between 0 and 1.");
            }
        }

        public GABuilder setMutrate(float mutrate) {
            checkParamIsProbability(mutrate);
            this.mutrate = mutrate;
            return this;
        }

        public GABuilder setCrossrate(float crossrate) {
            checkParamIsProbability(crossrate);
            this.crossrate = crossrate;
            return this;
        }

        public GABuilder setPopulationSize(int populationSize) {
            if (populationSize < 1) {
                throw new IllegalArgumentException(
                        "Population size must be at least 1, given: " + String.
                        valueOf(populationSize));
            }
            this.populationSize = populationSize;
            return this;
        }

        public GABuilder setGenerations(int generations) {
            if (generations < 1) {
                throw new IllegalArgumentException(
                        "Generations must be at least 1, given: " + String.
                        valueOf(generations));
            }
            this.generations = generations;
            return this;
        }

        public GABuilder setDemesize(int demesize) {
            if (demesize < 1) {
                throw new IllegalArgumentException(
                        "Deme size must be at least 1, given: " + String.
                        valueOf(demesize));
            }
            if (demesize > populationSize) {
                throw new IllegalArgumentException(
                        "Deme size must less than population size, given: "
                        + String.valueOf(demesize) + ", population size: "
                        + String.valueOf(populationSize));
            }
            this.demesize = demesize;
            return this;
        }

        public GAParameters build() {
            return new GAParameters(mutrate, crossrate, populationSize,
                    generations,
                    demesize);
        }

    }

}
