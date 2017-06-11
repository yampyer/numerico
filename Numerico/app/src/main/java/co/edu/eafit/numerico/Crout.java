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

public class Crout extends AppCompatActivity {

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
    if (MatrixData.withoutPhases) {
      tabla_grid.setVisibility(View.GONE);
    }
    resultados = (TextView) findViewById(R.id.editText1);

    matrizA = Matrix.matrixA;
    vectorB = Matrix.vectorB;
    matrizAB = Matrix.matrixAb;

    tamano = MatrixData.tamano_Matriz;
    crout(matrizA, vectorB);

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
        list.add(String.valueOf(matrizAB[i][j]));
        adaptador = new ArrayAdapter<>(this, R.layout.simple_list_item_1, list);
        tabla_grid.setNumColumns(tamano);
        tabla_grid.setAdapter(adaptador);
      }
    }
  }

  // Crout method
  public void crout(double[][] A, double[] b) {
    NumberFormat formatter = new DecimalFormat("0.##E0");

    int n = A.length;
    int l = A[0].length;
    double[][] L = new double[A.length][A.length];
    double[][] U = new double[A.length][A.length];
    list = new ArrayList<>();
    if (n != l) {
      Mensaje("Matrix A is not an square matrix");
    } else {

      double suma1, suma2, suma3;
      for (int k = 0; k < n; k++) {

        tituloEtapa("Stage " + (k + 1));

        suma1 = 0;
        for (int p = 0; p < k; p++) {
          suma1 = suma1 + L[k][p] * U[p][k];
        }

        L[k][k] = Double.valueOf(formatter.format(A[k][k] - suma1));

        if (L[k][k] == 0) {
          Mensaje("Error L[" + k + "," + k + "] is = 0");
        }
        U[k][k] = 1;
        for (int i = k + 1; i < n; i++) {
          suma2 = 0;
          for (int r = 0; r < k; r++) {
            suma2 = suma2 + L[i][r] * U[r][k];
          }
          L[i][k] = Double.valueOf(formatter.format(A[i][k] - suma2));
        }
        etapa = "Matrix L";
        tablaGS(L);
        for (int j = k + 1; j < n; j++) {
          suma3 = 0;
          for (int s = 0; s < k; s++) {
            suma3 = suma3 + L[k][s] * U[s][j];
          }
          U[k][j] = Double.valueOf(formatter.format(((A[k][j] - suma3) / L[k][k])));
        }
        etapa = "Matrix U" + "\n";
        tablaGS(U);
      }
      double suma;
      double[] z = new double[n];
      for (int i = 0; i < n; i++) {
        suma = 0;
        for (int j = 0; j < i; j++) {
          suma = suma + L[i][j] * z[j];
        }
        z[i] = (b[i] - suma) / L[i][i];
        resultados.setText(resultados.getText() + "\n" + "z" + i + " = " + z[i] + "\n");
      }
      suma = 0;
      double[] x = new double[n];
      for (int i = n - 1; i >= 0; i--) {
        suma = 0;
        for (int j = i + 1; j < n; j++) {
          suma = suma + U[i][j] * x[j];
        }
        x[i] = (z[i] - suma) / U[i][i];
        resultados.setText(resultados.getText() + "\n" + "x" + i + " = " + x[i] + "\n");
      }
      resultados.setText(resultados.getText() + "\n" + "det" + " = " + Matrix.determinant(A, n) + "\n");
    }
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
    }).setMessage("The direct factorization method of Crout pretends to decompose a matrix A into " +
      "the product of the two matrices L (lower triangular matrix) and U (upper triangular " +
      "matrix), so that LU = A.\n" +
      "\n" +
      "For that purpose, we begin with one lower triangular matrix where the elements different " +
      "from zero are unknown, and with a upper triangular one where the elements different " +
      "from zero are unknown but the elements of the diagonal are one (1), so that U[i][i].\n" +
      "\n" +
      "Then, we use the matrix multiplication concept to find both matrices unknown elements.\n" +
      "\n" +
      "Once we have L and U, progressive and regressive substitution are applied to solve the system of equations.");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}
