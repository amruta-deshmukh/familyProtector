package com.termproject.familyprotector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParentProfileSettings extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutName, linearLayoutEmail, linearLayoutPassword, linearLayoutSecAnswer;
    private TextView textProfileName, textProfileEmail, textProfileSecQues, textProfileSecAnswer;
    private User loggedUser;
    private UserLocalStore userLocalStore;
    private String emailAddress, profileNameStr, password, secAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile_settings);
        userLocalStore = new UserLocalStore(this);
        loggedUser = userLocalStore.getLoggedInUser();
        emailAddress = loggedUser.getUsername();
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        linearLayoutName = (LinearLayout) findViewById(R.id.linear_name);
        linearLayoutEmail = (LinearLayout) findViewById(R.id.linear_email);
        linearLayoutPassword = (LinearLayout) findViewById(R.id.linear_password);
        linearLayoutSecAnswer = (LinearLayout) findViewById(R.id.linear_security_ans);
        textProfileName = (TextView) findViewById(R.id.text_parent_profile_name);
        textProfileEmail = (TextView) findViewById(R.id.text_parent_profile_email);
        textProfileSecQues = (TextView) findViewById(R.id.text_parent_profile_sec_question);
        textProfileSecAnswer = (TextView) findViewById(R.id.text_parent_profile_sec_ans);
        linearLayoutName.setOnClickListener(this);
        linearLayoutEmail.setOnClickListener(this);
        linearLayoutPassword.setOnClickListener(this);
        linearLayoutSecAnswer.setOnClickListener(this);
        getProfileDetails();
    }


    private void getProfileDetails() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    profileNameStr = parseObject.getString("fullname");
                    textProfileName.setText(profileNameStr);
                    textProfileEmail.setText(parseObject.getString("username"));
                    password = parseObject.getString("password");
                    textProfileSecQues.setText(parseObject.getString("secQuestion"));
                    secAnswer = parseObject.getString("secAnswer");

                }
            }
        });

    }

    private void updateProfileNameOnParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("fullname", profileNameStr);
                    textProfileName.setText(parseObject.getString("fullname"));
                    parseObject.saveInBackground();

                }
            }
        });

    }

    private void updateProfilePasswordOnParse(String newPassword) {
        final String newPass = newPassword;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("password", newPass);
                    parseObject.saveInBackground();

                }
            }
        });

    }

    private void updateProfileSecAnsOnParse(String newAnswer) {
        final String newAns = newAnswer;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("secAnswer", newAns);
                    parseObject.saveInBackground();

                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_name:
                AlertDialog.Builder alertDialogProfileName = new AlertDialog.Builder(this);
                LinearLayout layout = new LinearLayout(this);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);
                layout.setPadding(40, 40, 40, 40);
                final EditText etProfileName = new EditText(this);
                etProfileName.setText(profileNameStr);
                etProfileName.setPadding(40, 40, 40, 40);
                layout.addView(etProfileName, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                alertDialogProfileName.setView(layout);
                alertDialogProfileName.setTitle(R.string.dialog_title_profile_name);
                alertDialogProfileName
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                profileNameStr = etProfileName.getText().toString();
                                updateProfileNameOnParse();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogProfileName.create();
                alertDialog.show();
                break;
            case R.id.linear_email:
                Toast.makeText(this, "The Email address of account cannot be changed.", Toast.LENGTH_LONG).show();
                break;
            case R.id.linear_password:

                LinearLayout layoutPassword = new LinearLayout(this);
                LinearLayout.LayoutParams passwordParms = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutPassword.setOrientation(LinearLayout.VERTICAL);
                layoutPassword.setLayoutParams(passwordParms);
                layoutPassword.setPadding(40, 40, 40, 40);
                final EditText etProfilepassword = new EditText(this);
                etProfilepassword.setPadding(40, 40, 40, 40);
                etProfilepassword.setHint("Enter current password");
                etProfilepassword.setHintTextColor(getResources().getColor(R.color.gray));
                etProfilepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.
                        TYPE_TEXT_VARIATION_PASSWORD);
                layoutPassword.addView(etProfilepassword, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final EditText etProfileNewpassword1 = new EditText(this);
                etProfileNewpassword1.setPadding(40, 40, 40, 40);
                etProfileNewpassword1.setHint("Enter new password");
                etProfileNewpassword1.setHintTextColor(getResources().getColor(R.color.gray));
                etProfileNewpassword1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.
                        TYPE_TEXT_VARIATION_PASSWORD);
                layoutPassword.addView(etProfileNewpassword1, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final EditText etProfileNewpassword2 = new EditText(this);
                etProfileNewpassword2.setPadding(40, 40, 40, 40);
                etProfileNewpassword2.setHint("Enter new password again");
                etProfileNewpassword2.setHintTextColor(getResources().getColor(R.color.gray));
                etProfileNewpassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.
                        TYPE_TEXT_VARIATION_PASSWORD);
                layoutPassword.addView(etProfileNewpassword2, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(layoutPassword)
                        .setTitle(R.string.dialog_title_profile_password)
                        .setPositiveButton("Update", null) //Set to null. We override the onclick
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                String passwordEntered = etProfilepassword.getText().toString();
                                String newPassword1 = etProfileNewpassword1.getText().toString();
                                String newPassword2 = etProfileNewpassword2.getText().toString();
                                if (passwordEntered.matches(password)) {
                                    if (newPassword1.matches(newPassword2)) {
                                        if(!newPassword1.matches("")) {
                                            updateProfilePasswordOnParse(newPassword1);
                                            dialog.dismiss();
                                        }
                                        else{
                                            etProfileNewpassword1.setError("New password cannot be blank");
                                            etProfileNewpassword2.setError("New password cannot be blank");
                                        }


                                    } else {
                                        etProfileNewpassword1.setError("Both passwords don't match");
                                        etProfileNewpassword2.setError("Both passwords don't match");
                                    }
                                } else {
                                    etProfilepassword.setError("Password is incorrect.");
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.linear_security_ans:
                LinearLayout layoutSecAnswer = new LinearLayout(this);
                LinearLayout.LayoutParams secAnsParms = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutSecAnswer.setOrientation(LinearLayout.VERTICAL);
                layoutSecAnswer.setLayoutParams(secAnsParms);
                layoutSecAnswer.setPadding(40, 40, 40, 40);
                final EditText etProfileanswer = new EditText(this);
                etProfileanswer.setPadding(40, 40, 40, 40);
                etProfileanswer.setHint("Enter current security answer");
                etProfileanswer.setHintTextColor(getResources().getColor(R.color.gray));
                etProfileanswer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.
                        TYPE_TEXT_VARIATION_PASSWORD);
                layoutSecAnswer.addView(etProfileanswer, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final EditText etProfileNewAns = new EditText(this);
                etProfileNewAns.setPadding(40, 40, 40, 40);
                etProfileNewAns.setHint("Enter new password");
                etProfileNewAns.setHintTextColor(getResources().getColor(R.color.gray));
                etProfileNewAns.setInputType(InputType.TYPE_CLASS_TEXT | InputType.
                        TYPE_TEXT_VARIATION_PASSWORD);
                layoutSecAnswer.addView(etProfileNewAns, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                final AlertDialog dialogSecAnswer = new AlertDialog.Builder(this)
                        .setView(layoutSecAnswer)
                        .setTitle(R.string.dialog_title_profile_sec_ans)
                        .setPositiveButton("Update", null) //Set to null. We override the onclick
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                dialogSecAnswer.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                String ansEntered = etProfileanswer.getText().toString();
                                String newAnsEntered = etProfileNewAns.getText().toString();
                                if (ansEntered.matches(secAnswer)) {
                                    if(!newAnsEntered.matches("")) {
                                        updateProfileSecAnsOnParse(newAnsEntered);
                                        dialog.dismiss();
                                    }else{
                                        etProfileNewAns.setError("Security Answer cannot be blank.");
                                    }
                                    } else {
                                    etProfileanswer.setError("Answer is incorrect.");
                                }
                            }
                        });
                    }
                });
                dialogSecAnswer.show();
                break;
        }
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
