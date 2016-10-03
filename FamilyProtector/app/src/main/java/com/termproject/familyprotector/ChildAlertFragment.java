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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        userLocalStore = new UserLocalStore(getActivity());
        user = userLocalStore.getLoggedInUser();
        childName = userLocalStore.getChildDetails();
        initDataset();
        getChildAlertFromParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child_alert, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.child_alert_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

    private void getChildAlertFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildAlerts");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", childName);
        query.orderByDescending("createdAt");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childAlertsFromParse, ParseException e) {

                if (e == null) {


                    if (childAlertsFromParse.size() > 0) {
                        mAdapter = new ChildAlertRecylerAdapter(getActivity(), childAlertsFromParse, childName);
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


}
