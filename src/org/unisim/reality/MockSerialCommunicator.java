package org.unisim.reality;

import jssc.SerialPortEvent;

/**
 * A mock serial port communicator, intended for development and testing offline
 * without a valid serial port connection.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class MockSerialCommunicator implements ISerialCommunicator {

    public String lastCommand = "";

    @Override
    public void sendCommand(String command) {
        lastCommand = command;
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {

    }

    @Override
    public void setListener(DataReceivedListener listener) {

    }

}
