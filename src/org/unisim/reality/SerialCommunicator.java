package org.unisim.reality;

import java.awt.Component;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Communicates with a real serial port.
 *
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public class SerialCommunicator implements SerialPortEventListener, ISerialCommunicator {

    private static final Logger LOG = Logger.getLogger(SerialCommunicator.class.getName());
    private final SerialPort serialPort;
    private DataReceivedListener listener;

    /**
     * Instantiates this, opening the serial port on COM4.
     *
     * @throws SerialPortException Thrown if serial port is missing, busy or
     * otherwise unavailable.
     */
    public SerialCommunicator() throws SerialPortException {
        this("COM4");
    }

    /**
     * Instantiates this, opening the specified serial port.
     * @param serialPortName Name of serial port, e.g. COM4
     * @throws SerialPortException Thrown if serial port is missing, busy or
     * otherwise unavailable.
     */
    public SerialCommunicator(String serialPortName) throws SerialPortException {
        serialPort = new SerialPort(serialPortName);
        openSerialPort();
    }

    private void openSerialPort() throws SerialPortException {
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
    @Override
    public void sendCommand(String command) throws SerialPortException {
        serialPort.writeBytes(command.getBytes());
    }

    /**
     * 
     * @param spe 
     */
    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.isRXCHAR() & listener != null) {
            try {
                byte[] buffer = serialPort.readBytes();
                String data = new String(buffer, "UTF-8");
                listener.dataReceivedEvent(data);
            } catch (SerialPortException | UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setListener(DataReceivedListener listener) {
        this.listener = listener;
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
