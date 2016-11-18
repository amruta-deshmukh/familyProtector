package com.termproject.familyprotector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mehul on 12/17/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    UserLocalStore userLocalStore;

    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        userLocalStore = new UserLocalStore(context);
        String appMode = userLocalStore.getAppMode();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context, appMode);
        }
    }
}
