/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * Communicates with the Unibot serial port.
 *
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public class SerialCommunicator implements SerialPortEventListener {

    private static final Logger LOG = Logger.getLogger(SerialCommunicator.class.getName());
    private SerialPort serialPort;

    /**
     * Instantiates this, opening the serial port on COM4.
     *
     * @throws SerialPortException Thrown if serial port is missing, busy or
     * otherwise unavailable.
     */
    public SerialCommunicator() {

    }

    public void openSerialPort() throws SerialPortException {
        openSerialPort("COM4");
    }
    
    public void closeSerialPort() throws SerialPortException {
        serialPort.closePort();
    }

    public void openSerialPort(String serialPortName) throws SerialPortException {
        serialPort = new SerialPort(serialPortName);

        serialPort.openPort();

        // set params as per unibot protocol
        serialPort.setParams(
                SerialPort.BAUDRATE_57600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );

        serialPort.addEventListener(this); //listen for data
    }

    /**
     * Sends the raw command to the serial port.
     *
     * @param command String with raw command, e.g. "#D 0.2 0.2"
     * @throws jssc.SerialPortException
     */
    public void sendCommand(String command) throws SerialPortException {
        command = command + "\r";
        serialPort.writeBytes(command.getBytes());
    }

    protected String dataBuffer = "";
    private final Object lock = new Object();

    protected void addToBuffer(String s) {
        synchronized (lock) {
            dataBuffer += s;
        }
    }
    
    public String readLine() throws SerialPortTimeoutException {
        return readLine(100);
    }

    public String readLine(long timeout) throws SerialPortTimeoutException {
        long timeOutTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() <= timeOutTime) {
            synchronized (lock) {
                int index = dataBuffer.indexOf('\n');
                if (index == -1) {
                    Thread.yield();
                } else {
                    String line = dataBuffer.substring(0, index);
                    if (index == dataBuffer.length() - 1) {
                        dataBuffer = "";
                    } else {
                        dataBuffer = dataBuffer.substring(index + 1);
                    }
                    return line;
                }
            }
        }
        throw new SerialPortTimeoutException(serialPort == null ?"no serial port" : serialPort.getPortName(), "readLine(), current buffer contents =" + dataBuffer, (int) timeout);
    }

    /**
     *
     * @param spe
     */
    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.isRXCHAR()) {
            try {
                String data = serialPort.readString();
                if (data != null) {
                    addToBuffer(data);
                }
            } catch (SerialPortException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void showErrorDialog(SerialPortException ex, Component parent) {
        Logger.getLogger(CalibrationUI.class.getName()).log(Level.SEVERE, null, ex);
        String errmsg = "",
                errtitle = "";
        switch (ex.getExceptionType()) {
            case SerialPortException.TYPE_PORT_ALREADY_OPENED:
                errtitle = "Port already opened";
                errmsg = "Serial port " + ex.getPortName() + " is already open. Check that another instance/application is not using it.";
                break;
            case SerialPortException.TYPE_PORT_BUSY:
                errtitle = "Port busy";
                errmsg = "Serial port " + ex.getPortName() + " is busy. Check that another instance/application is not using it.";
                break;
            case SerialPortException.TYPE_PORT_NOT_FOUND:
                errtitle = "Port not found";
                errmsg = "Serial port " + ex.getPortName() + " was not found. Check that serial interface is plugged in.";
            default:
                break;
        }
        JOptionPane.showMessageDialog(parent,
                errmsg,
                errtitle,
                JOptionPane.ERROR_MESSAGE);
    }

}
