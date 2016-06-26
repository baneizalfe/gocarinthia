package com.baneizalfe.gocarinthia.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.stations.Station;
import com.baneizalfe.gocarinthia.stations.StationsDownloadService;
import com.baneizalfe.gocarinthia.tracking.BackgroundLocationService;
import com.google.android.gms.location.DetectedActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    static final String TAG = "HomeActivity";

    @BindView(R.id.user_action_image)
    ImageView user_action_image;

    @BindView(R.id.current_status)
    TextView current_status;

    @BindView(R.id.e_ticket_title)
    TextView e_ticket_title;

    private SharedPreferences preferences;
    private DetectedActivity detectedActivity;
    private Station nearestStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(stationsReceiver, new IntentFilter(Const.ACTION.ACTION_STATIONS_DOWNLADED));
        LocalBroadcastManager.getInstance(this).registerReceiver(locationUpdateReceiver, new IntentFilter(Const.ACTION.LOCATION_ACQUIRED_ACTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(activityUpdateReceiver, new IntentFilter(Const.ACTION.ACTIVITY_ACQUIRED_ACTION));

        if (App.getApp().getStationList() != null) {
            checkPermission();
        } else {
            showProgressDialog(getString(R.string.updating_stops));
            startService(new Intent(this, StationsDownloadService.class));
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stationsReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationUpdateReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activityUpdateReceiver);
        super.onDestroy();
    }

    private void checkPermission() {

        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Log.i(TAG, "onPermissionsChecked: ");
                    onPermissionGranted();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                String misssingList = "";
                for (PermissionRequest permissionRequest : permissions) {
                    misssingList += " " + permissionRequest.getName();
                }
                showAlertDialog("Please enable following permissions:%s" + misssingList);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateActivityUI();
    }

    private void onPermissionGranted() {
        startService(new Intent(HomeActivity.this, BackgroundLocationService.class));
    }

    private final BroadcastReceiver stationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismissProgressDialog();
            if (intent.getBooleanExtra("success", false)) {
                showSnackbar(getString(R.string.stops_updated));
                checkPermission();
            }
        }
    };

    private final BroadcastReceiver activityUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) return;

            if (intent.getExtras() != null && intent.getParcelableExtra("activity") != null) {
                detectedActivity = intent.getParcelableExtra("activity");
                updateActivityUI();
            }

        }
    };

    private final BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) return;

            nearestStation = null;
            if (intent.getExtras() != null && intent.getParcelableExtra("station") != null) {
                nearestStation = intent.getParcelableExtra("station");
            }

            updateActivityUI();

        }
    };

    public void startQRActivity(View view){
        Intent myIntent = new Intent(this, QrCode.class);
        this.startActivity(myIntent);
    }

    private void updateActivityUI() {

        String statusLabel;
        if (detectedActivity != null && (detectedActivity.getType() == DetectedActivity.ON_FOOT || detectedActivity.getType() == DetectedActivity.WALKING || detectedActivity.getType() == DetectedActivity.STILL)) {
            statusLabel = "Currently on foot";
            user_action_image.setImageResource(R.drawable.imagestill);

        } else if (detectedActivity != null && (detectedActivity.getType() == DetectedActivity.IN_VEHICLE || detectedActivity.getType() == DetectedActivity.ON_BICYCLE)) {
            statusLabel = "Moving";
            user_action_image.setImageResource(R.drawable.imagemoving);
        } else {
            statusLabel = "Detecting";
            user_action_image.setImageResource(R.drawable.imageunknown);
        }

        if (nearestStation != null)
            statusLabel += String.format("<br><small>Near %s</small>", nearestStation.station_name);

        current_status.setText(Html.fromHtml(statusLabel));
    }
}
