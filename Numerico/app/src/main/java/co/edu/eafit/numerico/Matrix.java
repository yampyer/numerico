package co.edu.eafit.numerico;

import java.util.ArrayList;

public class Matrix {

    public static double[][] matrixAb;
    public static double[][] matrixA;
    public static double[] vectorB;
    public static double[] newVectorB;
    public static double[][] matrixIdentity;
    public static double [] [][] stageVectoryMatrix;
    public static double [] [][] stagesMatrixL;
    public static double [] [][] stagesMatrixU;
    public static ArrayList<ArrayList<String>> xNColumns;
    public static ArrayList<ArrayList<String>> columnsReverse;

    public Matrix(ArrayList<ArrayList<Double>> matrixAa, ArrayList<Double> vectorBa) {
        matrixA = new double[matrixAa.size()][matrixAa.size()];
        vectorB = new double[vectorBa.size()];
        newVectorB = new double[vectorBa.size()];
        matrixAb = new double[matrixAa.size()][matrixAa.size()+1];
        matrixIdentity = new double[matrixAa.size()][matrixAa.size()];
        fillMatrix(matrixAa, vectorBa);
        fillMatrixAb();
        fillMatrixIdentity();
    }

    public void fillMatrix(ArrayList<ArrayList<Double>> matrixAa, ArrayList<Double> vectorBa) {
        for (int i = 0; i < matrixAa.size(); i++) {
            for (int j = 0; j < matrixAa.size(); j++) {
                matrixA[i][j] = matrixAa.get(i).get(j);
            }
        }
        for (int r = 0; r < vectorBa.size(); r++) {
            vectorB[r] = vectorBa.get(r);
            newVectorB[r] = vectorBa.get(r);
        }
    }

    public void fillMatrixAb() {
        for(int i = 0; i < matrixAb.length; i++) {
            for(int j = 0; j < matrixAb.length +1; j++) {
                if(j == matrixAb.length) {
                    matrixAb[i][j] = vectorB[i];
                } else {
                    matrixAb[i][j] = matrixA[i][j];
                }
            }
        }
    }

    public void fillMatrixIdentity() {
        for(int i = 0; i < matrixIdentity.length; i++) {
            for(int j = 0; j < matrixIdentity.length; j++) {
                if(i == j) {
                    matrixIdentity[i][j] = 1.0;
                } else {
                    matrixIdentity[i][j] = 0.0;
                }
            }
        }
    }


    public static double[][] augmented(double[][] A, double[] b) {
        int n = A.length;
        int m = b.length;
        double tabla[][] = new double[n][n+1];
        if(n != m) {
            return null;
        } else {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n+1; j++) {
                    if(j == n) {
                        tabla[i][j] = b[i];
                    } else {
                        tabla[i][j] = A[i][j];
                    }
                }
            }
            return tabla;
        }
    }

    public static double[][] changeRow(double[][] A, int piv, int fCambio) {
        int m = A[0].length;
        double aux;
        for(int i = 0; i < m; i++) {
            aux = A[piv][i];
            A[piv][i] = A[fCambio][i];
            A[fCambio][i] = aux;
        }
        return A;
    }

    public static double[][] changeCol(double[][] A, int piv, int cCambio) {
        int n = A.length;
        double aux;
        for(int i = 0; i < n ; i++) {
            aux = A[i][piv];
            A[i][piv] = A[i][cCambio];
            A[i][cCambio] = aux;
        }
        return A;
    }

    public static int[] variableChange(int[] var, int piv, int cambio) {
        int aux = var[piv];
        var[piv] = var[cambio];
        var[cambio] = aux;
        return var;
    }


    public static int[] doX(int n, int[] var) {
        var = new int[n];
        for(int i = 0; i < n; i++) {
            var[i] = i;
        }
        return var;
    }

    public static double[][] addData(double[] dato,double tabla[][]) {
        double[][] aux = new double[tabla.length][tabla[0].length];
        for(int i = 0; i < tabla.length; i++) {
            System.arraycopy(tabla[i], 0, aux[i], 0, tabla[0].length);
        }

        tabla = new double[tabla.length+1][tabla[0].length];
        for(int i = 0; i < aux.length; i++) {
            System.arraycopy(aux[i], 0, tabla[i], 0, aux[0].length);
        }
        System.arraycopy(dato, 0, tabla[aux.length], 0, dato.length);
        return tabla;
    }

}
