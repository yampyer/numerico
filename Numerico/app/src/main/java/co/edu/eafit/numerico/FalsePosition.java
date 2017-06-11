package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import API.NumericoAPI;
import API.ServerAPI;
import models.Method;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SessionManager;

public class FalsePosition extends AppCompatActivity implements View.OnClickListener {

  EditText stringInicial;
  EditText stringSiguiente;
  EditText stringIteraciones;
  EditText stringTolerancia;
  EditText polinomioRF;
  RadioGroup errorType;

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
  boolean absoluteError = true;
  boolean relativeError = false;

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

  private SessionManager session;
  private ProgressDialog progressDialog;
  private NumericoAPI serverAPI;
  private String tokenPlayer;
  private String idUser;

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

    serverAPI = ServerAPI.getInstance();
    session = SessionManager.getInstance(getApplicationContext());
    progressDialog = new ProgressDialog(FalsePosition.this);

    // Check if user is already logged in or not
    if (session.isLoggedIn()) {
      HashMap<String, String> user = session.getUserDetails();

      //token
      tokenPlayer = user.get(SessionManager.KEY_TOKEN);

      //id
      idUser = user.get(SessionManager.KEY_ID);
    }

    errorType = (RadioGroup) findViewById(R.id.errorSelection);

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

  /**
   * Represents an asynchronous logger task used to save operations
   */
  private void logHistory(final String function, final String method, final String userId) {

    Method methodJson = new Method(function, method, userId);
    Call<Method> call = serverAPI.newMethod(methodJson);
    call.enqueue(new Callback<Method>() {
      @Override
      public void onResponse(Call<Method> call, Response<Method> response) {

        System.out.println(response.code());
        System.out.println(response.body());
        System.out.println(response.message());
      }

      @Override
      public void onFailure(Call<Method> call, Throwable t) {
      }
    });
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
    if (errorType.getCheckedRadioButtonId() == R.id.absoluteErrorButton) {
      absoluteError = true;
      relativeError = false;
    } else {
      absoluteError = false;
      relativeError = true;
    }
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
          if (OneVariableInput.fXTodos.matches("")) {
            OneVariableInput.fXTodos_edt.setText(funcionRF2);
            OneVariableInput.fXTodos = funcionRF2;
          }
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
    logHistory(funcionRF, "False Position", idUser);
    NumberFormat formatter = new DecimalFormat("#.#E0");
    try {
      Evaluator myParser = new Evaluator();

      fX0 = myParser.evaluate("x", xInicial, funcionRF);
      fX1 = myParser.evaluate("x", xSiguiente, funcionRF);
      if (fX0 == 0) {
        Mensaje(xInicial + " is a root");
      } else if (fX1 == 0) {
        Mensaje(xSiguiente + " is a root");

      } else if ((fX0 * fX1) < 0) {
        xMedio = xInicial - ((fX0 * (xInicial - xSiguiente)) / (fX0 - fX1));
        fXMedio = myParser.evaluate("x", xMedio, funcionRF);
        xError = tolerancia + 1;

        setContentView(R.layout.activity_table);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        estoyTabla = true;
        count = 0;

        String str_n = String.valueOf("  n   ");
        String str_ini = String.valueOf(" Xi ");
        String str_nxt = String.valueOf(" Xs ");
        String str_m = String.valueOf("  Xm  ");
        String str_fxn = String.valueOf("  f(Xm) ");
        String str_err;
        if (absoluteError) {
          str_err = String.valueOf(" Absolute Error ");
        } else {
          str_err = String.valueOf(" Relative Error ");
        }

        tablitaRF(str_n, str_ini, str_nxt, str_m, str_fxn, str_err);
        count++;

        str_n = String.valueOf(0);
        str_ini = String.valueOf(xInicial);
        str_nxt = String.valueOf(xSiguiente) + " ";
        str_m = String.valueOf(xMedio);
        str_fxn = " " + String.valueOf(formatter.format(fXMedio)) + " ";
        str_err = String.valueOf("-------");

        tablitaRF(str_n, str_ini, str_nxt, str_m, str_fxn, str_err);

        for (int i = 1; (fXMedio != 0) && (xError > tolerancia) && (i < iteraciones); i++) {
          if ((fX0 * fXMedio) < 0) {
            xSiguiente = xMedio;
            fX1 = fXMedio;
          } else {
            xInicial = xMedio;
            fX0 = fXMedio;
          }

          xAux = xMedio;
          xMedio = xInicial - ((fX0 * (xInicial - xSiguiente)) / (fX0 - fX1));
          fXMedio = myParser.evaluate("x", xMedio, funcionRF);
          if (absoluteError) {
            xError = Math.abs(xMedio - xAux);
          } else {
            xError = Math.abs((xMedio - xAux) / xMedio);
          }
          xErrorR = Math.abs((xMedio - xAux) / xMedio);

          cosa = i;
          str_n = " " + String.valueOf(cosa) + " ";
          str_ini = " " + String.valueOf(formatter.format(xInicial)) + " ";
          str_nxt = " " + String.valueOf(formatter.format(xSiguiente)) + " ";
          str_m = " " + String.valueOf(xMedio) + " ";
          str_fxn = " " + String.valueOf(formatter.format(fXMedio)) + " ";
          str_err = " " + String.valueOf(formatter.format(xError)) + " ";

          tablitaRF(str_n, str_ini, str_nxt, str_m, str_fxn, str_err);

        }
        if (fXMedio == 0) {
          Mensaje(xMedio + " is a root");
        } else if (xError < tolerancia) {
          Mensaje(xMedio + " is an approximation to a root with a tolerance = " + tolerancia);
        } else {
          Mensaje("Failure in " + cosa + " iterations");
        }

      } else {
        Mensaje("This is a bad interval");

      }
    } catch (NumberFormatException e) {
      Mensaje("Enter valid data");

    } catch (Exception e) {
      Mensaje("Error: Function not defined in the given interval");
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

  public void tablitaRF(String str_n, String str_ini, String str_nxt, String str_m, String str_fxn, String str_err) {

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

    TextView labelNext = new TextView(this);
    labelNext.setId(200 + count);
    labelNext.setTextSize(15);
    labelNext.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    labelNext.setText(str_nxt);
    labelNext.setTextColor(Color.BLACK);
    tr.addView(labelNext);

    TextView labelM = new TextView(this);
    labelM.setId(200 + count);
    labelM.setTextSize(15);
    labelM.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    labelM.setText(str_m);
    labelM.setTextColor(Color.BLACK);
    tr.addView(labelM);

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
    // Get the layout inflater
    LayoutInflater inflater = this.getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View dialogView = inflater.inflate(R.layout.alert_dialog_with_image, null);
    TextView text1 = (TextView) dialogView.findViewById(R.id.textView1);
    text1.setText("This method has all the characteristics and conditions of the Bisection method, except for the way how the midpoint inside the given interval is calculated.\n" +
      "\n" +
      "In this case, the line joining the points (a, f(a)) and (b, f(b)) is found and, because one of that points is above the x-axis and the other one is below it, we know that there’s a cut-off point between the line and the mentioned axis.\n" +
      "\n" +
      "This cut-off point has the form (Xm, 0), where Xm represents the searched midpoint.");
    ImageView image1 = (ImageView) dialogView.findViewById(R.id.dialog_imageview1);
    image1.setImageResource(R.drawable.false_position_equation);
    builder.setView(dialogView)
      // Add action buttons
      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      });
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

    errorType = (RadioGroup) findViewById(R.id.errorSelection);

    RadioButton error;

    if (absoluteError) {
      error = (RadioButton) errorType.getChildAt(0);
    } else {
      error = (RadioButton) errorType.getChildAt(1);
    }
    errorType.check(error.getId());

    Button calcularRF_btn = (Button) findViewById(R.id.calculate_btn);
    calcularRF_btn.setOnClickListener(this);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
