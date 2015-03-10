package org.unisim.simulation.robot.ctrnn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.unisim.io.Loggable;

/**
 *
 * @author miles
 */
public class CTRNN implements Loggable<Float>{
    
    private List<String> neuronNames;

    public CTRNN(CTRNNNeuron[] neurons,int[] sensorIndices, float stepSize) {
        this.neurons = neurons;
        inputarray = new float[neurons.length];
        this.sensorIndices = sensorIndices;
        Arrays.fill(inputarray, 0);
        activation = SIGMOID_ACTIVATION;
        this.stepSize = stepSize;
    }

    public void setNeuronNames(List<String> neuronNames) {
        this.neuronNames = neuronNames;
    }
    public int getNumberOfNeurons() {
        return neurons.length;
    }
    
    public CTRNNNeuron[] getNeurons() {
        return Arrays.copyOf(neurons, neurons.length);
    }
    
    private final CTRNNNeuron[] neurons;
    private final float[] inputarray;
    private final ActivationFunction activation;
    private final int[] sensorIndices;
    private final float stepSize;
    
    public void integrate(float[] inputs) {
        for (int i = 0; i < inputs.length; i++)
            inputarray[sensorIndices[i]] = inputs[i];
        for (int i = 0; i < neurons.length; i++) {
            float preSynInput = 0;
            for (CTRNNNeuron neuron : neurons) {
                preSynInput += neuron.weights[i] * neuron.activation;
            }
            neurons[i].state += stepSize * neurons[i].tau * (inputarray[i] + preSynInput - neurons[i].state);
            neurons[i].activation = activation.getActivation((neurons[i].state + neurons[i].bias) * neurons[i].gain);
        }
    }

    @Override
    public List<String> getHeaders() {
        final List<String> headers = new ArrayList<>(neurons.length);
        if(neuronNames == null)
            for (int i = 0; i < neurons.length; i++) {
                headers.add(String.valueOf(0) + "_act");
            }
        else
            for(String name : neuronNames)
                headers.add(name + "_act");
        return headers;
    }

    @Override
    public List<Float> getDataRow() {
        final List<Float> activations = new ArrayList<>(neurons.length);
        for(CTRNNNeuron neuron : neurons)
            activations.add(neuron.activation);
        return activations;
    }

    public static class CTRNNNeuron {
        final float gain;
        final float bias;
        final float tau;
        final float[] weights;
        float state;
        float activation;
        
        

        public CTRNNNeuron(float gain, float bias, float tau,float[] weights) {
            this.gain = gain;
            this.bias = bias;
            this.tau = tau;
            this.weights = weights;
            this.state = 0.5f;
        }
        
    }
    
    public interface ActivationFunction {
        public float getActivation(float x);
    }
    public static final ActivationFunction SIGMOID_ACTIVATION
            = new ActivationFunction() {
                @Override
                public float getActivation(float x) {
                    return (float) (1.0f / (1 + Math.exp(-x)));
                }
            };
}
