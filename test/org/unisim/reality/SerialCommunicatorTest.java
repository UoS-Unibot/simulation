/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import jssc.SerialPortTimeoutException;
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
