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

public class Cholesky extends AppCompatActivity {

  double[][] matrizA;
  double[] vectorB;
  double[][] matrizAB;
  int tamano;
  String etapa;

  GridView tabla_grid;
  TextView resultados;

  ArrayAdapter<String> adaptador;
  ArrayList<String> list;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_gaussian_elimination);

    tabla_grid = (GridView) findViewById(R.id.GridOpciones);
    if(MatrixData.withoutPhases) {
      tabla_grid.setVisibility(View.GONE);
    }
    resultados = (TextView) findViewById(R.id.editText1);

    matrizA = Matrix.matrixA;
    vectorB = Matrix.vectorB;
    matrizAB = Matrix.matrixAb;

    tamano = MatrixData.tamano_Matriz;
    checkMatriz(matrizA);
    cholesky(matrizA, vectorB);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  // Init for output
  public void tituloEtapa(String s) {
    for (int j = 0; j < tamano; j++) {
      if (j == 0) {
        list.add(s);
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      } else {
        list.add(" ");
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setAdapter(adaptador);
      }
    }
  }

  // Adding values to grid
  public void tablaGS(double[][] matrizAB) {
    NumberFormat formatter = new DecimalFormat("0.##E0");

    // Name of the stage
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

    // Marking columns
    for (int j = 0; j < matrizAB[0].length; j++) {
      list.add("x" + j);
      adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
      tabla_grid.setAdapter(adaptador);
    }

    // Calculated values
    for (int i = 0; i < matrizAB.length; i++) {

      for (int j = 0; j < matrizAB[i].length; j++) {
        list.add(String.valueOf(Double.valueOf(formatter.format(matrizAB[i][j]))));
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setNumColumns(tamano);
        tabla_grid.setAdapter(adaptador);
      }
    }
  }

  //Diagonal major than zero
  public boolean checkMatriz(double[][] a) {
    for (int i = 0; i < a.length; i++) {
      if (a[i][i] <= 0) {
        return false;
      }
    }
    return true;
  }

  // Cholesky method
  public void cholesky(double[][] a, double[] b) {

    if (!checkMatriz(a)) {
      Mensaje("The A's diagonal has to be major than zero");
    }

    int n = a.length;
    double[][] l = new double[a.length][a.length];
    double[][] u = new double[a.length][a.length];
    list = new ArrayList<>();

    for (int k = 1; k < n + 1; k++) {
      tituloEtapa("Stage " + (k));
      double suma = 0;
      for (int p = 0; p < k - 1; p++) {
        suma += l[k - 1][p] * u[p][k - 1];
      }
      u[k - 1][k - 1] = Math.sqrt(a[k - 1][k - 1] - suma);
      l[k - 1][k - 1] = u[k - 1][k - 1];
      for (int j = k + 1; j < n + 1; j++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += l[j - 1][p] * u[p][k - 1];
        }
        l[j - 1][k - 1] = (a[j - 1][k - 1] - suma) / l[k - 1][k - 1];
      }
      etapa = "Matrix L" + "\n";
      tablaGS(l);
      for (int i = k + 1; i < n + 1; i++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += l[k - 1][p] * u[p][i - 1];
        }
        u[k - 1][i - 1] = (a[k - 1][i - 1] - suma) / l[k - 1][k - 1];
      }
      etapa = "Matrix U" + "\n";
      tablaGS(u);
    }

    double[] z = progressiveSubstitution(l, b);
    u = augmentedMatrix(u, z);
    double[] x = regressiveSubstitution(u);

  }

  public double[][] augmentedMatrix(double matriz[][], double vector[]) {
    int n = matriz.length;
    double[][] matrizdat = new double[n][n + 1];
    for (int i = 0; i < n; i++) {
      for (int k = 0; k < n + 1; k++) {
        if (k < n) {
          matrizdat[i][k] = matriz[i][k];
        } else {
          matrizdat[i][k] = vector[i];
        }
      }
    }
    return matrizdat;
  }

  // Calculating X vector
  public double[] regressiveSubstitution(double a[][]) {
    int n = a.length;
    double x[] = new double[n];
    x[n - 1] = a[n - 1][n] / a[n - 1][n - 1];
    for (int i = n - 2; i >= 0; i--) {
      double suma = 0;
      for (int j = i + 1; j < n; j++) {
        suma += a[i][j] * x[j];
      }
      x[i] = (a[i][n] - suma) / a[i][i];
    }
    for (int i = 0; i < x.length; i++) {
      resultados.setText(resultados.getText() + "\n" + "x" + i + " = " + x[i] + "\n");
    }
    return x;
  }

  // Calculating Z vector
  public double[] progressiveSubstitution(double L[][], double b[]) {
    int n = L.length;
    double[] z = new double[n];
    z[0] = b[0] / L[0][0];
    for (int i = 1; i < n; ++i) {
      double total = 0;
      for (int j = i - 1; j >= 0; --j) {
        total += z[j] * L[i][j];
      }
      z[i] = (b[i] - total) / L[i][i];
    }
    for (int i = 0; i < z.length; i++) {
      resultados.setText(resultados.getText() + "\n" + "z" + i + " = " + z[i] + "\n");
    }

    return z;
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
    }).setMessage("The direct factorization method of Cholesky pretends to decompose a matrix A " +
      "into the product of the two matrices L (lower triangular matrix) and U (upper triangular " +
      "matrix), so that LU = A.\n" +
      "\n" +
      "For that purpose, we begin with one lower triangular matrix and with one upper triangular one " +
      "where the elements different from zero are unknown, but with the particularity that the " +
      "elements of the diagonals of both matrices have the same values, so that L[i][i] = U[i][i].\n" +
      "\n" +
      "Then, we use the matrix multiplication concept to find both matrices unknown elements.\n" +
      "\n" +
      "Once we have L and U, progressive and regressive substitution are applied to solve the system of equations.");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}
