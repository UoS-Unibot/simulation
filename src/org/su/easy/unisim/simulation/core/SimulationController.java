package org.su.easy.unisim.simulation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.simulation.robot.IRobot;
import org.su.easy.unisim.simulation.robot.IRobotController;
import org.su.easy.unisim.simulation.robot.SimulatedUnibot;
import org.su.easy.unisim.util.Shape2D;

/**
 * Controls the simulation. A new simulation is instantiated using the builder
 * pattern, calling
 * <pre><code>new SimulationController.SimulationBuilder(IRobotController).build() </code></pre>for
 * the default values, or e.g.
 * <pre><code> new SimulationController.SimulationBuilder(null).setWorldSize(new Vector2D(5,5)).build();</code></pre>
 * to specify optional parameters. See the SimulationBuilder javadoc for more
 * details.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulationController implements CollisionListener{

    private final SimulationWorld world;
    private final SimulatedUnibot robot;
    private final LinkedList<SimulationEventListener> listeners = new LinkedList<>();
    private final boolean loggingEnabled;
    private final SimLogger dataLogger = new SimLogger();
    private final double timeStep;

    /**
     * Called by the SimulationBuilder to create a new instance.
     *
     * @param builder
     */
    private SimulationController(SimulationBuilder builder) {
        world = new SimulationWorld(builder.worldSize);
        world.addWorldObjects(builder.worldObjects);
        robot = new SimulatedUnibot(builder.controller, builder.robotPosition, builder.robotSize, builder.robotInitialHeading,(float)builder.worldSize.getNorm());
        timeStep = builder.timeStepLength;
        robot.setTimeStepLength(builder.timeStepLength);
        loggingEnabled = builder.loggingEnabled;
        world.addListener(this);
    }

    /**
     * Steps the simulation one timestep.
     */
    public void step() {
        robot.step(robot.calculateInput(world));
        world.checkCollisions(robot);
        if(loggingEnabled)
            dataLogger.update(robot);
    }

    /**
     * Get the value of robot
     *
     * @return the value of robot
     */
    public IRobot getRobot() {
        return robot;
    }

    /**
     * Get the value of world
     *
     * @return the value of world
     */
    public SimulationWorld getWorld() {
        return world;
    }

    public SimLogger getDataLogger() {
        return dataLogger;
    }

    public double getTimeStep() {
        return timeStep;
    }
    
    
    
    public void addListener(SimulationEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(SimulationEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean collisionOccured() {
        for(SimulationEventListener sel : listeners)
            if(sel.collisionOccured())
                return true;
        return false;
    }

    /**
     * Builds an SimulationController instance, specifying an IRobotController
     * and any optional parameters. Call <code>build()</code> to get the
     * instance.
     */
    public static class SimulationBuilder {

        private Vector2D worldSize = new Vector2D(5, 5);
        private Vector2D robotSize = new Vector2D(0.6, 0.6);
        private Vector2D robotPosition = Vector2D.ZERO;
        private float robotInitialHeading = 0;
        private float timeStepLength = 1f / 60f;
        private Collection<Shape2D> worldObjects = new ArrayList<>(0);
        private IRobotController controller;
        private boolean loggingEnabled = false;

        /**
         * Instantiates the builder - optional parameters are set to their
         * defaults.
         *
         * @param controller IRobotController used to control the robot.
         */
        public SimulationBuilder(IRobotController controller) {
            this.controller = controller;
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

        /**
         * Sets the size of the robot in metres, 0.6mx0.6m by default.
         *
         * @param robotSize Vector2D with dimensions of the robot's size in
         * metres.
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
         * @param robotPosition Vector2D with the robot's initial position in
         * world coordinates
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
         * @param robotInitialHeading Angle in radians specifying initial
         * heading of the robot.
         * @return SimulationBuilder instance for further parameter setting or
         * building.
         */
        public SimulationBuilder setRobotInitialHeading(float robotInitialHeading) {
            this.robotInitialHeading = robotInitialHeading;
            return this;
        }

        /**
         * Sets the length of each timestep for integration - 1/60s by default,
         * but can be set for finer or coarser integration of the velocity,
         * controller etc. Note that this is entirely independent of the actual
         * speed the simulation runs at, which is determined by how regularly
         * the step() function is called.
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
         * Sets the collection of WorldObjs that this simulated world will
         * contain. Note that Bounding LineObjs are generated automatically by
         * the SimulatedWorld on instantiation.
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
         * Builds an instance of SimulationController including any parameters
         * set.
         *
         * @return final SimulationController
         */
        public SimulationController build() {
            return new SimulationController(this);
        }

    }

}
