/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.core;

import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.RunController;
import org.evors.core.IRobotController;
import org.evors.core.util.geometry.Shape2D;
import org.evors.rs.sim.robot.SimulatedRobotBody;
import org.evors.rs.unibot.sim.SimulatedUnibot;

/**
 * Builds an SimulationController instance, specifying an IRobotController and
 * any optional parameters. Call <code>build()</code> to get the instance.
 */
public class SimulationBuilder {

    private Vector2D worldSize = new Vector2D(5, 5);
    private Vector2D robotSize = new Vector2D(0.6, 0.6);
    private float timeStepLength = 1f / 60f;
    private IRobotController controller;
    private boolean loggingEnabled = false;
    private SimulationWorld world;
    private final SimulatedRobotBody robot;

    /**
     * Instantiates the builder - optional parameters are set to their defaults.
     *
     * @param controller IRobotController used to control the robot.
     */
    public SimulationBuilder(IRobotController controller,
            SimulatedRobotBody robot) {
        this.controller = controller;
        this.robot = robot;
    }

    public SimulationBuilder(IRobotController controller, SimulationWorld world,
            SimulatedRobotBody robot) {
        this.controller = controller;
        this.world = world;
        this.robot = robot;
    }

    public SimulationBuilder setWorld(SimulationWorld world) {
        this.world = world;
        return this;
    }

    /**
     * Sets the size of the robot in metres, 0.6mx0.6m by default.
     *
     * @param robotSize Vector2D with dimensions of the robot's size in metres.
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setRobotSize(Vector2D robotSize) {
        this.robotSize = robotSize;
        return this;
    }

    /**
     * Sets the length of each timestep for integration - 1/60s by default, but
     * can be set for finer or coarser integration of the velocity, controller
     * etc. Note that this is entirely independent of the actual speed the
     * simulation runs at, which is determined by how regularly the step()
     * function is called.
     *
     * @param timeStepLength Length of the integration timestep in seconds.
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setTimeStepLength(float timeStepLength) {
        this.timeStepLength = timeStepLength;
        return this;
    }

    /**
     * Sets the collection of WorldObjs that this simulated world will contain.
     * Note that Bounding LineObjs are generated automatically by the
     * SimulatedWorld on instantiation.
     *
     * @param worldObjects Collection of fully formed WorldObjs
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setWorldObjects(Collection<Shape2D> worldObjects) {
        SimulationWorld world = new SimulationWorld(worldSize);
        world.addWorldObjects(worldObjects);
        return setWorld(world);
    }

    public SimulationBuilder setLogging(boolean enabled) {
        loggingEnabled = enabled;
        return this;
    }

    /**
     * Builds an instance of SimulationController including any parameters set.
     *
     * @return final SimulationController
     */
    public RunController build() {
        if (world == null) {
            world = new SimulationWorld(worldSize);
        }
        return new RunController(controller, robot);
    }

}
