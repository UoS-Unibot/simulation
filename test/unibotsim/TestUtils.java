/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unibotsim;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class TestUtils {
    
    public static void assertTwoVector2DsEqual(Vector2D v1, Vector2D v2,boolean assertion) {
        boolean result;
        if(assertion)
            result = v1.distance(v2) < 0.0001;
        else
            result = v1.distance(v2) > 0.0001;
        assertTrue(
                String.format("Expected: %s, actual: %s",v1.toString(),v2.toString()),
                result
        );
    }
    
    public static void assertTwoVector2DsEqual(float x, float y, Vector2D v2,boolean assertion) {
        assertTwoVector2DsEqual(new Vector2D(x,y),v2,assertion);
    }
    
    @Test
    public void vectorsShouldBeEqual() {
        Vector2D v1 = new Vector2D(2,2);
        Vector2D v2 = new Vector2D(2,2);
        assertTwoVector2DsEqual(v1, v2,true);
    }
    
    @Test
    public void vectorsShouldntBeEqual() {
        Vector2D v1 = new Vector2D(2,2);
        Vector2D v2 = new Vector2D(3,2);
        assertTwoVector2DsEqual(v1, v2,false);
    }
    
}
