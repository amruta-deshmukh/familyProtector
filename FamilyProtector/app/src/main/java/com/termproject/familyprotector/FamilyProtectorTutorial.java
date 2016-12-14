package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FamilyProtectorTutorial extends AppCompatActivity implements View.OnClickListener{

    private CheckBox chkAddChild,chkAddLoc,chkAddWeb,chkAppChild, chkChildAlerts;
    private ImageButton imgAddChild,imgAddLoc,imgAddWeb,imgAppChild, imgChildAlerts;
    private Button btnSaveProgress;
    private UserLocalStore userLocalStore;
    private boolean isUserLogged;
    private User user;
    private String userName;
    private boolean boolAddChild = false,boolAddLoc = false,boolAddWeb =false,
            boolAppChild = false, boolChildAlerts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_protector_tutorial);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        userLocalStore = new UserLocalStore(this);
        isUserLogged = userLocalStore.getUserLoggedIn();
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();
        init();
        btnSaveProgress.setOnClickListener(this);
        imgAddChild.setOnClickListener(this);
        imgAddLoc.setOnClickListener(this);
        imgAddWeb.setOnClickListener(this);
        imgAppChild.setOnClickListener(this);
        imgChildAlerts.setOnClickListener(this);
    }

    private void init(){
        chkAddChild = (CheckBox) findViewById(R.id.checkbox_tut_add_child);
        chkAddLoc = (CheckBox) findViewById(R.id.checkbox_tut_add_loc_rule);
        chkAddWeb = (CheckBox) findViewById(R.id.checkbox_tut_add_web_rule);
        chkAppChild = (CheckBox) findViewById(R.id.checkbox_tut_app_to_child);
        chkChildAlerts = (CheckBox) findViewById(R.id.checkbox_tut_check_alerts);
        imgAddChild = (ImageButton) findViewById(R.id.image_button_add_child);
        imgAddLoc = (ImageButton) findViewById(R.id.image_button_add_loc_rule);
        imgAddWeb = (ImageButton) findViewById(R.id.image_button_add_web_rule);
        imgAppChild = (ImageButton) findViewById(R.id.image_button_add_app_to_child);
        imgChildAlerts = (ImageButton) findViewById(R.id.image_button_check_alerts);
        btnSaveProgress = (Button) findViewById(R.id.button_tut_save_progress);
        if(isUserLogged){
            btnSaveProgress.setVisibility(View.VISIBLE);
        }
        checkTheCheckBoxes();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_tut_save_progress:
                saveProgressToParse();
                startActivity(new Intent(this, ParentHomeScreen.class));

                break;
            case R.id.image_button_add_child:
                startActivity(new Intent(this,TutorialAddAChild.class));
                break;
            case R.id.image_button_add_loc_rule:
                startActivity(new Intent(this,TutorialAddLocationRule.class));
                break;
            case R.id.image_button_add_web_rule:
                startActivity(new Intent(this,TutorialAddWebRule.class));
                break;
            case R.id.image_button_add_app_to_child:
                startActivity(new Intent(this,TutorialAddAppChild.class));
                break;
            case R.id.image_button_check_alerts:
                startActivity(new Intent(this,TutorialCheckAlerts.class));
                break;

        }

    }

    private void checkTheCheckBoxes(){
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("TutorialCheckList");
        queryClass.whereEqualTo("userName", userName);
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {

                        if(parseObject.getString("addChild").equals("Yes")){
                            boolAddChild = true;
                        }
                        if(parseObject.getString("addLocRule").equals("Yes")){
                            boolAddLoc = true;
                        }
                        if(parseObject.getString("addWebRule").equals("Yes")){
                            boolAddWeb = true;
                        }
                        if(parseObject.getString("addAppChild").equals("Yes")) {
                            boolAppChild = true;
                        }
                        if(parseObject.getString("checkAlerts").equals("Yes")) {
                            boolChildAlerts = true;
                        }

                    }
                }

                setCheckBox();

            }
        });

    }

    private void setCheckBox(){
        if(boolAddChild){
            chkAddChild.setChecked(true);
        }
        if(boolAddLoc){
            chkAddLoc.setChecked(true);
        }
        if(boolAddWeb){
            chkAddWeb.setChecked(true);
        }
        if(boolAppChild) {
            chkAppChild.setChecked(true);
        }
        if(boolChildAlerts) {
            chkChildAlerts.setChecked(true);
        }
    }

    private void saveProgressToParse(){

        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("TutorialCheckList");
        queryClass.whereEqualTo("userName", userName);
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {
                        if (chkAddChild.isChecked()) {
                            parseObject.put("addChild", "Yes");
                        } else {
                            parseObject.put("addChild", "No");
                        }
                        if (chkAddLoc.isChecked()) {
                            parseObject.put("addLocRule", "Yes");
                        } else {
                            parseObject.put("addLocRule", "No");
                        }
                        if (chkAddWeb.isChecked()) {
                            parseObject.put("addWebRule", "Yes");
                        } else {
                            parseObject.put("addWebRule", "No");
                        }
                        if (chkAppChild.isChecked()) {
                            parseObject.put("addAppChild", "Yes");
                        } else {
                            parseObject.put("addAppChild", "No");
                        }
                        if (chkChildAlerts.isChecked()) {
                            parseObject.put("checkAlerts", "Yes");
                        } else {
                            parseObject.put("checkAlerts", "No");
                        }
                        parseObject.saveInBackground();
                    }
                } else {
                    ParseObject tutorialCheckList = new ParseObject("TutorialCheckList");
                    ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                    postACL.setPublicReadAccess(true);
                    postACL.setPublicWriteAccess(true);
                    tutorialCheckList.setACL(postACL);

                    tutorialCheckList.put("userName", userName);

                    if (chkAddChild.isChecked()) {
                        tutorialCheckList.put("addChild", "Yes");
                    } else {
                        tutorialCheckList.put("addChild", "No");
                    }
                    if (chkAddLoc.isChecked()) {
                        tutorialCheckList.put("addLocRule", "Yes");
                    } else {
                        tutorialCheckList.put("addLocRule", "No");
                    }
                    if (chkAddWeb.isChecked()) {
                        tutorialCheckList.put("addWebRule", "Yes");
                    } else {
                        tutorialCheckList.put("addWebRule", "No");
                    }
                    if (chkAppChild.isChecked()) {
                        tutorialCheckList.put("addAppChild", "Yes");
                    } else {
                        tutorialCheckList.put("addAppChild", "No");
                    }
                    if (chkChildAlerts.isChecked()) {
                        tutorialCheckList.put("checkAlerts", "Yes");
                    } else {
                        tutorialCheckList.put("checkAlerts", "No");
                    }


                    tutorialCheckList.saveInBackground();
                }

            }
        });

    }
}
