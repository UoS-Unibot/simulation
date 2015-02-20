package org.su.easy.unisim.robot;

import java.awt.geom.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.sim.world.SimulationWorld;
import org.su.easy.unisim.sim.world.WorldObj;

/**
 * The SimulatedUnibot class implements IRobot with a simulated differential
 * drive model of the Unibot.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulatedUnibot implements IRobot {

    private float timeStepLength = 1f / 60f,
            angularVelocity,heading, forwardVelocity;
    private Vector2D currentPos;
    private final IRobotController controller;
    private final RangeFinder rangeFinder;
    private final Vector2D size;

    /**
     * Initialises a default SimulatedUnibot with initial position of 0,0,
     * initial heading of 0 radians and size of 0.6mx0.6m
     *
     * @param controller IRobotController used to control this robot.
     */
    public SimulatedUnibot(IRobotController controller) {
        this(controller, Vector2D.ZERO, new Vector2D(0.6, 0.6), 0);
    }

    /**
     * Initialises a SimulatedUnibot specifying all parameters.
     *
     * @param controller IRobotController used to control this robot.
     * @param initialPosition initial world coordinates of the robot.
     * @param size Size of the robot in metres.
     * @param initialHeading Initial heading of the robot in radians.
     */
    public SimulatedUnibot(IRobotController controller, Vector2D initialPosition, Vector2D size, float initialHeading) {
        currentPos = initialPosition;
        heading = initialHeading;
        forwardVelocity = 0;
        this.size = size;
        this.controller = controller;
        rangeFinder = new RangeFinder(getRangeFinderBase(), initialHeading, timeStepLength);
    }

    /**
     * Steps the SimulatedUnibot one time step, updating the controller with the
     * RobotInput (which would normally be the result of calling
     * calculateInput()) and setting the robot's forward and angular velocity to
     * the relevant output of the controller.
     *
     * @param input Simulated RobotInput.
     */
    @Override
    public void step(RobotInput input) {
        currentPos = currentPos.add(new Vector2D(forwardVelocity, 0).scalarMultiply(timeStepLength));
        controller.step(input);
        setVelocity(controller.getVelocity(), controller.getAngularVelocity());
    }

    /**
     * Simulates the range finder and sonar of the Unibot given the current
     * SimulationWorld - normally passed to this robot's step() function.
     *
     * @param world SimulationWorld.
     * @return Simulated RobotInput with calculated values.
     */
    public RobotInput calculateInput(SimulationWorld world) {
        return new RobotInput(
                rangeFinder.findRange(world.getObjects()),
                0,
                0,
                0,
                0
        );
    }

    /**
     * Gets a Java 2D geometry Rectangle2D object representing the robot's size
     * and current position. Currently only used in rendering.
     *
     * @return Rectangle2D object with current size and position.
     */
    @Override
    public Rectangle2D getRectangle() {
        double halfW = getSize().getX() / 2,
                halfH = getSize().getY() / 2;
        Rectangle2D rect = new Rectangle2D.Double();
        rect.setFrame(
                getPosition().getX() - halfW,
                getPosition().getY() + halfH, 
                getSize().getX(), 
                -getSize().getY());
        return rect;
    }

    /**
     * @return Where the base of the rangefinder is currently located.
     */
    private Vector2D getRangeFinderBase() {
        return new Vector2D(getPosition().getX() + getSize().getX() / 2, getPosition().getY());
    }

    /**
     * Processes a collision with an object in the world.
     * @param collidedObj Object that is colliding with this robot.
     */
    public void doCollision(WorldObj collidedObj) {
        setVelocity(0);
    }

    @Override
    public void setVelocity(float forwardVelocity) {
        this.forwardVelocity = forwardVelocity;
    }
    
    @Override
    public void setVelocity(float forwardVelocity, float angularVelocity) {
        setVelocity(forwardVelocity);
        setAngularVelocity(angularVelocity);
    }

    @Override
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setTimeStepLength(float lengthTimeStep) {
        this.timeStepLength = lengthTimeStep;
    }

    /**
     * @return the current IRobotController controlling this robot.
     */
    public IRobotController getController() {
        return controller;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public Vector2D getSize() {
        return size;
    }

    @Override
    public float getHeading() {
        return heading;
    }

    @Override
    public Vector2D getPosition() {
        return currentPos;
    }


    @Override
    public float getVelocity() {
        return forwardVelocity;
    }

}
