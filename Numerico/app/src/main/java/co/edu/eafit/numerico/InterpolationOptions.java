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

public class InterpolationOptions extends AppCompatActivity implements View.OnClickListener  {

  private SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interpolation_options);

    Button newton_btn = (Button) findViewById(R.id.inter_Newton);
    newton_btn.setOnClickListener(this);
    Button lagrange_btn = (Button) findViewById(R.id.inter_Lagrange);
    lagrange_btn.setOnClickListener(this);
    Button lineal_btn = (Button) findViewById(R.id.lineal_spline);
    lineal_btn.setOnClickListener(this);
    Button cubic_spline = (Button) findViewById(R.id.cubic_spline);
    cubic_spline.setOnClickListener(this);
    Button neville_btn = (Button) findViewById(R.id.btn_neville);
    neville_btn.setOnClickListener(this);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    session = SessionManager.getInstance(getApplicationContext());
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  public void onClick(final View v) {
    switch(v.getId()) {
      case R.id.inter_Newton:
        Intent newton = new Intent(this, NewtonInterpolation.class);
        startActivity(newton);
        break;
      case R.id.inter_Lagrange:
        Intent lagrange = new Intent(this, Lagrange.class);
        startActivity(lagrange);
        break;
      case R.id.lineal_spline:
        Intent lineal = new Intent(this, LinealSpline.class);
        startActivity(lineal);
        break;
      case R.id.cubic_spline:
        Intent cubic = new Intent(this, NaturalSpline.class);
        startActivity(cubic);
        break;
      case R.id.btn_neville:
        Intent neville = new Intent(this, Neville.class);
        startActivity(neville);
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
