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
    private PendingIntent webCatAlarmIntent;
    private int geofenceReqCode = 0;
    private int currentLocReqCode = 1;
    private int websiteCategoryCode = 2;
    private int parentCurrLocCode = 3;
    private String alarmType;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            alarmType = intent.getStringExtra("alarmType");

        }
        if (alarmType != null) {
            Log.v("alarmType", alarmType);
            if (alarmType.equals("geofence")) {
//                Intent geofenceService = new Intent(context, GeofenceCreationService.class);
//                startWakefulService(context, geofenceService);
            } else if (alarmType.equals("currLoc")) {
//                Intent currentLocationService = new Intent(context, CurrentLocationWriterService.class);
//                startWakefulService(context, currentLocationService);
            } else if (alarmType.equals("webCat")) {
//                Intent webHistoryService = new Intent(context, WebHistoryCheckService.class);
//                startWakefulService(context, webHistoryService);
            } else if (alarmType.equals("parentAlarm")) {
//                Intent webHistoryService = new Intent(context, ParentCurrentLocationCheckService.class);
//                startWakefulService(context, webHistoryService);

            }

        }


    }

    public void setAlarm(Context context, String alarmMode) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMode.equals("child")) {
            //----------------Geofence Alarm --------------------------------
            //setting the intent for geofence alarm
            Intent geoFence_intent = new Intent(context, AlarmReceiver.class);
            geoFence_intent.putExtra("alarmType", "geofence");
            alarmIntent = PendingIntent.getBroadcast(context, geofenceReqCode, geoFence_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the time and frequency for the alarm
            Calendar geofenceServiceCalendar = Calendar.getInstance();
            geofenceServiceCalendar.setTimeInMillis(System.currentTimeMillis());
            geofenceServiceCalendar.set(Calendar.HOUR_OF_DAY, 8);
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
            currLocServiceCalendar.set(Calendar.HOUR_OF_DAY, 8);
            currLocServiceCalendar.set(Calendar.MINUTE, 01);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, currLocServiceCalendar.getTimeInMillis(),
                    1 * 60 * 1000, currentLocAlarmIntent);

            //----------------Web Category Alarm --------------------------------
            //setting the intent for website category alarm
            Intent webCat_intent = new Intent(context, AlarmReceiver.class);
            webCat_intent.putExtra("alarmType", "webCat");
            webCatAlarmIntent = PendingIntent.getBroadcast(context, websiteCategoryCode, webCat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the time and frequency for the alarm
            Calendar webCatServiceCalendar = Calendar.getInstance();
            webCatServiceCalendar.setTimeInMillis(System.currentTimeMillis());
            webCatServiceCalendar.set(Calendar.HOUR_OF_DAY, 8);
            webCatServiceCalendar.set(Calendar.MINUTE, 02);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, webCatServiceCalendar.getTimeInMillis(),
                    10 * 60 * 1000, webCatAlarmIntent);

        }

        else if (alarmMode.equals("parent")) {
            Intent parentCurrLocIntent = new Intent(context, AlarmReceiver.class);
            parentCurrLocIntent.putExtra("alarmType", "parentAlarm");
            alarmIntent = PendingIntent.getBroadcast(context, parentCurrLocCode, parentCurrLocIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the time and frequency for the alarm
            Calendar parentCurrentLocationCalendar = Calendar.getInstance();
            parentCurrentLocationCalendar.setTimeInMillis(System.currentTimeMillis());
            parentCurrentLocationCalendar.set(Calendar.HOUR_OF_DAY, 8);
            parentCurrentLocationCalendar.set(Calendar.MINUTE, 03);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, parentCurrentLocationCalendar.getTimeInMillis(),
                    5 * 60 * 1000, alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}
