package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etUsername, etPassword, etFullName;
    String username, password, fullName;
    TextView textSignIn;
    User registeredUser;
    User messageObj;
    UserLocalStore userLocalStore;
    Pattern pattern;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        userLocalStore = new UserLocalStore(this);
        messageObj = new User("");

        bRegister.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
        String regex = "^(.+)@(.+)$";
        pattern = Pattern.compile(regex);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    private void init() {
        bRegister = (Button) findViewById(R.id.bRegister);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        textSignIn = (TextView) findViewById(R.id.text_sign_in);

    }

    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bRegister:
                fullName = etFullName.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                int blankFieldCheck = checkForBlankFields();
                if (blankFieldCheck != 0) {
                    switch (blankFieldCheck) {
                        case 7:
                            etFullName.setError("Please enter a name for the account");
                            etUsername.setError("Email address is required");
                            etPassword.setError("Password id required");
                            break;
                        case 6:
                            etUsername.setError("Email address is required");
                            etPassword.setError("Password id required");
                            break;
                        case 5:
                            etFullName.setError("Please enter a name for the account");
                            etPassword.setError("Password id required");
                            break;
                        case 4:
                            etFullName.setError("Please enter a name for the account");
                            etUsername.setError("Email address is required");
                            break;
                        case 3:
                            etPassword.setError("Password id required");
                            break;
                        case 2:
                            etUsername.setError("Email address is required");
                            break;
                        case 1:
                            etFullName.setError("Please enter a name for the account");
                            break;
                        default:
                            Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show();
                            break;
                    }
                } else if (checkEmailFormat()) {
                    etUsername.setError("Please enter valid  email address");
//                    Toast.makeText(this,"Please enter valid  email address",Toast.LENGTH_LONG).show();
                } else if (checkforDuplicateEmails()) {
                    etUsername.setError("Email address already in use.");
//                    Toast.makeText(this,"Email Address already registered",Toast.LENGTH_LONG).show();
                } else {
                    storeToParse();
                }
                break;
            case R.id.text_sign_in:
                userLocalStore.setUserRegistered(true);
                startActivity(new Intent(this, Login.class));
                break;
        }
    }


    private int checkForBlankFields() {
        if (fullName.matches("")) {
            return 1;
        } else if (username.matches("")) {
            return 2;
        } else if (password.matches("")) {
            return 3;
        } else if (fullName.matches("") && username.matches("")) {
            return 4;
        } else if (fullName.matches("") && password.matches("")) {
            return 5;
        } else if (username.matches("") && password.matches("")) {
            return 6;
        } else if (fullName.matches("") && username.matches("") && password.matches("")) {
            return 7;
        }
        return 0;
    }

    private boolean checkEmailFormat() {
        Matcher matcher = pattern.matcher(username);
        if (matcher.matches() == false) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkforDuplicateEmails() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    if (users.size() > 0) {
                        messageObj.setMessage("Sorry, email address already registered!!");

                    } else {
                        messageObj.setMessage(null);

                    }
                } else {
                    Log.d("Login", "Error: " + e.getMessage());
                }

            }
        });
        if (messageObj.getMessage() != null) {
            return true;
        } else {
            return false;
        }
    }

    private void storeToParse() {

        fullName = fullName.substring(0, 1).toUpperCase() + fullName.substring(1);
        ParseObject userCredentials = new ParseObject("UserCredentials");
        userCredentials.put("fullname", fullName);
        userCredentials.put("username", username);
        userCredentials.put("password", password);
        userCredentials.saveInBackground();
        registeredUser = new User(username, password);
        userLocalStore.storeUserData(registeredUser);
        userLocalStore.setUserLoggedIn(true);
        userLocalStore.setUserRegistered(true);
        startActivity(new Intent(this, ChooseMode.class));

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

}
