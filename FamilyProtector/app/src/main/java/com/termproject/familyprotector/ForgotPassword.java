package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    private Button btnEnter,btnLoginReturn,btnEnterAnswer;
    private String emailAddress, secretAnswer;
    private EditText etEmail,etSecurityAnswer;
    private TextView textEmailNotRegistered, textSecurityTitle, textSecurityQuestion,
            textPasswordString, textPassword, textAnswerIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btnEnter = (Button)findViewById(R.id.btn_submit_email);
        btnLoginReturn = (Button)findViewById(R.id.btn_back_to_login);
        btnEnterAnswer = (Button)findViewById(R.id.btn_submit_secret);
        etEmail = (EditText)findViewById(R.id.edit_forgot_email);
        etSecurityAnswer = (EditText)findViewById(R.id.edit_secret_answer);
        textEmailNotRegistered = (TextView)findViewById(R.id.text_email_not_registered);
        textSecurityQuestion = (TextView)findViewById(R.id.text_security_question);
        textSecurityTitle = (TextView)findViewById(R.id.text_security_title);
        textPasswordString = (TextView)findViewById(R.id.text_password_string);
        textPassword = (TextView)findViewById(R.id.text_password_value);
        textAnswerIncorrect = (TextView)findViewById(R.id.text_answer_incorrect);

        btnEnter.setOnClickListener(this);
        btnEnterAnswer.setOnClickListener(this);
        btnLoginReturn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_email:
                emailAddress = etEmail.getText().toString();
                if(emailAddress.matches("")){
                    etEmail.setError("Email address is required");
                }
                else{
                    checkEmailAddress();
                }

                break;
            case R.id.btn_submit_secret:
                secretAnswer = etSecurityAnswer.getText().toString().toLowerCase();
                if(secretAnswer.matches("")){
                    etSecurityAnswer.setError("Please enter a valid answer");
                }
                else{
                    checkSecurityAnswer();
                }

                break;
            case R.id.btn_back_to_login:
                startActivity(new Intent(this,Login.class));
                break;
        }
    }


    private void checkEmailAddress(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    textEmailNotRegistered.setVisibility(View.VISIBLE);
                    textSecurityQuestion.setVisibility(View.INVISIBLE);
                    etSecurityAnswer.setVisibility(View.INVISIBLE);
                } else {

                    String sec_question = parseObject.getString("secQuestion");
                    textEmailNotRegistered.setVisibility(View.INVISIBLE);
                    textSecurityQuestion.setText(sec_question);
                    textSecurityTitle.setVisibility(View.VISIBLE);
                    textSecurityQuestion.setVisibility(View.VISIBLE);
                    etSecurityAnswer.setVisibility(View.VISIBLE);
                    btnEnter.setVisibility(View.GONE);
                    btnEnterAnswer.setVisibility(View.VISIBLE);




                }
            }
        });

    }


    private void checkSecurityAnswer(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("secAnswer",secretAnswer);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    textAnswerIncorrect.setVisibility(View.VISIBLE);
                    textPasswordString.setVisibility(View.INVISIBLE);
                    textPassword.setVisibility(View.INVISIBLE);
                } else {

                    String password = parseObject.getString("password");
                    textAnswerIncorrect.setVisibility(View.INVISIBLE);

                    textPassword.setText(password);
                    textPasswordString.setVisibility(View.VISIBLE);
                    textPassword.setVisibility(View.VISIBLE);
                    btnEnter.setVisibility(View.GONE);
                    btnEnterAnswer.setVisibility(View.GONE);
                    btnLoginReturn.setVisibility(View.VISIBLE);




                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                startActivity(new Intent(this, Login.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
