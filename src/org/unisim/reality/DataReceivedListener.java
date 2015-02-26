/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

/**
 * Signals that data has been received on the serial port, and passes it through.
 * @author Miles Bryant (mb459@sussex.ac.uk)
 */
public interface DataReceivedListener {
    public void dataReceivedEvent(String dataReceived);
}
