package com.neuralnetwork.network;

import com.neuralnetwork.util.Matrix;

/**
 *
 * @author Jonathan Elue
 */
public class Layer {
    
    private Layer nextLayer;
    private Layer backLayer;
    
    private final String tag; 
    private final int neuronCount;
    private final int activationType;
    
    private Matrix synapses;
    private Matrix sums;
    private Matrix error;
    private Matrix delta;
    
    public Layer(String tag, int neuronCount, boolean isLastLayer, int activationType) {
        this.tag = tag;
        this.activationType = activationType;
        
        if(isLastLayer)
            this.neuronCount = neuronCount;
        else
            this.neuronCount = neuronCount + 1;
        
        nextLayer = null;
        backLayer = null;
        
        synapses = null;
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
    
    public void feedForward() {
        sums = new Matrix(activationFunc(backLayer.getSums().dot(synapses)), "sums");
    }
    
    public void backwardPropagate(double learningRate) {
        error = new Matrix(nextLayer.getDelta().dot(nextLayer.getSynapses().transpose()), "error");
        delta = new Matrix(error.scale(activationPrimeFunc(sums), false).scale(learningRate, true), "delta");
        synapses.add(backLayer.getSums().transpose().dot(delta), true);
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
    
    public void setSums(Matrix sums) {
        this.sums = sums;
    }
    
    public void setDelta(Matrix delta) {
        this.delta = delta;
    }
    
    public void setNextLayer(Layer layer) {
        nextLayer = layer;
    }
    
    public void setBackLayer(Layer layer) {
        backLayer = layer;
        synapses = new Matrix(backLayer.getNeuronCount(), neuronCount, "synapses");
        synapses.randomizeMatrix();
    }
    
    @Override
    public String toString() {
        return "-" + tag + "-\nWeights: " + synapses + "\nOutputs: " + sums;
    }
}