/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package end2end;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evors.core.RunController;
import org.evors.genesis.Genotype;
import org.evors.rs.genesis.RobotExperiment;
import org.evors.rs.genesis.RobotGARunner;
import org.evors.rs.genesis.RobotPhenotype;
import org.evors.rs.sim.core.SimulationBuilder;
import org.evors.rs.sim.robot.ctrnn.CTRNNController;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

/**
 * Runs a full GA experiment for profiling purposes.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Profiling {

    public static void main(String[] args) {
        try {
            String line = "------------------------------";
            System.out.println(line);
            System.out.println("EvoRS console");
            System.out.println(line);
            
            final Stopwatch stopwatch = Stopwatch.createStarted();
            
            System.out.println("Starting...");
            
            RobotExperiment exp;
            exp = RobotExperiment.fromDirectory(System.getProperty("user.dir") + "/demo/"); //load the demo layout and world
            
            final RobotGARunner gaRunner = new RobotGARunner(new RobotPhenotype(exp), true);
            
            System.out.println("");
            System.out.printf("Initialised in %d milliseconds\n",stopwatch.elapsed(TimeUnit.MILLISECONDS));
            
            System.out.println(line);System.out.println("");
            
            System.out.println(gaRunner.getProgressReportHeader());
            
            gaRunner.setListener(new RobotGARunner.GAListener() {
                long lastGenTime = -1;
                
                @Override
                public void doUpdate() {
                    if(lastGenTime != -1)
                        System.out.printf("Generation done in %d milliseconds\n",stopwatch.elapsed(TimeUnit.MILLISECONDS) - lastGenTime);
                    lastGenTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                    System.out.println(gaRunner.getProgressReportLine());
                    
                }

                @Override
                public void finished() {
                    System.out.printf("Finished in %d milliseconds\n",stopwatch.elapsed(TimeUnit.MILLISECONDS));
                }
            });
            
            new Thread(gaRunner).start();

        } catch (IOException ex) {
            Logger.getLogger(Profiling.class.getName()).log(Level.SEVERE, null,
                    ex);
            System.exit(1);
        }
    }

}
