package org.unisim.reality;

/**
 * Signals that data has been received on the serial port, and passes it through.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public interface DataReceivedListener {
    public void dataReceivedEvent(String dataReceived);
}
