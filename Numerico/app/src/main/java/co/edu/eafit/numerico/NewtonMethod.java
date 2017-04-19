package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NewtonMethod extends AppCompatActivity implements View.OnClickListener {

    EditText stringInicial;
    EditText polinomioFxMN;
    EditText stringIteraciones;
    EditText stringTolerancia;
    EditText polinomioMN;

    String valorInicial;
    String funcionDFxMN;
    String valorIteraciones;
    String valorTolerancia;
    String funcionMN;

    static String val_iniIn;
    String val_iniIt;
    String val_iniT;
    static String funcionfx;
    static String funciondfx;

    boolean estoyTabla = false;

    double xInicial;
    double tolerancia;
    double xError;
    double xErrorR;
    double dFx;
    double x1 = 9999999;
    double fx;

    int iteraciones;
    int cosa;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton_method);

        stringInicial = (EditText) findViewById(R.id.x0_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioMN = (EditText) findViewById(R.id.function_edit);
        polinomioFxMN = (EditText) findViewById(R.id.fx_edit);

        if (!OneVariableInput.fXTodos.matches("")) {
            polinomioMN.setText(OneVariableInput.fXTodos);
        }
        if (!OneVariableInput.dfXTodos.matches("")) {
            polinomioFxMN.setText(OneVariableInput.dfXTodos);
        }

        Button calcularPF_btn = (Button) findViewById(R.id.calculate_btn);
        calcularPF_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_variable_method, menu);
        return true;
    }

    public void datosEdit() {
        valorInicial = stringInicial.getText().toString();
        valorIteraciones = stringIteraciones.getText().toString();
        valorTolerancia = stringTolerancia.getText().toString();
        funcionMN = polinomioMN.getText().toString();
        funcionDFxMN = polinomioFxMN.getText().toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (estoyTabla) {
            loadView();
            estoyTabla = false;
        } else {
            finish();
        }
        return true;
    }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.calculate_btn:
                datosEdit();
                if (valorInicial.matches("") || funcionDFxMN.matches("") || valorIteraciones.matches("") || valorTolerancia.matches("") || funcionMN.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();
                    return;

                } else {
                    val_iniIn = valorInicial;
                    val_iniIt = valorIteraciones;
                    val_iniT = valorTolerancia;
                    funcionfx = funcionMN;
                    funciondfx = funcionDFxMN;
                    comprobarValorMN();
                }

                break;
            default:
                break;

        }
    }

    void comprobarValorMN() {
        valorInicial = stringInicial.getText().toString();
        valorIteraciones = stringIteraciones.getText().toString();
        valorTolerancia = stringTolerancia.getText().toString();

        xInicial = Double.parseDouble(valorInicial);
        tolerancia = Double.parseDouble(valorTolerancia);
        iteraciones = Integer.parseInt(valorIteraciones);


        if (iteraciones == 0) {
            Mensaje("Iterations has to be major than zero");
            stringIteraciones.setText(" ");
        } else {
            metodoNewton();
        }


    }


    void metodoNewton() {
        funcionMN = polinomioMN.getText().toString();
        funcionDFxMN = polinomioFxMN.getText().toString();
        NumberFormat formatter = new DecimalFormat("0.###E0");
        NumberFormat formatter2 = new DecimalFormat("0.#####E0");
        try {

            Evaluator myParser = new Evaluator();

            fx = myParser.evaluate("x", xInicial, funcionMN);
            dFx = myParser.evaluate("x", xInicial, funcionDFxMN);

            setContentView(R.layout.activity_table);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            estoyTabla = true;
            count = 0;

            String str_n = String.valueOf(" n   ");
            String str_ini = String.valueOf(" Xn ");
            String str_fxn = String.valueOf(" f(Xn) ");
            String str_fxxn = String.valueOf(" f'(Xn) ");
            String str_err = String.valueOf(" Absolute Error ");
            String str_errR = String.valueOf(" Relative Error ");

            tablitaMN(str_n, str_ini, str_fxn, str_fxxn, str_err, str_errR);
            count++;

            str_n = String.valueOf(0);
            str_ini = String.valueOf(xInicial);
            str_fxn = String.valueOf(fx);
            str_fxxn = String.valueOf(dFx);
            str_err = String.valueOf("Doesn't exist");
            str_errR = String.valueOf("Doesn't exist");

            tablitaMN(str_n, str_ini, str_fxn, str_fxxn, str_err, str_errR);

            xError = tolerancia + 1;

            for (int i = 0; (fx != 0) && (dFx != 0) && (xError > tolerancia) && (i < iteraciones); i++) {
                x1 = xInicial - (fx / dFx);
                fx = myParser.evaluate("x", x1, funcionMN);
                dFx = myParser.evaluate("x", x1, funcionDFxMN);
                xError = Math.abs(x1 - xInicial);
                xErrorR = Math.abs((x1 - xInicial) / x1);
                xInicial = x1;

                cosa = i + 1;
                str_n = " " + String.valueOf(cosa) + " ";
                str_ini = " " + String.valueOf(formatter2.format(xInicial)) + " ";
                str_fxn = " " + String.valueOf(formatter.format(fx)) + " ";
                str_fxxn = " " + String.valueOf(formatter.format(dFx)) + " ";
                str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                tablitaMN(str_n, str_ini, str_fxn, str_fxxn, str_err, str_errR);

            }
            if (fx == 0) {
                Mensaje(xInicial + " is a root");
            } else if (xError < tolerancia) {
                Mensaje(x1 + " is an approximation to a root with a tolerance = " + tolerancia + ".");
            } else if (dFx == 0) {
                Mensaje(x1 + " possibly is a multiple root");
            } else {
                Mensaje("Failure in " + iteraciones + " iterations");
            }
        } catch (NumberFormatException e) {
            Mensaje("Enter valid data");

        } catch (Exception e) {
            Mensaje("Error: " + e.getMessage());

        }
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


    public void tablitaMN(String str_n, String str_ini, String str_fxn, String str_fxxn, String str_err, String str_errR) {
        TableLayout t1;

        TableLayout tl = (TableLayout) findViewById(R.id.main_table);

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

        TextView labelN = new TextView(this);
        labelN.setId(200 + count);
        labelN.setTextSize(20);
        labelN.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelN.setText(str_n);
        labelN.setTextColor(Color.BLACK);
        tr.addView(labelN);

        TextView labelInicial = new TextView(this);
        labelInicial.setId(200 + count);
        labelInicial.setTextSize(15);
        labelInicial.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelInicial.setText(str_ini);
        labelInicial.setTextColor(Color.BLACK);
        tr.addView(labelInicial);

        TextView labelFXMedio = new TextView(this);
        labelFXMedio.setId(200 + count);
        labelFXMedio.setTextSize(15);
        labelFXMedio.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelFXMedio.setText(str_fxn);
        labelFXMedio.setTextColor(Color.BLACK);
        tr.addView(labelFXMedio);

        TextView labelDFXMedio = new TextView(this);
        labelDFXMedio.setId(200 + count);
        labelDFXMedio.setTextSize(15);
        labelDFXMedio.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelDFXMedio.setText(str_fxxn);
        labelDFXMedio.setTextColor(Color.BLACK);
        tr.addView(labelDFXMedio);

        TextView labelErr = new TextView(this);
        labelErr.setId(200 + count);
        labelErr.setTextSize(15);
        labelErr.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelErr.setText(str_err);
        labelErr.setTextColor(Color.BLACK);
        tr.addView(labelErr);

        TextView labelErrR = new TextView(this);
        labelErrR.setId(200 + count);
        labelErrR.setTextSize(15);
        labelErrR.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelErrR.setText(str_errR);
        labelErrR.setTextColor(Color.BLACK);
        tr.addView(labelErrR);

        // finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        count++;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (estoyTabla) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                loadView();
                estoyTabla = false;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help_menu:
                help();
                return true;
            case R.id.graph_UV:
                datosEdit();
                if (valorInicial.matches("") || funcionDFxMN.matches("") || funcionMN.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();

                } else {
                    val_iniIn = valorInicial;
                    funcionfx = funcionMN;
                    funciondfx = funcionDFxMN;
                    Intent graficador = new Intent(this, Grapher.class);
                    graficador.putExtra("Uniqid", "Newton");
                    startActivity(graficador);
                }
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
        }).setMessage("This method conserves all the characteristics and conditions of the fixed-point method, except for how g function is calculated.\n" +
                "\n" +
                "In this case, g(x) = x – ( f(x) / f’(x) ). So Xn+1 is calculated as\n" +
                "\n" +
                "Xn+1 = Xn – ( f(Xn) / f’(Xn) )");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loadView() {
        setContentView(R.layout.activity_newton_method);

        stringInicial = (EditText) findViewById(R.id.x0_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioMN = (EditText) findViewById(R.id.function_edit);
        polinomioFxMN = (EditText) findViewById(R.id.fx_edit);

        stringInicial.setText(val_iniIn);
        stringIteraciones.setText(val_iniIt);
        stringTolerancia.setText(val_iniT);
        polinomioFxMN.setText(funciondfx);
        polinomioMN.setText(funcionfx);

        Button calcularPF_btn = (Button) findViewById(R.id.calculate_btn);
        calcularPF_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
