package co.edu.eafit.numerico;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import API.NumericoAPI;
import API.ServerAPI;
import models.Method;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SessionManager;

public class History extends AppCompatActivity {

  private List<Method> methodList = new ArrayList<>();
  private RecyclerView recyclerView;
  private HistoryAdapter mAdapter;
  private SessionManager session;
  private ProgressDialog progressDialog;
  private NumericoAPI serverAPI;
  private String tokenPlayer;
  private String idUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    recyclerView = (RecyclerView) findViewById(R.id.rv);

    mAdapter = new HistoryAdapter(methodList);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);

    serverAPI = ServerAPI.getInstance();
    session = SessionManager.getInstance(getApplicationContext());
    progressDialog = new ProgressDialog(History.this);

    // Check if user is already logged in or not
    if (session.isLoggedIn()) {
      HashMap<String, String> user = session.getUserDetails();

      //token
      tokenPlayer = user.get(SessionManager.KEY_TOKEN);

      //id
      idUser = user.get(SessionManager.KEY_ID);
    }

    getHistory();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.help_logout, menu);
    return true;
  }

  /**
   * Represents an asynchronous logger task used to save operations
   */
  private void getHistory() {

    String filter = "{\"where\": {\"userId\":\"" + idUser + "\"}}";
    Call<List<Method>> getHistory = serverAPI.getMethods(filter);
    getHistory.enqueue(new Callback<List<Method>>() {
      @Override
      public void onResponse(Call<List<Method>> call, Response<List<Method>> response) {
        for (int i = 0; i < response.body().size(); i++) {
          Method method;
          if(response.body().get(i).getSecondFunction() == null) {
            method = new Method(response.body().get(i).getFunction(), response.body().get(i).getMethod(), response.body().get(i).getUserId(), response.body().get(i).getDate());
          } else if(response.body().get(i).getThirdFunction() == null) {
            method = new Method(response.body().get(i).getFunction(), response.body().get(i).getSecondFunction(), response.body().get(i).getMethod(), response.body().get(i).getUserId(), response.body().get(i).getDate());
          } else {
            method = new Method(response.body().get(i).getFunction(), response.body().get(i).getSecondFunction(), response.body().get(i).getThirdFunction(), response.body().get(i).getMethod(), response.body().get(i).getUserId(), response.body().get(i).getDate());
          }
          methodList.add(method);
        }
        mAdapter.notifyDataSetChanged();
      }

      @Override
      public void onFailure(Call<List<Method>> call, Throwable t) {
      }
    });
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

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

}
