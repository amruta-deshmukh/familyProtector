package com.termproject.familyprotector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class ChildAlertFragment extends Fragment {

    private static final int DATASET_COUNT = 10;

    UserLocalStore userLocalStore;
    String childName;
    private RecyclerView mRecyclerView;
    private ChildAlertRecylerAdapter mAdapter;
    Context context;
    RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private User user;
    Spinner childAlertSpinner;
    TextView noAlertSelected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        userLocalStore = new UserLocalStore(getActivity());
        user = userLocalStore.getLoggedInUser();
        childName = userLocalStore.getChildDetails();
        initDataset();
//        getChildLocationAlertFromParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child_alert, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.child_alert_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        childAlertSpinner = (Spinner)view.findViewById(R.id.alert_spinner);
        noAlertSelected = (TextView)view.findViewById(R.id.no_alert_selected);
        noAlertSelected.setVisibility(View.VISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.child_alert_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childAlertSpinner.setAdapter(adapter);
        childAlertSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        return view;
    }

    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

    private void getChildLocationAlertFromParse() {
        final String alertType = "loc";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildAlerts");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", childName);
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childAlertsFromParse, ParseException e) {

                if (e == null) {


                    if (childAlertsFromParse.size() > 0) {
                        mAdapter = new ChildAlertRecylerAdapter(getActivity(), childAlertsFromParse, childName, alertType);
                        // Set CustomAdapter as the adapter for RecyclerView.
                        mRecyclerView.setAdapter(mAdapter);

                    }
                    else {

                    }
                }
                else {
                    Log.d("childAlertfragment", "Error in fetching data from parse"+ e.toString());
                }

            }
        });
    }


    private void getChildWebAlertFromParse(){

        final String alertType = "web";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildWebsiteAlerts");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", childName);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childAlertsFromParse, ParseException e) {

                if (e == null) {


                    if (childAlertsFromParse.size() > 0) {
                        mAdapter = new ChildAlertRecylerAdapter(getActivity(), childAlertsFromParse, childName, alertType);
                        // Set CustomAdapter as the adapter for RecyclerView.
                        mRecyclerView.setAdapter(mAdapter);

                    } else {

                    }
                } else {
                    Log.d("childAlertfragment", "Error in fetching data from parse" + e.toString());
                }

            }
        });

    }

    public class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();
            if(selected.equals("Location")){
                noAlertSelected.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                getChildLocationAlertFromParse();

            }
            else if (selected.equals("Web Access")){
                noAlertSelected.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                getChildWebAlertFromParse();

            }
            else if (selected.equals("See alerts for...")){
                mRecyclerView.setVisibility(View.GONE);
                noAlertSelected.setVisibility(View.VISIBLE);



            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


}
