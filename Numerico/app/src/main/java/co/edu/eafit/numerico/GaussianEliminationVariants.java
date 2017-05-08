package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class GaussianEliminationVariants extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaussian_elimination_variants);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button without_pivot_btn = (Button) findViewById(R.id.without_pivot_btn);
        Button partial_pivoting_btn = (Button) findViewById(R.id.partial_pivoting_btn);
        Button total_pivoting_btn = (Button) findViewById(R.id.total_pivoting_btn);

        without_pivot_btn.setOnClickListener(this);
        partial_pivoting_btn.setOnClickListener(this);
        total_pivoting_btn.setOnClickListener(this);
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
        }
    }

}
