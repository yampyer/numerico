package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import utils.SessionManager;

public class EquationsSystems extends AppCompatActivity implements View.OnClickListener {

  RadioGroup phases;
  private SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equations_systems);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Button gaussian_elimination_btn = (Button) findViewById(R.id.gaussian_elimination_btn);
    Button cholesky_btn = (Button) findViewById(R.id.cholesky_btn);
    Button crout_btn = (Button) findViewById(R.id.crout_btn);
    Button doolittle_btn = (Button) findViewById(R.id.doolittle_btn);
    Button iterative_methods_btn = (Button) findViewById(R.id.iterative_methods_btn);

    phases = (RadioGroup) findViewById(R.id.phaseSelector);
    session = SessionManager.getInstance(getApplicationContext());

    gaussian_elimination_btn.setOnClickListener(this);
    cholesky_btn.setOnClickListener(this);
    crout_btn.setOnClickListener(this);
    doolittle_btn.setOnClickListener(this);
    iterative_methods_btn.setOnClickListener(this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.gaussian_elimination_btn:
        checkPhases();
        Intent gaussianElimination = new Intent(this, GaussianEliminationVariants.class);
        startActivity(gaussianElimination);
        break;
      case R.id.doolittle_btn:
        checkPhases();
        Intent doolittle = new Intent(this, Doolittle.class);
        startActivity(doolittle);
        break;
      case R.id.crout_btn:
        checkPhases();
        Intent crout = new Intent(this, Crout.class);
        startActivity(crout);
        break;
      case R.id.cholesky_btn:
        checkPhases();
        Intent cholesky = new Intent(this, Cholesky.class);
        startActivity(cholesky);
        break;
      case R.id.iterative_methods_btn:
        Intent iterativeMethods = new Intent(this, IterativeMethods.class);
        startActivity(iterativeMethods);
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

  private void checkPhases() {
    if (phases.getCheckedRadioButtonId() == R.id.withoutPhasesButton) {
      MatrixData.withoutPhases = true;
      MatrixData.withPhases = false;
    } else {
      MatrixData.withoutPhases = false;
      MatrixData.withPhases = true;
    }
  }

}
