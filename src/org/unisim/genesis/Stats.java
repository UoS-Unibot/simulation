/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import java.util.List;

/**
 *
 * @author Miles
 */
public class Stats {

    private final int generationN;
    private final Individual bestInd;
    private float maxFit;
    private float minFit;
    private float avgFit;
    private float varFit;
    private final List<Float> fitnesses;

    public Stats(int generationN, Population pop) {
        this.generationN = generationN;
        maxFit = 0.0f;
        minFit = 0.0f;
        avgFit = 0.0f;
        varFit = 0.0f;
        float total = 0;
        float raw;
        int bestIndex = 0;
        fitnesses = pop.getFitnesses();
        minFit = fitnesses.get(0);
        maxFit = fitnesses.get(0);
        int size = fitnesses.size();
        for (int i = 0; i < size; i++) {
            raw = fitnesses.get(i);
            if (raw > maxFit) {
                maxFit = raw;
                bestIndex = i;
            } else if (raw < minFit) {
                minFit = raw;
            }
            total += raw;
        }
        avgFit = total / size;
        if (avgFit < minFit) {
            avgFit = minFit;
        } else if (avgFit > maxFit) {
            avgFit = maxFit;
        }

        if (fitnesses.size() > 1) {
            total = 0.0f;
            for (int i = 1; i < size; i++) {
                float d = fitnesses.get(i) - avgFit;
                total += d * d;
            }
            varFit = total / (size - 1);
        } else {
            varFit = 0.0f;
        }

        bestInd = pop.getIndividual(bestIndex);
    }

    public int getGenerationN() {
        return generationN;
    }

    public Individual getBestInd() {
        return bestInd;
    }

    public float getMaxFit() {
        return maxFit;
    }

    public float getMinFit() {
        return minFit;
    }

    public float getAvgFit() {
        return avgFit;
    }

    public float getVarFit() {
        return varFit;
    }

    public List<Float> getFitnesses() {
        return fitnesses;
    }

    @Override
    public String toString() {
        return String.format("%4d\t%.6f\t\t%.6f\t\t%.6f\t\t%.6f",
                getGenerationN(), getMaxFit(), getMinFit(), getAvgFit(),
                getVarFit());
    }


}
