package com.termproject.familyprotector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParentProfileSettings extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutName;
    private TextView textProfileName;
    private User loggedUser;
    private UserLocalStore userLocalStore;
    private String emailAddress, profileNameStr;

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
        textProfileName = (TextView) findViewById(R.id.text_parent_profile_name);
        linearLayoutName.setOnClickListener(this);
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

                }
            }
        });

    }

    private void updateProfileNameOnParse( String newprofileNameStr) {
        final String newprofileName = newprofileNameStr;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");
        query.whereEqualTo("username", emailAddress);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("fullname", newprofileName);
                    textProfileName.setText(parseObject.getString("fullname"));
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

                                String newprofileNameStr = etProfileName.getText().toString();
                                updateProfileNameOnParse(newprofileNameStr);

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
