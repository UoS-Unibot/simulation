/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import org.evors.core.util.geometry.Shape2D;
import org.evors.core.util.geometry.Line;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.evors.core.TestUtils.vIsIn;

/**
 *
 * @author miles
 */
public class Shape2DTest {

    public Shape2DTest() {
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
    public void createRectangleShouldCreatePoints() {
        Shape2D shape = Shape2D.createRectangleFromCenter(Vector2D.ZERO, new Vector2D(2, 2), 0);
        assertFalse(shape.isEmpty());
    }

    @Test
    public void createRectangleShouldCreatePointsAtn1_1__1_1__1_n1__n1_n1() {
        Shape2D shape = Shape2D.createRectangleFromCenter(Vector2D.ZERO, new Vector2D(2, 2), 0);
        ArrayList<Vector2D> points = new ArrayList<>(4);
        points.add(new Vector2D(-1,1));
        points.add(new Vector2D(1,1));
        points.add(new Vector2D(1,-1));
        points.add(new Vector2D(-1,-1));
        for(Line line : shape.getLines()) {
            assertThat(line.p1, vIsIn(points));
            assertThat(line.p2, vIsIn(points));
        }
    }

    @Test
    public void rectangleDoesntIntersectAnotherRectangle() {
        Shape2D rect1 = Shape2D.createRectangleFromCenter(Vector2D.ZERO, new Vector2D(2, 2), 0);
        Shape2D rect2 = Shape2D.createRectangleFromCenter(new Vector2D(0, 5), new Vector2D(2, 2), 0);
        assertFalse("Rectangles intersect", rect1.intersectsWith(rect2));
    }

    @Test
    public void rectangleIntersectsAnotherRectangle() {
        Shape2D rect1 = Shape2D.createRectangleFromCenter(Vector2D.ZERO, new Vector2D(2, 2), 0);
        Shape2D rect2 = Shape2D.createRectangleFromCenter(new Vector2D(0, 1), new Vector2D(2, 2), Math.PI / 4);
        assertTrue("Rectangles don't intersect", rect1.intersectsWith(rect2));
    }

}
