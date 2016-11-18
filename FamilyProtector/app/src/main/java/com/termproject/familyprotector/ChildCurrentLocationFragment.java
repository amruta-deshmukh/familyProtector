package com.termproject.familyprotector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ChildCurrentLocationFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    double latitude, longitude;
    UserLocalStore userLocalStore;
    ParseGeoPoint currLocLatLng;
    boolean resultFound = false;
    private GPSTracker gps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_current_location, container, false);
        mMapView = (MapView) view.findViewById(R.id.current_location_map_view);
        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());
//        getChildCurrentLocationFromParse();
        User user = userLocalStore.getLoggedInUser();
        final String childName = userLocalStore.getChildDetails();
        final String userName = user.getUsername();
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
                                MarkerOptions marker = new MarkerOptions().position(
                                        new LatLng(latitude, longitude)).title("Hello Maps");
                                googleMap.addMarker(marker);
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(latitude, longitude)).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                            }
                        } else {
                            latitude = 37.7238566;
                            longitude = -122.4762807;
                            MarkerOptions marker = new MarkerOptions().position(
                                    new LatLng(latitude, longitude)).title("No location");
                            googleMap.addMarker(marker);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude)).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                        }
                    }
                });

            }
        });
        return view;
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


    private void setupMapFrag(){

    }

}
