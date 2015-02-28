package org.unisim.reality;

import org.junit.*;
import mockit.*;
import jssc.*;

/**
 * Simple proof of concept for the use of JMockit to test the serial port communicator
 * outside of the robotics lab
 * @author patrick
 */
public class SerialCommunicatorMockTest {
	@Mocked SerialPortEvent mockedEvent;
	@Mocked SerialPort mockedSerialPort;
	@Mocked DataReceivedListener mockedListener; 
	@Test public void test() throws SerialPortException {
		new Expectations() {{
			mockedSerialPort.readBytes(); result = new byte[] {0x41, 0x31};
			mockedEvent.isRXCHAR(); result = true;
		}};
		SerialCommunicator communicatorUnderTest = new SerialCommunicator();
		communicatorUnderTest.setListener(mockedListener);
		communicatorUnderTest.serialEvent(mockedEvent);
		new Verifications() {{
			mockedListener.dataReceivedEvent("A1"); times = 1;
		}};
	}
}
