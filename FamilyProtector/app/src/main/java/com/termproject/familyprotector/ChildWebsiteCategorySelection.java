package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildWebsiteCategorySelection extends AppCompatActivity {

    UserLocalStore userLocalStore;
    private CheckBox catGambling, catHacking, catSocial, catChat, catMediaSharing,
            catAdult, catAbortion, catDrugs, catAlcohol, catWeapons,
            catProxy, catIllegal, catNewsMedia, catShopping, catGames, catVirtual;
    private boolean gambling=false, hacking=false, social=false, chat=false, mediaSharing=false,
            adult=false, abortion=false,drugs=false, alcohol=false, weapons=false,
            proxy=false, illegal=false, newsMedia=false, shopping=false, games=false, virtual=false;
    private Button btnSave;
    private String childName, userName;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        childName = userLocalStore.getChildDetails();
        user = userLocalStore.getLoggedInUser();
        userName = user.getUsername();
        getChildCheckedWebsiteFromParse();
        setTitle(childName + "'s web rules");
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_child_website_category_selection);
        init();
//        getChildCheckedWebsiteFromParse();

        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                saveChildWebsiteRuleToParse();
                Intent intent = new Intent(ChildWebsiteCategorySelection.this, ChildDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        catGambling = (CheckBox) findViewById(R.id.checkbox_cat_gambling);
        catHacking = (CheckBox) findViewById(R.id.checkbox_cat_hacking);
        catSocial = (CheckBox) findViewById(R.id.checkbox_cat_social);
        catChat = (CheckBox) findViewById(R.id.checkbox_cat_chat);
        catMediaSharing = (CheckBox) findViewById(R.id.checkbox_cat_media_sharing);
        catAdult = (CheckBox) findViewById(R.id.checkbox_cat_adult);
        catAbortion = (CheckBox) findViewById(R.id.checkbox_cat_abortion);
        catDrugs = (CheckBox) findViewById(R.id.checkbox_cat_drugs);
        catAlcohol = (CheckBox) findViewById(R.id.checkbox_cat_alcohol_and_tobacco);
        catWeapons = (CheckBox) findViewById(R.id.checkbox_cat_weapons);
        catProxy = (CheckBox) findViewById(R.id.checkbox_cat_proxyandfilteravoidance);
        catIllegal = (CheckBox) findViewById(R.id.checkbox_cat_illegalcontent);
        catNewsMedia = (CheckBox) findViewById(R.id.checkbox_cat_newsandmedia);
        catShopping = (CheckBox) findViewById(R.id.checkbox_cat_shopping);
        catGames = (CheckBox) findViewById(R.id.checkbox_cat_games);
        catVirtual = (CheckBox) findViewById(R.id.checkbox_cat_virtual_reality);

        btnSave = (Button) findViewById(R.id.rule_website_save_button);

    }

    private void setCheckBox(){

        if(gambling){
            catGambling.setChecked(true);
        }
        if(hacking){
            catHacking.setChecked(true);
        }
        if(social){
            catSocial.setChecked(true);
        }
        if(chat) {
            catChat.setChecked(true);
        }
        if(mediaSharing) {
            catMediaSharing.setChecked(true);
        }
        if(adult) {
            catAdult.setChecked(true);
        }
        if(abortion) {
            catAbortion.setChecked(true);
        }
        if(drugs) {
            catDrugs.setChecked(true);
        }
        if(alcohol) {
            catAlcohol.setChecked(true);
        }
        if(weapons) {
            catWeapons.setChecked(true);
        }
        if(proxy) {
            catProxy.setChecked(true);
        }
        if(illegal) {
            catIllegal.setChecked(true);
        }
        if(newsMedia) {
            catNewsMedia.setChecked(true);
        }
        if(shopping) {
            catShopping.setChecked(true);
        }
        if(games) {
            catGames.setChecked(true);
        }
        if(virtual) {
            catVirtual.setChecked(true);
        }

    }

    private void getChildCheckedWebsiteFromParse(){
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("ChildRuleWebsite");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {

                        if(parseObject.getString("gambling").equals("Yes")){

                            gambling = true;
                        }
                        if(parseObject.getString("hacking").equals("Yes")){
                            Log.v("status","done");
                            hacking = true;
                        }
                        if(parseObject.getString("social").equals("Yes")){
                            Log.v("status","done");
                            social = true;
                        }
                        if(parseObject.getString("chat").equals("Yes")) {
                            Log.v("status","done");
                            chat = true;
                        }
                        if(parseObject.getString("mediaSharing").equals("Yes")) {
                            Log.v("status","done");
                            mediaSharing = true;
                        }
                        if(parseObject.getString("adult").equals("Yes")) {
                            Log.v("status","done");
                            adult = true;
                        }
                        if(parseObject.getString("abortion").equals("Yes")) {
                            Log.v("status","done");
                            abortion = true;
                        }
                        if(parseObject.getString("drugs").equals("Yes")) {
                            Log.v("status","done");
                            drugs = true;
                        }
                        if(parseObject.getString("alcohol").equals("Yes")) {
                            Log.v("status","done");
                            alcohol = true;
                        }
                        if(parseObject.getString("weapons").equals("Yes")) {
                            Log.v("status","done");
                            weapons = true;
                        }
                        if(parseObject.getString("proxy").equals("Yes")) {
                            Log.v("status","done");
                            proxy = true;
                        }
                        if(parseObject.getString("illegal").equals("Yes")) {
                            Log.v("status","done");
                            illegal = true;
                        }
                        if(parseObject.getString("newsMedia").equals("Yes")) {
                            Log.v("status","done");
                            newsMedia = true;
                        }
                        if(parseObject.getString("shopping").equals("Yes")) {
                            Log.v("status","done");
                            shopping = true;
                        }
                        if(parseObject.getString("games").equals("Yes")) {
                            Log.v("status","done");
                            games = true;
                        }
                        if(parseObject.getString("virtualReality").equals("Yes")) {
                            Log.v("status","done");
                            virtual = true;
                        }
                    }
                }

                setCheckBox();

            }
        });

    }

    private void saveChildWebsiteRuleToParse(){

        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("ChildRuleWebsite");
        queryClass.whereEqualTo("userName", userName);
        queryClass.whereEqualTo("childName", childName);
        Log.v("childName",childName);
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    if (parseObject != null) {

                        Log.v("inside parseObject","parseobject ot null");
                        if(catGambling.isChecked()){
                            parseObject.put("gambling", "Yes");
                        }
                        else{
                            parseObject.put("gambling", "No");
                        }
                        if(catHacking.isChecked()){
                            parseObject.put("hacking", "Yes");
                        }
                        else{
                            parseObject.put("hacking", "No");
                        }
                        if(catSocial.isChecked()){
                            parseObject.put("social", "Yes");
                        }
                        else{
                            parseObject.put("social", "No");
                        }
                        if(catChat.isChecked()){
                            parseObject.put("chat", "Yes");
                        }
                        else{
                            parseObject.put("chat", "No");
                        }
                        if(catMediaSharing.isChecked()){
                            parseObject.put("mediaSharing", "Yes");
                        }
                        else{
                            parseObject.put("mediaSharing", "No");
                        }
                        if(catAdult.isChecked()){
                            parseObject.put("adult", "Yes");
                        }
                        else{
                            parseObject.put("adult", "No");
                        }
                        if(catAbortion.isChecked()){
                            parseObject.put("abortion", "Yes");
                        }
                        else{
                            parseObject.put("abortion", "No");
                        }
                        if(catDrugs.isChecked()){
                            parseObject.put("drugs", "Yes");
                        }
                        else{
                            parseObject.put("drugs", "No");
                        }
                        if(catAlcohol.isChecked()){
                            parseObject.put("alcohol", "Yes");
                        }
                        else{
                            parseObject.put("alcohol", "No");
                        }
                        if(catWeapons.isChecked()){
                            parseObject.put("weapons", "Yes");
                        }
                        else{
                            parseObject.put("weapons", "No");
                        }
                        if(catProxy.isChecked()){
                            parseObject.put("proxy", "Yes");
                        }
                        else{
                            parseObject.put("proxy", "No");
                        }
                        if(catIllegal.isChecked()){
                            parseObject.put("illegal", "Yes");
                        }
                        else{
                            parseObject.put("illegal", "No");
                        }
                        if(catNewsMedia.isChecked()){
                            parseObject.put("newsMedia", "Yes");
                        }
                        else{
                            parseObject.put("newsMedia", "No");
                        }
                        if(catShopping.isChecked()){
                            parseObject.put("shopping", "Yes");
                        }
                        else{
                            parseObject.put("shopping", "No");
                        }
                        if(catGames.isChecked()){
                            parseObject.put("games", "Yes");
                        }
                        else{
                            parseObject.put("games", "No");
                        }
                        if(catVirtual.isChecked()){
                            parseObject.put("virtualReality", "Yes");
                        }
                        else{
                            parseObject.put("virtualReality", "No");
                        }
                        Log.v("saving to parse","saving");
                        parseObject.saveInBackground();
                    }
                } else {
                    Log.v("inside error","error not null");
                    ParseObject childRuleWebsite = new ParseObject("ChildRuleWebsite");

                    childRuleWebsite.put("childName", childName);
                    childRuleWebsite.put("userName", userName);

                    if(catGambling.isChecked()){
                        childRuleWebsite.put("gambling", "Yes");
                    }
                    else{
                        childRuleWebsite.put("gambling", "No");
                    }
                    if(catHacking.isChecked()){
                        childRuleWebsite.put("hacking", "Yes");
                    }
                    else{
                        childRuleWebsite.put("hacking", "No");
                    }
                    if(catSocial.isChecked()){
                        childRuleWebsite.put("social", "Yes");
                    }
                    else{
                        childRuleWebsite.put("social", "No");
                    }
                    if(catChat.isChecked()){
                        childRuleWebsite.put("chat", "Yes");
                    }
                    else{
                        childRuleWebsite.put("chat", "No");
                    }
                    if(catMediaSharing.isChecked()){
                        childRuleWebsite.put("mediaSharing", "Yes");
                    }
                    else{
                        childRuleWebsite.put("mediaSharing", "No");
                    }
                    if(catAdult.isChecked()){
                        childRuleWebsite.put("adult", "Yes");
                    }
                    else{
                        childRuleWebsite.put("adult", "No");
                    }
                    if(catAbortion.isChecked()){
                        childRuleWebsite.put("abortion", "Yes");
                    }
                    else{
                        childRuleWebsite.put("abortion", "No");
                    }
                    if(catDrugs.isChecked()){
                        childRuleWebsite.put("drugs", "Yes");
                    }
                    else{
                        childRuleWebsite.put("drugs", "No");
                    }
                    if(catAlcohol.isChecked()){
                        childRuleWebsite.put("alcohol", "Yes");
                    }
                    else{
                        childRuleWebsite.put("alcohol", "No");
                    }
                    if(catWeapons.isChecked()){
                        childRuleWebsite.put("weapons", "Yes");
                    }
                    else{
                        childRuleWebsite.put("weapons", "No");
                    }
                    if(catProxy.isChecked()){
                        childRuleWebsite.put("proxy", "Yes");
                    }
                    else{
                        childRuleWebsite.put("proxy", "No");
                    }
                    if(catIllegal.isChecked()){
                        childRuleWebsite.put("illegal", "Yes");
                    }
                    else{
                        childRuleWebsite.put("illegal", "No");
                    }
                    if(catNewsMedia.isChecked()){
                        childRuleWebsite.put("newsMedia", "Yes");
                    }
                    else{
                        childRuleWebsite.put("newsMedia", "No");
                    }
                    if(catShopping.isChecked()){
                        childRuleWebsite.put("shopping", "Yes");
                    }
                    else{
                        childRuleWebsite.put("shopping", "No");
                    }
                    if(catGames.isChecked()){
                        childRuleWebsite.put("games", "Yes");
                    }
                    else{
                        childRuleWebsite.put("games", "No");
                    }
                    if(catVirtual.isChecked()){
                        childRuleWebsite.put("virtualReality", "Yes");
                    }
                    else{
                        childRuleWebsite.put("virtualReality", "No");
                    }

                    childRuleWebsite.saveInBackground();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child_website_category_selection, menu);
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
}
