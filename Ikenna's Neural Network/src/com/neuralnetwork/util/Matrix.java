/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnetwork.util;

import java.util.Arrays;

/**
 *
 * @author Jonathan Elue
 */
public class Matrix {
   
    private String errorMessageStd, errorMessageDim;
    
    protected final String tag;
    protected final int rows, columns;
    protected final double[][] matrix;
    private final Matrix extraMatrix;
    
    public Matrix(double[][] matrix) {
        this.tag = "";
        this.rows = matrix.length;
        this.columns = matrix[0].length;
        this.matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        setMatrix(matrix);
        init();
    } 
    
    public Matrix(Matrix m, String tag) {
        this.tag = tag;
        this.rows = m.rows;
        this.columns = m.columns;
        matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        setMatrix(m);
        init();
    }
    
    public Matrix(Matrix m) {
        this.tag = "";
        this.rows = m.rows;
        this.columns = m.columns;
        matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        setMatrix(m);
        init();
    }
    
    public Matrix(int rows, int columns) {
        this.tag = "";
        this.rows = rows;
        this.columns = columns;
        matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        init();
    }
  
    public Matrix(double[][] matrix, String tag) {
        this.tag = tag;
        this.rows = matrix.length;
        this.columns = matrix[0].length;
        this.matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        setMatrix(matrix);
        init();
    } 
    
    public Matrix(int rows, int columns, String tag) {
        this.tag = tag;
        this.rows = rows;
        this.columns = columns;
        matrix = new double[rows][columns];
        extraMatrix = new Matrix(rows, columns, false, tag);
        init();
    }
    
    public Matrix(int rows, int columns, boolean hasExtra, String tag) {
        this.tag = tag;
        this.rows = rows;
        this.columns = columns;
        matrix = new double[rows][columns];
        extraMatrix = null; 
        init();
    }
    
    private void init() {
        errorMessageStd = "Matrix \"" + tag + "\" Didn't Have Extra Matrix";
        errorMessageDim = "Matrix \"" + tag + "\" Didn't Match Dimensions";
    }
    
    public final void setMatrix(Matrix matrix) {
        if(matrix == null) {
            System.out.println("Matrix is Null");
            return;
        }
        
        if(rows != matrix.getRows() || columns != matrix.getColumns()) {
            System.out.println(errorMessageDim);
            return;
        }
        
        set(matrix.matrix);
    }
    
    public final void setMatrix(double[][] matrix) {
        if(matrix == null) {
            System.out.println("Matrix is Null");
            return;
        }
        
        if(rows != matrix.length || columns != matrix[0].length) {
            System.out.println(errorMessageDim);
            return;
        }
        set(matrix);
    }
    
    private void set(double[][] matrix) {
        for(int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, columns);
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public double[][] getMatrix() {
        return matrix;
    }
    
    public Matrix transpose() {
        Matrix m = new Matrix(columns, rows);
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                m.matrix[y][x] = this.matrix[x][y];
            }
        }
        return m;
    }
    
    public void randomizeMatrix() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                matrix[i][j] = Math.random();
            }
        }
    }
    
    public void identity() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                matrix[i][j] = 0;
            }
        }
    }
    
    public double average() {
        double average = 0;
        
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                average += matrix[i][j];
            }
        } 
        
        average /= rows;
        
        return average;
    }
    
    public Matrix absolute(boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    if(matrix[i][j] < 0)
                        matrix[i][j] *= -1;
                }
            }
            return this;
        }
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.absolute(true);
        }
    }
    
    public Matrix dot(Matrix matrix) {
        if(matrix == null)
            return null;
        
        if(columns != matrix.getRows()) {
            System.out.println(errorMessageDim);
            return null;
        }
        
        Matrix m = new Matrix(rows, matrix.getColumns());
        double sum;
        
        for(int i = 0; i < m.getRows(); i++) {
            for(int j = 0; j < m.getColumns(); j++) {
                sum = 0;
                for(int k = 0; k < matrix.getRows(); k++) {
                    sum += this.matrix[i][k] * matrix.matrix[k][j];
                }
                m.matrix[i][j] = sum;
            }
        }
        return m;
    }
    
    public Matrix add(Matrix matrix, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] += matrix.matrix[i][j];
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.add(matrix, true);
        }
    }
    
    public Matrix sub(Matrix matrix, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] -= matrix.matrix[i][j];
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.sub(matrix, true);
        }
    }
    
    public Matrix scale(double scalar, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] *= scalar;
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.scale(scalar, true);
        }
    }
    
    public Matrix scale(Matrix matrix, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] *= matrix.matrix[i][j];
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.scale(matrix, true);
        }
    }
    
    public Matrix hyperbolicTangent(boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] = Utils.hyperbolicTangent(this.matrix[i][j]);
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.hyperbolicTangent(true);
        }
    }
    
    public Matrix hyperbolicTangentPrime(boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] = Utils.hyperbolicTangentPrime(this.matrix[i][j]);
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.hyperbolicTangentPrime(true);
        }
    }
    
    public Matrix sigmoid(boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] = Utils.sigmoid(this.matrix[i][j]);
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.sigmoid(true);
        }
    }
    
    public Matrix sigmoidPrime(boolean save) {
       if(save) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j] = Utils.sigmoidPrime(this.matrix[i][j]);
                }
            }
            return this;
        } 
        else {
            if(extraMatrix==null) {
                System.out.println(errorMessageStd);
                return null;
            }
            extraMatrix.setMatrix(this);
            return extraMatrix.sigmoidPrime(true);
        }
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }
}
