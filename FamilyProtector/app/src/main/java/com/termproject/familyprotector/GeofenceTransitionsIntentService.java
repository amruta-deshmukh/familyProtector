package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";
    UserLocalStore userLocalStore;
    User storedUser;
    String userName, childNameStr, sysTime, geofenceIdToPutInArrayList;
    ArrayList<String> triggeringGeofencesNamesList;
    ArrayList<String> triggeringGeofenceIdList;
    Calendar c;
    GeofenceAlertObj geofenceAlertObj;


    public GeofenceTransitionsIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);
        storedUser = userLocalStore.getLoggedInUser();
        userName = storedUser.getUsername();
        childNameStr = userLocalStore.getChildForThisPhone();
        triggeringGeofencesNamesList = new ArrayList<String>();
        triggeringGeofenceIdList = new ArrayList<String>();
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            getGeofenceTransitionDetails(this,geofenceTransition,triggeringGeofences);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    private void getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.

        for (Geofence geofence : triggeringGeofences) {
            String id = geofence.getRequestId();

            performParseCheck(id, geofenceTransitionString);
        }
    }


    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }


    private void saveAlertToParse(String geofenceTransitionDetails, String geofenceTransitionId, String geofenceTriggeringAddress) {

        ParseObject childAlerts = new ParseObject("ChildAlerts");
        childAlerts.put("userName", userName);
        childAlerts.put("childName", childNameStr);
        childAlerts.put("alert", geofenceTransitionDetails);
        childAlerts.put("ruleIdStr", geofenceTransitionId);
        childAlerts.put("alertAddress",geofenceTriggeringAddress );
        childAlerts.saveInBackground();
    }

    private void sendParsePush(String geofenceTransitionDetails) {
        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("email", "parent:" + userName);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        int minutes = c.get(Calendar.MINUTE);
        String minStr = Integer.toString(minutes);
        if(minutes<10){
            minStr = "0"+Integer.toString(minutes);

        }

        push.setMessage(childNameStr +" "+ geofenceTransitionDetails + " at " + c.get(Calendar.HOUR_OF_DAY) + ":" + minStr);
        push.sendInBackground();

    }


    private void performParseCheck(final String geofenceId, final String geofenceTransitionString){
        //convert geofenceId to int
        int geofenceIntId = Integer.parseInt(geofenceId);

        c = Calendar.getInstance();
        int hours = (c.get(Calendar.HOUR_OF_DAY))*100;
        int minutes = c.get(Calendar.MINUTE);
        final int timeToCheck = hours+minutes;
         final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("userName", userName);
        query.whereEqualTo("childName", childNameStr);
        query.whereEqualTo("ruleLocationId", geofenceIntId);


        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            boolean addgeofence = false;
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){

                    String fromTime = parseObject.getString("ruleFromTime");
                    String[] fromTimeArr = fromTime.split(":");
                    int fromHour = Integer.parseInt(fromTimeArr[0]) * 100;
                    int fromMinute = Integer.parseInt(fromTimeArr[1]);
                    int fromTimeInt = fromHour+fromMinute;

                    String toTime = parseObject.getString("ruleToTime");
                    String[] toTimeArr = toTime.split(":");
                    int toHour = Integer.parseInt(toTimeArr[0])*100;
                    int toMinute = Integer.parseInt(toTimeArr[1]);
                    int toTimeInt = toHour+toMinute;
                    String dayRule = "";
                    switch(dayOfWeek){
                        case 1:
                            dayRule = parseObject.getString("Sunday");
                            break;
                        case 2:
                            dayRule = parseObject.getString("Monday");
                            break;
                        case 3:
                            dayRule = parseObject.getString("Tuesday");
                            break;
                        case 4:
                            dayRule = parseObject.getString("Wednesday");
                            break;
                        case 5:
                            dayRule = parseObject.getString("Thursday");
                            break;
                        case 6:
                            dayRule = parseObject.getString("Friday");
                            break;
                        case 7:
                            dayRule = parseObject.getString("Saturday");
                            break;
                    }

                    if(dayRule!=null) {

                        if (timeToCheck > fromTimeInt && timeToCheck < toTimeInt && dayRule.matches("Yes")) {


                            String triggeringGeofenceName = parseObject.getString("locationName");
                            String triggeringGeofenceId = Integer.toString(parseObject.getInt("ruleLocationId"));
                            String triggeringGeofenceAddress = parseObject.getString("locationAddress");
                            geofenceAlertObj = new GeofenceAlertObj(triggeringGeofenceName,
                                    triggeringGeofenceId, geofenceTransitionString);

                            saveAlertToParse(geofenceAlertObj.alertString(), geofenceAlertObj.alertIdString(), triggeringGeofenceAddress);
                            sendParsePush(geofenceAlertObj.alertString());


                        }

                    }


                }
                else {
                    Log.d("parse check", "error in fetching");
                }

            }

        });




    }
}
