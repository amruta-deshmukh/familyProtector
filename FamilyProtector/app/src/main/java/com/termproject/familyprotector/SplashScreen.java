package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userLocalStore = new UserLocalStore(this);
        final boolean value = authenticate();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (value) {
                        String appMode = userLocalStore.getAppMode();
                        if(appMode.equals("parent")) {
                            user = userLocalStore.getLoggedInUser();
                            Intent intent = new Intent(SplashScreen.this, BrowserHistory.class);
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
