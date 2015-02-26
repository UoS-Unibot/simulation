package org.unisim.genesis;

import org.unisim.simulation.robot.ctrnn.CTRNNLayout;
import java.util.Random;
import org.unisim.exp.Experiment;
import org.unisim.exp.params.Parameters;

/**
 * This class independently runs the GA, and provides access to per generation
 * statistics.
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 * @version 0.5
 */
public class MicrobialGA {
    
    public volatile RobotGenotype lastFit;
    public volatile int curGen = 0;
    Random rand = new Random();
    Experiment experiment;
    public Population pop 
            = new Population();   //Stores our population.
    
    public MicrobialGA(Experiment exp){
        experiment = exp;
    }
    
    public volatile PopulationStats popStats = new PopulationStats();
    
    public Stats getLastStats() {
        if(curGen < 1)
            return null;
        return popStats.getLastGeneration();
    }
    
    public void initPop() {
        pop = new Population(experiment);
    }
    
    public void step() {
        curGen += 1;
        int deme = experiment.getParam().getGa_demesize();
        for(int i = 0; i < experiment.getParam().getGa_population(); i++) {
            int a = 0; int b = 0; int W,L;
            while (a == b)  {
                a = pop.getRandIndex();
                b = a + rand.nextInt(deme * 2) - deme;
                if(b > pop.size() - 1)
                    b -= pop.size();
                else if(b < 0)
                    b = pop.size() + b;
            }
            if(pop.isABetterThanB(a, b))
                {W = a; L = b;}
            else {
                W = b; L = a;}
            pop.crossover(L, W); 
            pop.mutate(L);
        }
        popStats.addGeneration(pop);
    }
    
}
