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

public class Lagrange extends AppCompatActivity {

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
    LagrangeMethod(fxn, xn, x);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void LagrangeMethod(double[] fxn, double[] xn, double valorX) {
    nombre_metodo.setText("Lagrange Interpolating Polynomial");
    int n = xn.length;
    String result="";
    result+="Building funtion L(x):\n";
    double resultado = 0;
    String pol = "P(x): ";
    for(int k = 0; k<n;k++){
      double productoria = 1;
      String termino = "";
      for(int i = 0; i < n ; i++){
        if(i!=k){
          productoria = productoria * (valorX-xn[i])/(xn[k]-xn[i]);
          termino = termino + ("[(x-"+xn[i]+")/("+xn[k]+"-"+xn[i]+")]");
        }
      }
      result+="L"+k+"(x):"+termino+"\n";
      pol += (fxn[k]>0?"+":"")+fxn[k]+"*"+termino+"\n";
      resultado += productoria * fxn[k];
    }
    result+="\nInterpolation Polynomial:\n";
    result+=pol+"\n";
    result+="Result:\n";
    result+="p("+valorX+") = "+ resultado+"\n";
    polinomio_txt.setText("");
    resultado_metodo.setText(result);
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
    text1.setText("This method uses polynomial proposed by Joseph-Louis de Lagrange where a set of k+1 points " +
      "where we assumed all are different. " +
      "The interpolating polynomial in the Lagrange form is the linear combination: ");
    ImageView image1 = (ImageView) dialogView.findViewById(R.id.dialog_imageview1);
    image1.setImageResource(R.drawable.lagrange_equation_1);
    TextView text2 = (TextView) dialogView.findViewById(R.id.textView2);
    text2.setText("with the Lagrange polynomial bases: ");
    text2.setVisibility(View.VISIBLE);
    ImageView image2 = (ImageView) dialogView.findViewById(R.id.dialog_imageview2);
    image2.setImageResource(R.drawable.lagrange_equation_2);
    image2.setVisibility(View.VISIBLE);
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
