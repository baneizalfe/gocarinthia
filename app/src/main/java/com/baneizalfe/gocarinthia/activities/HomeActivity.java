package com.baneizalfe.gocarinthia.activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.models.Station;
import com.baneizalfe.gocarinthia.stations.StationsDownloadService;
import com.baneizalfe.gocarinthia.tracking.TrackingService;
import com.google.android.gms.location.DetectedActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private static final int REQUEST_ENABLE_BT = 1;

    @BindView(R.id.user_action_image)
    ImageView user_action_image;

    @BindView(R.id.current_status)
    TextView current_status;

    @BindView(R.id.e_ticket_title)
    TextView e_ticket_title;

    private SharedPreferences preferences;
    private DetectedActivity detectedActivity;
    private Station nearestStation;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

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
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateActivityUI();

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onPermissionGranted() {
        startService(new Intent(HomeActivity.this, TrackingService.class));
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

    @OnClick(R.id.e_ticket_btn)
    void startQRActivity() {
        Intent myIntent = new Intent(this, QrCodeActivity.class);
        startActivity(myIntent);
    }
}
