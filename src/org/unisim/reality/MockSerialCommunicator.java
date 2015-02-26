/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import jssc.SerialPortEvent;


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
