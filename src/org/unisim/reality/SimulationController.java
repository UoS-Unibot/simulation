/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import org.unisim.simulation.robot.IRobotBody;
import org.unisim.simulation.robot.IRobotController;

/**
 *
 * @author miles
 */
public class SimulationController {
    
    private final IRobotController controller;
    private final IRobotBody robot;

    public SimulationController(IRobotController controller, IRobotBody robot) {
        this.controller = controller;
        this.robot = robot;
    }
    
    public void step() {
        robot.step(controller.getVelocity(),controller.getAngularVelocity());
    }
    
}
