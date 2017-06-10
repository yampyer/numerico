package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Interpolation extends AppCompatActivity {

  RelativeLayout variable_relative;
  EditText valorX_edt;
  RelativeLayout datosInterp_relative;
  TableLayout xn_tab;
  TableLayout fXn_tab;

  boolean isInEnteringVariables = true;
  int nElements;
  static EditText valorX;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interpolation);
    initView();
    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void initView() {
    variable_relative = (RelativeLayout) findViewById(R.id.cant_Variables);
    valorX_edt = (EditText) findViewById(R.id.valor_Variables);
    datosInterp_relative = (RelativeLayout) findViewById(R.id.relativeLayout1);
    xn_tab = (TableLayout) findViewById(R.id.table_xn);
    fXn_tab = (TableLayout) findViewById(R.id.table_fxn);
    valorX = (EditText) findViewById(R.id.valorX_edt);
    //evaluarX = String.valueOf(valorX);
    datosInterp_relative.setVisibility(View.GONE);
    isInEnteringVariables = true;
    invalidateOptionsMenu();
    variable_relative.setVisibility(View.VISIBLE);
  }

  public void createTable(View v) {
    if (!valorX_edt.getText().toString().isEmpty()) {
      if (Double.parseDouble(valorX_edt.getText().toString()) > 1) {
        nElements = Integer.parseInt(valorX_edt.getText().toString());
        variable_relative.setVisibility(View.GONE);
        isInEnteringVariables = false;
        //invalidateOptionsMenu();
        datosInterp_relative.setVisibility(View.VISIBLE);
        numberCells();
      } else {
        Toast.makeText(getApplicationContext(), "Enter allowed values", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(getApplicationContext(), "Enter allowed values", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void numberCells() {
    xn_tab.removeAllViews();
    fXn_tab.removeAllViews();

    //For Xn and fXn tables.
    for (int i = 0; i < nElements; i++) {
      TableRow rowXn = new TableRow(this);
      TableRow rowFXn = new TableRow(this);
      EditText cellXn = new EditText(this);
      EditText cellFXn = new EditText(this);
      cellXn.setHint("X" + i);
      cellFXn.setHint("f(X" + i + ")");
      cellXn.setTextSize(13.5f);
      cellFXn.setTextSize(13.5f);
      cellXn.setWidth(120);
      cellFXn.setWidth(200);
      cellXn.setGravity(Gravity.CENTER);
      cellFXn.setGravity(Gravity.CENTER);
      cellXn.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
      cellFXn.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
      cellXn.setSingleLine(true);
      cellFXn.setSingleLine(true);
      rowXn.addView(cellXn);
      rowFXn.addView(cellFXn);
      xn_tab.addView(rowXn, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
      fXn_tab.addView(rowFXn, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }
  }

  public void takeVariables(View v) {
    ArrayList<Double> valoresXn = new ArrayList<>();
    ArrayList<Double> valoresfXn = new ArrayList<>();
    boolean saveData = true;

    //For Xn and fXn tables
    for (int j = 0; j < nElements && saveData; j++) {
      TableRow rowXn = (TableRow) xn_tab.getChildAt(j);
      TableRow rowFXn = (TableRow) fXn_tab.getChildAt(j);
      EditText posicionXn = (EditText) rowXn.getChildAt(0);
      EditText posicionFXn = (EditText) rowFXn.getChildAt(0);
      String valor_posicionXn = posicionXn.getText().toString();
      String valor_posicionFxn = posicionFXn.getText().toString();
      if (!valor_posicionXn.isEmpty() && !valor_posicionFxn.isEmpty()) {
        //The data is correct and can be saved
        valoresXn.add(Double.parseDouble(valor_posicionXn));
        valoresfXn.add(Double.parseDouble(valor_posicionFxn));
      } else {
        Toast.makeText(getApplicationContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
        saveData = false;
      }
    }

    Set unfXn_por_unXn = new HashSet();
    for (Double xN : valoresXn) {
      if (!unfXn_por_unXn.add(xN)) {
        Toast.makeText(getApplicationContext(), "Add a F(Xn) for each Xn", Toast.LENGTH_SHORT).show();
        saveData = false;
      }
    }

    if (saveData) {  //Data saved correctly.
      new InterpolationTable(valoresXn, valoresfXn);  //Creation of tables.
      Intent openSystemsOfEqs = new Intent(Interpolation.this, InterpolationOptions.class);
      //openSystemsOfEqs.putExtra("matrix", matrix);
      startActivity(openSystemsOfEqs);
    }
  }
}
