package com.termproject.familyprotector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ChildDetailActivity extends AppCompatActivity {

    private String childNameStr, childNameTitle;

    private UserLocalStore userLocalStore;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);
        mTabLayout = (TabLayout) findViewById(R.id.child_detail_tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("Alerts"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Rules"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Current\nLoc"));
        mTabLayout.setTabTextColors(Color.WHITE, Color.BLACK);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        userLocalStore = new UserLocalStore(this);
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            childNameStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//            userLocalStore.setChildDetails(childNameStr);
//        }
        childNameTitle = userLocalStore.getChildDetails();
        setTitle(childNameTitle + " Details");

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(resize(drawable));

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.child_detail_pager);
        ChildDetailPagerAdapter mPagerAdapter = new ChildDetailPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                userLocalStore.setChildDetails("");
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_profile_edit:
                startActivity(new Intent(this,ChildProfileAccSettings.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
