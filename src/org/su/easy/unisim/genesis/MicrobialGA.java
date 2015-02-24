package org.su.easy.unisim.genesis;

import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import java.util.Random;
import org.su.easy.unisim.exp.ExpParam;

/**
 * This class independently runs the GA, and provides access to per generation
 * statistics.
 * @author Miles
 * @version 0.5
 */
public class MicrobialGA {
    
    public volatile RobotGenotype lastFit;
    ExpParam param;
    public volatile int curGen = 0;
    CTRNNLayout layout;
    Random rand = new Random();
    
    public Population pop 
            = new Population();   //Stores our population.
    
    public MicrobialGA(ExpParam param, CTRNNLayout layout){
        this.param = param;this.layout = layout;
    }
    
    public volatile PopulationStats popStats = new PopulationStats();
    
    public Stats getLastStats() {
        if(curGen < 1)
            return null;
        return popStats.getLastGeneration();
    }
    
    public void initPop() {
        pop = new Population(
                (int)param.get("GA_NPOP"),
                ((Number)param.get("GA_P_MUT")).floatValue(),
                ((Number)param.get("GA_CROSSOVER_P_CROSS")).floatValue(), 
                param,
                layout
        );
    }
    
    public void step() {
        curGen += 1;
        int deme = (int)param.get("GA_DEME");
        for(int i = 0; i < (int)param.get("GA_NPOP"); i++) {
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
