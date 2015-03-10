/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.unibot.serial;

import org.evors.core.RunController;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.evors.core.IRobotController;
import org.evors.rs.sim.robot.RobotInput;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class SimulationControllerTest {
    
    public SimulationControllerTest() {
    }
    
    @Mocked IRobotController controller;
    @Mocked SimulatedRobotBody robot;
    @Mocked RobotInput robotinput;
    RunController sim;

    @Test
    public void simStepCallsRobotStepWithCorrectVelocityAndAngularVelocity() {
        sim = new RunController(controller, robot);
        new Expectations() {{
            robot.step(1,0); 
            controller.getVelocity();result = 1f;
            controller.getAngularVelocity(); result = 0f;
        }};
        sim.step();
    }
    
}
