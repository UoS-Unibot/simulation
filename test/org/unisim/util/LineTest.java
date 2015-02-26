/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.util;

import org.unisim.simulation.geometry.Line;
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
import org.unisim.simulation.geometry.Line.LineIntersection;
import static unibotsim.TestUtils.vEquals;

/**
 *
 * @author miles
 */
public class LineTest {

    public LineTest() {
    }

    static Line collinearDisjointA, collinearDisjointB,
            collinearOverlappingA, collinearOverlappingB,
            parallelA, parallelB,
            interAt1_1A, interAt1_1B,
            interAt4_4A, interAt4_4B,
            notIntersectingA, notIntersectingB,rotationLine,lineDist4A,lineDist4B;

    @BeforeClass
    public static void setUpClass() {
        collinearDisjointA = Line.fromCoords(0, 0, 1, 1);
        collinearDisjointB = Line.fromCoords(3, 3, 4, 4);
        
        collinearOverlappingA = Line.fromCoords(0, 0, 3, 3);
        collinearOverlappingB = Line.fromCoords(1, 1, 4, 4);
        
        parallelA = Line.fromCoords(0, 0, 2, 2);
        parallelB = Line.fromCoords(0, 2, 2, 4);
        
        interAt1_1A = Line.fromCoords(0, 0, 2, 2);
        interAt1_1B = Line.fromCoords(2, 0, 0, 2);
        
        interAt4_4A = Line.fromCoords(0, 0, 5, 5);
        interAt4_4B = Line.fromCoords(4, 0, 4, 5);
        
        notIntersectingA = Line.fromCoords(0, 0, 5, 5);
        notIntersectingB = Line.fromCoords(0, 3, 2, 9);
        
        rotationLine = Line.fromCoords(0,0,4,4);
        
        lineDist4A = Line.fromCoords(0, 0, 0, 5);
        lineDist4B = Line.fromCoords(-2, 4, 2, 4);
        
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
    public void intersectCollinearDisjointLinesReturnNaN() {
        LineIntersection i =  collinearDisjointA.getIntersection(collinearDisjointB);
        assertEquals(Vector2D.NaN,i.intersectionPoint);
    }
    
    @Test
    public void intersectCollinearOverlappingLinesReturnNaN() {
        LineIntersection i =  collinearOverlappingA.getIntersection(collinearOverlappingB);
        assertEquals(Vector2D.NaN, i.intersectionPoint);
    }
    
    @Test
    public void intersectParallelLinesReturnNaN() {
        assertEquals(Vector2D.NaN, parallelA.getIntersection(parallelB).intersectionPoint);
    }
    
    @Test
    public void linesIntersectAt1_1() {
        assertEquals(new Vector2D(1,1), interAt1_1A.getIntersection(interAt1_1B).intersectionPoint);
    }
    
    @Test
    public void line1IntersectionDistanceAt1_1isRoot2() {
        assertEquals(Math.sqrt(2), interAt1_1A.getIntersection(interAt1_1B).line1DistToIntersect,0.0001);
    }
    
    
    @Test
    public void line2IntersectionDistanceAt1_1isRoot2() {
        assertEquals(Math.sqrt(2), interAt1_1A.getIntersection(interAt1_1B).line2DistToIntersect,0.0001);
    }
    
    @Test
    public void rotatedLine1DistIs4() {
        assertThat(lineDist4A.getIntersection(lineDist4B).intersectionPoint, vEquals(0,4));
        assertEquals(4,lineDist4A.getIntersection(lineDist4B).line1DistToIntersect, 0.0001);
    }


    @Test
    public void linesIntersectAt4_4() {
        assertEquals(new Vector2D(4,4), interAt4_4A.getIntersection(interAt4_4B).intersectionPoint);
    }

    @Test
    public void linesDontIntersect() {
        assertEquals(Vector2D.NaN, notIntersectingA.getIntersection(notIntersectingB).intersectionPoint);
    }
    
    
    
    @Test
    public void rotatingLineByMinusPiOver2Around2_2MovesP1to0_4AndP2to4_0() {
        rotationLine.rotate(new Vector2D(2,2), -Math.PI/2);
        assertThat(rotationLine.p1,vEquals(new Vector2D(0,4)));
        assertThat(rotationLine.p2,vEquals(new Vector2D(4,0)));
    }
    
}
