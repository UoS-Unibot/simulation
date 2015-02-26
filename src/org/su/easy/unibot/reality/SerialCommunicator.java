/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unibot.reality;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public class SerialCommunicator implements SerialPortEventListener, ISerialCommunicator {
    private static final Logger LOG = Logger.getLogger(SerialCommunicator.class.getName());
    
    private static final String SERIAL_PORT_NAME = "COM4";
    private DataReceivedListener listener;
    
    private final SerialPort serialPort;

    public SerialCommunicator() {
        serialPort = new SerialPort("COM4");
        openSerialPort();
    }
    
    private void openSerialPort() {
        try {
            serialPort.openPort();

            // set params as per unibot protocol
            serialPort.setParams(
                    SerialPort.BAUDRATE_57600,
                    SerialPort.DATABITS_8, 
                    SerialPort.STOPBITS_1, 
                    SerialPort.PARITY_NONE
            );
            
            serialPort.addEventListener(this);
        } catch(SerialPortException ex) {
            LOG.log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    @Override
    public void sendCommand(String command) {
        try {
            serialPort.writeBytes(command.getBytes());
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if(spe.isRXCHAR() & listener != null) {
            try {
                byte[] buffer = serialPort.readBytes();
                String data = new String(buffer,"UTF-8");
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
    
    
    

}
