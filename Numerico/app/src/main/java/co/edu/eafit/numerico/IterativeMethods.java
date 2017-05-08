package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class IterativeMethods extends AppCompatActivity implements View.OnClickListener {

    double[][] matrizA;
    static EditText[] x0_edt = new EditText[Matrix.matrixA.length];
    double[] vectorB;
    double[][] matrizAB;
    static double[] datosX;
    static double[] datos_guardados;
    static double tol;
    static double lamda;
    static int iter;
    static int tamano;

    static String tol_str;
    static String iter_str;
    static String lamda_str;
    static TableLayout datos_X;

    GridView tabla_grid;
    EditText tol_edt;
    EditText iter_edt;
    EditText lamda_edt;
    static String[] valores;
    ArrayAdapter<String> adaptador;
    boolean estado_valX0 = false;
    static boolean estoyTabla = false;

    ArrayList<Double> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
    }

    public void nuevosX0() {
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER);
        for (int i = 0; i < vectorB.length; i++) {
            EditText cell = new EditText(this);
            cell.setHint(" Value X" + (i + 1));
            cell.setTextSize(13.5f);
            cell.setGravity(Gravity.CENTER_HORIZONTAL);
            cell.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            cell.setSingleLine(true);
            x0_edt[i] = cell;
            if (i > 0) {
                row.getChildAt(i - 1).setNextFocusDownId(cell.getId());
                row.getChildAt(i - 1).setNextFocusForwardId(cell.getId());
                row.addView(cell);
            } else {
                row.addView(cell);
            }
        }
        datos_X.addView(row, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

    }

    public void analisis_ingreso(View v) {

        if (!valor_vacio()) {
            if (estado_lambda()) {
                asignacion_valores();
            } else {
                Toast.makeText(getApplicationContext(), "Lambda must be between 0 and 2", Toast.LENGTH_SHORT).show();
            }
            botonesIterat();
        } else {
            Toast.makeText(getApplicationContext(), "Enter Values", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean valor_vacio() {
        for (int i = 0; i < x0_edt.length; i++) {
            if (x0_edt[i].getText().toString().isEmpty() || tol_edt.getText().toString().isEmpty() || iter_edt.getText().toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean estado_lambda() {
        if (!lamda_edt.getText().toString().isEmpty()) {
            double inputAlfa = Double.parseDouble(lamda_edt.getText().toString());
            if (inputAlfa >= 0 && inputAlfa <= 2) {
                lamda = Double.parseDouble(lamda_str);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void asignacion_valores() {
        datosX = new double[valores.length];
        datos_guardados = new double[valores.length];
        for (int i = 0; i < datosX.length; i++) {
            datosX[i] = Double.parseDouble(x0_edt[i].getText().toString());
            datos_guardados[i] = Double.parseDouble(x0_edt[i].getText().toString());
        }
        if (!lamda_edt.getText().toString().isEmpty()) {
            lamda = Double.parseDouble(lamda_edt.getText().toString());
        } else {
            lamda = 1;
        }
        tol_str = tol_edt.getText().toString();
        iter = Integer.parseInt(iter_edt.getText().toString());
        if (iter <= 0) {
            Toast.makeText(getApplicationContext(), "Iterations must be major than 0", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean analiTol() {
        return tol > 0;
    }

    public boolean analiIter() {
        return iter <= 0;
    }

    public void botonesIterat() {
        setContentView(R.layout.activity_iterative_methods);

        Button jacobi_btn = (Button) findViewById(R.id.jacobi_btn);
        Button gauss_seidel_btn = (Button) findViewById(R.id.gauss_seidel_btn);

        jacobi_btn.setOnClickListener(this);
        gauss_seidel_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(estoyTabla) {
            loadView();
            estoyTabla = false;
        } else {
            finish();
        }
        return true;
    }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.jacobi_btn:
                estoyTabla = true;
                Intent confi = new Intent(this, Jacobi.class);
                startActivity(confi);
                break;
            case R.id.gauss_seidel_btn:
                estoyTabla = true;
                Intent confi2 = new Intent(this, GaussSeidel.class);
                startActivity(confi2);
                break;
            case R.id.continuar_btn:
                tol_str = tol_edt.getText().toString();
                iter_str = iter_edt.getText().toString();
                lamda_str = lamda_edt.getText().toString();
                analisis_ingreso(v);
                estoyTabla = true;
                break;
        }
    }

    private void loadView() {
        setContentView(R.layout.activity_iterative_data);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (estoyTabla) {
            tabla_grid = (GridView) findViewById(R.id.GridOpciones);
            tol_edt = (EditText) findViewById(R.id.tol_edt);
            iter_edt = (EditText) findViewById(R.id.iter_edt);
            lamda_edt = (EditText) findViewById(R.id.alpha_edt);
            datos_X = (TableLayout) findViewById(R.id.datosx);

            tol_edt.setText(tol_str);
            iter_edt.setText(iter_str);
            lamda_edt.setText(lamda_str);

        } else {
            tabla_grid = (GridView) findViewById(R.id.GridOpciones);
            tol_edt = (EditText) findViewById(R.id.tol_edt);
            iter_edt = (EditText) findViewById(R.id.iter_edt);
            lamda_edt = (EditText) findViewById(R.id.alpha_edt);
            datos_X = (TableLayout) findViewById(R.id.datosx);
        }
        estoyTabla = false;
        Button sigui_btn = (Button) findViewById(R.id.continuar_btn);
        sigui_btn.setOnClickListener(this);


        matrizA = Matrix.matrixA;
        vectorB = Matrix.vectorB;
        matrizAB = Matrix.matrixAb;

        tamano = MatrixData.tamano_Matriz;
        list = new ArrayList<>();

        valores = new String[matrizA.length];

        nuevosX0();
    }

}
