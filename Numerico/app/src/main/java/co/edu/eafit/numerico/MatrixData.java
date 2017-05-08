package co.edu.eafit.numerico;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class MatrixData extends AppCompatActivity {

    private RelativeLayout tam_Matriz;
    private EditText tam_edt;
    private RelativeLayout val_Matriz;
    private TableLayout matrixA;
    private TableLayout vectorB;
    Button continuar;
    private boolean estado_Tam_Matriz = true;
    public static int tamano_Matriz;
    boolean estoyTabla = false;

    //Validate data from matrix
    private int valuesPointCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_data);
        initViewElements();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initViewElements() {
        tam_Matriz = (RelativeLayout) findViewById(R.id.cant_Variables);
        tam_edt = (EditText) findViewById(R.id.valor_Variables);
        val_Matriz = (RelativeLayout) findViewById(R.id.ingresar_matriz);
        matrixA = (TableLayout) findViewById(R.id.matriz_A);
        vectorB = (TableLayout) findViewById(R.id.vector_B);
        continuar = (Button) findViewById(R.id.continuar_btn);

        val_Matriz.setVisibility(View.GONE);
        estado_Tam_Matriz = true;
        //invalidateOptionsMenu();
        tam_Matriz.setVisibility(View.VISIBLE);
    }

    public void crearMatriz_btn(View v) {
        if (!tam_edt.getText().toString().isEmpty()) {
            if (Double.parseDouble(tam_edt.getText().toString()) >= 2) {
                tamano_Matriz = Integer.parseInt(tam_edt.getText().toString());
                tam_Matriz.setVisibility(View.GONE);
                estado_Tam_Matriz = false;
                //invalidateOptionsMenu();
                val_Matriz.setVisibility(View.VISIBLE);
                setInputMatrixView();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_valid_unk_number), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_valid_unk_number), Toast.LENGTH_SHORT).show();
        }
        estoyTabla = true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(estoyTabla) {
            loadView();
            estoyTabla = false;
        } else {
            finish();
        }
        return true;
    }

    public void setInputMatrixView() {
        matrixA.removeAllViews();
        vectorB.removeAllViews();

        //Matrix A.
        for (int i = 0; i < tamano_Matriz; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < tamano_Matriz; j++) {
                EditText cell = new EditText(this);
                cell.setHint("A[" + i + "][" + j + "]");
                cell.setTextSize(15f);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                cell.setGravity(Gravity.CENTER);
                cell.setSingleLine(true);
                if (j > 0) {
                    row.getChildAt(j - 1).setNextFocusDownId(cell.getId());
                    row.getChildAt(j - 1).setNextFocusForwardId(cell.getId());
                    row.addView(cell);
                } else {
                    row.addView(cell);
                }
            }
            matrixA.addView(row, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }

        //Vector B.
        TableRow row = new TableRow(this);
        for (int i = 0; i < tamano_Matriz; i++) {
            EditText cell = new EditText(this);
            cell.setHint("b[" + (i) + "][0]");
            cell.setTextSize(15f);
            cell.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            cell.setGravity(Gravity.CENTER);
            cell.setSingleLine(true);
            if (i > 0) {
                row.getChildAt(i - 1).setNextFocusDownId(cell.getId());
                row.getChildAt(i - 1).setNextFocusForwardId(cell.getId());
                row.addView(cell);
            } else {
                row.addView(cell);
            }
        }
        vectorB.addView(row, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }

    public void continuar_metodos(View v) {
        ArrayList<ArrayList<Double>> matrixAValues = new ArrayList<>();
        ArrayList<Double> vectorBValues = new ArrayList<>();
        boolean isInfoValid = true;

        //Matrix A.
        for (int i = 0; i < tamano_Matriz && isInfoValid; i++) {
            ArrayList<Double> matrixARowValues = new ArrayList<>();  //Save each row
            TableRow row = (TableRow) matrixA.getChildAt(i);
            for (int j = 0; j < tamano_Matriz && isInfoValid; j++) {
                EditText position = (EditText) row.getChildAt(j);
                String positionContent = position.getText().toString();
                if (!positionContent.isEmpty()) {
                    matrixARowValues.add(Double.parseDouble(positionContent));

                } else {
                    Toast.makeText(getApplicationContext(), getResources()
                            .getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
                    isInfoValid = false;
                }
            }
            if (isInfoValid) {  //In each position of the arrayList we save each row.
                matrixAValues.add(matrixARowValues);
            }
        }

        //Vector B.
        TableRow row = (TableRow) vectorB.getChildAt(0);
        for (int j = 0; j < tamano_Matriz && isInfoValid; j++) {
            EditText position = (EditText) row.getChildAt(j);
            String positionContent = position.getText().toString();
            if (!positionContent.isEmpty()) {
                vectorBValues.add(Double.parseDouble(positionContent));

            } else {
                Toast.makeText(getApplicationContext(), getResources()
                        .getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
                isInfoValid = false;
            }
        }

        if (isInfoValid) {  //All data were save successfully

            new Matrix(matrixAValues, vectorBValues);  //Creation of respective matrix.
            Intent openSystemsOfEqs = new Intent(MatrixData.this, EquationsSystems.class);
            //openSystemsOfEqs.putExtra("matrix", matrix);
            startActivity(openSystemsOfEqs);
        }
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

    private void loadView() {
        initViewElements();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
