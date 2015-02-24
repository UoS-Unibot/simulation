/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mb459.easy.premca.genesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mb459.easy.premca.exp.ExperimentController;

/**
 *
 * @author Miles
 */
public class PopulationStats {
    private ArrayList<Stats> statses = new ArrayList<>();
    
    public void addGeneration(Population pop) {
        statses.add(new Stats(statses.size(), pop));
    }
    
    public Stats getLastGeneration() {
        if(statses.isEmpty())
            return null;
        return statses.get(statses.size()-1);
    }
    
    public ArrayList<Stats> getAllStats() {
        return (ArrayList<Stats>) statses.clone();
    }
    
    public String getLastGenerationOutput() {
        if(statses.isEmpty())
            return "";
        return statses.get(statses.size()-1).toString();
    }
    
    public void saveToCSV(String filename) {
        try {
            Files.createDirectories(Paths.get(filename).getParent());
            File statsFile = new File(filename);
            try (FileWriter out = new FileWriter(statsFile)) {
                out.append("Generation,Maximum Fitness,Average Fitness,Variance" + "\n");
                for(Stats stat : statses) {
                    out.append(stat.n + ",");
                    out.append(stat.maxFit + ",");
                    out.append(stat.avgFit + ",");
                    out.append(stat.varFit + "\n");
                }
                out.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExperimentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
