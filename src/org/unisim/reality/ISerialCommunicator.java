package org.unisim.reality;

import jssc.SerialPortEvent;
import jssc.SerialPortException;

/**
 * Represents an abstract serial port communicator, allowing sending of
 * commands. Is notified by the serial port when a SerialEvent occurs (i.e. data
 * received).
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface ISerialCommunicator {

    void sendCommand(String command) throws SerialPortException;

    void serialEvent(SerialPortEvent spe);

    void setListener(DataReceivedListener listener);

}
