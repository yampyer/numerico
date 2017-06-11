package co.edu.eafit.numerico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NaturalSpline extends AppCompatActivity {

  EditText value;
  TextView nombre_metodo;
  TextView resultado_metodo;
  TextView polinomio_txt;

  double x;
  String el_valor;
  double[] fxn;
  double[] xn;

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
  }

  public void evalX() {
    if (el_valor.matches("")) {
      x = 1.0;
    } else {
      x = Double.parseDouble(el_valor);
    }
    fxn = InterpolationTable.fXn;
    xn = InterpolationTable.xn;
    LinealSplineMethod(fxn, xn, x);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void LinealSplineMethod(double[] fxn, double[] xn, double valorX) {
    nombre_metodo.setText("Cubic Spline");
    int n = xn.length;
    ArrayList<Double> xv = new ArrayList<>();
    ArrayList<Double> yv = new ArrayList<>();
    for(double x:xn){
      xv.add(x);
    }
    for(double y:fxn){
      yv.add(y);
    }
    CubicSpline cs = new  CubicSpline(xv,yv);

    polinomio_txt.setText("");
    resultado_metodo.setText(cs.toString()+"\n"+cs.evaluate(valorX));
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
      default:
        return super.onOptionsItemSelected(item);
    }
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

  public void help() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    // Get the layout inflater
    LayoutInflater inflater = this.getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View dialogView = inflater.inflate(R.layout.alert_dialog_with_image, null);
    TextView text1 = (TextView) dialogView.findViewById(R.id.textView1);
    text1.setText("A cubic spline is a spline constructed of piecewise third-order polynomials which " +
      "pass through a set of m control points. The second derivative of each polynomial is commonly set " +
      "to zero at the endpoints, since this provides a boundary condition that completes the system of m-2 " +
      "equations. This produces a so-called \"natural\" cubic spline and leads to a simple tridiagonal system " +
      "which can be solved easily to give the coefficients of the polynomials. However, this choice is not the " +
      "only one possible, and other boundary conditions can be used instead. ");
    ImageView image1 = (ImageView) dialogView.findViewById(R.id.dialog_imageview1);
    image1.setImageResource(R.drawable.cubic_spline_equation_1);
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
}
