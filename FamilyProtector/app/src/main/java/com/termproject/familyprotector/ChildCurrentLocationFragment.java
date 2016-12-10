package com.termproject.familyprotector;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class ChildCurrentLocationFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    double latitude, longitude;
    private UserLocalStore userLocalStore;
    ParseGeoPoint currLocLatLng;
    boolean resultFound = false;
    private GPSTracker gps;
    private User loggedInUser;
    private String childNameStr, emailAddress, queryParam, lastSeenDate, lastSeenTime;
    Context activityContext;
    private  HttpURLConnection urlConnection = null;
    private TextView textCurrLocAddress, currLocLastSeenDate,currLocLastSeenTime;
    private LinearLayout linearChildCurrentLoc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_current_location, container, false);
        activityContext = getActivity();
        mMapView = (MapView) view.findViewById(R.id.current_location_map_view);
        textCurrLocAddress = (TextView) view.findViewById(R.id.text_child_curr_loc_address);
        currLocLastSeenDate = (TextView) view.findViewById(R.id.text_child_curr_loc_Date);
        currLocLastSeenTime = (TextView) view.findViewById(R.id.text_child_curr_loc_Time);
        linearChildCurrentLoc = (LinearLayout)view.findViewById(R.id.linear_child_current_loc);


        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());

        loggedInUser = userLocalStore.getLoggedInUser();
        final String childName = userLocalStore.getChildDetails();
        childNameStr = userLocalStore.getChildDetails();
        final String userName = loggedInUser.getUsername();
        emailAddress = loggedInUser.getUsername();
        getChildCurrentLocationFromParse();
        final ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("childCurrentLocation");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);
        mMapView.onCreate(savedInstanceState);
        setupMapFrag();
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
//                googleMap.setMyLocationEnabled(true);
                // create marker
                queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            if (parseObject != null) {
                                currLocLatLng = (ParseGeoPoint) parseObject.get("currentLocGeo");
                                latitude = currLocLatLng.getLatitude();
                                longitude = currLocLatLng.getLongitude();
                                resultFound = true;
                                MarkerOptions markerOptions = new MarkerOptions().position(
                                        new LatLng(latitude, longitude)).title(childName+"'s current location");
                                Marker marker = googleMap.addMarker(markerOptions);
                                marker.showInfoWindow();

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(latitude, longitude)).zoom(15f).build();
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                            }
                        } else {
                            gps = new GPSTracker(activityContext);
                            if (gps.canGetLocationCheck()) {
                                latitude = gps.getLatitudeVal();
                                longitude = gps.getLongitudeVal();
                            } else{
                                latitude = 37.7238566;
                                longitude = -122.4784694;
                            }
//                            MarkerOptions markerOptions = new MarkerOptions().position(
//                                    new LatLng(latitude, longitude)).title("Your location");
//                            Marker marker = googleMap.addMarker(markerOptions);
//                            marker.showInfoWindow();
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude)).zoom(15).build();
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                        }
                    }
                });

            }
        });
        return view;
    }

    private void getChildCurrentLocationFromParse() {
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("childCurrentLocation");
        queryClass.whereEqualTo("userName", emailAddress);
        queryClass.whereEqualTo("childName", childNameStr);
        Log.v("userName", emailAddress);
        Log.v("childName", childNameStr);

        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {
                        currLocLatLng = (ParseGeoPoint) parseObject.get("currentLocGeo");
                        latitude = currLocLatLng.getLatitude();
                        longitude = currLocLatLng.getLongitude();
                        Date date = parseObject.getUpdatedAt();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
                        lastSeenDate = formatter.format(date);
                        lastSeenTime = formatTime.format(date);


                    }
                } else {
                    gps = new GPSTracker(activityContext);
                    if (gps.canGetLocationCheck()) {
                        latitude = gps.getLatitudeVal();
                        Log.v("got gps latitude", latitude + "");
                        longitude = gps.getLongitudeVal();
                        Log.v("got gps longitude", longitude + "");
                    }else{
                        latitude = 37.7238566;
                        longitude = -122.4784694;

                    }
                    lastSeenDate = "Current Loc unavailable";
                    lastSeenTime = "Current Loc unavailable";


                }
                queryParam = Double.toString(latitude) + "," + Double.toString(longitude);
                Log.v("queryparam",queryParam);
                Log.v("date",lastSeenDate);
                Log.v("time",lastSeenDate);
                MapSearchTask mapSearchTask = new MapSearchTask();
                mapSearchTask.execute(queryParam);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void setupMapFrag() {

    }

    // Async Task to search for places in google maps

    public class MapSearchTask extends AsyncTask<String, Void, HashMap<String, String>> {


        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            HashMap<String, String> currentLocDetails = new HashMap<String, String>();
            String apiKey = "AIzaSyAUSETHO5_4d_lGrGfjX4vAowf6DrqaNmk";
            try {
                final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
                final String QUERY_PARAM = "query";
                final String APIKEY_PARAM = "key";

                Uri builtUri = Uri.parse(GOOGLE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(APIKEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray resultsArr = jsonObject.getJSONArray("results");
                if (resultsArr.length() == 1) {
                    currentLocDetails.put("addressStr", resultsArr.getJSONObject(0).getString("formatted_address"));
                    currentLocDetails.put("lastSeenDate", lastSeenDate);
                    currentLocDetails.put("lastSeenTime", lastSeenTime);


                }


            } catch (Exception e) {
                Log.v("ERROR", e.toString());

            }


            return currentLocDetails;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> locationMap) {

            String textAdd,textDate,textTime;
            textAdd = "Address: "+ locationMap.get("addressStr");
            textDate = "Last Seen Date: "+ locationMap.get("lastSeenDate");
            textTime = "Last Seen Time: "+ locationMap.get("lastSeenTime");
            if(locationMap.get("lastSeenDate").matches("Current Loc unavailable")){
                textAdd = childNameStr+"'s current location is unavailable";
                textCurrLocAddress.setText(textAdd);
                textCurrLocAddress.setTextSize(25f);
                textCurrLocAddress.setTextColor(getResources().getColor(R.color.black));
                currLocLastSeenDate.setText("");
                currLocLastSeenTime.setText("");
                linearChildCurrentLoc.setVisibility(View.VISIBLE);
            } else {
                textCurrLocAddress.setText(textAdd);
                currLocLastSeenDate.setText(textDate);
                currLocLastSeenTime.setText(textTime);
                linearChildCurrentLoc.setVisibility(View.VISIBLE);

            }



        }
    }

}
