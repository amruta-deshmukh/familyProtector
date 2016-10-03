package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class ChildLocationRuleSaveActivity extends AppCompatActivity implements View.OnClickListener{

    EditText locationNameEditText;
    TextView addressText, perimeterText;
    TimePicker ruleLocationFromTime, ruleLocationToTime;
    Button saveButton;
    UserLocalStore userLocalStore;
    private MultiSelectionSpinner multiSelectionSpinner;
    double latitude,longitude;
    String locationNameStr,addressString,childName,username;
    float locationPerimeterValue;
    User user;
    String strStartTime,strEndTime, selectedDays;
    String[] days;
    int ruleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        childName = userLocalStore.getChildDetails();
        addressString = userLocalStore.getLocationAddress();
        locationPerimeterValue = userLocalStore.getLocationPerimeter();
        setTitle("Rule for " + childName);
        setContentView(R.layout.activity_child_location_rule_save);
        locationNameEditText = (EditText)findViewById(R.id.edit_location_name);
        ruleLocationFromTime = (TimePicker)findViewById(R.id.location_timepicker_from);
        ruleLocationToTime = (TimePicker)findViewById(R.id.location_timepicker_to);
        saveButton = (Button)findViewById(R.id.rule_location_save_button);
        addressText = (TextView)findViewById(R.id.text_address_string);
        perimeterText = (TextView)findViewById(R.id.location_perimeter_value);
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
        if(ruleLocationFromTime.getCurrentMinute()!=0) {
            strStartTime = ruleLocationFromTime.getCurrentHour() + ":" + ruleLocationFromTime.getCurrentMinute();
        } else{
            strStartTime = ruleLocationFromTime.getCurrentHour() + ":" + "00";
        }
        if(ruleLocationToTime.getCurrentMinute()!=0) {
            strEndTime = ruleLocationToTime.getCurrentHour() + ":" + ruleLocationToTime.getCurrentMinute();
        } else{
            strStartTime = ruleLocationFromTime.getCurrentHour() + ":" + "00";
        }
        selectedDays =  multiSelectionSpinner.getSelectedItemsAsString();
        days = selectedDays.split(",");
        latitude =userLocalStore.getLocationLatitude();
        longitude = userLocalStore.getLocationLongitude();
        locationNameStr = locationNameEditText.getText().toString();
        ruleId = userLocalStore.getRuleLocationId();
        ruleId = ruleId+1;
        userLocalStore.setRuleLocationId(ruleId);



        if(locationNameStr.matches("")){
            locationNameStr = "rule location";
        }

        childName = userLocalStore.getChildDetails();
        user = userLocalStore.getLoggedInUser();
        saveRuleLocationToParse();
        startActivity(new Intent(this,ParentHomeScreen.class));
    }

    private void saveRuleLocationToParse (){

        ParseObject ruleLocation = new ParseObject("ChildRuleLocation");
        ParseGeoPoint ruleLatLng = new ParseGeoPoint(latitude,longitude);
        ruleLocation.put("userName",user.getUsername());
        ruleLocation.put("childName",childName);
        ruleLocation.put("locationName",locationNameStr);
        ruleLocation.put("locationAddress", addressString);
        ruleLocation.put("geopoint", ruleLatLng);
        ruleLocation.put("ruleFromTime",strStartTime);
        ruleLocation.put("ruleToTime", strEndTime);
        ruleLocation.put("locationRadius", locationPerimeterValue);
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

}
