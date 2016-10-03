package com.termproject.familyprotector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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



public class ChildRuleFragment extends Fragment {
    FloatingActionButton addRuleFloatingActionButton;
    private static final int DATASET_COUNT = 10;

    UserLocalStore userLocalStore;
    String childName;
    private RecyclerView mRecyclerView;
    private ChildRuleRecyclerAdapter mAdapter;
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
        getChildRulesFromParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_rule, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.child_rule_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addRuleFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_rule_floating_action_button);
        addRuleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }


    private void getChildRulesFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", childName);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childRulesFromParse, ParseException e) {
                if (e == null) {
                    if (childRulesFromParse.size() > 0) {
                        mAdapter = new ChildRuleRecyclerAdapter(getActivity(),childRulesFromParse, childName);
                        // Set CustomAdapter as the adapter for RecyclerView.
                        mRecyclerView.setAdapter(mAdapter);

                    }
                    else {

                    }
                }
                else {
                    Log.e("Child Rule Fragment", "error in fetching"+e.toString());
                }

            }
        });
    }

}
