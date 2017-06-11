package co.edu.eafit.numerico;

import java.util.ArrayList;

public class Matrix {

  public static double[][] matrixAb;
  public static double[][] matrixA;
  public static double[] vectorB;
  public static double[] newVectorB;
  public static double[][] matrixIdentity;
  public static double[][] m;
  public static double[][][] stageVectoryMatrix;
  public static double[][][] stagesMatrixL;
  public static double[][][] stagesMatrixU;
  public static ArrayList<ArrayList<String>> xNColumns;
  public static ArrayList<ArrayList<String>> columnsReverse;

  public Matrix(ArrayList<ArrayList<Double>> matrixAa, ArrayList<Double> vectorBa) {
    matrixA = new double[matrixAa.size()][matrixAa.size()];
    vectorB = new double[vectorBa.size()];
    newVectorB = new double[vectorBa.size()];
    matrixAb = new double[matrixAa.size()][matrixAa.size() + 1];
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

  public static int[] fillMarks(int n) {
    int[] marks = new int[n];
    for (int i = 0; i < n; i++) {
      marks[i] = i + 1;
    }
    return marks;
  }

  public void fillMatrixAb() {
    for (int i = 0; i < matrixAb.length; i++) {
      for (int j = 0; j < matrixAb.length + 1; j++) {
        if (j == matrixAb.length) {
          matrixAb[i][j] = vectorB[i];
        } else {
          matrixAb[i][j] = matrixA[i][j];
        }
      }
    }
  }

  public void fillMatrixIdentity() {
    for (int i = 0; i < matrixIdentity.length; i++) {
      for (int j = 0; j < matrixIdentity.length; j++) {
        if (i == j) {
          matrixIdentity[i][j] = 1.0;
        } else {
          matrixIdentity[i][j] = 0.0;
        }
      }
    }
  }

  public static double determinant(double A[][], int N) {
    double det = 0;
    double res;
    if (N == 1)
      res = A[0][0];
    else if (N == 2) {
      res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
    } else {
      res = 0;
      for (int j1 = 0; j1 < N; j1++) {
        m = new double[N - 1][];
        for (int k = 0; k < (N - 1); k++)
          m[k] = new double[N - 1];
        for (int i = 1; i < N; i++) {
          int j2 = 0;
          for (int j = 0; j < N; j++) {
            if (j == j1)
              continue;
            m[i - 1][j2] = A[i][j];
            j2++;
          }
        }
        res += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1] * determinant(m, N - 1);
      }
    }
    return res;
  }

  public static double[][] augmented(double[][] A, double[] b) {
    int n = A.length;
    int m = b.length;
    double tabla[][] = new double[n][n + 1];
    if (n != m) {
      return null;
    } else {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n + 1; j++) {
          if (j == n) {
            tabla[i][j] = b[i];
          } else {
            tabla[i][j] = A[i][j];
          }
        }
      }
      return tabla;
    }
  }

  public static double[] regressiveSubstitutionElimination(double[][] Ab, int n) {
    double[] x = new double[n];
    x[n - 1] = Ab[n - 1][n] / Ab[n - 1][n - 1];
    for (int i = n - 2; i >= 0; i--) {
      double sum = 0;
      for (int p = i + 1; p < n; p++) {
        sum = sum + Ab[i][p] * x[p];
      }
      x[i] = (Ab[i][n] - sum) / Ab[i][i];
    }
    return x;
  }

  public static int[] changeMarks(int[] marks, int maxColum, int k) {
    int aux = marks[maxColum];
    marks[maxColum] = marks[k];
    marks[k] = aux;
    return marks;
  }

  public static double[][] changeRow(double[][] A, int piv, int fCambio) {
    int m = A[0].length;
    double aux;
    for (int i = 0; i < m; i++) {
      aux = A[piv][i];
      A[piv][i] = A[fCambio][i];
      A[fCambio][i] = aux;
    }
    return A;
  }

  public static double[][] changeCol(double[][] A, int piv, int cCambio) {
    int n = A.length;
    double aux;
    for (int i = 0; i < n; i++) {
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
    for (int i = 0; i < n; i++) {
      var[i] = i;
    }
    return var;
  }

  public static double[][] addData(double[] dato, double tabla[][]) {
    double[][] aux = new double[tabla.length][tabla[0].length];
    for (int i = 0; i < tabla.length; i++) {
      System.arraycopy(tabla[i], 0, aux[i], 0, tabla[0].length);
    }

    tabla = new double[tabla.length + 1][tabla[0].length];
    for (int i = 0; i < aux.length; i++) {
      System.arraycopy(aux[i], 0, tabla[i], 0, aux[0].length);
    }
    System.arraycopy(dato, 0, tabla[aux.length], 0, dato.length);
    return tabla;
  }

}
