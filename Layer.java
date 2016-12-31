package com.neuralnetwork.network;

import com.neuralnetwork.util.Matrix;

/**
 *
 * @author Jonathan Elue
 */
public class Layer {
    
    private final String tag; 
    private final int neuronCount;
    private final int activationType;
    
    private Matrix synapses;
    private Matrix bias; // Bias is really 1, this holds weights
    private Matrix sums;
    private Matrix error;
    private Matrix delta;
    
    public Layer(String tag, int neuronCount, int activationType) {
        this.tag = tag;
        this.activationType = activationType;
        this.neuronCount = neuronCount;
        
        synapses = null;
        bias = null;
        sums = null;
        error = null;
        delta = null;
    }
    
    private Matrix activationFunc(Matrix matrix) {
        if(activationType == Network.SIGMOID)
            return matrix.sigmoid(false);
        if(activationType == Network.HYPERTAN)
            return matrix.hyperbolicTangent(false);
        System.out.println("Error In Activation Function Type");
        return null;
    }
    
    private Matrix activationPrimeFunc(Matrix matrix) {
        if(activationType == Network.SIGMOID)
            return matrix.sigmoidPrime(false);
        if(activationType == Network.HYPERTAN)
            return matrix.hyperbolicTangentPrime(false);
        System.out.println("Error In Activation Function Type");
        return null;
    }
    
    public Matrix feedForward(Matrix input) {
        sums = new Matrix(activationFunc(input.dot(synapses).add(bias, true)), "sums");
        return sums;
    }
    
    public Layer backwardPropagate(Layer layer,  double learningRate) {
        layer.getSynapses().add(sums.transpose().dot(layer.getDelta()), true);
        layer.getBias().add(layer.getDelta(), true);
        error = new Matrix(layer.getDelta().dot(layer.getSynapses().transpose()), "error");
        delta = new Matrix(error.mult(activationPrimeFunc(sums), false).scale(learningRate, true), "delta");
        return this;
    }
    
    public Matrix getSynapses() {
        return synapses;
    }
    
    public Matrix getDelta() {
        return delta;
    }
    
    public Matrix getSums() {
        return sums;
    }
    
    public Matrix getBias() {
        return bias;
    }
    
    public String getTag() {
        return tag;
    }
    
    public int getNeuronCount() {
        return neuronCount;
    }
    
    public Matrix getSumsPrime() {
        return activationPrimeFunc(sums);
    }
    
    public void setSynapses(Matrix synapses) {
        this.synapses = synapses;
    }
    
    public void setBias(Matrix bias) {
        this.bias = bias;
    }
    
    public void setSums(Matrix sums) {
        this.sums = sums;
    }
    
    public void setDelta(Matrix delta) {
        this.delta = delta;
    }
 
    @Override
    public String toString() {
        return "-" + tag + "-\nWeights: " + synapses;
    }
}