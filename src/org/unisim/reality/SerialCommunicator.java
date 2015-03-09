package org.unisim.reality;

import org.unisim.ui.reality.CalibrationUI;
import java.awt.Component;
import java.nio.charset.StandardCharsets;
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
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class SerialCommunicator implements SerialPortEventListener {

    private static final Logger LOG = Logger.getLogger(SerialCommunicator.class.
            getName());
    private final SerialParameters parameters;
    private SerialPort serialPort;

    /**
     * Constructs a new SerialCommunicator with a default set of parameters
     * (COM4, 100msec timeout)
     */
    public SerialCommunicator() {
        this(new SerialParameters());
    }

    /**
     * Constructs a new SerialCommunicator, specifying parameters.
     *
     * @param parameters SerialParameters used to specify port name and timeout
     * length.
     */
    public SerialCommunicator(SerialParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Opens the serial port with the port name specified in the parameters.
     *
     * @throws SerialPortException Thrown by the underlying SerialPort when any
     * errors opening the serial port occur, such as can't find serial port,
     * serial port busy etc. See SerialPortException for more details.
     */
    public void openSerialPort() throws SerialPortException {
        serialPort = new SerialPort(parameters.getPortName());

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
     * Closes the serial port, making it available for further use.
     *
     * @throws SerialPortException Thrown by the underlying SerialPort when any
     * errors opening the serial port occur, such as can't find serial port,
     * serial port busy etc. See SerialPortException for more details.
     */
    public void closeSerialPort() throws SerialPortException {
        if (serialPort == null) {
            throw new NullPointerException(
                    "Serial port is null; make sure the port is opened first.");
        }
        serialPort.closePort();
    }

    /**
     * Sends the raw command to the serial port, appending with \r to signify a
     * single command sent.
     *
     * @param command String with raw command, e.g. "#D 0.2 0.2"
     * @throws SerialPortException Thrown by the underlying SerialPort when any
     * errors opening the serial port occur, such as can't find serial port,
     * serial port busy etc. See SerialPortException for more details.
     */
    public void sendCommand(String command) throws SerialPortException {
        command = command + "\r";
        if (!serialPort.writeBytes(command.getBytes(StandardCharsets.UTF_8))) {
            throw new RuntimeException("Command could not be sent. Command: "
                    + command);
        }
    }

    String dataBuffer = "";
    private final Object lock = new Object();

    /**
     * Adds the specified String to the buffer. Thread safe.
     *
     * @param s String to add.
     */
    void addToBuffer(String s) {
        synchronized (lock) {
            dataBuffer += s;
        }
    }

    /**
     * Waits for the serial port to read an entire line of data up until the \n
     * character.
     *
     * @return String line of data from the serial port, without the ending
     * newline character.
     * @throws SerialPortTimeoutException If the timeout is reached without a
     * full line of data being read. Normally thrown when communication with the
     * robot is lost - check that its on, serial port is plugged in, or the
     * battery hasn't run out!
     */
    public String readLine() throws SerialPortTimeoutException {
        long timeOutTime = System.currentTimeMillis() + parameters.
                getTimeOutMSec();
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
        throw new SerialPortTimeoutException(serialPort == null
                ? "no serial port" : serialPort.getPortName(),
                "readLine(), current buffer contents =" + dataBuffer,
                (int) parameters.getTimeOutMSec());
    }

    /**
     * Called by the SerialPort when a serial port event occurs, e.g. data is
     * received.
     *
     * @param spe SerialPortEvent
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

    /**
     * Displays a Swing GUI error message with natural language describing the
     * error.
     *
     * @param ex SerialPort
     * @param parent Component to display this error message from.
     */
    public static void showErrorDialog(SerialPortException ex, Component parent) {
        Logger.getLogger(CalibrationUI.class.getName()).log(Level.SEVERE, null,
                ex);
        String errmsg = "",
                errtitle = "";
        switch (ex.getExceptionType()) {
            case SerialPortException.TYPE_PORT_ALREADY_OPENED:
                errtitle = "Port already opened";
                errmsg = "Serial port " + ex.getPortName()
                        + " is already open. Check that another instance/application is not using it.";
                break;
            case SerialPortException.TYPE_PORT_BUSY:
                errtitle = "Port busy";
                errmsg = "Serial port " + ex.getPortName()
                        + " is busy. Check that another instance/application is not using it.";
                break;
            case SerialPortException.TYPE_PORT_NOT_FOUND:
                errtitle = "Port not found";
                errmsg = "Serial port " + ex.getPortName()
                        + " was not found. Check that serial interface is plugged in.";
            default:
                errtitle = "Unknown serial port error";
                errmsg = "An unknown error occurred in the serial port " + ex.
                        getPortName()
                        + ".";
                break;
        }
        showErrorDialog(parent, errmsg, errtitle);
    }

    /**
     * Displays a Swing GUI error message with natural language describing the
     * error.
     *
     * @param ex SerialPort
     * @param parent Component to display this error message from.
     */
    public static void showErrorDialog(SerialPortTimeoutException ex,
            Component parent) {
        showErrorDialog(parent,
                "A timeout on the serial port " + ex.getPortName()
                + " occurred whilst sending a command. Check that the robot is on.",
                "Time out occurred");
    }

    private static void showErrorDialog(Component parent, String message,
            String title) {
        JOptionPane.showMessageDialog(parent,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

}
