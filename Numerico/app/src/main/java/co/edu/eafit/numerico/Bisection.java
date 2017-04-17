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

public class Bisection extends AppCompatActivity implements View.OnClickListener {

    EditText stringInicial;
    EditText stringSiguiente;
    EditText stringIteraciones;
    EditText stringTolerancia;
    EditText polinomioBisc;

    String valorInicial;
    String valorSiguiente;
    String valorIteraciones;
    String valorTolerancia;
    String funcionbisc;

    static String val_iniIn;
    String val_iniIt;
    static String val_iniS;
    String val_iniT;
    static String funcionbisc2;

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

    int iteraciones;
    int cosa;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bisection);

        stringInicial = (EditText) findViewById(R.id.initial_value_edit);
        stringSiguiente = (EditText) findViewById(R.id.next_value_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioBisc = (EditText) findViewById(R.id.function_edit);

        if (!OneVariableInput.fXTodos.matches("")) {
            polinomioBisc.setText(OneVariableInput.fXTodos);
        }

        Button calcularBsc_btn = (Button) findViewById(R.id.calculate_btn);
        calcularBsc_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        funcionbisc = polinomioBisc.getText().toString();
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
                if (valorInicial.matches("") || valorSiguiente.matches("") || valorIteraciones.matches("") || valorTolerancia.matches("") || funcionbisc.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    val_iniIn = valorInicial;
                    val_iniIt = valorIteraciones;
                    val_iniS = valorSiguiente;
                    val_iniT = valorTolerancia;
                    funcionbisc2 = funcionbisc;
                    comprobarValor();
                }
                break;

            default:
                break;

        }
    }

    void comprobarValor() {
        xInicial = Double.parseDouble(valorInicial);
        xSiguiente = Double.parseDouble(valorSiguiente);
        tolerancia = Double.parseDouble(valorTolerancia);
        iteraciones = Integer.parseInt(valorIteraciones);

        if (iteraciones == 0) {
            Mensaje("Iterations has to be major than zero");
            stringIteraciones.setText(" ");
        } else {
            metodoBiseccion();
        }
    }

    void metodoBiseccion() {
        funcionbisc = polinomioBisc.getText().toString();
        NumberFormat formatter = new DecimalFormat("0.###E0");
        NumberFormat formatter2 = new DecimalFormat("0.#####E0");
        try {
            Evaluator myParser = new Evaluator();

            fX0 = myParser.evaluate("x", xInicial, funcionbisc);
            fX1 = myParser.evaluate("x", xSiguiente, funcionbisc);
            if (fX0 == 0) {
                Mensaje(xInicial + " is a root");
            } else if (fX1 == 0) {
                Mensaje(xSiguiente + " is a root");
            } else if ((fX0 * fX1) < 0) {
                xMedio = (xInicial + xSiguiente) / 2;
                fXMedio = myParser.evaluate("x", xMedio, funcionbisc);
                xError = tolerancia + 1;
                xErrorR = tolerancia + 1;

                setContentView(R.layout.activity_table);

                Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(myToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                estoyTabla = true;
                count = 0;

                String str_n = String.valueOf(" n   ");
                String str_ini = String.valueOf(" X0 ");
                String str_sig = String.valueOf(" X1 ");
                String str_xmed = String.valueOf(" XMedium ");
                String str_fxmed = String.valueOf(" f(XMedium) ");
                String str_err = String.valueOf(" Absolute Error ");
                String str_errR = String.valueOf(" Relative Error ");

                tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);
                count++;

                str_n = String.valueOf(0);
                str_ini = String.valueOf(xInicial);
                str_sig = String.valueOf(xSiguiente);
                str_xmed = String.valueOf(xMedio);
                str_fxmed = String.valueOf(formatter.format(fXMedio));
                str_err = String.valueOf("Doesn't exist");
                str_errR = String.valueOf("Doesn't exist");

                tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);


                for (int i = 0; (i < iteraciones) && (xError > tolerancia) && (xErrorR > tolerancia) && (fXMedio != 0); i++) {

                    if ((fX0 * fXMedio) < 0) {
                        xSiguiente = xMedio;
                        fX1 = fXMedio;
                    } else {
                        xInicial = xMedio;
                        fX0 = fXMedio;
                    }

                    xAux = xMedio;
                    xMedio = (xInicial + xSiguiente) / 2;
                    fXMedio = myParser.evaluate("x", xMedio, funcionbisc);
                    xError = Math.abs(xMedio - xAux);
                    xErrorR = Math.abs((xMedio - xAux) / xMedio);

                    cosa = i + 1;
                    str_n = " " + String.valueOf(cosa) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xInicial)) + " ";
                    str_sig = " " + String.valueOf(formatter2.format(xSiguiente)) + " ";
                    str_xmed = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxmed = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);

                }

                if (fXMedio == 0) {
                    Mensaje(xMedio + " is a root");

                    str_n = " " + String.valueOf(cosa + 1) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xInicial)) + " ";
                    str_sig = " " + String.valueOf(formatter2.format(xSiguiente)) + " ";
                    str_xmed = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxmed = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);

                } else if (xError < tolerancia) {
                    Mensaje(xMedio + " is an approximation to a root with a tolerance = " + tolerancia);

                    xMedio = (xInicial + xSiguiente) / 2;
                    xAux = xMedio;
                    fXMedio = myParser.evaluate("x", xMedio, funcionbisc);
                    xError = Math.abs(xMedio - xAux);
                    xErrorR = Math.abs((xMedio - xAux) / xMedio);

                    str_n = String.valueOf(cosa + 1);
                    str_ini = " " + String.valueOf(formatter2.format(xInicial)) + " ";
                    str_sig = " " + String.valueOf(formatter2.format(xSiguiente)) + " ";
                    str_xmed = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxmed = String.valueOf(fXMedio);
                    str_err = String.valueOf(formatter.format(xError));
                    str_errR = String.valueOf(formatter.format(xErrorR));

                    tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);
                } else {
                    Mensaje("Failure in " + iteraciones + " iterations");

                    xMedio = (xInicial + xSiguiente) / 2;
                    xAux = xMedio;
                    fXMedio = myParser.evaluate("x", xMedio, funcionbisc);
                    xError = Math.abs(xMedio - xAux);
                    xErrorR = Math.abs((xMedio - xAux) / xMedio);

                    str_n = " " + String.valueOf(cosa + 1) + " ";
                    str_ini = " " + String.valueOf(formatter2.format(xInicial)) + " ";
                    str_sig = " " + String.valueOf(formatter2.format(xSiguiente)) + " ";
                    str_xmed = " " + String.valueOf(formatter2.format(xMedio)) + " ";
                    str_fxmed = " " + String.valueOf(formatter.format(fXMedio)) + " ";
                    str_err = " " + String.valueOf(formatter.format(xError)) + " ";
                    str_errR = " " + String.valueOf(formatter.format(xErrorR)) + " ";

                    tablitaBisc(str_n, str_ini, str_sig, str_xmed, str_fxmed, str_err, str_errR);
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

    public void tablitaBisc(String str_n, String str_ini, String str_sig, String str_xmed, String str_fxmed, String str_err, String str_errR) {

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
        labelN.setTextSize(15);
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

        TextView labelSiguiente = new TextView(this);
        labelSiguiente.setId(200 + count);
        labelSiguiente.setTextSize(15);
        labelSiguiente.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelSiguiente.setText(str_sig);
        labelSiguiente.setTextColor(Color.BLACK);
        tr.addView(labelSiguiente);

        TextView labelXMedio = new TextView(this);
        labelXMedio.setId(200 + count);
        labelXMedio.setTextSize(15);
        labelXMedio.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelXMedio.setText(str_xmed);
        labelXMedio.setTextColor(Color.BLACK);
        tr.addView(labelXMedio);

        TextView labelFXMedio = new TextView(this);
        labelFXMedio.setId(200 + count);
        labelFXMedio.setTextSize(15);
        labelFXMedio.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        labelFXMedio.setText(str_fxmed);
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
                if (valorInicial.matches("") || valorSiguiente.matches("") || funcionbisc.matches("")) {
                    Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show();
                } else {
                    val_iniIn = valorInicial;
                    val_iniS = valorSiguiente;
                    funcionbisc2 = funcionbisc;
                    Intent graficador = new Intent(this, Grapher.class);
                    graficador.putExtra("Uniqid", "Biseccion");
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
        }).setMessage("The method consists in dividing the given interval, which contains a root, into two equal-sized subranges.\n" +
                "\n" +
                "Then you decide with which subinterval must continue to work, and this decision is based on the following criteria: which of the two subintervals contains the cusp in the range of the function?\n" +
                "\n" +
                "After taking the decision, we continue with the process iteratively, repeating the above steps, and getting a sequence of values formed by the midpoints obtained in each iteration.\n" +
                "\n" +
                "This sequence converges to a root of the equation, which is in that range.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loadView() {
        setContentView(R.layout.activity_bisection);

        stringInicial = (EditText) findViewById(R.id.initial_value_edit);
        stringSiguiente = (EditText) findViewById(R.id.next_value_edit);
        stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
        stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
        polinomioBisc = (EditText) findViewById(R.id.function_edit);

        stringInicial.setText(val_iniIn);
        stringSiguiente.setText(val_iniS);
        stringIteraciones.setText(val_iniIt);
        stringTolerancia.setText(val_iniT);
        polinomioBisc.setText(funcionbisc2);

        Button calcularBsc_btn = (Button) findViewById(R.id.calculate_btn);
        calcularBsc_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
