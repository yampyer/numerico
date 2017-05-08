package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button oneVariableEquations_btn = (Button) findViewById(R.id.oneVariableEquations_btn);
        Button equationSystems_btn = (Button) findViewById(R.id.equationSystems_btn);
        Button interpolation_btn = (Button) findViewById(R.id.interpolation_btn);
        oneVariableEquations_btn.setOnClickListener(this);
        equationSystems_btn.setOnClickListener(this);
        interpolation_btn.setOnClickListener(this);
    }

    public void onClick(final View v) {
        switch(v.getId()) {
            case R.id.oneVariableEquations_btn:
                Intent oneVariableEquations = new Intent(this, OneVariableInput.class);
                startActivity(oneVariableEquations);
                break;
            case R.id.equationSystems_btn:
                Intent equationSystems = new Intent(this, MatrixData.class);
                startActivity(equationSystems);
                break;
            case R.id.interpolation_btn:
                Intent interpolation = new Intent(this, OneVariableInput.class);
                startActivity(interpolation);
                break;
        }
    }

}
