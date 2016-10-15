package com.termproject.familyprotector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Mehul on 12/17/2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private PendingIntent currentLocAlarmIntent;
    private PendingIntent webCatAlarmIntent;
    private int geofenceReqCode = 0;
    private int currentLocReqCode = 1;
    private int websiteCategoryCode = 2;
    private String alarmType;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            alarmType = intent.getStringExtra("alarmType");

        }
        if (alarmType != null) {
            if (alarmType.equals("geofence")) {
                Intent geofenceService = new Intent(context, GeofenceCreationService.class);
                startWakefulService(context, geofenceService);
            } else if (alarmType.equals("currLoc")) {
                Intent currentLocationService = new Intent(context, CurrentLocationWriterService.class);
                startWakefulService(context, currentLocationService);
            } else if (alarmType.equals("webCat")){
//                Intent currentLocationService = new Intent(context, CurrentLocationWriterService.class);
//                startWakefulService(context, currentLocationService);
            }
        }


    }

    public void setAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //----------------Geofence Alarm --------------------------------
        //setting the intent for geofence alarm
        Intent geoFence_intent = new Intent(context, AlarmReceiver.class);
        geoFence_intent.putExtra("alarmType", "geofence");
        alarmIntent = PendingIntent.getBroadcast(context, geofenceReqCode, geoFence_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //setting the time and frequency for the alarm
        Calendar geofenceServiceCalendar = Calendar.getInstance();
        geofenceServiceCalendar.setTimeInMillis(System.currentTimeMillis());
        geofenceServiceCalendar.set(Calendar.HOUR_OF_DAY, 14);
        geofenceServiceCalendar.set(Calendar.MINUTE, 00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, geofenceServiceCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, alarmIntent);

        //----------------Current Location Alarm --------------------------------
        //setting the intent for current location alarm
        Intent currLoc_intent = new Intent(context, AlarmReceiver.class);
        currLoc_intent.putExtra("alarmType", "currLoc");
        currentLocAlarmIntent = PendingIntent.getBroadcast(context, currentLocReqCode, currLoc_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //setting the time and frequency for the alarm
        Calendar currLocServiceCalendar = Calendar.getInstance();
        currLocServiceCalendar.setTimeInMillis(System.currentTimeMillis());
        currLocServiceCalendar.set(Calendar.HOUR_OF_DAY, 14);
        currLocServiceCalendar.set(Calendar.MINUTE, 00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, currLocServiceCalendar.getTimeInMillis(),
                10 * 60 * 1000, currentLocAlarmIntent);

        //----------------Web Category Alarm --------------------------------
        //setting the intent for website category alarm
        Intent webCat_intent = new Intent(context, AlarmReceiver.class);
        webCat_intent.putExtra("alarmType", "webCat");
        webCatAlarmIntent = PendingIntent.getBroadcast(context, websiteCategoryCode, webCat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //setting the time and frequency for the alarm
        Calendar webCatServiceCalendar = Calendar.getInstance();
        webCatServiceCalendar.setTimeInMillis(System.currentTimeMillis());
        webCatServiceCalendar.set(Calendar.HOUR_OF_DAY, 14);
        webCatServiceCalendar.set(Calendar.MINUTE, 00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, webCatServiceCalendar.getTimeInMillis(),
                1 * 60 * 1000, webCatAlarmIntent);


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
