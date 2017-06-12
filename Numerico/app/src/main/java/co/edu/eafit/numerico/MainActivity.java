package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import utils.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private SessionManager session;

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
    session = SessionManager.getInstance(getApplicationContext());
  }

  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.oneVariableEquations_btn:
        Intent oneVariableEquations = new Intent(this, OneVariableInput.class);
        startActivity(oneVariableEquations);
        break;
      case R.id.equationSystems_btn:
        Intent equationSystems = new Intent(this, MatrixData.class);
        startActivity(equationSystems);
        break;
      case R.id.interpolation_btn:
        Intent interpolation = new Intent(this, Interpolation.class);
        startActivity(interpolation);
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.help_logout, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.help_logout:
        session.logoutUser();
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}
