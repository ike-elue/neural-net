package com.neuralnetwork.network;

import com.neuralnetwork.util.Matrix;
import com.neuralnetwork.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {
    
    public static final int SIGMOID = 0, HYPERTAN = 1;
    private final int activationType;
    
    private final List<Layer> layers;
    private final List<double[]> trainingDataIn;
    private final List<double[]> trainingDataOut;
    
    private final String tag;
    
    private Matrix error;
    
    private final List<String> tags;
    private final List<Integer> neuronCounts;
    
    private boolean initialized;
    
    private final double learningRate;
    
    public Network(String tag, int activationType, double learningRate) {
        this.tag = "\"" + tag + "\"";
        this.activationType = activationType;
        this.learningRate = learningRate;
        layers = new ArrayList<>();
        trainingDataIn = new ArrayList<>();
        trainingDataOut = new ArrayList<>();
        tags = new ArrayList<>();
        neuronCounts = new ArrayList<>();
        initialized = false;
    }
   
    public void init(double[][] inputs, double[][] outputs) {
        if(!initialized) {
            addFinalLayers();
            initSynapses();
        }
        
        if(layers.size() < 2) {
            System.out.println("Less than 2 layers in network");
            initialized = false;
            layers.clear();
            return;
        }
        
        
        for(int i = 0; i < inputs.length; i++) {
            if(!trainingDataIn.contains(inputs[i])) {
                trainingDataIn.add(inputs[i]);
                trainingDataOut.add(outputs[i]);
            }
        }
        System.out.println("\nTraining " + tag);
    }
    
    private void initSynapses() {
        Layer next;
        Layer prev = null;
        for(Layer layer : layers) {
            next = layer;
            if(prev != null) {
                next.setSynapses(new Matrix(prev.getNeuronCount(), next.getNeuronCount(), "synapses"));
                next.getSynapses().randomizeMatrix();
                next.setBias(new Matrix(1, next.getNeuronCount(), "Biases"));
                next.getBias().randomizeMatrix();
            }
            prev = layer;
        }
    }
    
    public void train(double[][] inputs, double[][] outputs, int iterations, int everyNth) {
        init(inputs, outputs);
        ArrayList<Matrix> input = Utils.toListOfMatrices(trainingDataIn);
        ArrayList<Matrix> output = Utils.toListOfMatrices(trainingDataOut);
        for(int i = 0; i < iterations; i++) {
            update(input, output, i, everyNth);
        }
        System.out.println("\nFinal Sturcture For Neural Network " + this);
    }
 
    public void update(ArrayList<Matrix> input, ArrayList<Matrix> output, int i, int everyNth) {
        for(int j = 0; j < input.size(); j++) {
            error = new Matrix(output.get(j).sub(feedForward(input, j), false));
            if(i % everyNth == 0 && j == input.size() - 1) {
                System.out.println(i + "th iteration --> Error: " + error.absolute(false).average());
            }
            backPropagate(error);
        }
    }
    
    public Matrix predict(double[][] inputs) {
        ArrayList<Matrix> results = new ArrayList<>();
        ArrayList<Matrix> input = Utils.toListOfMatrices(inputs);
        for(int i = 0; i < inputs.length; i++) {
            results.add(feedForward(input, i));
        }
        return new Matrix(results, "Prediction");
    }
    
    public void addLayer(String tag, int neuronCount) {
        tags.add(tag);
        neuronCounts.add(neuronCount);
    }
    
    private void addActualLayer(String tag, int neuronCount) {
        Layer layer = new Layer(tag, neuronCount, activationType);
        layers.add(layer);
    }
    
    private void addFinalLayers() {
        for(int i = 0; i < neuronCounts.size(); i++) {
            addActualLayer(tags.get(i), neuronCounts.get(i));
        }
        initialized = true;
    }
    
    
    private Matrix feedForward(ArrayList <Matrix> input, int index) {
        Matrix m = input.get(index);
        layers.get(0).setSums(m);
        for(int i = 1; i < layers.size(); i++) {
            m = layers.get(i).feedForward(m);
        }
        return m;
    }
    
    private void backPropagate(Matrix error) {
        layers.get(layers.size() - 1).setDelta(new Matrix(error.mult(layers.get(layers.size() - 1).getSumsPrime(), false)));
        Layer layer = layers.get(layers.size() - 1);
        for(int i = layers.size() - 2; i > -1; i--) {
            layer = layers.get(i).backwardPropagate(layer, learningRate);
        }
    }
   
    public void printTrainingData() {
        String str = "\nTraining Data For Neural Net "+ tag;
        
        for(int i = 0; i < trainingDataIn.size(); i++) {
            str += "\nInput " + i + ": " + Arrays.toString(trainingDataIn.get(i)) + " Output " + i + ": " + Arrays.toString(trainingDataOut.get(i));
        }
        
        System.out.println(str);
    }
    
    @Override
    public String toString() {
        String str = tag;
        for(int i = 1; i < layers.size(); i++) 
            str += "\n" + layers.get(i);
        return str;
    }
}
