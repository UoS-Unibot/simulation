/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unibotsim;

import org.su.easy.unisim.sim.world.RectangleObj;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static unibotsim.TestUtils.assertTwoVector2DsEqual;

/**
 *
 * @author miles
 */
public class RectangleObjTest {
    
    
    RectangleObj objOfSize2_2At4_0 = new RectangleObj(new Vector2D(4, 0), new Vector2D(2,2), 0);
    
    public RectangleObjTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void topLeftCornerShouldBe3_1() {
        Vector2D result = objOfSize2_2At4_0.getTopLeftCorner();
        assertTwoVector2DsEqual(new Vector2D(3,1),result,true);
    }
    
    @Test
    public void bottomRightCornerShouldBe5_neg1() {
        Vector2D result = objOfSize2_2At4_0.getBottomRightCorner();
        assertTwoVector2DsEqual(new Vector2D(5,-1),result,true);
    }

    @Test
    public void lineFrom0_0to6_0ShouldIntersectAt3_0and5_0() {
        Line2D line = new Line2D.Float(0,0,10,0);
        ArrayList<Double> dists = objOfSize2_2At4_0.getLineIntersectionDists(line);
        assertEquals(3,dists.get(0),0.001);
        assertEquals(5,dists.get(1),0.001);
        
    }
    
}
