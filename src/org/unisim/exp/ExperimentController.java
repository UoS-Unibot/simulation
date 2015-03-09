/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.exp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.unisim.exp.params.Parameters;
import org.unisim.genesis.Stats;
import org.unisim.ui.TextOutput;

/**
 *
 * @author Miles
 */
public class ExperimentController {

    private static final Logger LOG = Logger.getLogger(ExperimentController.class.getName());

    private final Parameters params;
    private final Experiment experiment;
    private ArrayList<EvolutionRun> experiments = new ArrayList<>();

    private Path workingDir;

    /**
     * Get the value of workingDir
     *
     * @return the value of workingDir
     */
    public Path getWorkingDir() {
        return workingDir;
    }

    /**
     * Set the value of workingDir
     *
     * @param workingDir new value of workingDir
     */
    public void setWorkingDir(Path workingDir) {
        this.workingDir = workingDir;
    }

    public ExperimentController(Experiment exp) {
        this.params = exp.getParam();
        experiment = exp;
        try {
            FileHandler handler = new FileHandler("prog.%u.%g.txt", 1024 * 1024, 3, true);
            handler.setFormatter(new SimpleFormatter());
            LOG.addHandler(handler);
            LOG.setLevel(Level.ALL);
        } catch (IOException | SecurityException e) {
            LOG.throwing("ExperimentController", "Constructor", e);
            System.exit(1);
        }
    }

    public final void initialise() {
        addLineOut("Initialising experiment...");
    }

    private final ArrayList<TextOutput> outputs = new ArrayList<>();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public void addTextOutput(TextOutput output) {
        outputs.add(output);
    }

    public void addLineOut(String newLine) {
        newLine = (sdf.format(new Date())) + "\t" + newLine + "\n";
        for (TextOutput out : outputs) {
            out.addLine(newLine);
            out.updateProgress(getProgress());
        }
    }

    private int genCount = 0;
    private int totalGen = 0;

    public int getProgress() {
        return Math.round((1000 * (float) genCount) / ((float) totalGen));
    }

    private void countGen(int n) {
        genCount += n;
    }

    private Executor exe;
    public boolean isRunning = false;

    public void run(int nRuns, int nThreads) {
        if (isRunning) {
            restart();
            return;
        }
        isRunning = true;
        exe = Executors.newFixedThreadPool(nThreads);
        try {
            experiment.saveToDir(workingDir.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        addLineOut("Thread\tGen\tMax\tAvg\tMin\tVar");

        totalGen = (int) params.getGa_generations() * nThreads;

        for (int i = 0; i < nRuns; i++) {
            final EvolutionRun evoRun = new EvolutionRun();
            evoRun.start(experiment);
            final int curID = i + 1;
            evoRun.setListener(new GAListener() {
                @Override
                public void GAupdateSummary(Stats stats) {
                    addLineOut(String.format("%3d\t%3d\t%4.3f\t%4.3f\t%4.3f\t%4.3f", curID, stats.n, stats.maxFit, stats.avgFit, stats.minFit, stats.varFit));
                    countGen(1);
                }

                @Override
                public void GAFinished() {
                    addLineOut(String.format("Run %d finished", curID));
                    evoRun.GA.popStats.saveToCSV(getStatsFileName(curID));
                    evoRun.GA.pop.saveToCSV(getPopFileName(curID));
                }
            }
            );
            experiments.add(evoRun);
            exe.execute(evoRun);
            addLineOut(String.format("Run %d started", curID));
        }

    }


    private String getStatsFileName(int run) {
        try {
            Files.createDirectories(workingDir);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (workingDir.toString() + "/stats-" + run + ".csv");
    }

    private String getPopFileName(int run) {
        try {
            Files.createDirectories(workingDir);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (workingDir.toString() + "/population-" + run + ".csv");
    }

    public void restart() {
        for (EvolutionRun e : experiments) {
            e.setPaused(false);
        }
    }

    public void stop() {
        for (EvolutionRun e : experiments) {
            e.setPaused(true);
        }
    }

}
