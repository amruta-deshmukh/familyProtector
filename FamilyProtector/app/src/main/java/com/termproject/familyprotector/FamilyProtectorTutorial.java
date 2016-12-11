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

public class FamilyProtectorTutorial extends AppCompatActivity implements View.OnClickListener{

    private CheckBox chkAddChild,chkAddLoc,chkAddWeb,chkAppChild, chkChildAlerts;
    private ImageButton imgAddChild,imgAddLoc,imgAddWeb,imgAppChild, imgChildAlerts;
    private Button btnSaveProgress;
    private UserLocalStore userLocalStore;
    private boolean isUserLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_protector_tutorial);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        userLocalStore = new UserLocalStore(this);
        isUserLogged = userLocalStore.getUserLoggedIn();
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
                break;
            case R.id.image_button_add_child:
                startActivity(new Intent(this,TutorialAddAChild.class));
                break;
            case R.id.image_button_add_loc_rule:
                break;
            case R.id.image_button_add_web_rule:
                break;
            case R.id.image_button_add_app_to_child:
                break;
            case R.id.image_button_check_alerts:
                break;

        }

    }
}
