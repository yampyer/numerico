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

public class FalsePosition extends AppCompatActivity implements View.OnClickListener {

    EditText stringInicial;
    EditText stringSiguiente;
    EditText stringIteraciones;
    EditText stringTolerancia;
    EditText polinomioRF;

    String valorInicial;
    String valorSiguiente;
    String valorIteraciones;
    String valorTolerancia;
    String funcionRF;

    static String val_iniIn;
    String val_iniIt;
    static String val_iniS;
    String val_iniT;
    static String funcionRF2;

    boolean estoyTabla = false;

    double xInicial;
    double xSiguiente;
    double tolerancia;
    double xError;
    double xErrorR;
    double xAux;
    double fX0;
    double fX1;
    double fXMedio;
    double xMedio;

    int cosa;
    int iteraciones;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_position);
        stringInicial = (EditText) findViewById(R.id.initial_value_edit);
        stringSiguiente = (EditText) findViewById(R.id.next_value_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioRF = (EditText) findViewById(R.id.function_edit);

        if (!OneVariableInput.fXTodos.matches("")) {
            polinomioRF.setText(OneVariableInput.fXTodos);
        }

        Button calcularRF_btn = (Button) findViewById(R.id.calculate_btn);
        calcularRF_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.one_variable_method, menu);
        return true;
    }

    public void datosEdit() {
        valorInicial = stringInicial.getText().toString();
        valorSiguiente = stringSiguiente.getText().toString();
        valorIteraciones = stringIteraciones.getText().toString();
        valorTolerancia = stringTolerancia.getText().toString();
        funcionRF = polinomioRF.getText().toString();
    }

    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.calculate_btn:
                datosEdit();
                if (valorInicial.matches("") || valorSiguiente.matches("") || valorIteraciones.matches("") || valorTolerancia.matches("") || funcionRF.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    val_iniIn = valorInicial;
                    val_iniIt = valorIteraciones;
                    val_iniS = valorSiguiente;
                    val_iniT = valorTolerancia;
                    funcionRF2 = funcionRF;
                    comprobarValorRF();
                }

                break;
            default:
                break;

        }
    }

    void comprobarValorRF() {
        valorInicial = stringInicial.getText().toString();
        valorSiguiente = stringSiguiente.getText().toString();
        valorIteraciones = stringIteraciones.getText().toString();
        valorTolerancia = stringTolerancia.getText().toString();

        xInicial = Double.parseDouble(valorInicial);
        xSiguiente = Double.parseDouble(valorSiguiente);
        tolerancia = Double.parseDouble(valorTolerancia);
        iteraciones = Integer.parseInt(valorIteraciones);

        if (iteraciones == 0) {
            Mensaje("Iterations has to be major than zero");
            stringIteraciones.setText(" ");
        } else {
            metodoReglaFalsa();
        }


    }


    void metodoReglaFalsa() {
        funcionRF = polinomioRF.getText().toString();
        NumberFormat formatter = new DecimalFormat("0.##E0");
        NumberFormat formatter2 = new DecimalFormat("0.#####E0");
        try {
            Evaluator myParser = new Evaluator();

            fX0 = myParser.evaluate("x", xInicial, funcionRF);
            fX1 = myParser.evaluate("x", xSiguiente, funcionRF);
            if (fX0 == 0) {
                Mensaje(xInicial + " is a root");
            } else if (fX1 == 0) {
                Mensaje(xSiguiente + " is a root");

            } else if ((fX0 * fX1) < 0) {
                xMedio = xInicial - ((fX0 * (xSiguiente - xInicial)) / (fX1 - fX0));
                fXMedio = myParser.evaluate("x", xMedio, funcionRF);
                xError = tolerancia + 1;
                xErrorR = tolerancia + 1;

                setContentView(R.layout.activity_table);
                Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(myToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                estoyTabla = true;
                count = 0;

                String str_n = String.valueOf("  n   ");
                String str_ini = String.valueOf(" Xn ");
                String str_fxn = String.valueOf(" f(Xn) ");
                String str_err = String.valueOf(" Absolute Error ");
                String str_errR = String.valueOf(" Relative Error ");

                tablitaRF(str_n, str_ini, str_fxn, str_err, str_errR);
                count++;

                str_n = String.valueOf(0);
                str_ini = String.valueOf(xInicial);
                str_fxn = String.valueOf(fXMedio);
                str_err = String.valueOf("Doesn't exist");
                str_errR = String.valueOf("Doesn't exist");

                tablitaRF(str_n, str_ini, str_fxn, str_err, str_errR);

                for (int i = 1; (i < iteraciones) && (xError > tolerancia) && (xErrorR > tolerancia) && (fXMedio != 0); i++) {
                    if ((fX0 * fXMedio) < 0) {
                        xSiguiente = xMedio;
                        fX1 = fXMedio;
                    } else {
                        xInicial = xMedio;
                        fX0 = fXMedio;
                    }
                    xAux = xMedio;
                    xMedio = xInicial - ((fX0 * (xSiguiente - xInicial)) / (fX1 - fX0));

                    fXMedio = myParser.evaluate("x", xMedio, funcionRF);
                    xError = Math.abs(xMedio - xAux);
                    xErrorR = Math.abs((xMedio - xAux) / xMedio);

                    cosa = i;
                    str_n = " " + String.valueOf(cosa) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxn = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaRF(str_n, str_ini, str_fxn, str_err, str_errR);

                }
                if (fXMedio == 0) {
                    Mensaje(xMedio + " is a root");

                    str_n = " " + String.valueOf(cosa + 1) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxn = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaRF(str_n, str_ini, str_fxn, str_err, str_errR);

                } else if (xError < tolerancia) {
                    Mensaje(xMedio + " is an approximation to a root with a tolerance = " + tolerancia + ".");

                    xMedio = xInicial - ((fX0 * (xSiguiente - xInicial)) / (fX1 - fX0));
                    xAux = xMedio;
                    fXMedio = myParser.evaluate("x", xMedio, funcionRF);
                    xError = Math.abs(xMedio - xAux);
                    xErrorR = Math.abs((xMedio - xAux) / xMedio);

                    str_n = " " + String.valueOf(cosa + 1) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxn = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaRF(str_n, str_ini, str_fxn, str_err, str_errR);


                } else {
                    Mensaje("Failure in " + iteraciones + " iterations");
                }

            } else {
                Mensaje("The interval is not optimum");

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


    public void tablitaRF(String str_n, String str_ini, String str_fxn, String str_err, String str_errR) {

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
        labelN.setPadding(2, 0, 5, 0);
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
                if (valorInicial.matches("") || valorSiguiente.matches("") || funcionRF.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show();
                } else {
                    val_iniIn = valorInicial;
                    val_iniS = valorSiguiente;
                    funcionRF2 = funcionRF;
                    Intent graficador = new Intent(this, Grapher.class);
                    graficador.putExtra("Uniqid", "ReglaFalsa");
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
        }).setMessage("This method has all the characteristics and conditions of the Bisection method, except for the way how the midpoint inside the given interval is calculated.\n" +
                "\n" +
                "In this case, the line joining the points (a, f(a)) and (b, f(b)) is found and, because one of that points is above the x-axis and the other one is below it, we know that there’s a cut-off point between the line and the mentioned axis.\n" +
                "\n" +
                "This cut-off point has the form (Xm, 0), where Xm represents the searched midpoint.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loadView() {
        setContentView(R.layout.activity_false_position);
        stringInicial = (EditText) findViewById(R.id.initial_value_edit);
        stringSiguiente = (EditText) findViewById(R.id.next_value_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioRF = (EditText) findViewById(R.id.function_edit);

        stringInicial.setText(val_iniIn);
        stringSiguiente.setText(val_iniS);
        stringIteraciones.setText(val_iniIt);
        stringTolerancia.setText(val_iniT);
        polinomioRF.setText(funcionRF2);

        Button calcularRF_btn = (Button) findViewById(R.id.calculate_btn);
        calcularRF_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
