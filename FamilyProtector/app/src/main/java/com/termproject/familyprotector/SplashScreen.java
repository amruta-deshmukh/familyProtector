package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {
    UserLocalStore userLocalStore;
    User user;
    private Intent parseNotificationIntent;
    private Bundle parseNotificationExtras;
    private String parseNotificationJsonData, parseNotificationAlertType, parseNotificationchildName;
    private JSONObject parseNotificationJsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseNotificationIntent = getIntent();
        Log.v("got intent?", "getting intent");
        if(parseNotificationIntent!=null) {
            Log.v("got intent?", "intent not null");
            parseNotificationExtras = parseNotificationIntent.getExtras();
            Log.v("got extras?", "getting extras");
        }
        if(parseNotificationExtras!=null) {
            Log.v("got extras?", "extras not null");
            parseNotificationJsonData = parseNotificationExtras.getString("com.parse.Data");
            if(parseNotificationJsonData != null) {
                Log.v("jsonData", parseNotificationJsonData);
                try {
                    parseNotificationJsonObj = new JSONObject(parseNotificationJsonData);
                    parseNotificationAlertType = parseNotificationJsonObj.getString("alertType");
                    parseNotificationchildName = parseNotificationJsonObj.getString("childName");
                    NotificationDBHelper db = new NotificationDBHelper(this);
                    ChildNotification notification = new ChildNotification(parseNotificationchildName,
                            parseNotificationAlertType, FamilyProtectorConstants.FALSE, FamilyProtectorConstants.FALSE);
                    Log.v("Database", "adding to Db");
                    db.addChildNotification(notification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
        setContentView(R.layout.activity_splash);
        userLocalStore = new UserLocalStore(this);
        final boolean value = authenticate();
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (value) {
                        String appMode = userLocalStore.getAppMode();
                        if(appMode.equals("parent")) {
                            user = userLocalStore.getLoggedInUser();
                            Intent intent = new Intent(SplashScreen.this, ParentHomeScreen.class);
                            startActivity(intent);
                        }
                        else if (appMode.equals("child")){
                            user = userLocalStore.getLoggedInUser();
                            if(!(userLocalStore.getChildForThisPhone().trim().equals(""))){
                                Intent intent = new Intent(SplashScreen.this, ChildHomeScreenAfterSetup.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(SplashScreen.this, ChildHomeScreen.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            Intent intent = new Intent(SplashScreen.this, ChooseMode.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Intent intent = new Intent(SplashScreen.this, WelcomePageTutorial.class);
                        startActivity(intent);

                    }
                }
            }
        };
        timerThread.start();


    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

}
