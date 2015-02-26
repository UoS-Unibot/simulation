package org.unisim.reality;

/**
 * Is notified when a data packet is received, with the relevant method called.
 *
 * @author mb459
 */
public interface RobotDataPacketListener {

    public void ackReceived();

    public void encoderSonarReceived(double[] values);

    public void encoderReceived(double[] values);

    public void sonarReceived(double[] values);

    public void rangeAllReceived(double[] value);

    public void rangeSingleReceived(double value);

    public void imuReceived(double[] values);
}
