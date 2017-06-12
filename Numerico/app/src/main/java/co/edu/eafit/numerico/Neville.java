package co.edu.eafit.numerico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import utils.SessionManager;

public class Neville extends AppCompatActivity {

  EditText value;
  TextView nombre_metodo;
  TextView resultado_metodo;
  TextView polinomio_txt;
  double x;
  String el_valor;

  double[] fxn;
  double[] xn;
  private SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interpolation_results);
    nombre_metodo = (TextView) findViewById(R.id.titulo_metodo_txt);
    resultado_metodo = (TextView) findViewById(R.id.restultado_metodo_txt);
    polinomio_txt = (TextView) findViewById(R.id.polinomio_txt);
    value = Interpolation.valorX;
    el_valor = value.getText().toString();
    evalX();

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    session = SessionManager.getInstance(getApplicationContext());
  }

  public void evalX() {
    if (el_valor.matches("")) {
      x = 1.0;
    } else {
      x = Double.parseDouble(el_valor);
    }
    fxn = InterpolationTable.fXn;
    xn = InterpolationTable.xn;
    nevilleMethod(fxn, xn, x);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  private String printMatriz(double [][] matrix, int n, double x[]){
    String result = "";
    result+="Xi__________________             ";
    for(int i=0;i<n;i++){
      result+="f"+(i)+"()________________             ";
    }
    result+="\n";
    for(int i=0; i< n;i++){
      String xString = printFixed(Double.toString(x[i]),20);
      result+=xString;
      for(int j=0; j<n; j++){
        String doubleString =  printFixed(Double.toString(matrix[i][j]),20);
        result += doubleString;
      }
      result+="\n";
    }
    result+="\n";
    return result;
  }

  private String printFixed(String num, int pres){
    String result = num;
    if(result.length()>pres) {
      result = result.substring(0, pres + 1);
    }else if(result.length()<pres){
      while (result.length()!=pres){
        result+="0";
      }
    }
    return result+"   ";
  }

  public void nevilleMethod(double[] fxn, double[] xn, double valorX) {
    nombre_metodo.setText("Neville");
    int n = xn.length;
    String result = "";
    double [][] tabla = new double[n][n];
    for(int i = 0; i<n;i++){
      tabla[i][0] = fxn[i];
    }
    result+= "Result table:\n";
    for(int i = 0; i<n;i++){
      for(int j = 1; j<i+1;j++){
        tabla[i][j] = ((valorX-xn[i-j])*tabla[i][j-1] - ((valorX-xn[i])*tabla[i-1][j-1]))/(xn[i] - xn[i-j]);
      }
    }
    result+=printMatriz(tabla, n, xn);
    result+= "\nResult:\n";
    result+="p("+valorX+") = "+ tabla[n-1][n-1];
    polinomio_txt.setText("");
    resultado_metodo.setText(result);
  }

  public void Message(String s) {

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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.help_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.help_menu:
        help();
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
    }).setMessage("Given a set of n+1 data points (xi, yi) where no two xi are the same, the interpolating " +
      "polynomial is the polynomial p of degree at most n with the property\n" +
      "\n" +
      "p(xi) = yi for all i = 0,…,n\n" +
      "\n" +
      "This polynomial exists and it is unique. Neville's algorithm evaluates the polynomial at " +
      "some point x.\n" +
      "\n" +
      "Let pi, j denote the polynomial of degree j − i which goes through the points (xk, yk)" +
      "for k = i, i + 1, …, j. The pi,j satisfy the recurrence relation.");
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }
}
