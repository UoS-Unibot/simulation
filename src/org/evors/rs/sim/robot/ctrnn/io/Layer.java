/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot.ctrnn.io;

import java.util.Arrays;

/**
 *
 * @author miles
 */
public class Layer {

        private String desc;
        private String params;
        private Neuron[] neurons;

        

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public Neuron[] getNeurons() {
            return neurons;
        }

        public void setNeurons(Neuron[] neurons) {
            this.neurons = neurons;
        }

        @Override
        public String toString() {
            return "Layer{" + "desc=" + desc + ", params=" + params + ", neurons=" + Arrays.toString(neurons) + '}';
        }
        
        

    }
