package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

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
    private GPSTracker gps;
    double phoneLatitude =0, phoneLongitude=0;
    float dist = 0.0f;


    public GeofenceTransitionsIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);
        gps = new GPSTracker(context);
        storedUser = userLocalStore.getLoggedInUser();
        userName = storedUser.getUsername();
        childNameStr = userLocalStore.getChildForThisPhone();
        triggeringGeofencesNamesList = new ArrayList<String>();
        triggeringGeofenceIdList = new ArrayList<String>();
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (gps.canGetLocationCheck()) {
            phoneLatitude = gps.getLatitudeVal();
            Log.v("got latitude", phoneLatitude + "");
            phoneLongitude = gps.getLongitudeVal();
            Log.v("got longitude", phoneLongitude + "");
        }

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
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);
        childAlerts.setACL(postACL);
        childAlerts.put("userName", userName);
        childAlerts.put("childName", childNameStr);
        childAlerts.put("alert", geofenceTransitionDetails);
        childAlerts.put("ruleIdStr", geofenceTransitionId);
        childAlerts.put("alertAddress", geofenceTriggeringAddress);
        childAlerts.saveInBackground();
    }

    private void sendParsePush(String geofenceTransitionDetails) {
        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("email", "parent:" + userName);

        int minutes = c.get(Calendar.MINUTE);
        String minStr = Integer.toString(minutes);
        if(minutes<10){
            minStr = "0"+Integer.toString(minutes);

        }

        JSONObject data = new JSONObject();
        try {
            data.put("childName",childNameStr);
            data.put("alertType",FamilyProtectorConstants.ALERT_TYPE_GEOFENCE);
            data.put("title",childNameStr+ ": "+ geofenceTransitionDetails + " at "
                    + c.get(Calendar.HOUR_OF_DAY) + ":" + minStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query

        push.setData(data);
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
        Log.v("day of the week", "int" + dayOfWeek);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("userName", userName);
        query.whereEqualTo("childName", childNameStr);
        query.whereEqualTo("ruleLocationId", geofenceIntId);
        Log.v("rule loc ID", geofenceId);


        query.getFirstInBackground(new GetCallback<ParseObject>() {
            //            boolean addgeofence = false;
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {
                        ParseGeoPoint ruleLatLng = (ParseGeoPoint) parseObject.get("geopoint");
                        double ruleLatitude = ruleLatLng.getLatitude();
                        double ruleLongitude = ruleLatLng.getLongitude();
                        if(phoneLatitude!=0 && phoneLongitude!=0) {
                            getDistance(phoneLatitude, phoneLongitude, ruleLatitude, ruleLongitude);
                        }
                        float locationRadius = parseObject.getNumber("locationRadius").floatValue();
                        String fromTime = parseObject.getString("ruleFromTime");
                        String[] fromTimeArr = fromTime.split(":");
                        int fromHour = Integer.parseInt(fromTimeArr[0]) * 100;
                        int fromMinute = Integer.parseInt(fromTimeArr[1]);
                        int fromTimeInt = fromHour + fromMinute;

                        String toTime = parseObject.getString("ruleToTime");
                        String[] toTimeArr = toTime.split(":");
                        int toHour = Integer.parseInt(toTimeArr[0]) * 100;
                        int toMinute = Integer.parseInt(toTimeArr[1]);
                        int toTimeInt = toHour + toMinute;
                        String dayRule = "";
                        switch (dayOfWeek) {
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

                        if (dayRule != null) {

                            if (timeToCheck > fromTimeInt && timeToCheck < toTimeInt &&
                                    dayRule.matches("Yes")) {

                                Log.v("inside", "rule is active");


                                String triggeringGeofenceName = parseObject.getString("locationName");
                                String triggeringGeofenceId = Integer.toString(parseObject.getInt("ruleLocationId"));
                                String triggeringGeofenceAddress = parseObject.getString("locationAddress");
                                geofenceAlertObj = new GeofenceAlertObj(triggeringGeofenceName,
                                        triggeringGeofenceId, geofenceTransitionString);

                                saveAlertToParse(geofenceAlertObj.alertString(), geofenceAlertObj.alertIdString(), triggeringGeofenceAddress);
                                sendParsePush(geofenceAlertObj.alertString());


                            }

                        } else {
                            Log.v("outside", "rule is inactive");

                        }


                    }
                } else {
                    Log.d("parse check", "error in fetching");
                }

            }

        });
    }


    private void getDistance(double phoneLat, double phoneLong, double ruleLat, double ruleLng){
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(ruleLat-phoneLat);
        double dLng = Math.toRadians(ruleLng-phoneLong);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(phoneLat)) * Math.cos(Math.toRadians(ruleLat)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        dist = (float) (earthRadius * c);

        Log.v("distance:",dist+" meters");

//        return dist;

    }
}
