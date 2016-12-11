package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button bLogin;
    EditText etUsername, etPassword;
    String username, password;
    User loggenInUser;
    UserLocalStore userLocalStore;
    TextView txtForgotPass,txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        userLocalStore = new UserLocalStore(this);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        bLogin.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
    }

    private void init() {
        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        txtForgotPass = (TextView)findViewById(R.id.text_forgot_password);
        txtRegister = (TextView)findViewById(R.id.text_register);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
//                if (checkForBlankFields()) {
//
//
//                    Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show();
//
                if (username.matches("")) {
                    etUsername.setError("Email cannot be blank");

                } else if (password.matches("")) {
                    etPassword.setError("Password cannot be blank");

                } else {
                    checkCredentials();
                }
                break;
            case R.id.text_forgot_password:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
            case R.id.text_register:
                startActivity(new Intent(this,Register.class));
                break;
        }
    }

    private boolean checkForBlankFields() {
        if (username.matches("") || password.matches("")) {
            return true;

        } else {

            return false;
        }
    }

    private void checkCredentials() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", username);
        query.whereEqualTo("password", password);


        query.getFirstInBackground( new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Toast.makeText(Login.this, "Invalid credentials please try again", Toast.LENGTH_LONG).show();
                } else {
                    loggenInUser = new User(username, password);
                    userLocalStore.setUserRegistered(true);
                    userLocalStore.storeUserData(loggenInUser);
                    userLocalStore.setUserLoggedIn(true);
                    startActivity(new Intent(Login.this, ChooseMode.class));

                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                startActivity(new Intent(this,WelcomePageTutorial.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
