package com.termproject.familyprotector;

/**
 * Created by Mehul on 12/18/2015.
 */
public class GeofenceAlertObj {

    String triggeringGeofenceName, triggeringGeofenceId, geofenceTransitionString;

    public GeofenceAlertObj(String triggeringGeofenceName, String triggeringGeofenceId, String geofenceTransitionString) {
        this.triggeringGeofenceName = triggeringGeofenceName;
        this.triggeringGeofenceId = triggeringGeofenceId;
        this.geofenceTransitionString = geofenceTransitionString;
    }

    public String alertString() {
        return geofenceTransitionString + " " + triggeringGeofenceName;
    }

    public String alertIdString() {
        return triggeringGeofenceId;
    }
}
