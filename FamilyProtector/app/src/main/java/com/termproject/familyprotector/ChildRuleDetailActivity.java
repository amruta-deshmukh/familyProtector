package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildRuleDetailActivity extends AppCompatActivity {

    private String locationStr,addressStr,perimeterStr, daysStr, timeStr, objectIdStr,
            childName, userName;
    private User user;
    private TextView locationText, addressText, perimeterText, daysText, timeText;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_rule_detail);
        userLocalStore = new UserLocalStore(this);


        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        childName = userLocalStore.getChildDetails();
        setTitle(childName+" Rule Detail");
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();

        Intent intent = getIntent();
        if (intent != null) {
            addressStr = intent.getStringExtra("ruleAddress");
            daysStr = intent.getStringExtra("days");
            timeStr = intent.getStringExtra("time");
            locationStr = intent.getStringExtra("location");
            perimeterStr = intent.getStringExtra("perimeter");
            objectIdStr = intent.getStringExtra("objectId");

        }
        locationText= (TextView)findViewById(R.id.text_rule_location_string);
        addressText= (TextView)findViewById(R.id.text_rule_address_string);
        perimeterText= (TextView)findViewById(R.id.text_rule_perimter_string);
        daysText= (TextView)findViewById(R.id.text_rule_days_string);
        timeText= (TextView)findViewById(R.id.text_rule_time_string);

        locationText.setText(locationStr);
        addressText.setText(addressStr);
        perimeterText.setText(perimeterStr+" meters");
        daysText.setText(daysStr);
        timeText.setText(timeStr);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_rule_detail, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.location_rule_action_delete:
//                addWebsiteAlertToParse();
                deleteItemFromParse();
                Toast.makeText(this, "Location Rule Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ChildDetailActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteItemFromParse(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("objectId", objectIdStr);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {
                    try {
                        parseObject.delete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    parseObject.saveInBackground();

                }
            }
        });

    }


    private void addWebsiteAlertToParse(){


        ParseObject childWebsiteAlerts = new ParseObject("ChildRuleLocation");
        ParseGeoPoint geopoint= new ParseGeoPoint(37.7238566,-122.4784694);
        childWebsiteAlerts.put("userName", userName);
        childWebsiteAlerts.put("childName", childName);
        childWebsiteAlerts.put("Monday","Yes");
        childWebsiteAlerts.put("Saturday","Yes");
        childWebsiteAlerts.put("Sunday","Yes");
        childWebsiteAlerts.put("geopoint",geopoint);
        childWebsiteAlerts.put("locationAddress","San Francisco " +
                "State University, Gymnasium, 1600 Holloway Ave, San Francisco, CA 94132, USA");
        childWebsiteAlerts.put("locationName","University");
        childWebsiteAlerts.put("locationRadius",25.5);
        childWebsiteAlerts.put("ruleFromTime","14:30");
        childWebsiteAlerts.put("ruleToTime","15:30");
        childWebsiteAlerts.put("ruleLocationId", 2);
        childWebsiteAlerts.saveInBackground();



    }


}
