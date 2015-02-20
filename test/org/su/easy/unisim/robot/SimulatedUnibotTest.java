/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.robot;

import java.awt.geom.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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
    
    @After
    public void tearDown() {
    }

    @Test
    public void getRectangleGeneratesRectangleAt_n1_1_withRotationOf0() {
        Rectangle2D rect = robot.getRectangle();
        assertEquals("center x",0,rect.getCenterX(),0.001);
        assertEquals("center y",0,rect.getCenterY(),0.001);
        assertEquals("top left x",-1,rect.getX(),0.001);
        assertEquals("top left y",1,rect.getY(),0.001);
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
