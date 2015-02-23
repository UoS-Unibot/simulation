package org.su.easy.unisim.robot;

import java.awt.geom.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.sim.world.SimulationWorld;
import org.su.easy.unisim.util.Shape2D;

/**
 * The SimulatedUnibot class implements IRobot with a simulated differential
 * drive model of the Unibot.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulatedUnibot implements IRobot {

    private float timeStepLength = 1f / 60f,
            angularVelocity,forwardVelocity;
    private final IRobotController controller;
    private final RangeFinder rangeFinder;
    private final Vector2D size;
    private Shape2D shape;

    /**
     * Initialises a default SimulatedUnibot with initial position of 0,0,
     * initial heading of 0 radians and size of 0.6mx0.6m
     *
     * @param controller IRobotController used to control this robot.
     */
    public SimulatedUnibot(IRobotController controller,float maxRangeFinderRayLength) {
        this(controller, Vector2D.ZERO, new Vector2D(0.6, 0.6), 0,maxRangeFinderRayLength);
    }

    /**
     * Initialises a SimulatedUnibot specifying all parameters.
     *
     * @param controller IRobotController used to control this robot.
     * @param initialPosition initial world coordinates of the robot.
     * @param size Size of the robot in metres.
     * @param initialHeading Initial heading of the robot in radians.
     */
    public SimulatedUnibot(IRobotController controller, Vector2D initialPosition, Vector2D size, float initialHeading,float maxRangeFinderRayLength) {
        shape = Shape2D.createRectangleFromCenter(initialPosition, size, initialHeading);
        forwardVelocity = 0;
        this.size = size;
        this.controller = controller;
        rangeFinder = new RangeFinder(getRangeFinderBase(), initialHeading, maxRangeFinderRayLength);
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
        move(forwardVelocity*timeStepLength,angularVelocity*timeStepLength);
        controller.step(input);
        setVelocity(controller.getVelocity(), controller.getAngularVelocity());
    }
    
    public void move(float distance, float rotation) {
        shape.move(distance, rotation);
        rangeFinder.setPosition(getRangeFinderBase());
        rangeFinder.setAngle(getHeading());
    }

    /**
     * Simulates the range finder and sonar of the Unibot given the current
     * SimulationWorld - normally passed to this robot's step() function.
     *
     * @param world SimulationWorld.
     * @return Simulated RobotInput with calculated values.
     */
    public RobotInput calculateInput(SimulationWorld world) {
        float range = rangeFinder.findRange(world.getObjects());
        return new RobotInput(
                range,
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
    private Rectangle2D getRectangle() {
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
    public Vector2D getRangeFinderBase() {
        return shape.getLocalToWorldCoords(new Vector2D(size.getX() / 2,size.getY() / 2));
    }

    /**
     * Processes a collision with an object in the world.
     * @param collidedObj Object that is colliding with this robot.
     */
    public void doCollision(Shape2D collidedObj) {
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
    public double getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public Vector2D getSize() {
        return size;
    }

    @Override
    public double getHeading() {
        return shape.getRotation();
    }

    @Override
    public Vector2D getPosition() {
        return shape.getCenter();
    }

    public Shape2D getShape() {
        return shape;
    }

    public RangeFinder getRangeFinder() {
        return rangeFinder;
    }

    

    @Override
    public double getVelocity() {
        return forwardVelocity;
    }

}
