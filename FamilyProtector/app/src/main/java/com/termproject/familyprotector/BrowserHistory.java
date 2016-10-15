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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BrowserHistory extends AppCompatActivity implements View.OnClickListener{

    private Button getHistory;
    private TextView apiOutput;
    HttpsURLConnection urlConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        getHistory = (Button) findViewById(R.id.button_getHistory);
        apiOutput = (TextView)findViewById(R.id.api_output);

        getHistory.setOnClickListener(this);

//        String encodedURL = encodingStrToBase64("https://www.bovada.lv/");
        String encodedURL = encodingStrToBase64("adultfriendfinder.com");
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
            StringBuffer buffer = new StringBuffer();

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


                HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                urlConnection = (HttpsURLConnection) url.openConnection();

                SSLContext context = SSLContext.getInstance("TLS");
                TrustManager tm[] = {new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};
                context.init(null, tm, null);
                SSLSocketFactory preferredCipherSuiteSSLSocketFactory = new PreferredCipherSuiteSSLSocketFactory(context.getSocketFactory());
                urlConnection.setSSLSocketFactory(preferredCipherSuiteSSLSocketFactory);


                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty(HEADER_PARAM, HEADER_VALUE);
                urlConnection.setHostnameVerifier(hostnameVerifier);
                urlConnection.connect();
                Log.v("connected", builtUri.toString());

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                while ((output = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(output + "\n");
                }
//                buffer = buffer.append("{\"data\":[{\"categories\":[\"informationtech\",\"business\"],\"url\":\"http:\\/\\/webshrinker.com\\/\"}]}");


                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray dataArr = jsonObject.getJSONArray("data");
                JSONArray categoryArr = dataArr.getJSONObject(0).getJSONArray("categories");

                Log.v("category", categoryArr.length()+"");
//                JSONArray categoryArr = dataArr.getJSONArray(0);

            }catch (Exception e){
                Log.e("API error",e.toString());
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String output) {
            Log.v("output", output);
            apiOutput.setText(output);

        }
    }
}



