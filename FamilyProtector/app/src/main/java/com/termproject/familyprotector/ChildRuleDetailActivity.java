package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ChildRuleDetailActivity extends AppCompatActivity {

    String locationStr,addressStr,perimeterStr, daysStr, timeStr;
    TextView locationText, addressText, perimeterText, daysText, timeText;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_rule_detail);
        userLocalStore = new UserLocalStore(this);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(userLocalStore.getChildDetails()+" Rule Detail");

        Intent intent = getIntent();
        if (intent != null) {
            addressStr = intent.getStringExtra("ruleAddress");
            daysStr = intent.getStringExtra("days");
            timeStr = intent.getStringExtra("time");
            locationStr = intent.getStringExtra("location");
            perimeterStr = intent.getStringExtra("perimeter");

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
        timeText.setText(timeStr + " hrs");

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
}
