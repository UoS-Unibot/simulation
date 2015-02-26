/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.exp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.su.easy.unisim.exp.params.Parameters;
import org.su.easy.unisim.genesis.MicrobialGA;
import org.su.easy.unisim.genesis.RobotGenotype;
import org.su.easy.unisim.genesis.Stats;

/**
 *
 * @author Miles
 */
public class EvolutionRun extends Thread {

    Parameters param;
    public MicrobialGA GA;
    Thread GAthread;
    public volatile boolean PAUSED = false;
    public volatile boolean RESET = false;
    public volatile boolean FINISHED = false;
    private static final Logger LOG = Logger.getLogger(EvolutionRun.class.getName());

    public void start(Experiment exp) {
        this.param = exp.getParam();
        GA = new MicrobialGA(exp);
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "Starting experiment in thread {0}", Thread.currentThread().getName());
        GA.initPop();
        while (!RESET & !FINISHED) {
            if (GA != null) {
                while (!PAUSED) {
                    Stats stats = GA.getLastStats();
                    if (stats != null) {
                        if (listener != null) {
                            listener.GAupdateSummary(stats);
                        }
                    }
                    GA.step();
                    if (Thread.currentThread().isInterrupted()) {
                        LOG.log(Level.WARNING, "Thread {0} was interrupted", Thread.currentThread().getName());
                        reset();
                        break;
                    }
                    if (isFinished()) {
                        FINISHED = true;
                        LOG.log(Level.INFO, "Finishing experiment in thread {0}", Thread.currentThread().getName());
                        if (listener != null) {
                            listener.GAFinished();
                        }
                        break;
                    }
                }
            }
        }
    }

    public RobotGenotype getBestGenotype() {
        return GA.getLastStats().bestInd.getGenotype();
    }

    GAListener listener;

    public void setListener(GAListener listener) {
        this.listener = listener;
    }

    public void pause() {
        PAUSED = !PAUSED;
    }

    public void setPaused(boolean paused) {
        this.PAUSED = paused;
    }

    public void reset() {
        RESET = true;
        param = null;
        GA = null;
        GAthread = null;
    }

    public boolean isFinished() {
        if (GA.curGen >= (int) param.getGa_generations()) {
            return true;
        }
        return false;
    }

    public void infoToFile(String filename) {
        StringBuilder out = new StringBuilder();
        out.append("Experiment info\n");
        //out.append("Layout filename: ").append(layout.filename);
        out.append("Params:\n");
        out.append(param.toString());
        //StringToFile.save(filename, out.toString());
    }

    public String getSummary() {
        Stats laststats = GA.getLastStats();
        if (laststats == null) {
            return "";
        }
        StringBuilder report = new StringBuilder("---------------------------------------------------\n");
        report.append("SUMMARY\n");
        report.append("---------------------------------------------------\n");
        report.append(String.format("Max fitness  : %f\n", laststats.maxFit));
        report.append(String.format("Min fitness  : %f\n", laststats.minFit));
        report.append(String.format("Avg fitness  : %f\n", laststats.avgFit));
        report.append(String.format("Variance     : %f\n", laststats.varFit));
        report.append(String.format("Best genotype: %s\n", laststats.bestInd.getGenotype().toString()));
        return report.toString();
    }

    public String getGenerationsCSV() {
        Stats laststats = GA.getLastStats();
        if (laststats == null) {
            return "";
        }
        StringBuilder report = new StringBuilder("---------------------------------------------------\n");
        report.append("GENERATIONS\n");
        report.append("---------------------------------------------------\n");
        report.append("N\tMaxFitness\tMinFitness\tAverageFitness\tVariance\n");
        for (Stats stats : GA.popStats.getAllStats()) {
            report.append(stats.toString());
            report.append("\n");
        }
        return report.toString();
    }

    public String getPopulationCSV() {
        Stats laststats = GA.getLastStats();
        if (laststats == null) {
            return "";
        }
        StringBuilder report = new StringBuilder("---------------------------------------------------\n");
        report.append("\n");
        report.append("---------------------------------------------------\n");
        report.append("POPULATION\n");
        report.append("---------------------------------------------------\n");
        report.append(GA.pop.toString());
        return report.toString();
    }

    public String generateReport() {
        Stats laststats = GA.getLastStats();
        if (laststats == null) {
            return "";
        }
        StringBuilder report = new StringBuilder();
        report.append(getSummary());
        report.append("\n");
        report.append(getGenerationsCSV());
        report.append("\n");
        report.append(getPopulationCSV());
        report.append("\n");
        report.append("\n---------------------------------------------------\n");
        report.append("\n");
        report.append("\n---------------------------------------------------\n");
        report.append("PARAMS\n");
        report.append("---------------------------------------------------\n");
        report.append(param.toString());
        return report.toString();
    }

}
