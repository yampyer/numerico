package co.edu.eafit.numerico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import models.Method;

public class History extends AppCompatActivity {

  private List<Method> methodList = new ArrayList<>();
  private RecyclerView recyclerView;
  private HistoryAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    recyclerView = (RecyclerView) findViewById(R.id.rv);
    recyclerView.setHasFixedSize(true);


    mAdapter = new HistoryAdapter(methodList);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);

    prepareHistoryData();
  }

  private void prepareHistoryData() {
    Method method = new Method("Function", "Method", "2015");
    methodList.add(method);

    mAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

}
