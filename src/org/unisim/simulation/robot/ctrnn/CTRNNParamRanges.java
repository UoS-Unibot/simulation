package org.unisim.simulation.robot.ctrnn;

import org.unisim.util.Range;

/**
 * Stores a range for each parameter which specifies how each parameter should
 * be mapped from gene to real parameter.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class CTRNNParamRanges {

    public Range tau, gain, bias, weights;

    public CTRNNParamRanges() {
        this.tau = new Range(1, 2);
        this.gain = new Range(1, 1);
        this.bias = new Range(-5, 5);
        this.weights = new Range(-5, 5);
    }

    public CTRNNParamRanges(Range tau, Range gain, Range bias, Range weights) {
        this.tau = tau;
        this.gain = gain;
        this.bias = bias;
        this.weights = weights;
    }

}
