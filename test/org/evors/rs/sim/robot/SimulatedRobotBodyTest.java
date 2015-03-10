/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot;

import org.evors.rs.sim.robot.SimulatedRobotBody;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.util.geometry.Line;
import static org.evors.core.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class SimulatedRobotBodyTest {

    public SimulatedRobotBodyTest() {
    }

    SimulatedRobotBody robot;
    @Mocked SimulationWorld world;

    @Before
    public void setUp() {
        new Expectations() {{
            world.getBounds(); result = new Vector2D(5,5);
        }};
        robot = new SimulatedRobotBody(world);
    }
    
    public void setUpForRangeFinder() {
        new Expectations() {{
            world.getBounds(); result = new Vector2D(5,5);
        }};
        robot = new SimulatedRobotBody(world,new Vector2D(2,2),1f/60f);
    }
    
    @Test
    public void newRobotCreatesRangeFinderAt1_0__1PlusRoot50_0() {
        setUpForRangeFinder();
        assertThat(robot.getRangeFinderLine().p1, vEquals(1,0));
        assertThat(robot.getRangeFinderLine().p2, vEquals(1+Math.sqrt(50),0));
    }

    @Test
    public void stepWith0VelocityDoesNotMoveRobot() {
        robot.step(0, 0);
        assertThat(robot.getPosition(), vEquals(0, 0));
    }
    
    @Test
    public void shapeDoesntMoveWhenRobotDoesnt() {
        robot.step(0, 0);
        assertThat(robot.getShape().getCenter(), vEquals(0,0));
    }
    
    @Test
    public void shapeMoves1MWhenRobotMoves1M() {
        for (int i = 0; i < 60; i++) {
            robot.step(1, 0);
        }
        assertThat(robot.getShape().getCenter(), vEquals(1,0));
    }
    
    @Test
    public void rangeFinderDoesntMoveWhenRobotDoesntMove() {
        setUpForRangeFinder();
        robot.step(0, 0);
        assertThat(robot.getRangeFinderLine().p1, vEquals(1,0));
        assertThat(robot.getRangeFinderLine().p2, vEquals(1+Math.sqrt(50),0));
    }
    
    @Test
    public void rangeFinderMoves1MeterWhenRobotMoves1Meter() {
        setUpForRangeFinder();
        for (int i = 0; i < 60; i++) {
            robot.step(1, 0);
        }
        assertThat(robot.getRangeFinderLine().p1, vEquals(2, 0));
        assertThat(robot.getRangeFinderLine().p2, vEquals(2+Math.sqrt(50), 0));
    }

    @Test
    public void stepWith1VelocityMovesRobot1MeterInOneSecond() {
        for (int i = 0; i < 60; i++) {
            robot.step(1, 0);
        }
        assertThat(robot.getPosition(), vEquals(1, 0));
    }

    @Test
    public void robotTurnsQuarterCircleNorthInOneSecondIfAngVelocityIsPiOver2() {
        double angVelo = Math.PI / 2;
        for (int i = 0; i < 60; i++) {
            robot.step(0, angVelo);
        }
        assertEquals(angVelo, robot.getHeading(), 0.0001);
    }

    @Test
    public void robotTurnsQuarterCircleNorthInFiveSecondsIfAngVelocityIsPiOver2() {
        double angVelo = Math.PI / 2;
        for (int i = 0; i < 60; i++) {
            robot.step(0, angVelo);
        }
        assertEquals(angVelo, robot.getHeading(), 0.0001);
    }

    @Test
    public void robotIsAtPosition_2OverPI_2OverPIIfVeloIs1AndAngVeloIsPiOver2InOneSecond() {
        double angVelo = Math.PI / 2;
        for (int i = 0; i < 60; i++) {
            robot.step(1f, angVelo);
        }
        assertThat(robot.getPosition(),
                vEquals(2f / Math.PI, 2f / Math.PI, 0.05));
    }

    @Test
    public void getRangePollsWorldForRange() {
        new Expectations() {
            {
                world.findRange(robot.getRangeFinderLine()); result = 1;
            }
        };
        assertEquals(1,robot.getRange(),0.0001);
    }

}
