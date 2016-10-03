package com.termproject.familyprotector;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){

        try {

            userLocalDatabase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
           // Log.v("UserLocalStore","done with initialize");
        }
        catch (Exception e){
           // Log.v("UserLocalStore",e.toString());
        }

    }

    public void storeUserData(User user){
//        Log.v("storeUserData",User.username+" "+User.password);

        try {
            SharedPreferences.Editor spEditor = userLocalDatabase.edit();

            spEditor.putString("username", user.getUsername());
            spEditor.putString("password", user.getPassword());
            spEditor.commit();
        }
        catch (Exception e){
            Log.v("UserLocalStore",e.toString());

        }
    }
    public User getLoggedInUser(){
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password","");
        User storedUser = new User(username,password);
        return storedUser;
    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }
    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn",false)==true){
            return true;
        }
        else{
            return false;
        }

    }
    public void setAppMode(String appMode){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("appMode", appMode);
        spEditor.commit();
    }
    public String getAppMode(){
        String appMode = userLocalDatabase.getString("appMode","");
        return appMode;
    }
    public void setChildDetails(String childName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("childName", childName);
        spEditor.commit();
    }
    public String getChildDetails(){
        String childName = userLocalDatabase.getString("childName","");
        return childName;
    }
    public void setLocationAddress(String addressString){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("addressString", addressString);
        spEditor.commit();
    }
    public String getLocationAddress(){
        String addressStr = userLocalDatabase.getString("addressString","");
        return addressStr;
    }

    public void setLocationPerimeter( float perimeterValue){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putFloat("perimeterValue", perimeterValue);
        spEditor.commit();
    }
    public float getLocationPerimeter(){
        float perimeterValue = userLocalDatabase.getFloat("perimeterValue", 30.0f);
        return perimeterValue;
    }

    public void setLocationLatitude( double latitude){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putLong("latitude", Double.doubleToLongBits(latitude));
        spEditor.commit();
    }
    public double getLocationLatitude(){
        double latitude = Double.longBitsToDouble(userLocalDatabase.getLong("latitude", Double.doubleToLongBits(37.72189700000001)));
        return latitude;
    }

    public void setLocationLongitude( double longitude){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putLong("longitude", Double.doubleToLongBits(longitude));
        spEditor.commit();
    }
    public double getLocationLongitude(){
        double longitude = Double.longBitsToDouble(userLocalDatabase.getLong("longitude", Double.doubleToLongBits(-122.4782094)));
        return longitude;
    }
    public void setChildForThisPhone(String childName){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("childForThisPhone", childName);
        spEditor.commit();
    }
    public String getChildForThisPhone(){
        String childName = userLocalDatabase.getString("childForThisPhone", "");
        return childName;
    }
    public void setChildForThisPhoneGender(String gender){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("childForThisPhoneGender", gender);
        spEditor.commit();
    }
    public String getChildForThisPhoneGender(){
        String gender = userLocalDatabase.getString("childForThisPhoneGender", "");
        return gender;
    }

    public void setRuleLocationId(int ruleLocationId){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("latestLocationId", ruleLocationId);
        spEditor.commit();
    }
    public int getRuleLocationId(){
        int ruleLocationID = userLocalDatabase.getInt("latestLocationId", 0);
        return ruleLocationID;
    }


    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
