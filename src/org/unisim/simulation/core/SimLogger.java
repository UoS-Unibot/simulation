package org.unisim.simulation.core;

import org.unisim.simulation.robot.SimulatedUnibot;
import org.unisim.io.DataFile;

/**
 * Not implemented yet; intended for logging useful simulation data for later
 * analysis.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimLogger {

    public final DataFile data;

    public SimLogger() {
        data = new DataFile(new String[]{"RobotX", "RobotY", "RobotHeading", "RobotVelocity", "RobotAngularVelocity", "Rangefinder"});
    }

    public void update(SimulatedUnibot robot) {
        data.addData("RobotX", robot.getPosition().getX());
        data.addData("RobotY", robot.getPosition().getY());
        data.addData("RobotHeading", robot.getHeading());
        data.addData("RobotVelocity", robot.getVelocity());
        data.addData("RobotAngularVelocity", robot.getAngularVelocity());
        data.addData("Rangefinder", robot.getLastRangeFinderValue());
    }

    public DataFile getData() {
        return data;
    }

}
