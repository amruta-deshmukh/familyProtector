package com.termproject.familyprotector;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;

public class BrowserHistory extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{

    private Button getHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        getHistory = (Button) findViewById(R.id.button_getHistory);

        getHistory.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, sel, null, null);
        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String url = "";
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                // Do something with title and url
                Log.v("title",title);
                Log.v("url",url);

                mCur.moveToNext();
            }
        }


    }

    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}



