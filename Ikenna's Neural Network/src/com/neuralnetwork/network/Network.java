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
    private Matrix trainingDataOutMatrix;
    
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
   
    public void train(double[][] inputs, double[][] outputs, int iterations, int everyNth) {
        if(!initialized)
            addFinalLayers();
        if(layers.size() < 2) {
            System.out.println("Less than 2 layers in network");
            return;
        }
        int i;
        for(i = 0; i < inputs.length; i++) {
            if(!trainingDataIn.contains(inputs[i])) {
                trainingDataIn.add(inputs[i]);
                trainingDataOut.add(outputs[i]);
            }
        }
        setInitialValues();
        setOutputMatrix();
        System.out.println("\nTraining " + tag);
        for(i = 0; i < iterations; i++) {
            feedForward();
            error = new Matrix(trainingDataOutMatrix.sub(getLastLayer().getSums(), false));
            if(i % everyNth == 0) {
                System.out.println(i + "th iteration --> Error: " + error.absolute(false).average());
            }
            getLastLayer().setDelta(new Matrix(error.scale(getLastLayer().getSumsPrime(), false)));
            getLastLayer().setSynapses(new Matrix(getLastLayer().getSynapses()
                    .add(layers.get(getLast() - 1).getSums().transpose()
                            .dot(getLastLayer().getDelta()), false)));
            backPropagate();
        }
        System.out.println("\nFinal Sturcture For Neural Network " + this);
    }
 
    public Matrix predict(double[][] inputs) {
        setInitialValues(inputs);
        feedForward();
        return getLastLayer().getSums();
    }
    
    public void addLayer(String tag, int neuronCount) {
        tags.add(tag);
        neuronCounts.add(neuronCount);
    }
    
    private void addActualLayer(String tag, int neuronCount, boolean isLastLayer) {
        Layer layer = new Layer(tag, neuronCount, isLastLayer, activationType);
        if(!layers.isEmpty()) {
            layer.setBackLayer(getLastLayer());
            getLastLayer().setNextLayer(layer);
        }
        layers.add(layer);
    }
    
    private void addFinalLayers() {
        for(int i = 0; i < neuronCounts.size(); i++) {
            if(i == neuronCounts.size() - 1)
                addActualLayer(tags.get(i), neuronCounts.get(i), true);
            else
                addActualLayer(tags.get(i), neuronCounts.get(i), false);
        }
        
        initialized = true;
    }
    
    private void setInitialValues(double[][] inputs) {
        layers.get(0).setSums(new Matrix(addColumn(inputs)));
    }
    
    private double[][] addColumn(double[][] inputs) {
        double[][] new_array = new double[inputs.length][inputs[0].length + 1];
        
        for(int i = 0; i < inputs.length; i++) {
            System.arraycopy(inputs[i], 0, new_array[i], 0, inputs[0].length);
            new_array[i][inputs[0].length] = 1;
        }
        
        return new_array;
    }
    
    private void setInitialValues() {
        layers.get(0).setSums(new Matrix(Utils.convertToMultiArrayAndAddColumn(trainingDataIn)));
    }
    
    private void setOutputMatrix() {
        trainingDataOutMatrix = new Matrix(Utils.convertToMultiArray(trainingDataOut));
    }
    
    private void feedForward() {
        for(int i = 1; i < layers.size(); i++) {
            layers.get(i).feedForward();
        }    
    }
    
    private void backPropagate() {
        for(int i = layers.size() - 2; i > 0; i--) {
            layers.get(i).backwardPropagate(learningRate);
        }
    }
    
    private int getLast() {
        return layers.size() - 1;
    }
    
    private Layer getLastLayer() {
        return layers.get(getLast());
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
