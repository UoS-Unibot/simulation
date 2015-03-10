/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.unibot.serial;

import org.evors.rs.unibot.serial.SerialCommunicator;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import mockit.Expectations;
import mockit.Mocked;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mb459
 */
public class SerialCommunicatorTest {

    public SerialCommunicatorTest() {
    }

    SerialCommunicator serial;

    @Before
    public void setup() {
        serial = new SerialCommunicator();
    }
    
    @Mocked SerialPort port;
    
    @Test
    public void openPortOpensPort() throws SerialPortException {
        new Expectations() {{
            port.openPort();
        }};
        serial.openSerialPort();
    }
    
    @Test(expected=NullPointerException.class)
    public void closePortWithoutOpenCausesNullPointerException() throws SerialPortException {
        serial.closeSerialPort();
    }
    
    @Test
    public void closePortClosesPort() throws SerialPortException {
        serial.openSerialPort();
        new Expectations() {{
            port.closePort();
        }};
        serial.closeSerialPort();
    }
    
    @Test
    public void sendCommandAppendsCarriageReturn() throws SerialPortException {
        serial.openSerialPort();
        String sentCommand = "#d 0 0";
        final String expCommand = "#d 0 0\r";
        new Expectations() {{
            port.writeBytes(expCommand.getBytes()); result = true;
        }};
        serial.sendCommand(sentCommand);
    }
    
    @Test(expected=RuntimeException.class)
    public void ifSendCommandNotSuccessfulExceptionThrown() throws SerialPortException {
        serial.openSerialPort();
        String sentCommand = "#d 0 0";
        new Expectations() {{
            port.writeBytes((byte[])any); result = false;
        }};
        serial.sendCommand(sentCommand);
    }
    @Mocked SerialPortEvent spe;
    public void serialPortEventWritesToBuffer() throws SerialPortException, SerialPortTimeoutException {
        final String data = "data\n";
        serial.openSerialPort();
        new Expectations() {{
            spe.isRXCHAR();result = true;
            port.readString(); result = data;
        }};
        serial.serialEvent(spe);
        assertEquals(data,serial.readLine());
    }
    

    @Test(expected = SerialPortTimeoutException.class)
    public void getLineThrowsTimeOutExceptionIfBufferIsEmpty() throws SerialPortTimeoutException  {
        assertEquals("", serial.readLine());
    }

    @Test() 
    public void getLineReturnsEmptyStringIfBufferContainsOnlyNewLine() throws SerialPortTimeoutException{
        serial.dataBuffer = "\n";
        assertEquals("", serial.readLine());
        assertEquals("", serial.dataBuffer);
    }

    @Test(expected = SerialPortTimeoutException.class)
    public void getLineThrowsTimeOutExceptionAfterTimeoutIfBufferContainsCommandWithNoNewLine()  throws SerialPortTimeoutException{
        serial.dataBuffer = "#q 206.00";
        assertEquals("", serial.readLine());
        assertEquals("#q 206.00", serial.dataBuffer);
    }

    @Test
    public void getLineReturnsCommandIfBufferContainsCommand() throws SerialPortTimeoutException {
        serial.dataBuffer = "#q 206.00\n";
        assertEquals("#q 206.00", serial.readLine());
        assertEquals("", serial.dataBuffer);
    }

    @Test
    public void getLineReturnsOnlyCommandIfBufferContainsStuffAfterNewLine()throws SerialPortTimeoutException {
        serial.dataBuffer = "#q 206.00\n#k";
        assertEquals("#q 206.00", serial.readLine());
        assertEquals("#k", serial.dataBuffer);
    }

    @Test
    public void addingDataAddsDataToBuffer() throws SerialPortTimeoutException{
        serial.dataBuffer = "#q 206.00\n";
        serial.addToBuffer("#k");
        assertEquals("#q 206.00\n#k", serial.dataBuffer);
    }

    @Test
    public void addingDataAddsDataAndIsReadableByReadLine() throws SerialPortTimeoutException {
        serial.addToBuffer("#q 206.00\n#k");
        assertEquals("#q 206.00", serial.readLine());
        serial.addToBuffer("\n");
        assertEquals("#k", serial.readLine());
        assertEquals("", serial.dataBuffer);
    }
    
    

}
