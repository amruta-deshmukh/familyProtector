package com.termproject.familyprotector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements
        View.OnClickListener,GoogleMap.OnMapLongClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    GPSTracker gps;
    double latitude, longitude;
    Marker marker;
    Circle circle;
    HttpURLConnection urlConnection = null;
    FloatingActionButton floatingActionButton;
    String addressString = "";
    String locationNameStr, childNameStr;
    float locationPerimeterValue;
    EditText locationNameEditText, locationPerimeterEditText;
    String queryParam;
    UserLocalStore userLocalStore;
    Button continueButton;
    GoogleApiClient mGoogleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        continueButton = (Button) findViewById(R.id.rule_location_continue_button);
        userLocalStore = new UserLocalStore(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        gps = new GPSTracker(this);
        if (gps.canGetLocationCheck()) {
            latitude = gps.getLatitudeVal();
            Log.v("got latitude", latitude+"");
            longitude = gps.getLongitudeVal();
            Log.v("got longitude", longitude+"");

            queryParam = Double.toString(latitude) + "," + Double.toString(longitude);
            Log.v("Lat and long", queryParam);



        } else {
            queryParam = "Thornton+Hall,+San+Francisco+State+University,+1600+Holloway+Ave,+San+Francisco,+CA+94132";
        }

        setUpMapIfNeeded();

        MapSearchTask mapSearchTask = new MapSearchTask();
        mapSearchTask.execute(queryParam);

        floatingActionButton.setOnClickListener(this);
        mMap.setOnMapLongClickListener(this);
        continueButton.setOnClickListener(this);

    }

    public void onClick(View view) {
        final View v = view;
        if (view.getId() == R.id.floating_action_button) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View perimeterCreator = layoutInflater.inflate(R.layout.dialog_location_save, null);
            AlertDialog.Builder dialogPerimeter = new AlertDialog.Builder(this);
            dialogPerimeter.setTitle(R.string.dialog_title);
            dialogPerimeter.setView(perimeterCreator);
            TextView childNameTextView = (TextView) perimeterCreator.findViewById(R.id.text_childName);
            TextView locationAddressTextView = (TextView) perimeterCreator.findViewById(R.id.text_address);
            locationPerimeterEditText = (EditText) perimeterCreator.findViewById(R.id.edit_location_perimeter);
            childNameStr = userLocalStore.getChildDetails();
            childNameTextView.setText(childNameStr);
            locationAddressTextView.setText(addressString);
            dialogPerimeter
                    .setCancelable(false)
                    .setPositiveButton("View Perimeter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String locationPerimeterStr = locationPerimeterEditText.getText().toString();
                            if(locationPerimeterStr.matches("")){
                                locationPerimeterValue = 30.0f;
                            }
                            else{
                                locationPerimeterValue = Float.valueOf(locationPerimeterStr);
                            }

                            createGeofenceCircle();
                            String toastStr = "Location with perimeter";
                            Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_LONG).show();
                            showSnackbar(v);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alertDialog = dialogPerimeter.create();
            alertDialog.show();
        }
        else if (view.getId() == R.id.rule_location_continue_button){

            userLocalStore.setLocationAddress(addressString);
            userLocalStore.setLocationPerimeter(locationPerimeterValue);
            userLocalStore.setLocationLatitude(latitude);
            userLocalStore.setLocationLongitude(longitude);
            startActivity(new Intent(this, ChildLocationRuleSaveActivity.class));

        }
    }

    public void showSnackbar(View v){
        String message = "Press continue to save";
        final Snackbar sn = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
        sn.setAction("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sn.dismiss();

                userLocalStore.setLocationAddress(addressString);
                userLocalStore.setLocationPerimeter(locationPerimeterValue);
                userLocalStore.setLocationLatitude(latitude);
                userLocalStore.setLocationLongitude(longitude);
                startActivity(new Intent(MapsActivity.this, ChildLocationRuleSaveActivity.class));
            }

        });
        sn.show();
    }
    

    @Override
    public void onMapLongClick(LatLng point){
        marker.remove();
        if(circle!=null){
            circle.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("New Location"));
        marker.showInfoWindow();
        MapSearchTask mapSearchTask = new MapSearchTask();
        queryParam = Double.toString(point.latitude) + "," + Double.toString(point.longitude);
        mapSearchTask.execute(queryParam);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_maps_activity, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String queryText) {return true;}

            public boolean onQueryTextSubmit(String query) {
                Log.v("LISTENER", "OnQuerySubmit called - " + query);
                hideSoftKeyboard(MapsActivity.this);
                MapSearchTask mapSearchTask = new MapSearchTask();
                mapSearchTask.execute(query);

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

//    private void getAutocompleteResults(String queryText) {
//        LatLng latLng = new LatLng(latitude,longitude);
//        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(32.5342622,-124.415165),new LatLng(42.0095169,-114.1313927));
//
//    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void createGeofenceCircle(){
        if(circle!=null){
            circle.remove();
        }

        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(locationPerimeterValue)
                .strokeColor(Color.parseColor("#0084d3"))
                .fillColor(Color.parseColor("#500084d3")));

        if(mMap.getCameraPosition().zoom <=16.9f)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomBy(2.5f));

        }



    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("API client error", connectionResult.getErrorCode()+":"+connectionResult.getErrorMessage());
    }

    // Async Task to search for places in google maps

    public class MapSearchTask extends AsyncTask<String, Void, HashMap<String, String>> {


        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            HashMap<String, String> location = new HashMap<String, String>();
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
                    location.put("addressStr", resultsArr.getJSONObject(0).getString("formatted_address"));
                    JSONObject locationObject = resultsArr.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    location.put("lat", locationObject.getString("lat"));
                    location.put("lng", locationObject.getString("lng"));

                }


            } catch (Exception e) {
                Log.v("ERROR", e.toString());

            }


            return location;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> locationMap) {
            String markerTitle = locationMap.get("addressStr");
            addressString = locationMap.get("addressStr");
            latitude = Double.parseDouble(locationMap.get("lat"));
            longitude = Double.parseDouble(locationMap.get("lng"));
            marker.remove();


            setUpMap(latitude, longitude, markerTitle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only call setUpmap
     * if it is not null.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(latitude, longitude, "Current Location");
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called when we are sure that map is not null.
     */
    private void setUpMap(double latitude, double longitude, String titleStr) {

        mMap.getUiSettings().setMapToolbarEnabled(false);

        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(titleStr));
        marker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.9f));

        if(circle!=null){
            createGeofenceCircle();
        }

    }
}
