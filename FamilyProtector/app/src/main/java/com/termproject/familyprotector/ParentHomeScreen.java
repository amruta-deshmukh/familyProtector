package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ParentHomeScreen extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton addChildFab;
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_screen);
        addChildFab = (FloatingActionButton)findViewById(R.id.add_child_fab);
        mRecyclerView = (RecyclerView)findViewById(R.id.parent_screen_recycler_view);
        userLocalStore = new UserLocalStore(this);
        setTitle("Parent Mode");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getChildrenDetailsFromParse();


        try {

            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e("Parent screen", e.toString());
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerRoot);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolBar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView nv = (NavigationView) findViewById(R.id.navView);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String txt;
                switch (menuItem.getItemId()) {
                    case R.id.logout_drawer:
                        userLocalStore.setUserLoggedIn(false);
                        userLocalStore.setAppMode("");
                        userLocalStore.setChildDetails("");
                        Intent intent = new Intent(ParentHomeScreen.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.about_drawer:
                        startActivity(new Intent(ParentHomeScreen.this, AboutActivity.class));
                        break;
                    case R.id.help_drawer:
                        startActivity(new Intent(ParentHomeScreen.this,Help.class));
                        break;
                    case R.id.settings_drawer:
                        startActivity(new Intent(ParentHomeScreen.this,ParentProfileSettings.class));
                        break;

                    default:
                        txt = "Please wait for some time";
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });



        addChildFab.setOnClickListener(this);


    }

    public void onClick(View view){
        Intent intent = new Intent(ParentHomeScreen.this, AddChildDetails.class);
        startActivity(intent);

    }


    private void getChildrenDetailsFromParse() {
        User storedUser = userLocalStore.getLoggedInUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDetails");
        query.whereEqualTo("username", storedUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> children, ParseException e) {
                if (e == null) {
                    if (children.size() > 0) {

                        mAdapter = new RecyclerAdapter (ParentHomeScreen.this, children);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    Log.d("Login", "Error: " + e.getMessage());
                }

            }
        });
    }



    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


}
