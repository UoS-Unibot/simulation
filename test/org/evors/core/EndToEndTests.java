/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.evors.core.RunController;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.IRobotController;
import org.evors.rs.sim.robot.SimulatedRobotBody;
import static org.evors.core.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class EndToEndTests {
    @Mocked IRobotController controller;
    @Mocked SimulationWorld world;
    SimulatedRobotBody robot;
    RunController sim;
    
    @Test
    public void controllerWith0VelocityDoesNotMoveRobot() {
        robot = new SimulatedRobotBody(world);
        sim = new RunController(controller,robot);
        new Expectations() {{
            controller.getVelocity(); result = 0;
            controller.getAngularVelocity(); result = 0;
        }};
        for(int i = 0; i < 60; i++) {
            sim.step();
        }
        assertThat(robot.getPosition(), vEquals(Vector2D.ZERO));
    }
    
    @Test
    public void controllerWith1VelocityMoves1Meter() {
        robot = new SimulatedRobotBody(world);
        sim = new RunController(controller,robot);
        new Expectations() {{
            controller.getVelocity(); result = 1;
            controller.getAngularVelocity(); result = 0;
        }};
        for(int i = 0; i < 60; i++) {
            sim.step();
        }
        assertThat(robot.getPosition(), vEquals(1,0));
    }
    
    
}
