package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ChildLocationRuleSaveActivity extends AppCompatActivity implements View.OnClickListener{

    EditText locationNameEditText;
    TextView addressText, perimeterText;
    TimePicker ruleLocationFromTime, ruleLocationToTime;
    Button saveButton;
    UserLocalStore userLocalStore;
    private MultiSelectionSpinner multiSelectionSpinner;
    double latitude,longitude;
    private String locationNameStr,addressString,childName,userName;
    float locationPerimeterValue;
    User user;
    private String strStartTime,strEndTime, selectedDays;
    private String[] days;
    private int ruleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        childName = userLocalStore.getChildDetails();
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();
        generateRuleId();
//        addressString = userLocalStore.getLocationAddress();
//        locationPerimeterValue = userLocalStore.getLocationPerimeter();
//        addressText.setText(addressString);
//        perimeterText.setText(locationPerimeterValue + " meters");
        setTitle(childName+"'s Location Rule Details");
        setContentView(R.layout.activity_child_location_rule_save);
        locationNameEditText = (EditText)findViewById(R.id.edit_location_name);
        ruleLocationFromTime = (TimePicker)findViewById(R.id.location_timepicker_from);
        ruleLocationToTime = (TimePicker)findViewById(R.id.location_timepicker_to);
        saveButton = (Button)findViewById(R.id.rule_location_save_button);
        addressText = (TextView)findViewById(R.id.text_address_string);
        perimeterText = (TextView)findViewById(R.id.location_perimeter_value);


        Intent intent = getIntent();
        if (intent != null) {
            addressString = intent.getStringExtra("addressStr");
            locationPerimeterValue = intent.getFloatExtra("locPerimeter", 30f);
            latitude = intent.getDoubleExtra("locLat", 37.7238566);
            longitude = intent.getDoubleExtra("locLng", -122.4784694);

        }
        if(addressString.matches("")){
            addressString = "Thornton Hall, Holloway Avenue, San Francisco, CA";
        }
        addressText.setText(addressString);
        perimeterText.setText(locationPerimeterValue + " meters");

        String[] array = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setSelection(new int[]{0});

        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(ruleLocationFromTime.getCurrentMinute()<10) {
            strStartTime = ruleLocationFromTime.getCurrentHour() + ":0" + ruleLocationFromTime.getCurrentMinute();
        } else{
            strStartTime = ruleLocationFromTime.getCurrentHour() + ":" + ruleLocationFromTime.getCurrentMinute();;
        }
        if(ruleLocationToTime.getCurrentMinute()<10) {
            strEndTime = ruleLocationToTime.getCurrentHour() + ":0" + ruleLocationToTime.getCurrentMinute();
        } else{
            strEndTime = ruleLocationToTime.getCurrentHour() + ":" + ruleLocationToTime.getCurrentMinute();
        }
        selectedDays =  multiSelectionSpinner.getSelectedItemsAsString();
        days = selectedDays.split(",");
//        latitude =userLocalStore.getLocationLatitude();
//        longitude = userLocalStore.getLocationLongitude();
        locationNameStr = locationNameEditText.getText().toString();
//        ruleId = userLocalStore.getRuleLocationId();
//        ruleId = ruleId+1;
//        userLocalStore.setRuleLocationId(ruleId);
//        generateRuleId();



        if(locationNameStr.matches("")){
            locationNameEditText.setError("Location name cannot be blank");

        } else {

            saveRuleLocationToParse();
            startActivity(new Intent(this, ChildDetailActivity.class));
        }
    }

    private void saveRuleLocationToParse (){

        ParseObject ruleLocation = new ParseObject("ChildRuleLocation");
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);
        ruleLocation.setACL(postACL);
        ParseGeoPoint ruleLatLng = new ParseGeoPoint(latitude,longitude);
        ruleLocation.put("userName",userName);
        ruleLocation.put("childName",childName);
        ruleLocation.put("locationName",locationNameStr);
        ruleLocation.put("locationAddress", addressString);
        ruleLocation.put("geopoint", ruleLatLng);
        ruleLocation.put("ruleFromTime",strStartTime);
        ruleLocation.put("ruleToTime", strEndTime);
        ruleLocation.put("locationRadius", locationPerimeterValue);
        Log.v("ruleId", ruleId+"");
        ruleLocation.put("ruleLocationId",ruleId);
        for(String day:days){
            switch(day){
                case "Monday":
                    ruleLocation.put("Monday","Yes");
                    break;
                case "Tuesday":
                    ruleLocation.put("Tuesday","Yes");
                    break;
                case "Wednesday":
                    ruleLocation.put("Wednesday","Yes");
                    break;
                case "Thursday":
                    ruleLocation.put("Thursday","Yes");
                    break;
                case "Friday":
                    ruleLocation.put("Friday","Yes");
                    break;
                case "Saturday":
                    ruleLocation.put("Saturday","Yes");
                    break;
                case "Sunday":
                    ruleLocation.put("Sunday","Yes");
                    break;
                case "Weekdays":
                    ruleLocation.put("Monday","Yes");
                    ruleLocation.put("Tuesday","Yes");
                    ruleLocation.put("Wednesday","Yes");
                    ruleLocation.put("Thursday","Yes");
                    ruleLocation.put("Friday","Yes");
                    break;
                case "Weekends":
                    ruleLocation.put("Saturday","Yes");
                    ruleLocation.put("Sunday","Yes");
                    break;
                case "Everyday":
                    ruleLocation.put("Monday","Yes");
                    ruleLocation.put("Tuesday","Yes");
                    ruleLocation.put("Wednesday","Yes");
                    ruleLocation.put("Thursday","Yes");
                    ruleLocation.put("Friday","Yes");
                    ruleLocation.put("Saturday","Yes");
                    ruleLocation.put("Sunday","Yes");
                    break;
            }
        }
        ruleLocation.saveInBackground();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateRuleId() {

        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("ChildLocationRuleId");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);

        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {
                        ruleId = (parseObject.getInt("locRuleId"))+1;
                        parseObject.put("locRuleId",ruleId);
                        parseObject.saveInBackground();
                    }
                } else {

                    ruleId = 1;
                    ParseObject ruleIdObj = new ParseObject("ChildLocationRuleId");
                    ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                    postACL.setPublicReadAccess(true);
                    postACL.setPublicWriteAccess(true);
                    ruleIdObj.setACL(postACL);
                    ruleIdObj.put("userName", userName);
                    ruleIdObj.put("childName", childName);
                    ruleIdObj.put("locRuleId",ruleId);
                    ruleIdObj.saveInBackground();

                }
                Log.v("ruleId inside", ruleId+"***");
            }
        });
    }

}
