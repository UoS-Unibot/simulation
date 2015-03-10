/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis.robotGA;

import com.google.common.base.Joiner;
import org.unisim.genesis.GA;
import org.unisim.genesis.GAParameters;
import org.unisim.genesis.Population;
import org.unisim.io.DataFile;

/**
 *
 * @author miles
 */
public class RobotGARunner implements Runnable {

    public static interface GAListener {

        public void doUpdate();
        public void finished();
    }

    private volatile boolean running = false;
    private final GA ga;
    private DataFile gaDataFile;
    private GAListener listener;
    private final GAParameters parameters;

    public RobotGARunner(RobotPhenotype robotPhenotype, boolean loggingEnabled) {
        parameters = new GAParameters.GABuilder().build();
        ga = new GA(robotPhenotype);
        if (loggingEnabled) {
            gaDataFile = DataFile.fromLoggable(ga);
        }
    }

    public String getProgressReportHeader() {
        return "GA on thread " + Thread.currentThread().getName() + ":\n\t" +
                        Joiner.on("\t").join(ga.getHeaders()) + "\n";
    }

    public String getProgressReportLine() {
        StringBuilder sb = new StringBuilder();
        for(Object data : ga.getDataRow()) {
            if(data.getClass() == Float.class)
                sb.append(String.format("\t%.4f", (float)data));
            else if(data.getClass() == Integer.class)
                sb.append(String.format("\t%d", (int)data));
        }
        sb.append("\n");
        return sb.toString();
    }

    public float getProgress() {
        return (float) ga.getCurrentGen() / (float) parameters.getGenerations();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public DataFile getGaDataFile() {
        if (running) {
            throw new IllegalThreadStateException(
                    "Cannot access datafile whilst GA is running");
        }
        return gaDataFile;
    }

    public void setListener(GAListener listener) {
        this.listener = listener;
    }
    
    public Population getPopulation() {
        return ga.getPopulation();
    }

    @Override
    public void run() {
        running = true;
        while (!ga.isFinished() && isRunning()) {
            ga.doNextGeneration();
            if (gaDataFile != null) {
                gaDataFile.update();
            }
            if (listener != null) {
                listener.doUpdate();
            }
        }
        if(listener != null)
            listener.finished();
    }

}
