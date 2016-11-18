package com.termproject.familyprotector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mehul on 11/4/2016.
 */
public class NotificationDBHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 1;

    private static final String DATABASE_NAME    = "FamilyProtectorData";

    private static final String TABLE_NOTIFICATION   = "childNotifications";

    private static final String KEY_CHILDNAME    = "childName";
    private static final String KEY_ALERTTYPE    = "alertType";
    private static final String KEY_OPENEDCHILD  = "openedChild";
    private static final String KEY_OPENEDINALERT  = "openedInAlert";

    public NotificationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHILDNOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "(" + KEY_CHILDNAME
                + " TEXT NOT NULL," + KEY_ALERTTYPE + " TEXT NOT NULL," + KEY_OPENEDCHILD + " TEXT NOT NULL," +
                KEY_OPENEDINALERT + " TEXT NOT NULL"+ ")";
        db.execSQL(CREATE_CHILDNOTIFICATIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);

    }

    public void addChildNotification(ChildNotification childNotification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHILDNAME, childNotification.getDbEntryChildName());
        values.put(KEY_ALERTTYPE, childNotification.getDbEntryAlertType());
        values.put(KEY_OPENEDCHILD, childNotification.getDbEntryOpenedChild());
        values.put(KEY_OPENEDINALERT,childNotification.getDbEntryOpenedInAlert());
        db.insert(TABLE_NOTIFICATION, null, values);
        db.close();
        Log.v("Database", "added to Db");
    }

    public int getNotificationCountForChild(String childName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATION,new String[]{KEY_ALERTTYPE},
                KEY_CHILDNAME+ "=? AND "+ KEY_OPENEDCHILD + "=?",
                new String [] {childName,FamilyProtectorConstants.FALSE},null,null,null,null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return 0;
        }

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getAlertTypeCountForChild(String childName, String alertType) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATION,new String[]{KEY_ALERTTYPE},
                KEY_CHILDNAME+ "=? AND "+ KEY_ALERTTYPE + "=?",new String [] {childName,alertType},null,null,null,null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return 0;
        }

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int deleteNotificationEntry(String childName, String alertType){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTIFICATION,KEY_CHILDNAME+ "=? AND "+KEY_ALERTTYPE + "=?",new String[]{childName,alertType});
        db.close();
        Log.v("Database", "deleted child device"+ result);
        return result;
    }

    public int updateOpenedChild(String childName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OPENEDCHILD, FamilyProtectorConstants.TRUE);

        int result = db.update(TABLE_NOTIFICATION,values,KEY_CHILDNAME+ "=?",new String[]{childName});
        Log.v("Database", "updated child notification:"+ result);

        return result;
    }
}
