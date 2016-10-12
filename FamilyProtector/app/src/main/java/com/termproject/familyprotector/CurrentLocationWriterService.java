package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class CurrentLocationWriterService extends IntentService {

    GPSTracker gps;
    double latitude, longitude;
    UserLocalStore userLocalStore;


    public CurrentLocationWriterService() {
        super("CurrentLocationWriterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Handle Intent", "inside handle intent");
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
    }


    private void writeLatLngToParse(){
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
                }
                else{
                    Log.v("error is not null", "error is not null");
                    ParseObject childCurrentLocation = new ParseObject("childCurrentLocation");

                    childCurrentLocation.put("childName", childName);
                    childCurrentLocation.put("userName", userName);
                    childCurrentLocation.put("currentLocGeo", currLocLatLng);
                    childCurrentLocation.saveInBackground();
                }

            }
        });



            }


        }
