package com.termproject.familyprotector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildProfileAccSettings extends AppCompatActivity implements View.OnClickListener {

    private String childNameStr, parseChildName, parseChildGender, parseChildDob,
            emailAddress, dialogchildNameStr, dialogChildGenderStr, dialogChildDobStr;
    private UserLocalStore userLocalStore;
    private User loggedUser;
    private LinearLayout linearLayoutChildName, linearLayoutChildGender, linearLayoutChildDob;
    private TextView textChildProfileName, textChildProfileGender, textChildProfileDob;
    private Button btnDeleteAccount;
    private int childProfileYear, childProfileMonth, childProfileDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile_acc_settings);
        userLocalStore = new UserLocalStore(this);
        loggedUser = userLocalStore.getLoggedInUser();
        emailAddress = loggedUser.getUsername();
        init();
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        childNameStr = userLocalStore.getChildDetails();
        setTitle(childNameStr+"'s Profile Settings");
        linearLayoutChildName.setOnClickListener(this);
        linearLayoutChildGender.setOnClickListener(this);
        linearLayoutChildDob.setOnClickListener(this);
        btnDeleteAccount.setOnClickListener(this);
        getChildProfileDetails();
    }


    private void init() {
        linearLayoutChildName = (LinearLayout) findViewById(R.id.linear_child_name);
        linearLayoutChildGender = (LinearLayout) findViewById(R.id.linear_child_gender);
        linearLayoutChildDob = (LinearLayout) findViewById(R.id.linear_child_dob);
        textChildProfileName = (TextView) findViewById(R.id.text_child_profile_name);
        textChildProfileGender = (TextView) findViewById(R.id.text_child_profile_gender);
        textChildProfileDob = (TextView) findViewById(R.id.text_child_profile_dob);
        btnDeleteAccount = (Button) findViewById(R.id.btn_delete_child_account);
    }

    private void getChildProfileDetails(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("name",childNameStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseChildName = parseObject.getString("name");
                    textChildProfileName.setText(parseChildName);
                    parseChildGender = parseObject.getString("gender");
                    textChildProfileGender.setText(parseChildGender);
                    parseChildDob = parseObject.getString("birthdate");
                    textChildProfileDob.setText(parseChildDob);
                    String[] childDobArr = parseChildDob.split("-");
                    childProfileMonth = (Integer.parseInt(childDobArr[0]))-1;
                    childProfileDate = Integer.parseInt(childDobArr[1]);
                    childProfileYear = Integer.parseInt(childDobArr[2]);

                }
            }
        });

    }

    private void updateProfileChildNameOnParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("name",childNameStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("name", dialogchildNameStr);
                    parseObject.saveInBackground();
                    textChildProfileName.setText(dialogchildNameStr);
                    setTitle(dialogchildNameStr + "'s Profile Settings");
                    userLocalStore.setChildDetails(dialogchildNameStr);
                    childNameStr = dialogchildNameStr;
                    parseChildName = dialogchildNameStr;

                }
            }
        });

    }

    private void updateProfileChildGenderOnParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("name",childNameStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("gender", dialogChildGenderStr);
                    parseObject.saveInBackground();
                    textChildProfileGender.setText(dialogChildGenderStr);
                    parseChildGender = dialogChildGenderStr;

                }
            }
        });

    }

    private void updateProfileChildDobOnParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("name",childNameStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    parseObject.put("birthdate", dialogChildDobStr);
                    parseObject.saveInBackground();
                    textChildProfileDob.setText(dialogChildDobStr);
                    parseChildDob = dialogChildDobStr;
                    String[] childDobArr = parseChildDob.split("-");
                    childProfileMonth = (Integer.parseInt(childDobArr[0]))-1;
                    childProfileDate = Integer.parseInt(childDobArr[1]);
                    childProfileYear = Integer.parseInt(childDobArr[2]);

                }
            }
        });

    }

    private void deleteProfileChildOnParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", emailAddress);
        query.whereEqualTo("name",childNameStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    try {
                        parseObject.delete();
                        parseObject.saveInBackground();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    parseObject.saveInBackground();

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_child_name:

                LinearLayout childNameLayout = new LinearLayout(this);
                LinearLayout.LayoutParams childNameParms = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                childNameLayout.setOrientation(LinearLayout.VERTICAL);
                childNameLayout.setLayoutParams(childNameParms);
                childNameLayout.setPadding(40, 40, 40, 40);
                final EditText etProfileChildName = new EditText(this);
                etProfileChildName.setText(parseChildName);
                etProfileChildName.setPadding(40, 40, 40, 40);
                childNameLayout.addView(etProfileChildName, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                final AlertDialog alertDialogProfileChildName = new AlertDialog.Builder(this)
                        .setView(childNameLayout)
                        .setTitle(R.string.dialog_title_child_profile_name)
                        .setPositiveButton("Update", null) //Set to null. We override the onclick
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                alertDialogProfileChildName.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something

                                dialogchildNameStr = etProfileChildName.getText().toString();
                                if (dialogchildNameStr.matches("")) {
                                    etProfileChildName.setError("Child Name cannot be blank");
                                }
                                else {
                                    dialogchildNameStr = dialogchildNameStr.substring(0,1).toUpperCase() +
                                            dialogchildNameStr.substring(1);
                                    updateProfileChildNameOnParse();
                                    dialog.dismiss();
                                }

                            }
                        });
                    }
                });
                alertDialogProfileChildName.show();
                break;
            case R.id.linear_child_gender:
                AlertDialog.Builder alertDialogChildProfileGender = new AlertDialog.Builder(this);
                LinearLayout childGenderLayout = new LinearLayout(this);
                LinearLayout.LayoutParams childGenderParms = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                childGenderLayout.setOrientation(LinearLayout.VERTICAL);
                childGenderLayout.setLayoutParams(childGenderParms);
                childGenderLayout.setPadding(40, 40, 40, 40);
                final RadioButton[] childGenderRb = new RadioButton[2];
                final RadioGroup childGenderRg = new RadioGroup(this);
                childGenderRg.setOrientation(RadioGroup.VERTICAL);
                childGenderRb[0]  = new RadioButton(this);
                childGenderRb[0].setText("Male");
                childGenderRb[0].setId(0+100);
                childGenderRb[1]  = new RadioButton(this);
                childGenderRb[1].setText("Female");
                childGenderRb[1].setId(1 + 100);

                if(parseChildGender.matches("Male")){
                    childGenderRb[0].setChecked(true);
                }
                else
                    childGenderRb[1].setChecked(true);
                childGenderRg.addView(childGenderRb[0]);
                childGenderRg.addView(childGenderRb[1]);
                childGenderRg.setPadding(40, 40, 40, 40);
                childGenderLayout.addView(childGenderRg, new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                alertDialogChildProfileGender.setView(childGenderLayout);
                alertDialogChildProfileGender.setTitle(R.string.dialog_title_child_profile_gender);
                alertDialogChildProfileGender
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int genderId = childGenderRg.getCheckedRadioButtonId();
                                if (genderId == childGenderRb[0].getId()){
                                    dialogChildGenderStr = "Male";
                                }
                                else{
                                    dialogChildGenderStr = "Female";
                                }

                                updateProfileChildGenderOnParse();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertChildGenderDialog = alertDialogChildProfileGender.create();
                alertChildGenderDialog.show();
                break;
            case R.id.linear_child_dob:
                AlertDialog.Builder alertDialogChildProfileDob = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View childDobLayout = layoutInflater.inflate(R.layout.dialog_child_profile_dob, null);
                childDobLayout.setPadding(40, 40, 40, 40);
                alertDialogChildProfileDob.setView(childDobLayout);
                final DatePicker childProfileDobDP = (DatePicker) childDobLayout.findViewById(R.id.datepicker_child_profile_dob);
                childProfileDobDP.updateDate(childProfileYear,childProfileMonth,childProfileDate);
                alertDialogChildProfileDob.setTitle(R.string.dialog_title_child_profile_dob);
                alertDialogChildProfileDob
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialogChildDobStr = (childProfileDobDP.getMonth()+1)+"-"+
                                        childProfileDobDP.getDayOfMonth()+"-"+childProfileDobDP.getYear();
                                updateProfileChildDobOnParse();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertChildDobDialog = alertDialogChildProfileDob.create();
                alertChildDobDialog.show();
                break;
            case R.id.btn_delete_child_account:
                deleteProfileChildOnParse();
                startActivity(new Intent(this, ParentHomeScreen.class));
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
