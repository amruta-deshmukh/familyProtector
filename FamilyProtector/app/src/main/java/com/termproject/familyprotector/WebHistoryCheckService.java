package com.termproject.familyprotector;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WebHistoryCheckService extends IntentService {


    public WebHistoryCheckService() {
        super("WebHistoryCheckService");
    }

    UserLocalStore userLocalStore;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Handle Intent", "inside handle intent");
        Context context = getApplicationContext();
        userLocalStore = new UserLocalStore(context);

    }


}
