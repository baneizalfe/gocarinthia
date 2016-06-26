package com.baneizalfe.gocarinthia.tracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.stations.Station;
import com.baneizalfe.gocarinthia.stations.StationHelper;
import com.google.android.gms.location.LocationResult;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class LocationReceiver extends BroadcastReceiver {

    public static final String TAG = "LocationIntentService";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                // use the Location
                Log.i(TAG, "onReceive: " + location.toString());
                Station nearestStation = StationHelper.getNearestStation(location);
                if (nearestStation != null) {
                    Log.d(TAG, "onHandleIntent: STANICA: " + nearestStation.station_name + " distance " + nearestStation.distance);
                    startBeaconSniffing();
                }

            }
        }
    }

    private void startBeaconSniffing() {

    }

    private void sendLocationUpdate(Context context, Location location, Station station) {
        Intent updateIntent = new Intent(Const.ACTION.LOCATION_ACQUIRED_ACTION);
        Bundle args = new Bundle();
        args.putParcelable("location", location);
        args.putParcelable("station", station);
        updateIntent.putExtras(args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
    }


}
