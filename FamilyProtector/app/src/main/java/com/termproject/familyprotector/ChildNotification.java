package com.termproject.familyprotector;

/**
 * Created by Mehul on 11/4/2016.
 */
public class ChildNotification {

    private String dbEntryChildName;
    private String dbEntryAlertType;
    private String dbEntryOpenedChild;
    private String dbEntryOpenedInAlert;

    public ChildNotification(String dbEntryChildName, String dbEntryAlertType, String dbEntryOpenedChild,
                             String dbEntryOpenedInAlert) {
        this.dbEntryChildName = dbEntryChildName;
        this.dbEntryAlertType = dbEntryAlertType;
        this.dbEntryOpenedChild = dbEntryOpenedChild;
        this.dbEntryOpenedInAlert = dbEntryOpenedInAlert;
    }

    public String getDbEntryChildName() {
        return dbEntryChildName;
    }

    public String getDbEntryAlertType() {
        return dbEntryAlertType;
    }

    public String getDbEntryOpenedChild(){
        return dbEntryOpenedChild;
    }

    public String getDbEntryOpenedInAlert(){
        return dbEntryOpenedInAlert;
    }
}
