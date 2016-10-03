package com.termproject.familyprotector;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Mehul on 10/7/2015.
 */
public class StorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Vdtbz5r7WmY2BipSx2MvvkrVlIFmsYpLmyYxEDis", "PuWA8YcEGG5dZRmXlj0Ij5tdy1vO6SsdosDEOZBl");


        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }
}
