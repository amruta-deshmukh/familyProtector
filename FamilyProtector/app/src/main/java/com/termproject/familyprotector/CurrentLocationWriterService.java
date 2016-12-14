package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class CurrentLocationWriterService extends IntentService {

    GPSTracker gps;
    double latitude, longitude;
    UserLocalStore userLocalStore;
    private boolean done = false;


    public CurrentLocationWriterService() {
        super("CurrentLocationWriterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);

        gps = new GPSTracker(this);
        if (gps.canGetLocationCheck()) {
            latitude = gps.getLatitudeVal();
            Log.v("got latitude", latitude + "");
            longitude = gps.getLongitudeVal();
            Log.v("got longitude", longitude + "");
        }
        writeLatLngToParse();
        while (!done) {

        }

    }


    private void writeLatLngToParse() {
        User user = userLocalStore.getLoggedInUser();
        final String childName = userLocalStore.getChildForThisPhone();
        final String userName = user.getUsername();
        final ParseGeoPoint currLocLatLng = new ParseGeoPoint(latitude, longitude);
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("childCurrentLocation");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);

        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {
                        Log.v("parseobject is not null", "parseobject is not null");
                        parseObject.put("currentLocGeo", currLocLatLng);
                        parseObject.saveInBackground();
                    }
                } else {
                    Log.v("error is not null", "error is not null");
                    ParseObject childCurrentLocation = new ParseObject("childCurrentLocation");
                    ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                    postACL.setPublicReadAccess(true);
                    postACL.setPublicWriteAccess(true);
                    childCurrentLocation.setACL(postACL);
                    childCurrentLocation.put("childName", childName);
                    childCurrentLocation.put("userName", userName);
                    childCurrentLocation.put("currentLocGeo", currLocLatLng);
                    childCurrentLocation.saveInBackground();
                }

            }
        });
        done = true;
    }
}
