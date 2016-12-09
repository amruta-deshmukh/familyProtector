package com.termproject.familyprotector;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChildWebAlertDetailActivity extends AppCompatActivity {

    private UserLocalStore userLocalStore;
    private String childName, userName, ruleIdStr, urlDateStr, urlNameStr, urlCategoriesStr, urlTimeStr, urlAlertStr;
    private User user;
    private TextView urlName,urlCategories, urlAlertDate,urlAlertTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_web_alert_detail);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        userLocalStore = new UserLocalStore(this);
        childName = userLocalStore.getChildDetails();
        setTitle(childName+"'s Web Alert "+"Details");
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();
        Intent intent = getIntent();
        if (intent != null) {
            urlNameStr = intent.getStringExtra("urlName");
            urlCategoriesStr = intent.getStringExtra("categoriesList");
            urlDateStr = intent.getStringExtra("visitedDate");
            urlTimeStr = intent.getStringExtra("visitedTime");
        }
        urlName = (TextView)findViewById(R.id.text_alert_url_string);
        urlCategories =  (TextView)findViewById(R.id.text_alert_url_categories_string);
        urlAlertDate = (TextView)findViewById(R.id.text_alert_url_date_string);
        urlAlertTime = (TextView)findViewById(R.id.text_alert_url_time_string);



        urlName.setText(urlNameStr);
        urlCategories.setText(urlCategoriesStr);
        urlAlertDate.setText(urlDateStr);
        urlAlertTime.setText(urlTimeStr);

        urlAlertStr = childName+" visited "+ urlNameStr +" (categories: "+ urlCategoriesStr + ") on "+urlDateStr+" at "+urlTimeStr+ ".";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_web_alert_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.url_action_share);
        ShareActionProvider mshareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mshareActionProvider!=null){
            mshareActionProvider.setShareIntent(createUrlAlertIntent());

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createUrlAlertIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,urlAlertStr);
        return shareIntent;
    }
}
