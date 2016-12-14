package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ParentCurrentLocationCheckService extends IntentService {

    UserLocalStore userLocalStore;
    User user;
    String userName;
    private boolean done = false;

    public ParentCurrentLocationCheckService() {
        super("ParentCurrentLocationCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("intent service", "ParentCurrentLocationCheckService");
        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);
        User user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();

        checkCurrentLocationForChild();
        while (!done) {

        }

    }

    private void checkCurrentLocationForChild() {
        Log.v("parse", "check for children");
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("ChildDetails");
        queryClass.whereEqualTo("username", userName);
        queryClass.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> children, ParseException e) {
                if (e == null) {
                    if (children.size() > 0) {
                        Log.v("parse", "more than 0 children found");
                        for (int i = 0; i < children.size(); i++) {
                            final String childName = children.get(i).getString("name");
                            ParseQuery<ParseObject> queryChildClass = ParseQuery.getQuery("childCurrentLocation");
                            queryChildClass.whereEqualTo("userName", userName);
                            queryChildClass.whereEqualTo("childName", childName);
                            queryChildClass.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        if (parseObject != null) {
                                            Log.v("parse", "entry for current location:" + childName);
                                            Date date = parseObject.getUpdatedAt();
                                            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                                            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                                            String dateStr = formatter.format(date);
                                            String timeStr = formatTime.format(date);
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat sysFormatter = new SimpleDateFormat("MM-dd-yyyy");
                                            SimpleDateFormat sysFormatTime = new SimpleDateFormat("HH:mm");
                                            String sysDateStr = sysFormatter.format(calendar.getTime());
                                            String sysTimeStr = sysFormatTime.format(calendar.getTime());
                                            if (dateStr.equals(sysDateStr)) {
                                                String[] currLocTimeStrArr = timeStr.split(":");
                                                String[] sysTimeStrArr = sysTimeStr.split(":");
                                                int timeDifference = Integer.parseInt(sysTimeStrArr[1]) - Integer.parseInt(currLocTimeStrArr[1]);
                                                if (timeDifference > 5 || timeDifference < -5) {
                                                    saveToParseChildCurrentLocAlert(childName, dateStr, timeStr);
                                                }
                                            } else {
                                                saveToParseChildCurrentLocAlert(childName, dateStr, timeStr);
                                            }


                                        }
                                    } else {
                                        //no child current location do nothing
                                    }
                                }
                            });
                        }

                    }
                } else {
                    Log.d("Login", "Error: " + e.getMessage());
                }

            }
        });
        done = true;
    }

    private void saveToParseChildCurrentLocAlert(String childName, String dateStr, String timeStr) {

        ParseObject childCurrentLocationAlerts = new ParseObject("ChildCurrentLocationAlerts");
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);
        childCurrentLocationAlerts.setACL(postACL);
        childCurrentLocationAlerts.put("userName", userName);
        childCurrentLocationAlerts.put("childName", childName);
        childCurrentLocationAlerts.put("dateSinceLastOnline", dateStr);
//        String[] currLocTimeStrArr = timeStr.split(":");
        String currentLocTime = timeStr;
        SimpleDateFormat currentLocTimeFormatter = new SimpleDateFormat("HH:mm");//HH for hour of the day (0 - 23)
        Date d = null;
        try {
            d = currentLocTimeFormatter.parse(currentLocTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat currentLocTime12HourFormatter = new SimpleDateFormat("hh:mm a");
        String timeStr12Hour = currentLocTime12HourFormatter.format(d); // "12:18am"
        childCurrentLocationAlerts.put("timeSinceLastOnline", timeStr12Hour);
        childCurrentLocationAlerts.saveInBackground();
        parseAlertForChildCurrentLocation(childName, dateStr, timeStr12Hour);

    }

    private void parseAlertForChildCurrentLocation(String childName, String dateStr, String timeStr) {

        ParseQuery pushWebQuery = ParseInstallation.getQuery();
        String parentName = "parent:" + userName;
        pushWebQuery.whereEqualTo("email", parentName);
        JSONObject data = new JSONObject();
        try {
            data.put("childName",childName);
            data.put("alertType",FamilyProtectorConstants.ALERT_TYPE_CURRENT_LOC);
            data.put("title",childName+ "'s phone has been offline");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send push notification to query
        ParsePush pushWeb = new ParsePush();
        pushWeb.setQuery(pushWebQuery); // Set our Installation query
        pushWeb.setData(data);
        pushWeb.sendInBackground();

    }


}
