/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import org.evors.core.util.geometry.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.evors.core.util.geometry.Line.LineIntersection;
import static org.evors.core.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class LineTest {

    public LineTest() {
    }

    private static Line rotationLine;

    @BeforeClass
    public static void setUpClass() {
        
        rotationLine = Line.fromCoords(0,0,4,4);
        
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
    public void lineFromCentreWith0RotationHasP1_n1_0_AndP2_1_0(){
        Line l = Line.fromCenterPoint(0, 0, 2, 0);
        assertThat(l.p1,vEquals(-1,0));
        assertThat(l.p2,vEquals(1,0));
    }
    @Test
    public void lineFromCentreWithPI2RotationHasP1_0_n1_AndP2_0_1(){
        Line l = Line.fromCenterPoint(0, 0, 2, Math.PI/2);
        assertThat(l.p1,vEquals(0,-1));
        assertThat(l.p2,vEquals(0,1));
    }
    
    @Test
    public void rotatingLineByMinusPiOver2Around2_2MovesP1to0_4AndP2to4_0() {
        rotationLine.rotate(new Vector2D(2,2), -Math.PI/2);
        assertThat(rotationLine.p1,vEquals(new Vector2D(0,4)));
        assertThat(rotationLine.p2,vEquals(new Vector2D(4,0)));
    }
    
}
