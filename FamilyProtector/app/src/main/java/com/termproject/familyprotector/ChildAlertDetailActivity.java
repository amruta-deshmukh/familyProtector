package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChildAlertDetailActivity extends AppCompatActivity {

    UserLocalStore userLocalStore;
    String childName, userName, ruleIdStr, dateStr, addressStr, timeStr, locationStr, alertString;
    User user;
    TextView alertLocation,alertAddress, alertDate,alertTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_alert_detail);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        userLocalStore = new UserLocalStore(this);
        childName = userLocalStore.getChildDetails();
        setTitle(childName+" Alert "+"Details");
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();
        Intent intent = getIntent();
        if (intent != null) {
            addressStr = intent.getStringExtra("alertAddress");
            dateStr = intent.getStringExtra("dateStr");
            timeStr = intent.getStringExtra("timeStr");
            locationStr = intent.getStringExtra("location");
        }
        alertLocation = (TextView)findViewById(R.id.text_alert_location_string);
        alertAddress =  (TextView)findViewById(R.id.text_alert_address_string);
        alertDate = (TextView)findViewById(R.id.text_alert_date_string);
        alertTime = (TextView)findViewById(R.id.text_alert_time_string);



        alertLocation.setText(locationStr);
        alertAddress.setText(addressStr);
        alertDate.setText(dateStr);
        alertTime.setText(timeStr);

        //inorder to share with another parent

        alertString = childName+" left from "+locationStr+" ("+addressStr+" )"+" on "+dateStr+" at "+timeStr+ ".";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_alert_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mshareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mshareActionProvider!=null){
            mshareActionProvider.setShareIntent(createAlertIntent());

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

    private Intent createAlertIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,alertString);
        return shareIntent;
    }
}
