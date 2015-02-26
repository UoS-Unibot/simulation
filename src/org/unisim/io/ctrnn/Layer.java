package org.unisim.io.ctrnn;

import java.util.Arrays;

/**
 * 
 * @author Miles Bryant (mb459 at sussex.ac.uk)
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
