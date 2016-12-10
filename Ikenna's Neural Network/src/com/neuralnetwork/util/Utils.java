package com.neuralnetwork.util;

import java.util.List;

/**
 *
 * @author Jonathan Elue
 */
public class Utils {
    
    public static double hyperbolicTangent(double weightedSum) {
        double e = Math.exp(weightedSum);
        double eNeg = Math.exp(-weightedSum);
        return (e - eNeg) / (e + eNeg);
    }
    
    public static double hyperbolicTangentPrime(double sum) {
        return 1 - (sum * sum);
    }
    
    /**
    * @param weightedSum The activation from the neuron.
    * @return The activation applied to the threshold method.
    */
    public static double sigmoid(double weightedSum) {
        return 1.0 / (1 + Math.exp(-1.0 * weightedSum));
    }
    
    public static double sigmoidPrime(double sum) {
        return sum * (1 - sum);
    }
    
    public static double[][] convertToMultiArrayAndAddColumn(List<double[]> array) {
        double[][] new_array = new double[array.size()][array.get(0).length + 1];
        
        for(int i = 0; i < array.size(); i++) {
            for(int j = 0; j < array.get(0).length; j++) {
                new_array[i][j] = array.get(i)[j];
            }
            new_array[i][array.get(0).length] = 1;
        }
        
        return new_array;
    }
    
    public static double[][] convertToMultiArray(List<double[]> array) {
        double[][] new_array = new double[array.size()][array.get(0).length];
        
        for(int i = 0; i < array.size(); i++) {
            for(int j = 0; j < array.get(0).length; j++) {
                new_array[i][j] = array.get(i)[j];
            }
        }
    
        return new_array;
    }
    
    public static double[][] columnize(List<double[]> array, int index) {
        double[][] new_array = new double[array.size()][1];
        
        for(int i = 0; i < array.size(); i++) {
            new_array[i][0] = array.get(i)[index];
        }
        
        return new_array;
    }
    
    public static double[][] columnize(double[][] array, int index) {
        double[][] new_array = new double[array.length][1];
        
        for(int i = 0; i < array.length; i++) {
            new_array[i][0] = array[i][index];
        }
        
        return new_array;
    }
    
     public static int contains(String[] array, String s) {
        for(int i = 0; i < array.length; i++) {
            if(array[i].equals(s))
                return i;
        }
        return -1;
    }
}
