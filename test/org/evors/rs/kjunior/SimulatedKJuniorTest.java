/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.kjunior;

import mockit.Mocked;
import static org.evors.core.TestUtils.vEquals;
import org.evors.rs.sim.core.SimulationWorld;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author miles
 */
public class SimulatedKJuniorTest {
    
    public SimulatedKJuniorTest() {
    }

    @Mocked SimulationWorld world;
    
    @Test
    public void getIRBase0GetsPointAt0065_0() {
        SimulatedKJunior robot = new SimulatedKJunior(world, 1f/60f);
        assertThat(robot.getIRBase(0),vEquals(0.065,0));
    }
    
    @Test
    public void getIRBasePIGetsPointAtneg0065_0() {
        SimulatedKJunior robot = new SimulatedKJunior(world, 1f/60f);
        assertThat(robot.getIRBase((float) Math.PI),vEquals(-0.065,0));
    }
    
    @Test
    public void getIRBase3PI2GetsPointAt0_0065() {
        SimulatedKJunior robot = new SimulatedKJunior(world, 1f/60f);
        assertThat(robot.getIRBase((float) (3 * Math.PI / 2)),vEquals(0,-0.065));
    }
}
