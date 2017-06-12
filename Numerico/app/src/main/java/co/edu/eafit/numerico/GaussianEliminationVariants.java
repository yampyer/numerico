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

public class GaussianEliminationVariants extends AppCompatActivity implements View.OnClickListener {

  private SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gaussian_elimination_variants);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    session = SessionManager.getInstance(getApplicationContext());

    Button without_pivot_btn = (Button) findViewById(R.id.without_pivot_btn);
    Button partial_pivoting_btn = (Button) findViewById(R.id.partial_pivoting_btn);
    Button total_pivoting_btn = (Button) findViewById(R.id.total_pivoting_btn);
    Button scaled_pivoting_btn = (Button) findViewById(R.id.scaled_pivoting_btn);

    without_pivot_btn.setOnClickListener(this);
    partial_pivoting_btn.setOnClickListener(this);
    total_pivoting_btn.setOnClickListener(this);
    scaled_pivoting_btn.setOnClickListener(this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.without_pivot_btn:
        Intent withoutPivoting = new Intent(this, SimpleGaussianElimination.class);
        startActivity(withoutPivoting);
        break;
      case R.id.partial_pivoting_btn:
        Intent partialPivoting = new Intent(this, PartialPivoting.class);
        startActivity(partialPivoting);
        break;
      case R.id.total_pivoting_btn:
        Intent totalPivoting = new Intent(this, TotalPivoting.class);
        startActivity(totalPivoting);
        break;
      case R.id.scaled_pivoting_btn:
        Intent scaledPivoting = new Intent(this, ScaledPivoting.class);
        startActivity(scaledPivoting);
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
