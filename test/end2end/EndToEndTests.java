/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package end2end;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.unisim.reality.SimulationController;
import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.SimulatedRobotBody;
import static unibotsim.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class EndToEndTests {
    @Mocked IRobotController controller;
    SimulatedRobotBody robot;
    SimulationController sim;
    
    @Test
    public void controllerWith0VelocityDoesNotMoveRobot() {
        robot = new SimulatedRobotBody(1f/60f);
        sim = new SimulationController(controller,robot);
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
        robot = new SimulatedRobotBody(1f/60f);
        sim = new SimulationController(controller,robot);
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
