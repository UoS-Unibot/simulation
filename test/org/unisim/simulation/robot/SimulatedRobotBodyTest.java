/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static unibotsim.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class SimulatedRobotBodyTest {
    
    public SimulatedRobotBodyTest() {
    }
    
    SimulatedRobotBody robot = new SimulatedRobotBody(1f/60f);

    @Test
    public void stepWith0VelocityDoesNotMoveRobot() {
        robot.step(0, 0);
        assertThat(robot.getPosition(), vEquals(0,0));
    }
    
    @Test
    public void stepWith1VelocityMovesRobot1MeterInOneSecond() {
        for(int i = 0; i < 60; i++)
            robot.step(1, 0);
        assertThat(robot.getPosition(), vEquals(1,0));
    }
    
    @Test
    public void robotTurnsQuarterCircleNorthInOneSecondIfAngVelocityIsPiOver2() {
        double angVelo = Math.PI / 2;
        for(int i = 0; i < 60; i++)
            robot.step(0, angVelo);
        assertEquals(angVelo,robot.getHeading(), 0.0001);
    }
    
    @Test
    public void robotTurnsQuarterCircleNorthInFiveSecondsIfAngVelocityIsPiOver2() {
        double angVelo = Math.PI / 2;
        for(int i = 0; i < 300; i++)
            robot.step(0, angVelo);
        assertEquals(angVelo,robot.getHeading(), 0.0001);
    }
    
    @Test
    public void robotIsAtPosition_2OverPI_2OverPIIfVeloIs1AndAngVeloIsPiOver2InOneSecond() {
        double angVelo = Math.PI / 2;
        for(int i = 0; i < 60; i++)
            robot.step(1f, angVelo);
        assertThat(robot.getPosition(), vEquals(2f/Math.PI,2f/Math.PI,0.05));
    }
    
}
