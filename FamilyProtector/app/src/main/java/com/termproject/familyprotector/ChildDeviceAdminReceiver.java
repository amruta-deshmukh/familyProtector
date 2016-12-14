package com.termproject.familyprotector;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Mehul on 10/18/2016.
 */
public class ChildDeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.v("disabled", "onDisabled");
//        Toast.makeText(context, "disabled dpm", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }

    private UserLocalStore userLocalStore;


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Enabled device administration", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Toast.makeText(context, "Request to disable device policy admin received", Toast.LENGTH_SHORT)
                .show();
        userLocalStore = new UserLocalStore(context);
        User user = userLocalStore.getLoggedInUser();
        String childName = userLocalStore.getChildForThisPhone();
        String userName = user.getUsername();
        saveToParseDeviceAdminAlert(userName,childName);
        parsePushForDeviceAdmin(userName,childName);
        return super.onDisableRequested(context, intent);
    }

    private void saveToParseDeviceAdminAlert(String userName, String childName){

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy::::hh:mm a");
        Calendar calendar = Calendar.getInstance();
        String alertDateTime[] = formatter.format(calendar.getTime()).split("::::");
        ParseObject childDeviceAdminAlerts = new ParseObject("ChildDeviceAdminAlerts");
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);
        childDeviceAdminAlerts.setACL(postACL);
        childDeviceAdminAlerts.put("userName", userName);
        childDeviceAdminAlerts.put("childName", childName);
        childDeviceAdminAlerts.put("alertDate",alertDateTime[0]);
        childDeviceAdminAlerts.put("alertTime",alertDateTime[1]);
        childDeviceAdminAlerts.saveInBackground();

    }

    private void parsePushForDeviceAdmin(String userName, String childName) {

        ParseQuery pushWebQuery = ParseInstallation.getQuery();
        String parentName = "parent:" + userName;
        pushWebQuery.whereEqualTo("email", parentName);
        JSONObject data = new JSONObject();
        try {
            data.put("childName",childName);
            data.put("alertType",FamilyProtectorConstants.ALERT_TYPE_DEVICE_ADMIN);
            data.put("title",childName+ ": Uninstallation Attempt of Family Protector");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send push notification to query
        ParsePush pushWeb = new ParsePush();
        pushWeb.setQuery(pushWebQuery); // Set our Installation query
        pushWeb.setData(data);
//        pushWeb.setMessage("Uninstallation Attempt of Family Protector for: " + childName);

        pushWeb.sendInBackground();

    }
}
