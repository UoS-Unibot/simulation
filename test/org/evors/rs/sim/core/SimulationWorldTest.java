/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.core;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.evors.core.util.geometry.Line;
import org.evors.core.util.geometry.Polygon;
import org.evors.core.util.geometry.Shape2D;

/**
 *
 * @author miles
 */
public class SimulationWorldTest {
    
    public SimulationWorldTest() {
    }
    
    SimulationWorld world;
    Line rangeF;
    
    private Polygon create22obj(float x, float y) {
        return Polygon.createRectangleFromCenter(new Vector2D(x, y), new Vector2D(2, 2), 0);
    }
    
    Collection<Shape2D> objects = new ArrayList<>();
    Polygon objOfSize2_2At4_0 = create22obj(4, 0);
    Polygon objOfSize2_2At4_4 = create22obj(4, 4);
    Polygon objOfSize2_2At0_8 = create22obj(0, 8);
    Polygon objOfSize2_2Atn6_6 = create22obj(-6, 6);
    Polygon objOfSize2_2Atn9_0 = create22obj(-9, 0);
    Polygon objOfSize2_2Atn5_n5 = create22obj(-5, -5);
    Polygon objOfSize2_2At0_n3 = create22obj(0, -3);
    Polygon objOfSize2_2At3_n3 = create22obj(3, -3);
    
    @Before
    public void setUp() {
        world = new SimulationWorld(new Vector2D(20,20));
        objects.add(objOfSize2_2At4_0);
        objects.add(objOfSize2_2At4_4);
        objects.add(objOfSize2_2At0_8);
        objects.add(objOfSize2_2Atn6_6);
        objects.add(objOfSize2_2Atn9_0);
        objects.add(objOfSize2_2Atn5_n5);
        objects.add(objOfSize2_2At0_n3);
        objects.add(objOfSize2_2At3_n3);
        world.addWorldObjects(objects);
        rangeF = Line.fromPolarCoords(0, 0, 0, 100);
    }

    @Test
    public void rangeFindShouldReturn3WithAngleOf0() {
        double result = world.traceRay(rangeF);
        assertEquals(3f, result, 0.0001);
    }

    @Test
    public void rangeFindShouldReturnroot18WithAngleOfPi4() {
        rangeF.rotate((float) (Math.PI / 4));
        double result = world.traceRay(rangeF);
        assertEquals(Math.sqrt(18), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn7WithAngleOfPi2() {
        rangeF.rotate((float) (Math.PI / 2));
        double result = world.traceRay(rangeF);
        assertEquals(7f, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnsqr50WithAngleOf3Pi4() {
        rangeF.rotate((float) (3 * Math.PI / 4));
        double result = world.traceRay(rangeF);
        assertEquals(Math.sqrt(50), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn8WithAngleOfPi() {
        rangeF.rotate((float) (Math.PI));
        double result = world.traceRay(rangeF);
        assertEquals(8f, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnroot32WithAngleOf5Pi4() {
        rangeF.rotate((float) (5 * Math.PI / 4));
        double result = world.traceRay(rangeF);
        assertEquals(Math.sqrt(32), result, 0.0001);
    }

    @Test
    public void findRangeShouldReturn2WithAngleOf3Pi2() {
        rangeF.rotate((float) (3 * Math.PI / 2));
        double result = world.traceRay(rangeF);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void findRangeShouldReturnroot8WithAngleOf7Pi4() {
        rangeF.rotate((float) (7 * Math.PI / 4));
        double result = world.traceRay(rangeF);
        assertEquals(Math.sqrt(8), result, 0.001);
    }
    
}
