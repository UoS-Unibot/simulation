/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

/**
 *
 * @author mb459
 */
public interface VisualiserListener {
    public void setRunning(boolean isRunning);
    public void restart();
    public void speedChanged(double newSpeed);
}
