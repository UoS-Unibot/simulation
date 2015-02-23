/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unibotsim;

import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class TestUtils {
    
    public static final double EPS = 1E-5;
    
    public static Matcher<Vector2D> vEquals(double x, double y) {
        return vEquals(new Vector2D(x,y));
    }
    
    public static Matcher<Vector2D> vEquals(final Vector2D v) {
        return new BaseMatcher<Vector2D>() {

            @Override
            public boolean matches(Object item) {
                return ((Vector2D)item).distance(v) < EPS;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Vector does not equal expected vector " + v.toString());
            }
        };
        
    }
    
    public static Matcher<Vector2D> vIsIn(final Collection<Vector2D> vc) {
        return new BaseMatcher<Vector2D>() {

            @Override
            public boolean matches(Object item) {
                Vector2D v2 = (Vector2D)item;
                boolean matchFound = false;
                for(Vector2D v : vc) {
                    if(v2.distance(v) < EPS)
                        matchFound = true;
                }
                return matchFound;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Vector not found in collection :" + vc.toString());
            }
            
        };
    }
    
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
