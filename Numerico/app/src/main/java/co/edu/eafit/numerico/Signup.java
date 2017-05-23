package co.edu.eafit.numerico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import API.NumericoAPI;
import API.ServerAPI;
import butterknife.ButterKnife;
import butterknife.Bind;
import models.Token;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.SessionManager;

public class Signup extends AppCompatActivity {
    private static final String TAG = "Signup";
    private NumericoAPI serverAPI;
    private String tokenPlayer;
    private String idUser;
    private SessionManager session;
    private ProgressDialog progressDialog;

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        serverAPI = ServerAPI.getInstance();
        session = SessionManager.getInstance(getApplicationContext());
        progressDialog = new ProgressDialog(Signup.this);

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();

            //token
            tokenPlayer = user.get(SessionManager.KEY_TOKEN);

            //id
            idUser = user.get(SessionManager.KEY_ID);

            // User is already logged in. Take him to main activity   <-- Change for main activity
            onSignupSuccess();
        }
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        signUp(name, email, password);
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    private void signUp(final String mUsername, final String mEmail, final String mPassword) {

        User userJson = new User(mUsername, mEmail, mPassword);
        Call<User> call = serverAPI.newUser(userJson);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    User player = new User(mEmail, mPassword, true);
                    Call<Token> callLogin = serverAPI.login(player);
                    callLogin.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            progressDialog.dismiss();

                            if (response.isSuccessful()) {
                                tokenPlayer = response.body().getId();
                                idUser = response.body().getUserId();
                                session.createLoginSession(idUser, tokenPlayer);
                                onSignupSuccess();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    onSignupFailed();
                }
                Log.i("code", "" + response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
