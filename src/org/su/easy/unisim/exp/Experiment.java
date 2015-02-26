/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.exp;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.su.easy.unisim.exp.params.Parameters;
import org.su.easy.unisim.sim.world.json.JSONWorld;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.su.easy.unisim.simulation.robot.ctrnn.jsonIO.JSONCTRNNLayout;

/**
 *
 * @author miles
 */
public class Experiment {
    private Parameters param;
    private CTRNNLayout layout;
    private SimulationWorld world;
    private String dir;

    public Experiment(Parameters param, CTRNNLayout layout, SimulationWorld world) {
        this.param = param;
        this.layout = layout;
        this.world = world;
    }

    public Experiment() {
    }
    
    
    
    public static Experiment fromDirectory(String dirstr) throws IOException {
        File dir = new File(dirstr);
        if(!dir.exists())
            throw new IOException("Directory " + dir + "not found");
        if(!dir.isDirectory())
            throw new IOException("Directory " + dir + "is not a directory");
        
        Experiment ex = new Experiment();
        ex.param = Parameters.fromJSONFile(new File(dir.getAbsoluteFile() + "/params.json"));
        ex.layout = JSONCTRNNLayout.fromFile(new File(dir.getAbsoluteFile() + "/layout.json")).toCTRNNLayout();
        ex.world = JSONWorld.fromFile(new File(dir.getAbsoluteFile() + "/world.json"));
        ex.dir = dirstr;
        
        return ex;
    }

    public void saveToDir(String dirstr) throws IOException {
        File dir = new File(dirstr);
        dir.mkdirs();
        param.saveToFile(new File(dirstr + "/params.json"));
        if(world.getFilename().isEmpty())
            throw new IllegalStateException("World filename is empty; cannot create world.json");
        if(layout.filename.isEmpty())
            throw new IllegalStateException("Layout filename is empty; cannot create layout.json");
        FileUtils.copyFile(new File(layout.filename), new File(dirstr + "/layout.json"));
        FileUtils.copyFile(new File(world.getFilename()), new File(dirstr + "/world.json"));
    }
    
    public String getDir() {
        return dir;
    }
    
    public Parameters getParam() {
        return param;
    }

    public void setParam(Parameters param) {
        this.param = param;
    }

    public CTRNNLayout getLayout() {
        return layout;
    }

    public void setLayout(CTRNNLayout layout) {
        this.layout = layout;
    }

    public SimulationWorld getWorld() {
        return world;
    }

    public void setWorld(SimulationWorld world) {
        this.world = world;
    }
    
    
    
}
