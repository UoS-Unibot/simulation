/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import mockit.Mocked;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class RectangleTest {
    
    public RectangleTest() {
    }

    Rectangle rect = Rectangle.createFromCenter(Vector2D.ZERO, Vector2D.ZERO, 0);
    @Mocked Line line;
    
    @Test(expected = UnsupportedOperationException.class)
    public void addingLinesToARectangleThrowsAnException() {
        rect.addLine(line);
    }
    
}
