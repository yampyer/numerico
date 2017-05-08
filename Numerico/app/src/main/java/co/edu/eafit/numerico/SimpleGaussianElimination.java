package co.edu.eafit.numerico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class SimpleGaussianElimination extends AppCompatActivity {

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
        resultados = (TextView) findViewById(R.id.editText1);

        matrizA = Matrix.matrixA;
        vectorB = Matrix.vectorB;
        matrizAB = Matrix.matrixAb;

        tamano = MatrixData.tamano_Matriz;
        gaussianElimination(matrizA, vectorB);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void tablaGS(double[][] matrizAB) {

        for (int j = 0; j < matrizAB[0].length; j++) {
            if (j == 0) {
                list.add(etapa);
                adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                tabla_grid.setAdapter(adaptador);
            } else {
                list.add(" ");
                adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                tabla_grid.setAdapter(adaptador);
            }
        }

        for (int j = 0; j < matrizAB[0].length; j++) {
            if (j == matrizAB[0].length - 1) {
                list.add("b");
                adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                tabla_grid.setAdapter(adaptador);
            } else {
                list.add("x" + j);
                adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                tabla_grid.setAdapter(adaptador);
            }
        }
        for (int i = 0; i < matrizAB.length; i++) {

            for (int j = 0; j < matrizAB[i].length; j++) {
                list.add(String.valueOf(matrizAB[i][j]));
                adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                tabla_grid.setNumColumns(tamano + 1);
                tabla_grid.setAdapter(adaptador);
            }
        }
    }

    public void gaussianElimination(double[][] A, double[] b) {
        NumberFormat formatter = new DecimalFormat("#.##E0");

        int n = A.length;
        int l = A[0].length;
        double tabla[][] = new double [A.length][A[0].length + 1];
        list = new ArrayList<>();

        if (n != l) {
            Mensaje("A is not a square matrix");

        } else {
            tabla = Matrix.augmented(A, b);

            int[] var = null;
            // This solve the system
            var = Matrix.doX(tabla.length, var);

            double m = 0;
            for (int k = 0; k < n - 1; k++) {
                etapa = "Stage " + (k + 1);
                for (int i = k + 1; i < n; i++) {
                    m = tabla[i][k] / tabla[k][k];
                    for (int j = k; j < n + 1; j++) {
                        tabla[i][j] = Double.valueOf(formatter.format(tabla[i][j] - m * tabla[k][j]));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
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

    public void help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Given an augmented matrix Ab, the purpose of the Gaussian Elimination is to do " +
                "elementary row operations until we get the equivalent system, in which the coefficient " +
                "matrix is an upper triangular matrix.\n" +
                "\n" +
                "For that purpose, in the stage k, we calculate the " +
                "multipliers of each row dividing each element of the column k under the element in the " +
                "diagonal by the element of the row k.\n" +
                "\n" +
                " After that, we change every row under the row k for a new row, calculated as: \n" +
                "\n" +
                "New Row (i) = Row (i) – Multiplier(i)(k)*Row (k)\n" +
                "\n" +
                "Once we get the desired upper triangular matrix, regressive substitution is applied to solve the system of equations.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
