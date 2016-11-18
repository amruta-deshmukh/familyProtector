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
    AlarmReceiver alarm =  new AlarmReceiver();
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
                installation.put("email", "parent:" + storedUser.getUsername());

                installation.saveInBackground();
                alarm.setAlarm(this,"parent");
                startActivity(new Intent(this, ParentHomeScreen.class));
                break;
            case R.id.button_child_mode:
                userLocalStore.setAppMode("child");
                installation.put("email", "child:" + storedUser.getUsername());
                installation.saveInBackground();

                //in order to activate policy manger
//                if (!policyManager.isAdminActive()) {
//                    Intent activateDeviceAdmin = new Intent(
//                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                    activateDeviceAdmin.putExtra(
//                            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
//                            policyManager.getAdminComponent());
//                    activateDeviceAdmin
//                            .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                                    "After activating Device Admin for the child mode, " +
//                                            "the application can't be uninstalled. In case child disables device admin," +
//                                            "you will receive a notification for the same.");
//                    startActivityForResult(activateDeviceAdmin,
//                            PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
//                }
                startActivity(new Intent(this,ChildHomeScreen.class));
        }

    }

}
