package com.termproject.familyprotector;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehul on 12/17/2015.
 */
public class GeofenceCreationService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    protected GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList;
    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;
    private SharedPreferences mSharedPreferences;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";
    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

//    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 24;

    //setting the geofence for one hour
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
             15 * 60 * 1000;
    UserLocalStore userLocalStore;

    //    UserLocalStore userLocalStore;
    public GeofenceCreationService() {
        super("GeofenceCreationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();

        mGeofenceList = new ArrayList<Geofence>();
        userLocalStore = new UserLocalStore(context);
        mGeofencePendingIntent = null;
        mSharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        mGeofencesAdded = mSharedPreferences.getBoolean(GEOFENCES_ADDED_KEY, false);
        buildGoogleApiClient();
        readGeoFenceDataFromParse();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void readGeoFenceDataFromParse() {
        User user = userLocalStore.getLoggedInUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", userLocalStore.getChildForThisPhone());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childrenLocationRules, ParseException e) {
                if (e == null) {
                    if (childrenLocationRules.size() > 0) {
                        for (int i = 0; i < childrenLocationRules.size(); i++) {

                            ParseObject childRuleLocationRow = childrenLocationRules.get(i);
                            int locID = childRuleLocationRow.getInt("ruleLocationId");
                            String locationID = Integer.toString(locID);
                            ParseGeoPoint ruleLatLng = (ParseGeoPoint) childRuleLocationRow.get("geopoint");
                            double latitude = ruleLatLng.getLatitude();
                            double longitude = ruleLatLng.getLongitude();
                            float locationRadius = childRuleLocationRow.getNumber("locationRadius").floatValue();
                            populateGeofenceList(locationID, latitude, longitude, locationRadius);
                        }
                        createGeofencesFromList();
                    } else {
                    }
                } else {
                    Log.d("Geofence Service", "Error in reading rule date from parse" + e.getMessage());
                }
            }
        });
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("GeofenceService", "Connected to GoogleApiClient");

    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("GeofenceService", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i("GeofenceService", "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);

        builder.addGeofences(mGeofenceList);

        return builder.build();
    }

    public void createGeofencesFromList() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),

                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }
    private void logSecurityException(SecurityException securityException) {
        Log.e("GeofenceService", "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e("GeofenceService", errorMessage);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {

            return mGeofencePendingIntent;
        }


        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void populateGeofenceList(String locationID, double latitude, double longitude, float locationRadius) {

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(locationID)

                        // Set the circular region of this geofence.
                .setCircularRegion(latitude, longitude, locationRadius)

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track exit transitions.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)

                        // Create the geofence.
                .build());
    }
}
