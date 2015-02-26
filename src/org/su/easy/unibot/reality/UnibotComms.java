/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unibot.reality;

import java.util.logging.Logger;

/**
 *
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public class UnibotComms {
    private static final Logger LOG = Logger.getLogger(UnibotComms.class.getName());

    private final ISerialCommunicator serial;

    public UnibotComms() {
        this(new SerialCommunicator());
    }

    public UnibotComms(ISerialCommunicator serial) {
        this.serial = serial;
    }
    
    public void setDataReceivedListener(DataReceivedListener listener) {
        this.serial.setListener(listener);
    }

    private String getCommandStr(UnibotCommands command, String... args) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("#").append(command.getCommandStr());
        for (String arg : args) {
            cmd.append(arg);
            cmd.append(" ");
        }
        cmd.append("\r");
        return cmd.toString();
    }

    public void doEmergencyStop() {
        sendCommand(UnibotCommands.EMERGENCY_STOP);
    }

    public void doDifferentialDrive(float velocity, float wheelDiff) {
        if(velocity < -1f | velocity > 1f | wheelDiff < -1f | wheelDiff > 1f) {
            LOG.warning(String.format("Parameters supplied outside of [-1,1] range, ignoring command. Velocity=%.3f, WheelDiff=%.3f", velocity, wheelDiff));;
            return;
        }
        sendCommand(UnibotCommands.DRIVE_DIFFERENTIAL_EXT,String.valueOf(velocity),String.valueOf(wheelDiff));
    }

    public void doVectorDrive(float distance, float angleDegrees) {
        if(distance < -1f | distance > 1f | angleDegrees < -90 | angleDegrees > 90) {
            LOG.warning(String.format("Parameters supplied outside of ranges, ignoring command. distance[-1,1]=%.3f, angleDegrees[-90,90]=%3.f", distance, angleDegrees));;
            return;
        }
        sendCommand(UnibotCommands.DRIVE_VECTOR_EXT,String.valueOf(distance),String.valueOf(angleDegrees));
    }

    public void sendCommand(UnibotCommands command) {
        serial.sendCommand(getCommandStr(command));
    }

    public void sendCommand(UnibotCommands command, String... args) {
        serial.sendCommand(getCommandStr(command, args));
    }


}
