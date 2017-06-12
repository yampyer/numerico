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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import API.NumericoAPI;
import API.ServerAPI;
import models.Method;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SessionManager;

public class FixedPoint extends AppCompatActivity implements View.OnClickListener {

  EditText stringInicial;
  EditText polinomioG;
  EditText stringIteraciones;
  EditText stringTolerancia;
  EditText polinomioPF;
  RadioGroup errorType;

  String valorInicial;
  String funcionG;
  String valorIteraciones;
  String valorTolerancia;
  String funcionPF;

  static String val_iniIn;
  String val_iniIt;
  static String val_funcionPF;
  String val_iniT;
  static String val_funcionG;

  boolean estoyTabla = false;
  boolean absoluteError = true;
  boolean relativeError = false;

  double xInicial;
  double tolerancia;
  double xError;
  double xErrorR;
  double fX0;
  double x1;

  int iteraciones;
  int cosa;
  int count = 0;

  private SessionManager session;
  private ProgressDialog progressDialog;
  private NumericoAPI serverAPI;
  private String tokenPlayer;
  private String idUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fixed_point);

    stringInicial = (EditText) findViewById(R.id.x0_edit);
    stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
    stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
    polinomioPF = (EditText) findViewById(R.id.function_edit);
    polinomioG = (EditText) findViewById(R.id.gx_edit);

    if (!OneVariableInput.fXTodos.matches("")) {
      polinomioPF.setText(OneVariableInput.fXTodos);
    }
    if (!OneVariableInput.gXTodos.matches("")) {
      polinomioG.setText(OneVariableInput.gXTodos);
    }

    serverAPI = ServerAPI.getInstance();
    session = SessionManager.getInstance(getApplicationContext());
    progressDialog = new ProgressDialog(FixedPoint.this);

    // Check if user is already logged in or not
    if (session.isLoggedIn()) {
      HashMap<String, String> user = session.getUserDetails();

      //token
      tokenPlayer = user.get(SessionManager.KEY_TOKEN);

      //id
      idUser = user.get(SessionManager.KEY_ID);
    }

    errorType = (RadioGroup) findViewById(R.id.errorSelection);

    Button calcularPF_btn = (Button) findViewById(R.id.calculate_btn);
    calcularPF_btn.setOnClickListener(this);

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
    valorIteraciones = stringIteraciones.getText().toString();
    valorTolerancia = stringTolerancia.getText().toString();
    funcionPF = polinomioPF.getText().toString();
    funcionG = polinomioG.getText().toString();
    if (errorType.getCheckedRadioButtonId() == R.id.absoluteErrorButton) {
      absoluteError = true;
      relativeError = false;
    } else {
      absoluteError = false;
      relativeError = true;
    }
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
        if (valorInicial.matches("") || funcionG.matches("") || valorIteraciones.matches("") || valorTolerancia.matches("") || funcionPF.matches("")) {
          Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();
          return;
        } else {
          val_iniIn = valorInicial;
          val_iniIt = valorIteraciones;
          val_iniT = valorTolerancia;
          val_funcionPF = funcionPF;
          val_funcionG = funcionG;
          if (OneVariableInput.fXTodos.matches("")) {
            OneVariableInput.fXTodos_edt.setText(val_funcionPF);
            OneVariableInput.fXTodos = val_funcionPF;

          }
          if (OneVariableInput.gXTodos.matches("")) {
            OneVariableInput.gXTodos_edt.setText(val_funcionG);
            OneVariableInput.gXTodos = val_funcionG;
          }
          comprobarValor();
        }
        break;
      default:
        break;

    }
  }

  /**
   * Represents an asynchronous logger task used to save operations
   */
  private void logHistory(final String function, final String secondFunction, final String method, final String userId) {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String date = formatter.format(new Date());
    Method methodJson = new Method(function, secondFunction, method, userId, date);
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

  void comprobarValor() {
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
      metodoPuntoFijo();
    }
  }


  void metodoPuntoFijo() {
    funcionPF = polinomioPF.getText().toString();
    funcionG = polinomioG.getText().toString();
    logHistory(funcionPF, funcionG, "Fixed Point", idUser);
    NumberFormat formatter = new DecimalFormat("#.#E0");

    try {
      Evaluator myParser = new Evaluator();

      fX0 = myParser.evaluate("x", xInicial, funcionPF);
      xError = tolerancia + 1;

      setContentView(R.layout.activity_table);

      Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(myToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      estoyTabla = true;
      count = 0;

      String str_n = String.valueOf(" n   ");
      String str_ini = String.valueOf(" Xn ");
      String str_fxn = String.valueOf(" f(Xn) ");
      String str_err;
      if (absoluteError) {
        str_err = String.valueOf(" Absolute Error ");
      } else {
        str_err = String.valueOf(" Relative Error ");
      }
      tablitaPF(str_n, str_ini, str_fxn, str_err);
      count++;

      str_n = String.valueOf(0);
      str_ini = String.valueOf(xInicial);
      str_fxn = " " + String.valueOf(formatter.format(fX0)) + " ";
      str_err = String.valueOf("-------");

      tablitaPF(str_n, str_ini, str_fxn, str_err);


      for (int i = 1; (fX0 != 0) && (xError > tolerancia) && (i < iteraciones); i++) {
        x1 = myParser.evaluate("x", xInicial, funcionG);
        fX0 = myParser.evaluate("x", x1, funcionPF);
        xError = Math.abs(x1 - xInicial);
        xErrorR = Math.abs((x1 - xInicial) / x1);
        if (absoluteError) {
          xError = Math.abs(x1 - xInicial);
        } else {
          xError = Math.abs((x1 - xInicial) / x1);
        }
        xErrorR = Math.abs((x1 - xInicial) / x1);
        xInicial = x1;

        cosa = i;
        str_n = " " + String.valueOf(cosa) + " ";
        str_ini = " " + String.valueOf(xInicial) + " ";
        str_fxn = " " + String.valueOf(formatter.format(fX0)) + " ";
        str_err = " " + String.valueOf(formatter.format(xError)) + " ";

        tablitaPF(str_n, str_ini, str_fxn, str_err);
      }

      if (fX0 == 0) {
        Mensaje(xInicial + " is a root");
      } else if (xError < tolerancia || xErrorR < tolerancia) {
        Mensaje(xInicial + " is an approximation to a root with a tolerance = " + tolerancia + ".");
      } else {
        Mensaje("Failure in " + cosa + " iterations");
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

  public void tablitaPF(String str_n, String str_ini, String str_fxn, String str_err) {

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
        if (valorInicial.matches("") || funcionPF.matches("") || funcionG.matches("")) {
          Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show();
        } else {
          val_iniIn = valorInicial;
          val_funcionPF = funcionPF;
          val_funcionG = funcionG;
          Intent graficador = new Intent(this, Grapher.class);
          graficador.putExtra("Uniqid", "PuntoFijo");
          startActivity(graficador);
        }
        return true;
      case R.id.help_history:
        Intent history = new Intent(this, History.class);
        startActivity(history);
        return true;
      case R.id.help_logout:
        session.logoutUser();
        finish();
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
    }).setMessage("This method is part of the equation f (x) = 0, to generate x = g (x), to which the method seeks solution. To determine g (X), which is made clear some (any) x of  original f (x).\n" +
      "To find the solution to the equation\n" +
      "x = g (x), is looking for a value of x in g to replace the result is the same x, ie x we do not look for a change to the function g.\n" +
      "The graphical relationship that exists between the equations f (x) = 0 and\n" +
      "x = g (x) \n" +
      "\n" +
      "We can see that from a geometrical point of view, the problem changes. To solve the equation f (x) = 0,\n" +
      "we seek a value for x in the function y = f (x) cut the x axis, while to solve the equation x = g (x) the intersection is sought line y = x and the curve y = g (x).");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  public void loadView() {
    setContentView(R.layout.activity_fixed_point);

    stringInicial = (EditText) findViewById(R.id.x0_edit);
    stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
    stringTolerancia = (EditText) findViewById(R.id.tolerance_edit);
    polinomioPF = (EditText) findViewById(R.id.function_edit);
    polinomioG = (EditText) findViewById(R.id.gx_edit);

    stringInicial.setText(val_iniIn);
    stringIteraciones.setText(val_iniIt);
    stringTolerancia.setText(val_iniT);
    polinomioPF.setText(val_funcionPF);
    polinomioG.setText(val_funcionG);

    errorType = (RadioGroup) findViewById(R.id.errorSelection);

    RadioButton error;

    if (absoluteError) {
      error = (RadioButton) errorType.getChildAt(0);
    } else {
      error = (RadioButton) errorType.getChildAt(1);
    }
    errorType.check(error.getId());

    Button calcularPF_btn = (Button) findViewById(R.id.calculate_btn);
    calcularPF_btn.setOnClickListener(this);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
