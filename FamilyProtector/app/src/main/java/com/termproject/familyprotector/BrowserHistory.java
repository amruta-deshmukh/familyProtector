package com.termproject.familyprotector;


import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class BrowserHistory extends AppCompatActivity implements View.OnClickListener{

    private Button getHistory;
    private TextView apiOutput;
    GoogleApiClient mGoogleApiClient;
    HttpsURLConnection urlConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        getHistory = (Button) findViewById(R.id.button_getHistory);
        apiOutput = (TextView)findViewById(R.id.api_output);

        getHistory.setOnClickListener(this);

        String encodedURL = encodingStrToBase64("webshrinker.com");
        String authCredentials = encodingStrToBase64("txV7oMZEZ2cUrziueVtk:eCdjAaLnJVbrKz29Swnp");
        WebSiteLookUpParams webLookupParams = new WebSiteLookUpParams(encodedURL, authCredentials);
        WebsiteCategoryLookUp websiteCategoryLookUp = new WebsiteCategoryLookUp();
        websiteCategoryLookUp.execute(webLookupParams);


    }

    @Override
    public void onClick(View view) {


        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,
        Browser.BookmarkColumns.DATE};
        Calendar ci = Calendar.getInstance();
        long endTime = ci.getTimeInMillis();
        ci.add(Calendar.HOUR, -1);
        long startTime = ci.getTimeInMillis();
//        Log.v("Start+end", startTime+ " + "+ endTime);
        String[] time = new String[] {String.valueOf(startTime),String.valueOf(endTime)};
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0" + " AND "+ Browser.BookmarkColumns.DATE
                + " BETWEEN ? AND ?"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, sel, time, null);
        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String url = "";
        @SuppressWarnings("unused")
        String date = "";
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                date = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));
                // Do something with title and url
                Log.v("title",title);
                Log.v("url",url);
                Log.v("date", date);

                mCur.moveToNext();
            }
        }


    }

    //encoding the string to base64
    private String encodingStrToBase64(String abc) {
        byte[] strBytes = abc.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.encodeToString(strBytes, Base64.DEFAULT);
        encoded = encoded.replaceAll("(\\r|\\n)", "");

        Log.v("encoded url", encoded+"-a");
        return encoded;
    }

    private class WebSiteLookUpParams{
        String encodedURL;
        String authCredentials;

        WebSiteLookUpParams(String encodedURL, String authCredentials){
            this.encodedURL = encodedURL;
            this.authCredentials = authCredentials;
            Log.v("assigned values", encodedURL+authCredentials);
        }
    }

    public class WebsiteCategoryLookUp extends AsyncTask<WebSiteLookUpParams, Void, String> {
        @Override
        protected String doInBackground(WebSiteLookUpParams... params) {
            String encodedURL = params[0].encodedURL;
            String authCredentials = params[0].authCredentials;
            String output = null;

            try{
                final String WEBSITE_CATEGORY_API_BASE_URL = "https://api.webshrinker.com/categories/v2/";
                final String WEBSITE_CATEGORY_API_WITH_URL = WEBSITE_CATEGORY_API_BASE_URL+encodedURL;
                Log.v("final url", WEBSITE_CATEGORY_API_WITH_URL);
                final String HEADER_PARAM = "authorization";
                final String HEADER_VALUE = " Basic "+authCredentials;
                Log.v("header value", HEADER_VALUE);

                Uri builtUri = Uri.parse(WEBSITE_CATEGORY_API_WITH_URL).buildUpon()
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
//                urlConnection.setRequestProperty(HEADER_PARAM, HEADER_VALUE);
                urlConnection.connect();
                Log.v("connected", builtUri.toString());

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                while ((output = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(output + "\n");
                }

            }catch (Exception e){
                Log.e("API error",e.toString());
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            apiOutput.setText(output);

        }
    }
}



