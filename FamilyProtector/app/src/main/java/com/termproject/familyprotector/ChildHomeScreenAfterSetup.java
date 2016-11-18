package com.termproject.familyprotector;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ChildHomeScreenAfterSetup extends AppCompatActivity {
    AlarmReceiver alarm =  new AlarmReceiver();
    UserLocalStore userLocalStore;
    Drawable icon;
    TextView childIconText;
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PolicyManager policyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        policyManager = new PolicyManager(this);
        userLocalStore = new UserLocalStore(this);
        setTitle("Child Mode");
        String genderStr = userLocalStore.getChildForThisPhoneGender();
        setContentView(R.layout.activity_child_home_screen_after_setup);
        alarm.setAlarm(this, "child");
        if (!policyManager.isAdminActive()) {
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    policyManager.getAdminComponent());
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Click on Activate button to protect \"Family Protector\" application from uninstalling! \n\n\n\n" +
                                    "If your child tries to disable the device administration rights " +
                                    "for \"Family Protector\", you will receive a notification for the same.");
            startActivityForResult(activateDeviceAdmin,
                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
        }


//        Progress progress = new Progress(this);
//        progress.execute();

        //setting up the UI elements
        childIconText= (TextView)findViewById(R.id.child_for_text);
        if (genderStr.matches("Male")) {
            icon = getResources().getDrawable(R.drawable.child_boy_icon);
        }
        else {
            icon = getResources().getDrawable(R.drawable.child_girl_icon);
        }
        icon.setBounds(0, 0, 300, 300);
        childIconText.setText(userLocalStore.getChildForThisPhone());
        childIconText.setCompoundDrawables(icon,null,null,null);


        try {

            mToolBar = (Toolbar) findViewById(R.id.toolbar_child);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e("Parent screen", e.toString());
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerRoot_child);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolBar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView nv = (NavigationView) findViewById(R.id.navView_child);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String txt;
                switch (menuItem.getItemId()) {
                    case R.id.settings_drawer_child:
                        startActivity(new Intent(ChildHomeScreenAfterSetup.this,ChildProfileSettings.class));
                        return true;
                    case R.id.drawer_change_child:
                        startActivity(new Intent(ChildHomeScreenAfterSetup.this, ChildHomeScreen.class));

//                        userLocalStore.setUserLoggedIn(false);
//
//                        startActivity(new Intent(ChildHomeScreenAfterSetup.this, Login.class));
                        break;
                    case R.id.logout_drawer_child:
                        startActivity(new Intent(ChildHomeScreenAfterSetup.this, Confirmation.class));
                        break;
                    default:
                        txt = "Invalid Item Selected";
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        Progress progress = new Progress(this);
        progress.execute();

    }

    public class Progress extends AsyncTask<Void,Void,Void>{
        private ProgressDialog dialog;

        public Progress(ChildHomeScreenAfterSetup activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...while we setup...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


}
