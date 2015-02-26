/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unibot.reality;

/**
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
