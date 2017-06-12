package co.edu.eafit.numerico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import utils.SessionManager;

public class ScaledPivoting extends AppCompatActivity {

  double[][] matrizA;
  double[] vectorB;
  double[][] matrizAB;
  int tamano;
  String etapa;

  GridView tabla_grid;
  TextView resultados;

  ArrayAdapter<String> adaptador;
  ArrayList<String> list;
  private boolean stop;
  private SessionManager session;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_gaussian_elimination);

    tabla_grid = (GridView) findViewById(R.id.GridOpciones);
    if (MatrixData.withoutPhases) {
      tabla_grid.setVisibility(View.GONE);
    }
    resultados = (TextView) findViewById(R.id.editText1);

    matrizA = Matrix.matrixA;
    vectorB = Matrix.vectorB;
    matrizAB = Matrix.matrixAb;

    tamano = MatrixData.tamano_Matriz;
    gaussianElimination(matrizA, vectorB);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    session = SessionManager.getInstance(getApplicationContext());
  }

  public void tablaGS(double[][] matrizAB) {

    for (int j = 0; j < matrizAB[0].length; j++) {
      if (j == 0) {
        list.add(etapa);
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      } else {
        list.add(" ");
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      }
    }

    for (int j = 0; j < matrizAB[0].length; j++) {
      if (j == matrizAB[0].length - 1) {
        list.add("b");
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      } else {
        list.add("x" + j);
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      }
    }
    for (int i = 0; i < matrizAB.length; i++) {

      for (int j = 0; j < matrizAB[i].length; j++) {
        list.add(String.valueOf(matrizAB[i][j]));
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setNumColumns(tamano + 1);
        tabla_grid.setAdapter(adaptador);
      }
    }
  }

  public void gaussianElimination(double[][] A, double[] b) {
    NumberFormat formatter = new DecimalFormat("#.##E0");

    int n = A.length;
    int l = A[0].length;
    double tabla[][] = new double[n][l + 1];
    list = new ArrayList<>();

    if (n != l) {
      Mensaje("The matrix A is not an square matrix");
    } else {
      tabla = Matrix.augmented(A, b);
      double[] s = majorOnRow(n, tabla);

      int[] var = null;
      // This solve the system
      var = Matrix.doX(tabla.length, var);

      double m = 0;
      for (int k = 1; k < n; k++) {
        tabla = scaledPivoting(tabla, k, n, s);
        etapa = "Stage " + (k);
        if (stop) {
          Mensaje("System has multiple solutions");
        } else {
          for (int i = k + 1; i < (n + 1); i++) {
            double multiplicador = tabla[i - 1][k - 1] / tabla[k - 1][k - 1];
            for (int j = k; j < n + 2; j++) {
              tabla[i - 1][j - 1] = Double.valueOf(formatter.format(tabla[i - 1][j - 1] - multiplicador * tabla[k - 1][j - 1]));
            }
          }
        }
        tablaGS(tabla);

      }
      double suma;
      double[] x = new double[n];
      for (int i = n - 1; i >= 0; i--) {
        suma = 0;
        for (int j = i + 1; j < n; j++) {
          suma = suma + tabla[i][j] * x[j];
        }
        x[i] = (tabla[i][n] - suma) / tabla[i][i];

        resultados.setText(resultados.getText() + "\n" + "x" + var[i] + " = " + x[i] + "\n");
      }
    }
  }

  public double[] majorOnRow(int n, double[][] matrix) {
    double[] s = new double[n];
    for (int i = 1; i < n + 1; i++) {
      for (int j = 1; j < n + 1; j++) {
        if (Math.abs(matrix[i - 1][j - 1]) > s[i - 1]) {
          s[i - 1] = Math.abs(matrix[i - 1][j - 1]);
        }
      }
    }
    return s;
  }

  public double[][] scaledPivoting(double[][] matrix, int k, int n, double[] s) {
    double mayor = 0;
    int filamayor = k - 1;
    double[] cocientes = new double[n];
    for (int i = k; i < n + 1; i++) {
      cocientes[i - 1] = Math.abs(matrix[i - 1][k - 1] / s[i - 1]);
    }
    for (int i = k - 1; i < n; i++) {
      if (cocientes[i] > mayor) {
        mayor = cocientes[i];
        filamayor = i;
      }
    }
    if (mayor == 0) {
      stop = Boolean.TRUE;
    } else {
      if (filamayor != k - 1) {
        for (int i = 0; i < matrix.length; i++) {
          double aux = matrix[k - 1][i];
          matrix[k - 1][i] = matrix[filamayor][i];
          matrix[filamayor][i] = aux;
        }
        double aux2 = s[k - 1];
        s[k - 1] = s[filamayor];
        s[filamayor] = aux2;
      }
    }
    return matrix;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.help_menu, menu);
    return true;
  }

  public void Mensaje(String s) {

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

    dialog.setMessage(s);
    dialog.setCancelable(false);
    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    dialog.show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.help_menu:
        help();
        return true;
      case R.id.help_logout:
        session.logoutUser();
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void help() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    }).setMessage("The Gaussian Elimination method with scaled partial pivoting is a variant of Gaussian " +
      "Elimination with partial pivoting. But with the objective to reduce propagation of error, " +
      "first and only at the beginning of the process, we find and store the maximum value of " +
      "each row (excluding the column of the independent terms).\n" +
      "\n" +
      "Then, we try to locate into " +
      "the diagonal all the possible maximum values (in absolute value) between the elements of " +
      "each column divided by the maximum value of its respective row, using the maximum row " +
      "values that we found at the beginning.\n" +
      "\n" +
      "For that purpose, in the stage k, we have to search " +
      "the higher value (in absolute value), under the element in the diagonal, between the " +
      "elements of the column k divided by its respective maximum row value (found at the " +
      "beginning) and change the row in which that higher value is located with the row k.");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}
