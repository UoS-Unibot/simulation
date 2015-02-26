/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import jssc.SerialPortEvent;
import jssc.SerialPortException;

/**
 * 
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public interface ISerialCommunicator {

    void sendCommand(String command) throws SerialPortException;

    void serialEvent(SerialPortEvent spe);

    void setListener(DataReceivedListener listener);

}