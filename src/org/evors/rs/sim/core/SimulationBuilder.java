/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.core;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.RunController;
import org.evors.core.util.geometry.Shape2D;
import org.evors.core.IRobotController;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 * Builds an SimulationController instance, specifying an IRobotController and
 * any optional parameters. Call <code>build()</code> to get the instance.
 */
public class SimulationBuilder {

    private Vector2D worldSize = new Vector2D(5, 5);
    private Vector2D robotSize = new Vector2D(0.6, 0.6);
    private Vector2D robotPosition = Vector2D.ZERO;
    private float robotInitialHeading = 0;
    private float timeStepLength = 1f / 60f;
    private Collection<Shape2D> worldObjects = new ArrayList<>(0);
    private IRobotController controller;
    private boolean loggingEnabled = false;
    private SimulationWorld world;

    /**
     * Instantiates the builder - optional parameters are set to their defaults.
     *
     * @param controller IRobotController used to control the robot.
     */
    public SimulationBuilder(IRobotController controller) {
        this.controller = controller;
    }

    public SimulationBuilder(IRobotController controller, SimulationWorld world) {
        this.controller = controller;
        this.world = world;
    }

    /**
     * Sets the world bounds in metres, 5mx5m by default.
     *
     * @param worldSize Vector2D with dimensions of the world's bounds in
     * metres.
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setWorldSize(Vector2D worldSize) {
        this.worldSize = worldSize;
        return this;
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
     * Sets the initial position of the robot in metres - [0,0] by default.
     *
     * @param robotPosition Vector2D with the robot's initial position in world
     * coordinates
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setRobotPosition(Vector2D robotPosition) {
        this.robotPosition = robotPosition;
        return this;
    }

    /**
     * Sets the initial heading of the robot in radians - 0 by default.
     *
     * @param robotInitialHeading Angle in radians specifying initial heading of
     * the robot.
     * @return SimulationBuilder instance for further parameter setting or
     * building.
     */
    public SimulationBuilder setRobotInitialHeading(float robotInitialHeading) {
        this.robotInitialHeading = robotInitialHeading;
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
        this.worldObjects = worldObjects;
        return this;
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
        if(world == null)
            world = new SimulationWorld(worldSize);
        return new RunController(controller,new SimulatedRobotBody(world,robotSize,timeStepLength));
    }

}
