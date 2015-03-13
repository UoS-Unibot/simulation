/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class CircleTest {

    public CircleTest() {
    }

    @Test
    public void twoCirclesShouldIntersect() {
        Circle circle1 = Circle.getFromCenter(Vector2D.ZERO, 4),
                circle2 = Circle.getFromCenter(7, 0, 4);
        assertTrue(circle1.intersectsWith(circle2));
    }
    
    @Test
    public void circleShouldIntersectWithLine() {
        Circle circle1 = Circle.getFromCenter(Vector2D.ZERO, 4);
        Line line = Line.fromCoords(2, 0, 0, 4);
        assertTrue(circle1.intersectsWith(line));
    }

}
