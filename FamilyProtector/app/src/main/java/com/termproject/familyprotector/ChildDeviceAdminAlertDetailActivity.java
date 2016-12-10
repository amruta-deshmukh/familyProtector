package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildDeviceAdminAlertDetailActivity extends AppCompatActivity {
    private UserLocalStore userLocalStore;
    private String childName, userName, alertShareStr, dateStr, timeStr, alertString, objectIdStr;
    private User user;
    private TextView alertHeader,alertDate,alertTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_device_admin_alert_detail);
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
            alertString = childName+ " tried uninstalling Family Protector";
            dateStr = intent.getStringExtra("alertDate");
            timeStr = intent.getStringExtra("alertTime");
            objectIdStr = intent.getStringExtra("objectId");
        }
        alertHeader = (TextView)findViewById(R.id.text_uninstall_alert_header_string);
        alertDate = (TextView)findViewById(R.id.text_uninstall_alert_date_string);
        alertTime = (TextView)findViewById(R.id.text_uninstall_alert_Time_string);



        alertHeader.setText(alertString);
        alertDate.setText(dateStr);
        alertTime.setText(timeStr);

        //inorder to share with another parent

        alertShareStr = childName+" tried to uninstall Family Protector "+
                " on "+dateStr+" at "+timeStr+ ".";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_device_admin_alert_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.device_admin_action_share);
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
                onBackPressed();
                return true;
            case R.id.device_admin_action_delete:
//                addWebsiteAlertToParse();
                deleteItemFromParse();
                Toast.makeText(this, "Un-installation Alert Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ChildDetailActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createAlertIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,alertShareStr);
        return shareIntent;
    }
    private void deleteItemFromParse(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildDeviceAdminAlerts");
        query.whereEqualTo("objectId", objectIdStr);
        Log.v("objectId", objectIdStr);

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

        ParseObject childWebsiteAlerts = new ParseObject("ChildDeviceAdminAlerts");
        childWebsiteAlerts.put("userName", userName);
        childWebsiteAlerts.put("childName", childName);
        childWebsiteAlerts.put("alertTime","8:10 PM");
        childWebsiteAlerts.put("alertDate", "11-05-2016");
        childWebsiteAlerts.saveInBackground();


    }
}
