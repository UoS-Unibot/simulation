/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.robot;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class SimulatedUnibotTest {
    
    public SimulatedUnibotTest() {
    }
    
    SimulatedUnibot robot;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        robot = new SimulatedUnibot(new MockRobotController(),Vector2D.ZERO,new Vector2D(2, 2),0);
    }
    
    @Test
    public void areaHasCoordinatesOf() {
        Area area = robot.getArea();
        assertFalse("Area should not be empty",area.isEmpty());
        PathIterator pi = area.getPathIterator(null);
        double[] dcoords = new double[6];
        Set<Point2D> coords = new HashSet<>();
        for(int curSeg = pi.currentSegment(dcoords);pi.isDone();pi.next()) {
            System.out.println(Arrays.toString(dcoords));
        }
    }
    
    @After
    public void tearDown() {
    }

    class MockRobotController implements IRobotController {

        @Override
        public void step(RobotInput input) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getVelocity() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getAngularVelocity() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
