package com.neuralnetwork.util;

import java.util.ArrayList;
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
    
    public static ArrayList<Matrix> toListOfMatrices(double[][] array) {
        ArrayList<Matrix> matrices = new ArrayList<>();
        
        for (double[] a : array) {
            matrices.add(new Matrix(a));
        }
        
        return matrices;
    }
    
    public static ArrayList<Matrix> toListOfMatrices(List<double[]> array) {
        ArrayList<Matrix> matrices = new ArrayList<>();
        
        array.stream().forEach((a) -> {
            matrices.add(new Matrix(a));
        });
        
        return matrices;
    }
    
    
}
