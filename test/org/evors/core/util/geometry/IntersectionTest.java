/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.evors.core.TestUtils.vEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author miles
 */
public class IntersectionTest {
    
    public IntersectionTest() {
    }

    static Line collinearDisjointA, collinearDisjointB,
            collinearOverlappingA, collinearOverlappingB,
            parallelA, parallelB,
            interAt1_1A, interAt1_1B,
            interAt4_4A, interAt4_4B,
            notIntersectingA, notIntersectingB,rotationLine,lineDist4A,lineDist4B;
    static Circle circle;

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
        circle = Circle.getFromCenter(Vector2D.ZERO, 2);
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
    public void intersectCollinearDisjointLinesReturnNaN() {
        Intersection i =  collinearDisjointA.getIntersection(collinearDisjointB);
        assertEquals(Vector2D.NaN,i.intersectionPoint);
    }
    
    @Test
    public void intersectCollinearOverlappingLinesReturnNaN() {
        Intersection i =  collinearOverlappingA.getIntersection(collinearOverlappingB);
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
    public void circleAndLine3DontIntersect() {
        Line circLine3 = Line.fromCoords(3,-3, 3, 3);
        assertFalse(Intersection.CircleLine.calculate(circle, circLine3).isIntersection);
    }
    
    @Test
    public void line2IsTangentToCircle() {
        Line circLine2 = Line.fromCoords(2, -4, 2, 4);
        assertTrue(Intersection.CircleLine.calculate(circle, circLine2).isTangent);
    }
    
    @Test
    public void line2TangentPointIs2_0() {
        Line circLine2 = Line.fromCoords(2, -4, 2, 4);
        assertThat(new Vector2D(2,0), vEquals(Intersection.CircleLine.calculate(circle, circLine2).intersectp1));
    }
    
    @Test
    public void line1IntersectsButIsNotTangent() {
        Line circLine1 = Line.fromCoords(0, -4, 0, 4);
        Intersection.CircleLine icl = Intersection.CircleLine.calculate(circle,
                circLine1);
        assertTrue(icl.isIntersection);
        assertFalse(icl.isTangent);
    }
    
    @Test
    public void line1IntersectionPointsAre0_n2__0_2() {
        Line circLine1 = Line.fromCoords(0, -4, 0, 4);
        Intersection.CircleLine icl = Intersection.CircleLine.calculate(circle,
                circLine1);
        assertThat(icl.intersectp1,vEquals(new Vector2D(0,2)));
        assertThat(icl.intersectp2,vEquals(new Vector2D(0,-2)));
    }
    
    @Test
    public void circleAndLineDoNotIntersect() {
        //Reproducing a bug
        circle = Circle.getFromCenter(new Vector2D(0.3467132624,0.0035458654), 0.065);
        Line line = Line.fromCoords(-1.2, -1.2, -0.8, -1.2);
        assertFalse(circle.intersectsWith(line));
    }
    
    @Test
    public void circleAndLineDoNotIntersect2() {
        //Reproducing a bug
        circle = Circle.getFromCenter(new Vector2D(0.3396085131,-0.0168142506), 0.065);
        Line line = Line.fromCoords(0.8, 0.8, 1.2, 0.8);
        assertFalse(circle.intersectsWith(line));
    }
}
