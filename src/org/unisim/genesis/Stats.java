package org.unisim.genesis;

import java.util.ArrayList;

/**
 * Generates and stores statistics of the population for one generation in time.
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class Stats {
        public int n;
        public RobotIndividual bestInd;
        public float maxFit,minFit,avgFit,varFit;
        public ArrayList<Float> fitnesses;
        public ArrayList<String> genotypes;

        public Stats(int n, Population pop) {
            this.n = n; maxFit = 0.0f; minFit = 0.0f; avgFit = 0.0f; varFit = 0.0f;
            float total = 0;
            float raw;
            int bestIndex = 0;
            ArrayList<Float> fitnesses = pop.getFitnesses();
            minFit = fitnesses.get(0);
            maxFit = fitnesses.get(0);
            int size = pop.size();
            for(int i = 0; i< size; i++) {
                raw = fitnesses.get(i);
                if(raw > maxFit) {
                    maxFit = raw;
                    bestIndex = i;
                } else if(raw < minFit) {
                    minFit = raw;
                }
                total += raw;
            }
            avgFit = total / size;
            if(avgFit < minFit)
                avgFit = minFit;
            else if(avgFit > maxFit)
                avgFit = maxFit;

            if(pop.size() > 1) {
                total = 0.0f;
                for(int i = 1; i < size; i++) {
                    float d = fitnesses.get(i) - avgFit;
                    total += d * d;
                }
                varFit = total / (size - 1);
            } else {varFit = 0.0f;}

            bestInd = pop.getIndividual(bestIndex);
        }
        
        
        @Override
        public String toString() {
            return String.format("%4d\t%.6f\t\t%.6f\t\t%.6f\t\t%.6f", n,maxFit,minFit,avgFit,varFit);
        }
    
}
