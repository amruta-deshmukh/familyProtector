package com.termproject.familyprotector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Mehul on 12/17/2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private PendingIntent currentLocAlarmIntent;
    private int geofenceReqCode = 0;
    private int currentLocReqCode = 1;
    private String alarmType;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.v("inside intent", "intent is not null");
            alarmType = intent.getStringExtra("alarmType");

        }
        if (alarmType != null) {
            Log.v("reqCode", alarmType);

            if (alarmType.equals("geofence")) {
                Intent geofenceService = new Intent(context, GeofenceCreationService.class);
                startWakefulService(context, geofenceService);
            } else if (alarmType.equals("currLoc")) {
                Intent currentLocationService = new Intent(context, CurrentLocationWriterService.class);
                startWakefulService(context, currentLocationService);
            }
        }


    }

    public void setAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent geoFence_intent = new Intent(context, AlarmReceiver.class);
        geoFence_intent.putExtra("alarmType", "geofence");
        alarmIntent = PendingIntent.getBroadcast(context, geofenceReqCode, geoFence_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar geofenceServiceCalendar = Calendar.getInstance();
        geofenceServiceCalendar.setTimeInMillis(System.currentTimeMillis());
        geofenceServiceCalendar.set(Calendar.HOUR_OF_DAY, 15);
        geofenceServiceCalendar.set(Calendar.MINUTE, 33);
        geofenceServiceCalendar.set(Calendar.SECOND,00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, geofenceServiceCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, alarmIntent);
        Log.v("the geofenceAlarm", "Geo fence alarm set at 3.25");

        Intent currLoc_intent = new Intent(context, AlarmReceiver.class);
        currLoc_intent.putExtra("alarmType", "currLoc");
        currentLocAlarmIntent = PendingIntent.getBroadcast(context, currentLocReqCode, currLoc_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar currLocServiceCalendar = Calendar.getInstance();
        currLocServiceCalendar.setTimeInMillis(System.currentTimeMillis());
        currLocServiceCalendar.set(Calendar.HOUR_OF_DAY, 17);
        currLocServiceCalendar.set(Calendar.MINUTE, 23);
        currLocServiceCalendar.set(Calendar.SECOND,00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, currLocServiceCalendar.getTimeInMillis(),
                1 * 60 * 1000, currentLocAlarmIntent);
        Log.v("the current Loc", "Curr location alarm set at 3.26");


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
