/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.robot.ctrnn;

import org.su.easy.unisim.util.Range;

/**
 *
 * @author Miles
 */
/**
     * Stores mapping ranges for a CTRNN
     */
public class CTRNNParamRanges {
    public Range tau,gain,bias,weights;

    public CTRNNParamRanges() {
        this.tau = new Range(1,2);
        this.gain = new Range(1,1);
        this.bias = new Range(-5,5);
        this.weights = new Range(-5,5);
    }

    public CTRNNParamRanges(Range tau, Range gain, Range bias, Range weights) {
        this.tau = tau;
        this.gain = gain;
        this.bias = bias;
        this.weights = weights;
    }
        
        
}