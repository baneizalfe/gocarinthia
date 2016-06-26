package com.baneizalfe.gocarinthia.tracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baneizalfe.gocarinthia.Const;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class ActivityReceiver extends BroadcastReceiver {

    static final String TAG = "ActivityReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        if (result != null) {
            DetectedActivity activity = result.getMostProbableActivity();
            Log.d(TAG, "onReceive: " + activity.toString());
            sendLocationUpdate(context, activity);
        }
    }

    private void sendLocationUpdate(Context context, DetectedActivity detectedActivity) {
        Intent updateIntent = new Intent(Const.ACTION.ACTIVITY_ACQUIRED_ACTION);
        Bundle args = new Bundle();
        args.putParcelable("activity", detectedActivity);
        updateIntent.putExtras(args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
    }
}
