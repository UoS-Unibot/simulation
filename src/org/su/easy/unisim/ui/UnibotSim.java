/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.ui;

import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author miles
 */
public class UnibotSim {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimulationWorld world = new SimulationWorld(new Vector2D(10, 10));
    }
    
}
