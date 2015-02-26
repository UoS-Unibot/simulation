package org.unisim.reality;

/**
 * Contains valid unibot commands with the corresponding command character to
 * send through the serial port.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public enum UnibotCommands {

    DATA_GET_PACKET("!"),
    DATA_GET_INERTIAL_MEASUREMENT("I"),
    DATA_SET_PACKET_TYPE("t"),
    DATA_REQUEST_WHEEL_STATE("W"),
    DRIVE_DIFFERENTIAL_EXT("D"),
    DRIVE_DIFFERENTIAL_INT("d"),
    DRIVE_ACKERMANN_VAR("C"),
    DRIVE_ACKERMANN_FRONT("A"),
    DRIVE_ACKERMANN_REAR("a"),
    DRIVE_ACKERMANN_SET_AXLE_POS("z"),
    DRIVE_VECTOR_EXT("V"),
    DRIVE_VECTOR_INT("v"),
    RANGEFINDER_SET_ANGLE("Q"),
    RANGEFINDER_GET_RANGE("q"),
    RANGEFINDER_START_SCANNING("L"),
    RANGEFINDER_STOP_SCANNING("l"),
    PING("?"),
    SET_PAUSE_STATE("P"),
    EMERGENCY_STOP("X"),
    SET_STEERING_MOTOR_SPEED("g"),
    SET_PID("M"),;

    private final String commandStr;

    private UnibotCommands(String commandStr) {
        this.commandStr = commandStr;
    }

    /**
     * Gets the command string associated with this command that is sent to the
     * serial port (without the # prefix)
     *
     * @return
     */
    public String getCommandStr() {
        return commandStr;
    }
}
