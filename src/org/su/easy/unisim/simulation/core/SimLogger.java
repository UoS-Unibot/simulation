/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.core;

import org.su.easy.unisim.simulation.robot.SimulatedUnibot;
import org.su.easy.unisim.util.DataFile;

/**
 *
 * @author miles
 */
public class SimLogger {
    public final DataFile data;

    public SimLogger() {
        data = new DataFile(new String[]{"RobotX","RobotY","RobotHeading","RobotVelocity","RobotAngularVelocity","Rangefinder"});
    }
    
    public void update(SimulatedUnibot robot) {
        data.addData("RobotX", robot.getPosition().getX());
        data.addData("RobotY", robot.getPosition().getY());
        data.addData("RobotHeading", robot.getHeading());
        data.addData("RobotVelocity", robot.getVelocity());
        data.addData("RobotAngularVelocity", robot.getAngularVelocity());
        data.addData("Rangefinder", robot.getLastRangeFinderValue());
    }
    
    public DataFile getData() {
        return data;
    }
    
}
