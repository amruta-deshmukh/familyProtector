package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.parse.ParseInstallation;

public class ChooseMode extends AppCompatActivity implements View.OnClickListener{

    private Button buttonParentMode,buttonChildMode;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosemode);

        init();
        userLocalStore = new UserLocalStore(this);


        buttonParentMode.setOnClickListener(this);
        buttonChildMode.setOnClickListener(this);

    }

    private void init(){

        buttonParentMode = (Button)findViewById(R.id.button_parent_mode);
        buttonChildMode = (Button)findViewById(R.id.button_child_mode);
    }
    public void onClick(View view){
        User storedUser = userLocalStore.getLoggedInUser();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        switch(view.getId()){
            case R.id.button_parent_mode:
                userLocalStore.setAppMode("parent");

                installation.put("email", "parent:"+storedUser.getUsername());
                installation.saveInBackground();
                startActivity(new Intent(this, ParentHomeScreen.class));
                break;
            case R.id.button_child_mode:
                userLocalStore.setAppMode("child");
                installation.put("email", "child:" + storedUser.getUsername());
                installation.saveInBackground();
                startActivity(new Intent(this,ChildHomeScreen.class));
        }

    }




}
