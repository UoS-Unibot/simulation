/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.RobotInput;
import org.unisim.simulation.robot.SimulatedRobotBody;

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
