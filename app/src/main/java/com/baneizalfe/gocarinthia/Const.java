package com.baneizalfe.gocarinthia;

import com.karumi.dexter.BuildConfig;

/**
 * Created by baneizalfe on 5/11/16.
 */
public class Const {

    public static final String PREF_WELCOME_SEEN = "pref_welcome";
    public static final String PREF_TOKEN = "pref_token";
    public static final String PREF_BEACON = "pref_curr_beacon";
    public static final String PREF_PAYMENT_ADDED = "pref_payment_added";
    public static final String STATIONS_DOWNLOADED = "pref_stations_downloaded";

    public interface ACTION {
        public static String MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action.main";
        public static String LOCATION_SERVICE_CHANGE_ACTION = BuildConfig.APPLICATION_ID + "location_service_change";
        public static String LOCATION_ACQUIRED_ACTION = BuildConfig.APPLICATION_ID + ".action.location_acquired";
        public static String ACTIVITY_ACQUIRED_ACTION = BuildConfig.APPLICATION_ID + ".action.activity_acquired";
        public static String ACTION_STATIONS_DOWNLADED = BuildConfig.APPLICATION_ID + ".action.stations_downloaded";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static final int MIN_DISTANCE = 100;
}
