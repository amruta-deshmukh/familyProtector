package com.termproject.familyprotector;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by Mehul on 10/18/2016.
 */
public class SampleDeviceAdminReceiver extends DeviceAdminReceiver {

//    @Override
//    public void onDisabled(Context context, Intent intent) {
//        // TODO Auto-generated method stub
//        Log.v("disabled","onDisabled");
//        Toast.makeText(context, "disabled dpm", Toast.LENGTH_SHORT).show();
//        super.onDisabled(context, intent);
//    }

    private UserLocalStore userLocalStore;


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.v("enable", "onEnabled");
        Toast.makeText(context, "enabled dpm", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.v("disable requested", "onDisablerequested");

        Toast.makeText(context, "disable dpm request", Toast.LENGTH_SHORT)
                .show();

        parsePush(context);
        return super.onDisableRequested(context, intent);
//        return null;
    }

    private void parsePush(Context context){
        ParseQuery pushWebQuery = ParseInstallation.getQuery();
        userLocalStore = new UserLocalStore(context);
        User user = userLocalStore.getLoggedInUser();
        String parentName = "parent:" + user.getUsername();
        pushWebQuery.whereEqualTo("email", parentName);
        String childName = userLocalStore.getChildForThisPhone();

        // Send push notification to query
        ParsePush pushWeb = new ParsePush();
        pushWeb.setQuery(pushWebQuery); // Set our Installation query

        pushWeb.setMessage("Device Admin for Family Protector disabled for: " + childName);
        pushWeb.sendInBackground();

    }
}
