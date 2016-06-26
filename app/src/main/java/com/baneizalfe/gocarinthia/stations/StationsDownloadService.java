package com.baneizalfe.gocarinthia.stations;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.models.Station;
import com.baneizalfe.gocarinthia.network.Api;

import java.io.IOException;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class StationsDownloadService extends IntentService {

    private static final String TAG = "StationsDownloadService";

    public StationsDownloadService() {
        super("StationsDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            boolean stopsUpdated = false;

            StationsResponse stations = Api.getApiService().getStations("http://baneizalfe.com/stations.json").execute().body();
            if (stations != null) {
                Log.i(TAG, "onHandleIntent: " + stations.size());

                ActiveAndroid.beginTransaction();
                try {
                    for (Station station : stations) {
                        station.save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                    App.getApp().setStationList(stations);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putBoolean(Const.STATIONS_DOWNLOADED, true).apply();

                    stopsUpdated = true;

                    Log.d(TAG, "onHandleIntent: DONE!");
                } catch (Exception e) {
                    Log.e(TAG, "onHandleIntent: " + e.getMessage());
                } finally {
                    ActiveAndroid.endTransaction();
                    sendResult(this, stopsUpdated);
                }

            }
        } catch (IOException ioe) {
            Log.e(TAG, "onHandleIntent: " + ioe.getMessage());
            sendResult(this, false);
        }

    }

    private void sendResult(Context context, boolean result) {
        Intent intent = new Intent(Const.ACTION.ACTION_STATIONS_DOWNLADED);
        intent.putExtra("success", result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
