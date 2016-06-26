package com.baneizalfe.gocarinthia.tracking;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.activities.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Lokator";

    private static BackgroundLocationService instance;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private PendingIntent locationIntent;
    private PendingIntent activityIntent;
    private boolean isLocationOn;

    public BackgroundLocationService() {
    }

    public class LocalBinder extends Binder {
        public BackgroundLocationService getServerInstance() {
            return BackgroundLocationService.this;
        }
    }

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BackgroundLocationService getInstance() {
        return instance;
    }

    public boolean getIsLocationOn() {
        return isLocationOn;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Const.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_is_running))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher).build();


        startForeground(Const.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        if (mGoogleApiClient == null) buildGoogleApiClient();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if (mGoogleApiClient != null) {

            stopLocationRequest();

            if (mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();

        }

        super.onDestroy();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    private void buildLocationRequest() {

        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

    }

    private void startLocationRequests() {
        try {

            Intent lIntent = new Intent(this, LocationReceiver.class);
            locationIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, lIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationIntent);

            Intent aIntent = new Intent(this, ActivityReceiver.class);
            activityIntent = PendingIntent.getBroadcast(getApplicationContext(), 2, aIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 10000, activityIntent);

            isLocationOn = true;

            sendUpdateSignal();
        } catch (SecurityException sex) {
            sex.printStackTrace();
        }
    }

    private void stopLocationRequest() {
        if (mGoogleApiClient != null && locationIntent != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationIntent);
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, activityIntent);
        }

        isLocationOn = false;
        sendUpdateSignal();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mLocationRequest == null) buildLocationRequest();
        startLocationRequests();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) mGoogleApiClient.connect();
    }

    private void sendUpdateSignal() {
        Intent intent = new Intent(Const.ACTION.LOCATION_SERVICE_CHANGE_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
