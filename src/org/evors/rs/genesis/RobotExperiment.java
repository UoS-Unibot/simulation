/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.genesis;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.evors.rs.sim.core.worldio.JSONWorld;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.SimulatedRobotBody;
import org.evors.rs.sim.robot.ctrnn.io.JSONCTRNNLayout;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author miles
 */
public class RobotExperiment {
    private CTRNNLayout layout;
    private SimulatedRobotBody robot;
    private SimulationWorld world;
    private String dir;

    public RobotExperiment(CTRNNLayout layout, SimulatedRobotBody robot,
            SimulationWorld world) {
        this.layout = layout;
        this.robot = robot;
        this.world = world;
    }

    
    
    public RobotExperiment(CTRNNLayout layout, SimulatedRobotBody robot,
            SimulationWorld world, String dir) {
        this.layout = layout;
        this.robot = robot;
        this.world = world;
        this.dir = dir;
    }


    public RobotExperiment() {
    }
    
    public static RobotExperiment fromDirectory(String dirstr) throws IOException {
        File dir = new File(dirstr);
        if(!dir.exists())
            throw new IOException("Directory " + dir + "not found");
        if(!dir.isDirectory())
            throw new IOException("Directory " + dir + "is not a directory");
        
        RobotExperiment ex = new RobotExperiment();
        ex.layout = JSONCTRNNLayout.fromFile(new File(dir.getAbsoluteFile() + "/layout.json")).toCTRNNLayout();
        ex.world = JSONWorld.fromFile(new File(dir.getAbsoluteFile() + "/world.json"));
        ex.dir = dirstr;
        
        return ex;
    }

    public void saveToDir(String dirstr) throws IOException {
        File dir = new File(dirstr);
        dir.mkdirs();
        if(world.getFilename().isEmpty())
            throw new IllegalStateException("World filename is empty; cannot create world.json");
        if(layout.getFilename().isEmpty())
            throw new IllegalStateException("Layout filename is empty; cannot create layout.json");
        FileUtils.copyFile(new File(layout.getFilename()), new File(dirstr + "/layout.json"));
        FileUtils.copyFile(new File(world.getFilename()), new File(dirstr + "/world.json"));
    }
    
    public String getDir() {
        return dir;
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

    public SimulatedRobotBody getRobot() {
        return robot;
    }

    public void setRobot(SimulatedRobotBody robot) {
        this.robot = robot;
    }
    
    
    
    
}
