package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GaussSeidel extends AppCompatActivity {

    double[][] matrizA;
    double[] vectorB;
    double[] datosX_gauss = new double[Matrix.vectorB.length];
    double tol;
    double lamda;
    String[] datosX_strg = new String[Matrix.vectorB.length];

    String[] datosXs_array = new String[Matrix.vectorB.length];

    String str_n;
    String str_err;
    static int iter;
    int tamano;
    int count = 0;
    boolean estoyTabla = false;

    ArrayList<String> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iterations_table);

        IterativeMethods.estoyTabla = true;
        matrizA = Matrix.matrixA;
        vectorB = Matrix.vectorB;
        datosX_strg = IterativeMethods.valores;
        datosX_gauss = IterativeMethods.datos_guardados;

        tamano = MatrixData.tamano_Matriz;
        tol = IterativeMethods.tol;
        lamda = IterativeMethods.lamda;
        iter = IterativeMethods.iter;

        gauss(matrizA, vectorB, tol, iter, datosX_gauss, lamda);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void gauss(double[][] A, double[] b, double tol, int iter, double[] x0, double lamda) {
        int n = A.length;
        String error;
        NumberFormat formatter = new DecimalFormat("#.#E0");
        NumberFormat xform = new DecimalFormat("#.#E0");

        double tabla[][] = new double[1][x0.length + 2];
        double[] x = new double[n];
        double aux;
        int cont = 0;
        double errorAbs = tol + 1;

        tabla[0][0] = cont + 0.0;
        tabla[0][x0.length + 1] = errorAbs;

        for (int i = 1; i <= x0.length; i++) {
            tabla[0][i] = x0[i - 1];
        }

        estoyTabla = true;
        count = 0;
        for (int i = 0; i < datosX_strg.length; i++) {
            datosXs_array[i] = "  x" + i + "  ";
        }

        str_n = String.valueOf("  n  ");
        str_err = String.valueOf("  Error  ");

        tablitaGauss(str_n, datosXs_array, str_err);
        count++;

        double[] dato = new double[x0.length + 2];

        error = String.valueOf(formatter.format(errorAbs));
        str_n = String.valueOf(cont);

        for (int i = 0; i < datosX_strg.length; i++) {
            datosXs_array[i] = "  " + x0[i] + "  ";
        }

        tablitaGauss(str_n, datosXs_array, error);

        while (errorAbs > tol && cont < iter) {
            errorAbs = 0.0;
            for (int i = 0; i < n; i++) {
                Double suma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        suma = suma + A[i][j] * x0[j];
                    }
                }
                x[i] = lamda * ((b[i] - suma) / A[i][i]) + (1 - lamda) * x0[i];
                aux = x[i] - x0[i];
                errorAbs = errorAbs + Math.pow(aux, 2);
                x0[i] = x[i];
            }
            errorAbs = Math.pow(errorAbs, 0.5);

            dato[dato.length - 1] = errorAbs;

            for (int i = 0; i < n; i++) {
                dato[i + 1] = x[i];
            }
            cont++;
            dato[0] = cont + 0.0;
            tabla = Matrix.addData(dato, tabla);

            error = String.valueOf(formatter.format(errorAbs));
            str_n = String.valueOf(cont);

            for (int k = 0; k < datosX_strg.length; k++) {
                datosXs_array[k] = " " + String.valueOf(xform.format(x0[k])) + "  ";
            }

            tablitaGauss(str_n, datosXs_array, error);
        }
        cont++;
        if (cont == iter) {
            Mensaje("Solution not found in  " + iter + " iterations");
        }
    }

    public void tablitaGauss(String str_n, String[] datosX_strg, String str_err) {

        TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        String str_tabla;

        //Create rows
        TableRow tr = new TableRow(this);
        if (count == 0) {
            tr.setId(9 + 1);
            tr.setBackgroundColor(Color.argb(104, 12, 66, 204));
            tr.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            tr.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        } else {
            if (count % 2 != 0) tr.setBackgroundColor(Color.argb(34, 14, 68, 205));
            tr.setId(100 + count);
            tr.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }

        //Create columns
        TextView labelN = new TextView(this);
        labelN.setId(200 + count);
        labelN.setTextSize(15);
        labelN.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelN.setText(str_n);
        labelN.setTextColor(Color.BLACK);
        tr.addView(labelN);

        final TextView[] myTextViews = new TextView[datosX_strg.length]; // create an empty array;

        for (int i = 0; i < datosX_strg.length; i++) {
            str_tabla = datosX_strg[i];
            final TextView rowTextView = new TextView(this);
            labelN.setId(200 + count);
            rowTextView.setText(str_tabla);
            rowTextView.setTextSize(15);
            rowTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            rowTextView.setTextColor(Color.BLACK);
            tr.addView(rowTextView);
            myTextViews[i] = rowTextView;
        }

        TextView labelErr = new TextView(this);
        labelErr.setId(200 + count);
        labelErr.setTextSize(15);
        labelErr.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelErr.setText(str_err);
        labelErr.setTextColor(Color.BLACK);
        tr.addView(labelErr);

        // finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        count++;
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
        }).setMessage("This method is based on the Fixed Point method. That means that with an initial approximation " +
                "of the solution, we can generate other approximations nearer to the real values of the variables." +
                "\n" +
                "The Gauss Seidel method is an iterative algorithm for determining the solutions of a system " +
                "of linear equations.\n" +
                "\n" +
                "For the execution of the method, first we try to convert the given A matrix " +
                "to a diagonal dominant one moving its rows and columns.\n" +
                "\n" +
                "Then, we take each equation and put the diagonal variable in terms of the other variables.\n" +
                "\n" +
                "After that, we have to assign initial values to the variables and find the first approximation " +
                "of each one using the cleared equations, but with the particularity that we replace the variable " +
                "values not with the previous iterations ones, but with the current calculated ones.\n" +
                "\n" +
                "We continue replacing the values of the previous iterations in the mentioned " +
                "equations to generate more approximations to the real solution until we reach the permitted tolerance.\n" +
                "\n" +
                "The method stops when the higher value of the dispersion (in absolute value) is less " +
                "than the tolerance.\n" +
                "\n" +
                "One feature of this method is that in every iteration the new approximations are used " +
                "to calculate the other current ones, which haven’t been calculated yet.\n" +
                "\n" +
                "Remember that the Alpha value is used to obtain or to improve the system’s convergence.\n" +
                "\n" +
                "For non-relaxation use Alpha = 1; to obtain the convergence of non-convergent systems use Alpha " +
                "between 0 and 1; and to accelerate the convergence of convergent but slow systems use Alpha between 1 and 2.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
