/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import com.google.common.collect.Lists;
import java.util.List;
import org.unisim.io.Loggable;
import org.unisim.simulation.robot.IRobotBody;
import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.RobotInput;

/**
 *
 * @author miles
 */
public class RunController {
    
    private final IRobotController controller;
    private final IRobotBody robot;
    private final double timeStep;
    private boolean live = true;

    public RunController(IRobotController controller, IRobotBody robot) {
        this(controller,robot,1f/60f);
    }
    
    public RunController(IRobotController controller, IRobotBody robot,double timeStep) {
        this.controller = controller;
        this.robot = robot;
        this.timeStep = timeStep;
    }
    
    public void step() {
        controller.step(new RobotInput(robot.getRange(), robot.getSonars()));
        robot.step(controller.getVelocity(),controller.getAngularVelocity());
        live = robot.isLive();
    }

    public boolean isLive() {
        return live;
    }
    
    

    public IRobotController getController() {
        return controller;
    }

    public IRobotBody getRobot() {
        return robot;
    }

    public double getTimeStep() {
        return timeStep;
    }

    
    
    
}
