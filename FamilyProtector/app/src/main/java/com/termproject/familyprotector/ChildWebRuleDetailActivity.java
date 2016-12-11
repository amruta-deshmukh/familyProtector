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
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildWebRuleDetailActivity extends AppCompatActivity {

    private User user;
    private UserLocalStore userLocalStore;
    private String categoryStr, dateStr, childName, userName, catDescStr;
    private TextView categoryText, dateText, catDescText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_web_rule_detail);
        userLocalStore = new UserLocalStore(this);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        childName = userLocalStore.getChildDetails();
        setTitle(childName + " Rule Detail");
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();

        Intent intent = getIntent();
        if (intent != null) {
            categoryStr = intent.getStringExtra("category");
            dateStr = intent.getStringExtra("lastUpdated");

        }

        checkCatDesc();

        categoryText = (TextView) findViewById(R.id.text_web_cat_name_string);
        dateText = (TextView) findViewById(R.id.text_web_cat_last_updated_string);
        catDescText = (TextView) findViewById(R.id.text_web_cat_desc_string);

        categoryText.setText(categoryStr);
        dateText.setText(dateStr);
        catDescText.setText(catDescStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_web_rule_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.web_rule_action_delete:
//                addWebsiteAlertToParse();
                deleteItemFromParse();
                Toast.makeText(this, "Website Category Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ChildDetailActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void deleteItemFromParse(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleWebsite");
        query.whereEqualTo("userName", userName);
        query.whereEqualTo("childName", childName);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {


                } else {
                    if (categoryStr.matches("Gambling")) {
                        parseObject.put("gambling", "No");
                    } else if (categoryStr.matches("Hacking")) {
                        parseObject.put("hacking", "No");
                    } else if (categoryStr.matches("Social Networking")) {
                        parseObject.put("social", "No");
                    } else if (categoryStr.matches("Chat and Messaging")) {
                        parseObject.put("chat", "No");
                    } else if (categoryStr.matches("Media Sharing")) {
                        parseObject.put("mediaSharing", "No");
                    } else if (categoryStr.matches("Adult")) {
                        parseObject.put("adult", "No");
                    } else if (categoryStr.matches("Abortion")) {
                        parseObject.put("abortion", "No");
                    } else if (categoryStr.matches("Drugs")) {
                        parseObject.put("drugs", "No");
                    } else if (categoryStr.matches("Alcohol & Tobacco")) {
                        parseObject.put("alcohol", "No");
                    } else if (categoryStr.matches("Weapons")) {
                        parseObject.put("weapons", "No");
                    } else if (categoryStr.matches("Proxy & Web Filter Avoidance")) {
                        parseObject.put("proxy", "No");
                    } else if (categoryStr.matches("Illegal Content")) {
                        parseObject.put("illegal", "No");
                    } else if (categoryStr.matches("News & Media")) {
                        parseObject.put("newsMedia", "No");
                    } else if (categoryStr.matches("Shopping")) {
                        parseObject.put("shopping", "No");
                    } else if (categoryStr.matches("Games")) {
                        parseObject.put("games", "No");
                    } else if (categoryStr.matches("Virtual Reality")) {
                        parseObject.put("virtualReality", "No");
                    }
                    parseObject.saveInBackground();

                }
            }
        });

    }




    private void addWebsiteAlertToParse(){


        ParseObject childWebsiteAlerts = new ParseObject("ChildRuleWebsite");
        childWebsiteAlerts.put("userName", userName);
        childWebsiteAlerts.put("childName", childName);
        childWebsiteAlerts.put("gambling","Yes");
        childWebsiteAlerts.put("hacking","Yes");
        childWebsiteAlerts.put("social","Yes");
        childWebsiteAlerts.put("chat","Yes");
        childWebsiteAlerts.put("mediaSharing","Yes");
        childWebsiteAlerts.put("adult","Yes");
        childWebsiteAlerts.put("abortion","Yes");
        childWebsiteAlerts.put("drugs","Yes");
        childWebsiteAlerts.put("alcohol","Yes");
        childWebsiteAlerts.put("weapons","Yes");
        childWebsiteAlerts.put("proxy","Yes");
        childWebsiteAlerts.put("illegal","Yes");
        childWebsiteAlerts.put("newsMedia","Yes");
        childWebsiteAlerts.put("shopping","Yes");
        childWebsiteAlerts.put("games","Yes");
        childWebsiteAlerts.put("virtualReality","Yes");

        childWebsiteAlerts.saveInBackground();
    }


    private void checkCatDesc() {

        if (categoryStr.matches("Gambling")) {
            catDescStr = "Sites that allow a visitor to play games using wagers/placing " +
                    "bets, lottery pools, or provides information on such activities.";
        } else if (categoryStr.matches("Hacking")) {
            catDescStr = "Sites that disseminate information, hold discussions, " +
                    "or provide a means to gain unauthorized or illegal access to " +
                    "computers and networks.";
        } else if (categoryStr.matches("Social Networking")) {
            catDescStr = "Sites that provide a community portal whereby members join " +
                    "and contribute posts or media and forge connections with other members.";
        } else if (categoryStr.matches("Chat and Messaging")) {
            catDescStr = "Sites which provide chat or text messaging services or such " +
                    "abilities through a download or application.";
        } else if (categoryStr.matches("Media Sharing")) {
            catDescStr = "Sites that allow visitors to upload content and share media " +
                    "such as photos and videos.";
        } else if (categoryStr.matches("Adult")) {
            catDescStr = "Sites which may contain sexually explicit content, images, " +
                    "or that are portrayed through visually expressive language.";
        } else if (categoryStr.matches("Abortion")) {
            catDescStr = "Sites which provide views either in favor or against " +
                    "abortion, provide details on procedures, offer help or discuss " +
                    "outcomes or consequences of abortion.";
        } else if (categoryStr.matches("Drugs")) {
            catDescStr = "Sites that contain content whose main focus is on controlled " +
                    "substances, including the sale, discussion, or glorification of such " +
                    "substances. Does not include alcohol and tobacco as that has " +
                    "its own category.";
        } else if (categoryStr.matches("Alcohol & Tobacco")) {
            catDescStr = "Sites that sell, discuss, or glorify the consumption of " +
                    "various alcoholic and tobacco products, including beer, wine, and liquor.";
        } else if (categoryStr.matches("Weapons")) {
            catDescStr = "Sites that primarily discuss, review, or sell items such as " +
                    "hunting knives, guns, rifles, or BB guns.";
        } else if (categoryStr.matches("Proxy & Web Filter Avoidance")) {
            catDescStr = "Sites that provides information or a means to circumvent " +
                    "filtering proxies or detection systems, including VPN services " +
                    "and anonymous surfing.";
        } else if (categoryStr.matches("Illegal Content")) {
            catDescStr = "Sites that focus on providing links to pirated movies, " +
                    "commercial software, or providing application keys and cracks " +
                    "for commercial applications.";
        } else if (categoryStr.matches("News & Media")) {
            catDescStr = "Sites whose content is mostly focused on current events " +
                    "and topics. Includes various news outlets, radio, TV stations, " +
                    "and magazines.";
        } else if (categoryStr.matches("Shopping")) {
            catDescStr = "Sites that sell products or services, normally with an " +
                    "online purchasing interface.";
        } else if (categoryStr.matches("Games")) {
            catDescStr = "Sites that provide games, including online games " +
                    "or through an application.";
        } else if (categoryStr.matches("Virtual Reality")) {
            catDescStr = "Sites that host files specific to virtual reality or " +
                    "run communities related to the technology.";
        }


    }
}
