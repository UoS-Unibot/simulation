/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.robot;

import org.su.easy.unisim.simulation.robot.RangeFinder;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.su.easy.unisim.util.Shape2D;

/**
 *
 * @author miles
 */
public class RangeFinderTest {

    public RangeFinderTest() {
    }

    private Shape2D create22obj(float x, float y) {
        return Shape2D.createRectangleFromCenter(new Vector2D(x, y), new Vector2D(2, 2), 0);
    }

    Collection<Shape2D> objects = new ArrayList<>();
    Shape2D objOfSize2_2At4_0 = create22obj(4, 0);
    Shape2D objOfSize2_2At4_4 = create22obj(4, 4);
    Shape2D objOfSize2_2At0_8 = create22obj(0, 8);
    Shape2D objOfSize2_2Atn6_6 = create22obj(-6, 6);
    Shape2D objOfSize2_2Atn9_0 = create22obj(-9, 0);
    Shape2D objOfSize2_2Atn5_n5 = create22obj(-5, -5);
    Shape2D objOfSize2_2At0_n3 = create22obj(0, -3);
    Shape2D objOfSize2_2At3_n3 = create22obj(3, -3);
    RangeFinder rangeFinder;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Before
    public void setUp() {
        rangeFinder = new RangeFinder(Vector2D.ZERO, 0, 20);
        objects.add(objOfSize2_2At4_0);
        objects.add(objOfSize2_2At4_4);
        objects.add(objOfSize2_2At0_8);
        objects.add(objOfSize2_2Atn6_6);
        objects.add(objOfSize2_2Atn9_0);
        objects.add(objOfSize2_2Atn5_n5);
        objects.add(objOfSize2_2At0_n3);
        objects.add(objOfSize2_2At3_n3);
    }

    @After
    public void tearDown() {
        objects.clear();
    }

    @Test
    public void rangeFindShouldReturn3WithAngleOf0() {
        float result = rangeFinder.findRange(objects);
        assertEquals(3f, result, 0.0001);
    }

    @Test
    public void rangeFindShouldReturnroot18WithAngleOfPi4() {
        rangeFinder.setAngle((float) (Math.PI / 4));
        float result = rangeFinder.findRange(objects);
        assertEquals(Math.sqrt(18), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn7WithAngleOfPi2() {
        rangeFinder.setAngle((float) (Math.PI / 2));
        float result = rangeFinder.findRange(objects);
        assertEquals(7f, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnsqr50WithAngleOf3Pi4() {
        rangeFinder.setAngle((float) (3 * Math.PI / 4));
        float result = rangeFinder.findRange(objects);
        assertEquals(Math.sqrt(50), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn8WithAngleOfPi() {
        rangeFinder.setAngle((float) (Math.PI));
        float result = rangeFinder.findRange(objects);
        assertEquals(8f, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnroot32WithAngleOf5Pi4() {
        rangeFinder.setAngle((float) (5 * Math.PI / 4));
        float result = rangeFinder.findRange(objects);
        assertEquals(Math.sqrt(32), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn2WithAngleOf3Pi2() {
        rangeFinder.setAngle((float) (3 * Math.PI / 2));
        float result = rangeFinder.findRange(objects);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnroot8WithAngleOf7Pi4() {
        rangeFinder.setAngle((float) (7 * Math.PI / 4));
        float result = rangeFinder.findRange(objects);
        assertEquals(Math.sqrt(8), result, 0.0001);
    }

}
