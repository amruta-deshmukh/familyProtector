package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AddChildDetails extends AppCompatActivity implements View.OnClickListener{
    EditText editTextChildName;
    RadioButton radioButtonGender;
    Button buttonSave;
    DatePicker childBirthDay;
    RadioGroup childGenderRadioGroup;
    String childNameStr, childBirthDateStr, childGenderStr;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_details);
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        userLocalStore = new UserLocalStore(this);
        init();

        buttonSave.setOnClickListener(this);
    }

    private void init(){

        editTextChildName = (EditText)findViewById(R.id.child_name_edit_text);
        childBirthDay = (DatePicker)findViewById(R.id.datepicker_childbirthdate);
        childGenderRadioGroup = (RadioGroup)findViewById(R.id.child_gender_radio_group);
        buttonSave = (Button)findViewById(R.id.button_save);
    }

    @Override
    public void onClick(View view){

        childNameStr = editTextChildName.getText().toString();
        if(!(childNameStr.matches("")) && childGenderRadioGroup.getCheckedRadioButtonId()!=-1){
            childNameStr = childNameStr.substring(0,1).toUpperCase() + childNameStr.substring(1);
            int genderId= childGenderRadioGroup.getCheckedRadioButtonId();
//            radioButtonGender = (RadioButton)childGenderRadioGroup.indexOfChild(genderId);
            if (genderId == R.id.radio_button_male){
                childGenderStr = "Male";
            }
            else{
                childGenderStr = "Female";
            }

            childBirthDateStr = (childBirthDay.getMonth()+1)+"-"+childBirthDay.getDayOfMonth()+"-"+childBirthDay.getYear();
            storeToParse();
            startActivity(new Intent(this,ParentHomeScreen.class));

        }
        else {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeToParse (){
        User storedUser = userLocalStore.getLoggedInUser();
        ParseObject childDetails = new ParseObject("ChildDetails");
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);
        childDetails.setACL(postACL);
        childDetails.put("username", storedUser.getUsername());
        childDetails.put("name",childNameStr);
        childDetails.put("gender", childGenderStr);
        childDetails.put("birthdate", childBirthDateStr);
        childDetails.saveInBackground();

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
