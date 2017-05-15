package co.edu.eafit.numerico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OneVariableInput extends AppCompatActivity implements View.OnClickListener {

    static EditText fXTodos_edt;
    static EditText dfXTodos_edt;
    static EditText d2fXTodos_edt;
    static EditText gXTodos_edt;

    static String fXTodos;
    static String dfXTodos;
    static String d2fXTodos;
    static String gXTodos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_variable_input);

        fXTodos_edt = (EditText) findViewById(R.id.fxTodos_edt);
        dfXTodos_edt = (EditText) findViewById(R.id.dfxTodos_edt);
        d2fXTodos_edt = (EditText) findViewById(R.id.d2fxTodos_edt);
        gXTodos_edt = (EditText) findViewById(R.id.gxTodos_edt);

        Button continue_btn = (Button) findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.one_variable_data, menu);
        return true;
    }

    public static void getData() {
        fXTodos = fXTodos_edt.getText().toString();
        dfXTodos = dfXTodos_edt.getText().toString();
        d2fXTodos = d2fXTodos_edt.getText().toString();
        gXTodos = gXTodos_edt.getText().toString();
    }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                getData();

                if (fXTodos.matches("") || dfXTodos.matches("") || d2fXTodos.matches("") || gXTodos.matches("")) {
                    Message("If you don't enter the functions, you'll have to enter them by method");
                    return;
                } else {
                    salto();
                }
                break;
            default:
                break;

        }
    }

    public void Message(String s) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage(s);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                salto();
            }
        });
        dialog.setNegativeButton("Enter them",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }

    void salto() {
        Intent oneVariable = new Intent(this, OneVariableEquations.class);
        startActivity(oneVariable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.graph_UV:
                getData();
                if (fXTodos.matches("")) {
                    Toast.makeText(this, "F(x) is a must in order to graph the functions", Toast.LENGTH_SHORT).show();
                } else {
                    if (dfXTodos.matches("") || d2fXTodos.matches("") || gXTodos.matches("")) {
                        fXTodos = fXTodos_edt.getText().toString();

                        if (!dfXTodos.matches("")) {
                            dfXTodos = dfXTodos_edt.getText().toString();
                        } else {
                            dfXTodos = null;
                        }

                        if (!d2fXTodos.matches("")) {
                            d2fXTodos = d2fXTodos_edt.getText().toString();
                        } else {
                            d2fXTodos = null;
                        }

                        if (!gXTodos.matches("")) {
                            gXTodos = gXTodos_edt.getText().toString();
                        } else {
                            gXTodos = null;
                        }
                        Toast.makeText(this, "Only functions wrote down will be graphed", Toast.LENGTH_LONG).show();
                        grapher("AlgunasFunciones");

                    } else {
                        getData();
                        grapher("DatosUnaVariable");
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void grapher(String param) {
        Intent grapher = new Intent(this, Grapher.class);
        grapher.putExtra("Uniqid", param);
        startActivity(grapher);
    }
}
