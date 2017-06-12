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

public class IncrementalSearch extends AppCompatActivity implements View.OnClickListener {

  EditText stringInicial;
  EditText stringDelta;
  EditText stringIteraciones;
  EditText polinomioBsq;

  String valorInicial;
  String valorDelta;
  String valorIteraciones;
  String funcionbsq;

  static String val_iniIn;
  String val_iniIt;
  static String val_iniD;
  static String funcionbsq2;

  boolean estoyTabla = false;

  double xInicial;
  double xDelta;
  double x1;
  double fX0;
  double fX1;

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
    setContentView(R.layout.activity_incremental_search);

    stringInicial = (EditText) findViewById(R.id.initial_value_edit);
    stringDelta = (EditText) findViewById(R.id.delta_value_edit);
    stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
    polinomioBsq = (EditText) findViewById(R.id.function_edit);

    if (!OneVariableInput.fXTodos.matches("")) {
      polinomioBsq.setText(OneVariableInput.fXTodos);
    }

    serverAPI = ServerAPI.getInstance();
    session = SessionManager.getInstance(getApplicationContext());
    progressDialog = new ProgressDialog(IncrementalSearch.this);

    // Check if user is already logged in or not
    if (session.isLoggedIn()) {
      HashMap<String, String> user = session.getUserDetails();

      //token
      tokenPlayer = user.get(SessionManager.KEY_TOKEN);

      //id
      idUser = user.get(SessionManager.KEY_ID);
    }

    Button calcularBsq_btn = (Button) findViewById(R.id.calculate_btn);
    calcularBsq_btn.setOnClickListener(this);

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

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String date = formatter.format(new Date());
    Method methodJson = new Method(function, method, userId, date);
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

  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.calculate_btn:
        valorInicial = stringInicial.getText().toString();
        valorDelta = stringDelta.getText().toString();
        valorIteraciones = stringIteraciones.getText().toString();
        funcionbsq = polinomioBsq.getText().toString();

        if (valorInicial.matches("") || valorDelta.matches("") || valorIteraciones.matches("") || funcionbsq.matches("")) {
          Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();
          return;
        } else {
          val_iniIn = valorInicial;
          val_iniIt = valorIteraciones;
          val_iniD = valorDelta;
          funcionbsq2 = funcionbsq;
          if (OneVariableInput.fXTodos.matches("")) {
            OneVariableInput.fXTodos_edt.setText(funcionbsq2);
            OneVariableInput.fXTodos = funcionbsq2;
          }
          comprobarValor();
        }
        break;

