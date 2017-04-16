package co.edu.eafit.numerico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OneVariableEquations extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_variable_equations);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button incremental_search_btn = (Button) findViewById(R.id.incremental_search_btn);
        Button bisection_btn = (Button) findViewById(R.id.bisection_btn);
        Button false_position_btn = (Button) findViewById(R.id.false_position_btn);
        Button fixed_point_btn = (Button) findViewById(R.id.fixed_point_btn);
        Button newton_btn = (Button) findViewById(R.id.newton_btn);
        Button secant_btn = (Button) findViewById(R.id.secant_btn);
        Button multiple_roots_btn = (Button) findViewById(R.id.multiple_roots_btn);

        incremental_search_btn.setOnClickListener(this);
        bisection_btn.setOnClickListener(this);
        false_position_btn.setOnClickListener(this);
        fixed_point_btn.setOnClickListener(this);
        newton_btn.setOnClickListener(this);
        secant_btn.setOnClickListener(this);
        multiple_roots_btn.setOnClickListener(this);
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

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.incremental_search_btn:
                Intent incrementalSearch = new Intent(this, IncrementalSearch.class);
                startActivity(incrementalSearch);
                break;
            case R.id.bisection_btn:
                Intent bisection = new Intent(this, Bisection.class);
                startActivity(bisection);
                break;
            case R.id.false_position_btn:
//                Intent falsePosition = new Intent(this, FalsePosition.class);
//                startActivity(falsePosition);
                break;
            case R.id.fixed_point_btn:
//                Intent fixedPoint = new Intent(this, FixedPoint.class);
//                startActivity(fixedPoint);
                break;
            case R.id.newton_btn:
//                Intent newtonMethod = new Intent(this, NewtonMethod.class);
//                startActivity(newtonMethod);
                break;
            case R.id.secant_btn:
//                Intent secantMethod = new Intent(this, SecantMethod.class);
//                startActivity(secantMethod);
                break;
            case R.id.multiple_roots_btn:
//                Intent multipleRoots = new Intent(this, MultipleRoots.class);
//                startActivity(multipleRoots);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.graph_UV:
                OneVariableInput.getData();
                if (OneVariableInput.fXTodos.matches("")) {
                    Toast.makeText(this, "F(x) is a must in order to graph the functions", Toast.LENGTH_SHORT).show();
                } else {
                    if (OneVariableInput.dfXTodos.matches("") || OneVariableInput.d2fXTodos.matches("") || OneVariableInput.gXTodos.matches("")) {
                        OneVariableInput.fXTodos = OneVariableInput.fXTodos_edt.getText().toString();

                        if (!OneVariableInput.dfXTodos.matches("")) {
                            OneVariableInput.dfXTodos = OneVariableInput.dfXTodos_edt.getText().toString();
                        } else {
                            OneVariableInput.dfXTodos = null;
                        }

                        if (!OneVariableInput.d2fXTodos.matches("")) {
                            OneVariableInput.d2fXTodos = OneVariableInput.d2fXTodos_edt.getText().toString();
                        } else {
                            OneVariableInput.d2fXTodos = null;
                        }

                        if (!OneVariableInput.gXTodos.matches("")) {
                            OneVariableInput.gXTodos = OneVariableInput.gXTodos_edt.getText().toString();
                        } else {
                            OneVariableInput.gXTodos = null;
                        }
                        Toast.makeText(this, "Only functions wrote down will be graphed", Toast.LENGTH_LONG).show();
                        grapher("AlgunasFunciones");

                    } else {
                        OneVariableInput.getData();
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
