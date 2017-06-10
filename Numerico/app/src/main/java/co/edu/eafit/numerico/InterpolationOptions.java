package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class InterpolationOptions extends AppCompatActivity implements View.OnClickListener  {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interpolation_options);

    Button newton_btn = (Button) findViewById(R.id.inter_Newton);
    newton_btn.setOnClickListener(this);
    Button lagrange_btn = (Button) findViewById(R.id.inter_Lagrange);
    lagrange_btn.setOnClickListener(this);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }
  }
}