      default:
        break;
    }
  }

  void comprobarValor() {
    valorInicial = stringInicial.getText().toString();
    valorDelta = stringDelta.getText().toString();
    valorIteraciones = stringIteraciones.getText().toString();

    xInicial = Double.parseDouble(valorInicial);
    xDelta = Double.parseDouble(valorDelta);
    iteraciones = Integer.parseInt(valorIteraciones);


    if (iteraciones <= 0) {
      Mensaje("Iterations has to be major than zero");
      stringIteraciones.setText(" ");
    } else {
      metodoBusquedaIncremental();
    }
  }

  void metodoBusquedaIncremental() {
    funcionbsq = polinomioBsq.getText().toString();
    logHistory(funcionbsq, "Incremental Search", idUser);
    NumberFormat formatter = new DecimalFormat("#.#E0");

    try {
      Evaluator myParser = new Evaluator();

      fX0 = myParser.evaluate("x", xInicial, funcionbsq);
      if (fX0 == 0) {
        Mensaje(xInicial + " is a root");
      } else {
        x1 = xInicial + xDelta;
        fX1 = myParser.evaluate("x", x1, funcionbsq);

        setContentView(R.layout.activity_table);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        estoyTabla = true;
        count = 0;

        String str_n = String.valueOf(" n  ");
        String str_ini = String.valueOf("  X0  ");
        String str_fx = String.valueOf("  f(X0) ");

        tablitaBsq(str_n, str_ini, str_fx);
        count++;

        str_n = String.valueOf(0);
        str_ini = " " + String.valueOf(xInicial) + " ";
        str_fx = " " + String.valueOf(formatter.format(fX0)) + " ";

        tablitaBsq(str_n, str_ini, str_fx);

        for (int i = 0; ((fX0 * fX1) > 0) && (i < iteraciones); i++) {
          xInicial = x1;
          fX0 = fX1;
          x1 = xInicial + xDelta;
          fX1 = myParser.evaluate("x", x1, funcionbsq);

          cosa = i + 1;
          str_n = " " + String.valueOf(cosa) + " ";
          str_ini = " " + String.valueOf(xInicial) + " ";
          str_fx = " " + String.valueOf(formatter.format(fX0)) + " ";

          tablitaBsq(str_n, str_ini, str_fx);

        }
        if (fX1 == 0) {
          Mensaje(x1 + " is a root");

          x1 = xInicial + xDelta;
          xInicial = x1;
          fX1 = myParser.evaluate("x", x1, funcionbsq);
          fX0 = fX1;

          str_n = " " + String.valueOf(cosa + 1) + " ";
          str_ini = " " + String.valueOf(xInicial) + " ";
          str_fx = " " + String.valueOf(formatter.format(fX0)) + " ";

          tablitaBsq(str_n, str_ini, str_fx);

        } else if ((fX0 * fX1) < 0) {
          Mensaje("There is a root between " + xInicial + " and " + x1);

          x1 = xInicial + xDelta;
          xInicial = x1;
          fX1 = myParser.evaluate("x", x1, funcionbsq);
          fX0 = fX1;

          str_n = " " + String.valueOf(cosa + 1) + " ";
          str_ini = " " + String.valueOf(formatter.format(xInicial)) + " ";
          str_fx = " " + String.valueOf(formatter.format(fX0)) + " ";

          tablitaBsq(str_n, str_ini, str_fx);

        } else {
          Mensaje("Failure in " + cosa + " iterations");
        }
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

  public void tablitaBsq(String str_n, String str_ini, String str_fxmed) {

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

    TextView labelFXMedio = new TextView(this);
    labelFXMedio.setId(200 + count);
    labelFXMedio.setTextSize(15);
    labelFXMedio.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    labelFXMedio.setText(str_fxmed);
    labelFXMedio.setTextColor(Color.BLACK);
    tr.addView(labelFXMedio);

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
        valorInicial = stringInicial.getText().toString();
        valorDelta = stringDelta.getText().toString();
        funcionbsq = polinomioBsq.getText().toString();

        if (valorInicial.matches("") || valorDelta.matches("") || funcionbsq.matches("")) {
          Toast.makeText(this, "Enter values", Toast.LENGTH_LONG).show();
        } else {
          val_iniIn = valorInicial;
          val_iniD = valorDelta;
          funcionbsq2 = funcionbsq;
          Intent intent = new Intent(this, Grapher.class);
          intent.putExtra("Uniqid", "Busquedas");
          startActivity(intent);
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
    }).setMessage("It is used to search for those sub-ranges in which the existence of a root is guaranteed.\n" +
      "\n" +
      "An equation is given (f (x) = 0), an interval ([a, b]) and an increase\n" +
      "(delta Δ).\n" +
      "\n" +
      "It consists of starting from one end of the interval (a) and adding the increment. The function is evaluated for where there is a change of sign. If this occurs, in that interval there is at least one root.\n" +
      "\n" +
      "This method will give me those intervals where there are roots of the given equation");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  public void loadView() {
    setContentView(R.layout.activity_incremental_search);

    stringInicial = (EditText) findViewById(R.id.initial_value_edit);
    stringDelta = (EditText) findViewById(R.id.delta_value_edit);
    stringIteraciones = (EditText) findViewById(R.id.iterations_edit);
    polinomioBsq = (EditText) findViewById(R.id.function_edit);

    stringInicial.setText(val_iniIn);
    stringDelta.setText(val_iniD);
    stringIteraciones.setText(val_iniIt);
    polinomioBsq.setText(funcionbsq2);

    Button calcularBsq_btn = (Button) findViewById(R.id.calculate_btn);
    calcularBsq_btn.setOnClickListener(this);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
