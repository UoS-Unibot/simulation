/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package end2end;

import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;
import org.unisim.reality.RealRobotBody;
import org.unisim.reality.RunController;
import org.unisim.reality.SerialCommunicator;
import org.unisim.simulation.robot.IRobotController;
import org.unisim.simulation.robot.RobotInput;

/**
 *
 * @author miles
 */
public class RunControllerRealRobotBodyIntegration {

    public RunControllerRealRobotBodyIntegration() {
    }

    @Mocked IRobotController controller;
    @Mocked SerialCommunicator serial;
    RunController run;
    RealRobotBody robot;

    @Before
    public void setUp() throws SerialPortException {
        robot = new RealRobotBody();
        run = new RunController(controller, robot);
    }

    @Test
    public void stepSendsMovementAndRangeCommands() throws SerialPortException,
            SerialPortTimeoutException {
        new Expectations() {
            {
                controller.step((RobotInput) any);
                controller.getVelocity();
                result = 1.0;
                controller.getAngularVelocity();
                result = 1.0;
                serial.sendCommand("#d 1.000000 1.000000");
                serial.readLine();
                result = "#! 300.0 300.0 30.0 20.0";
                result = "#q 360.00";
                serial.sendCommand("#q");
            }
        };
        run.step();
    }

    @Test
    public void stepOnlyCallsSendCommandTwice() throws SerialPortException,
            SerialPortTimeoutException {
        new Expectations() {
            {
                controller.step((RobotInput) any);
                controller.getVelocity();
                result = 1.0;
                controller.getAngularVelocity();
                result = 1.0;
                serial.sendCommand("#d 1.000000 1.000000");
                serial.readLine();
                result = "#! 300.0 300.0 30.0 20.0";
                result = "#q 360.00";
                serial.sendCommand("#q");
            }
        };

        run.step();
        
        new Verifications() {
            {
                serial.sendCommand(anyString); times =2;
            }
        };
    }

}
