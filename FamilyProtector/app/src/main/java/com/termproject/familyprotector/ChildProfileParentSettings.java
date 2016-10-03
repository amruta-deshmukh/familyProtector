package com.termproject.familyprotector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChildProfileParentSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("childNamePrefParent");
        editor.remove("childGenderPrefParent");
        editor.apply();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);
//        addPreferencesFromResource(R.xml.pref_child_profile);

        EditTextPreference childNamePref =  new EditTextPreference(this);



////        PreferenceCategory category = new PreferenceCategory(this);
        childNamePref.setTitle("Child Name");
        childNamePref.setKey("childNamePref");
//        Log.v("name", userLocalStore.getChildForThisPhone());
        childNamePref.setDefaultValue(userLocalStore.getChildDetails());
        childNamePref.setSummary(userLocalStore.getChildDetails());
//
//        screen.addPreference(childNamePref);

        ListPreference childGenderPref = new ListPreference(this);
        childGenderPref.setTitle("Gender");
        childGenderPref.setKey("childGenderPref");
        childGenderPref.setEntries(R.array.pref_child_gender_options);
        childGenderPref.setEntryValues(R.array.pref_child_gender_values);

        if(userLocalStore.getChildForThisPhoneGender().matches("Male")) {
            childGenderPref.setDefaultValue("Male");
            childGenderPref.setSummary("Male");
        }
        else{
            childGenderPref.setDefaultValue("Female");
            childGenderPref.setSummary("Female");
        }
        screen.addPreference(childNamePref);
        screen.addPreference(childGenderPref);
        setPreferenceScreen(screen);
//        CheckBoxPreference checkBoxPref = new CheckBoxPreference(this);
//        checkBoxPref.setTitle("title");
//        checkBoxPref.setSummary("summary");
//        checkBoxPref.setChecked(true);
//
//        category.addPreference(checkBoxPref);
//        setPreferenceScreen(screen);
//        addPreferencesFromResource(R.xml.pref_child_profile);
//        EditTextPreference childNamePref = (EditTextPreference)findPreference(getString(R.string.pref_child_name_key));
//        Log.v("check",childNamePref+"--");
//        ListPreference childGenderPref = (ListPreference)findPreference(getString(R.string.pref_child_gender_key));
//        childNamePref.setDefaultValue(userLocalStore.getChildForThisPhone());
//        if(userLocalStore.getChildForThisPhoneGender().matches("Male")) {
//            childGenderPref.setValueIndex(0);
//        }
//        else{
//            childGenderPref.setValueIndex(1);
//        }
//        bindPreferenceSummaryToValue(childNamePref);
//        bindPreferenceSummaryToValue(childGenderPref);

//        findPreference(getString(R.string.pref_child_name_key)).setDefaultValue(userLocalStore.getChildForThisPhone());
//        findPreference(getString(R.string.pref_child_gender_key)).setDefaultValue(userLocalStore.getChildForThisPhoneGender());
        bindPreferenceSummaryToValue(findPreference("childNamePref"));
        bindPreferenceSummaryToValue(findPreference("childGenderPref"));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(stringValue);
            saveChildNameToParse(stringValue);
        }


        return true;

    }

    private void saveChildNameToParse(final String stringValue){
        Log.v("name", "inside parse");
        Log.v("name", stringValue);

        User storedUSer = userLocalStore.getLoggedInUser();
        ParseQuery query = new ParseQuery("ChildDetails");
        query.whereEqualTo("username", storedUSer.getUsername());
        query.whereEqualTo("name",userLocalStore.getChildForThisPhone());

        query.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {

            }
        });
//        query.getFirstInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> children, ParseException e) {
//                if (e == null) {
//                    if (children.size() > 0) {
//                        for (int i = 0; i < children.size(); i++) {
//                            ParseObject child = children.get(i);
//                            Log.v("size", children.size() + "");
//                            Log.v("name", child.getString("name"));
//                            child.put("name", stringValue);
//                            child.saveInBackground();
//                        }
//
//
//                    }
//                } else {
//                    Log.d("Login", "Error: " + e.getMessage());
//                }
//
//            }
//        });

    }
}
