/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util;

/**
 *
 * @author miles
 */
public class LookupFunctions {
    
    private final static int TABLE_SIZE = 10000;
    private final static double eps = 0.0001;
    private final static float sinestep, invstep, 
            pi2 = (float) (2 * Math.PI),
            piover2 = (float) (Math.PI / 2);
    private final static float[] sines;
    
    
    static {
        
        sines = new float[TABLE_SIZE * 2];
        sinestep = pi2 / TABLE_SIZE; invstep = 1/sinestep;
        
        for (int i = 0; i < TABLE_SIZE * 2; i++) {
            sines[i] = (float) Math.sin(sinestep * i);
            //sigmoids[i] = (float) (1.0f / (1 + Math.exp(-i * sigmoidstep)));
        }
    }
    
    public static double sin(double theta) {
        //http://nerds-central.blogspot.co.uk/2012/11/very-fast-sin-function-for-java.html
        double t = theta;
        if(t < 0)
            while(t < 0)
                t += pi2;
        t %= (pi2-eps);
        int indexA = (int) (Math.floor(t * invstep));
        int indexB = indexA + 1;
        if(indexA >= TABLE_SIZE) {
            indexA = 0; indexB = 1;
        }
        if(indexB >= TABLE_SIZE)
            return sines[indexA];
        double a = sines[indexA];
        return a + (sines[indexB] - a) * (t - (indexA * sinestep)) * invstep;
    }
    
    public static double cos(double theta) {
        return sin(theta + piover2);
    }
    
    
    
    
}
