package org.su.easy.unisim.robot;

import java.awt.geom.Rectangle2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The IRobot interface abstractly represents a robot, either real or
 * simulated.<br><br>
 * Implementation of these methods would normally depend on an IRobotController,
 * although convenience methods for setting robot velocity and angular velocity
 * are provided.<br><br>
 *
 * @version 0.1
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface IRobot {

    /**
     * Integrates the robot one timestep; for a real robot this would only
     * update the controller, providing real input gained from the interface.
     * For a simulated robot the input should be calculated.
     *
     * @param input RobotInput information to be provided to the controller.
     */
    public void step(RobotInput input);

    /**
     * @return Current heading of the robot in radians.
     */
    public float getHeading();

    /**
     * @return Vector2D containing world coordinates specifying this robot's
     * current position.
     */
    public Vector2D getPosition();

    /**
     * Sets the robot's angular velocity.
     *
     * @param angularVelocity Angular velocity in radians/second.
     */
    public void setAngularVelocity(float angularVelocity);

    /**
     * Sets the robot's forward velocity. Convenience method leaving angular
     * velocity unchanged.
     *
     * @param forwardVelocity Velocity in metres/second.
     */
    public void setVelocity(float forwardVelocity);

    /**
     * Sets the robot's forward and angular velocities.
     *
     * @param forwardVelocity Velocity in metres/second.
     * @param angularVelocity Angular velocity in radians/second.
     */
    public void setVelocity(float forwardVelocity, float angularVelocity);

    /**
     * @return the robot's angular velocity in radians/second.
     */
    public float getAngularVelocity();

    /**
     * Returns a Java 2D geometry Rectangle2D object; currently only used for
     * drawing the robot
     *
     * @return A Rectangle2D object representing the robot's body
     */
    public Rectangle2D getRectangle();

    /**
     * @return The robot's size in metres.
     */
    public Vector2D getSize();

    /**
     * @return The robot's current forward velocity in metres/second.
     */
    public float getVelocity();

}
