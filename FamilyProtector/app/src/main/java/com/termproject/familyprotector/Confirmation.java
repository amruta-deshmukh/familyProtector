package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Confirmation extends AppCompatActivity implements View.OnClickListener{
    Button bLogout;
    EditText etUsername, etPassword;
    String username, password;
    UserLocalStore userLocalStore;
    User loggenInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        final ActionBar actionBar = getSupportActionBar();
        userLocalStore = new UserLocalStore(this);
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        bLogout = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.confirmationUsername);
        etPassword = (EditText) findViewById(R.id.confirmationPassword);
        bLogout = (Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogout:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if (checkForBlankFields()) {

                    Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show();

                } else {
                    checkCredentials();
                }
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


        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Toast.makeText(Confirmation.this, "Invalid credentials please try again", Toast.LENGTH_LONG).show();
                } else {
                    userLocalStore.setUserLoggedIn(false);
                    userLocalStore.setChildForThisPhone("");
                    startActivity(new Intent(Confirmation.this, Login.class));

                }
            }
        });

    }



}
