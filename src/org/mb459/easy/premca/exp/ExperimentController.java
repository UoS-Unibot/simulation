/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mb459.easy.premca.exp;

import java.io.File;
import java.io.FileWriter;
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
import org.mb459.easy.premca.genesis.Stats;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.su.easy.ui.exprun.TextOutput;

/**
 *
 * @author Miles
 */
public class ExperimentController {

    private static final Logger LOG = Logger.getLogger(ExperimentController.class.getName());

    private ExpParam params = new ExpParam();
    private CTRNNLayout layout = new CTRNNLayout();
    private ArrayList<Experiment> experiments = new ArrayList<>();

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

    public ExperimentController() {
        try {
            FileHandler handler = new FileHandler("prog.%u.%g.txt", 1024 * 1024, 3, true);
            handler.setFormatter(new SimpleFormatter());
            LOG.addHandler(handler);
            LOG.setLevel(Level.ALL);
        } catch (Exception e) {
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
        saveParamAndLayout();

        addLineOut("Thread\tGen\tMax\tAvg\tMin\tVar");

        totalGen = (int) params.get("GA_MAXGEN") * nThreads;

        for (int i = 0; i < nRuns; i++) {
            final Experiment exp = new Experiment();
            exp.start(params, layout);
            final int curID = i + 1;
            exp.setListener(new GAListener() {
                @Override
                public void GAupdateSummary(Stats stats) {
                    addLineOut(String.format("%3d\t%3d\t%4.3f\t%4.3f\t%4.3f\t%4.3f", curID, stats.n, stats.maxFit, stats.avgFit, stats.minFit, stats.varFit));
                    countGen((int) params.get("UPDATE_SUMMARY_FREQ"));
                }

                @Override
                public void GAFinished() {
                    addLineOut(String.format("Run %d finished", curID));
                    exp.GA.popStats.saveToCSV(getStatsFileName(curID));
                    exp.GA.pop.saveToCSV(getPopFileName(curID));
                }
            }
            );
            experiments.add(exp);
            exe.execute(exp);
            addLineOut(String.format("Run %d started", curID));
        }

    }

    private void saveParamAndLayout() {
        try {
            Files.createDirectories(workingDir);
            //params.saveToFile(new FileWriter(workingDir.toString() + "\\params.param"));
            if(!layout.filename.isEmpty()) {
                //FileUtils.copyFile(new File(layout.filename), new File(workingDir.toString() + "\\layout.nnl"));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getStatsFileName(int run) {
        try {
            Files.createDirectories(workingDir);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (workingDir.toString() + "\\stats-" + run + ".csv");
    }

    private String getPopFileName(int run) {
        try {
            Files.createDirectories(workingDir);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (workingDir.toString() + "\\population-" + run + ".csv");
    }

    public void restart() {
        for (Experiment e : experiments) {
            e.setPaused(false);
        }
    }

    public void stop() {
        for (Experiment e : experiments) {
            e.setPaused(true);
        }
    }

    /**
     * @return the params
     */
    public ExpParam getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(ExpParam params) {
        this.params = params;
    }

    /**
     * @return the layout
     */
    public CTRNNLayout getLayout() {
        return layout;
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(CTRNNLayout layout) {
        this.layout = layout;
    }

}
