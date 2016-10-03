package com.termproject.familyprotector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ChildHomeScreen extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ChildRecyclerAdapter mAdapter;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home_screen);
        userLocalStore = new UserLocalStore(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.child_screen_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getChildrenDetailsFromParse();
    }

    private void getChildrenDetailsFromParse() {
        User storedUSer = userLocalStore.getLoggedInUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", storedUSer.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> children, ParseException e) {
                if (e == null) {
                    if (children.size() > 0) {

                        mAdapter = new ChildRecyclerAdapter (ChildHomeScreen.this, children);
                        mRecyclerView.setAdapter(mAdapter);


                    }
                } else {
                    Log.d("Login", "Error: " + e.getMessage());
                }

            }
        });
    }

}
