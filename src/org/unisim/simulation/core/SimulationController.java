/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.core;

import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class SimulationController {
    
    private final SimulatedRobotBody robot;
    private final IRobotController controller;

    public SimulationController(SimulatedRobotBody robot,
            IRobotController controller) {
        this.robot = robot;
        this.controller = controller;
    }
    
    
    
}
