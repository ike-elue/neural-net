package com.neuralnetwork.core;

import com.neuralnetwork.network.Network;

/**
 *
 * @author CSLAB313-1740
 */
public class Core {
    // Need to figure out way to add context with the sums from previous layer in order to get the outputs
    
    /**
     * To Create Normal ANN is 4 steps
    1) Create Inputs and Outputs
    2) Create Network with tag, activation type, and learning rate
    3) Add Layers
    4) Train
    5) (Optional) print training data
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[][] inputsXOR = {{0,0},{0,1},{1,0},{1,1}};
        double[][] outputsXOR = {{0},{1},{1},{0}};
        double[][] inputsPredict = {{0,0},{0,1}};
        
        Network xor = new Network("XOR", Network.SIGMOID, .7);
        
        xor.addLayer("input", 2);
        xor.addLayer("middle", 3);
        xor.addLayer("output", 1);
        
        xor.train(inputsXOR, outputsXOR, 50000, 10000);
        
        System.out.println(xor.predict(inputsPredict));
    }   
}