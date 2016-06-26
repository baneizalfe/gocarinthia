package com.baneizalfe.gocarinthia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.activeandroid.query.Select;
import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.stations.Station;
import com.baneizalfe.gocarinthia.user.AuthToken;

import java.util.List;

public class MainActivity extends BaseActivity {

    SharedPreferences preferences;
    AsyncTask<Void, Void, List<Station>> stationsDbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        AuthToken fakeToken = new AuthToken();
        fakeToken.authToken = "ertsdfw324juy";
        fakeToken.id = 1;
        fakeToken.type = "pax";
        App.getApp().setAuthToken(fakeToken);
        App.getApp().setPaymentSet(true);

        if (App.getApp().getAuthToken() != null && App.getApp().getPaymentSet()) {

            stationsDbTask = new AsyncTask<Void, Void, List<Station>>() {
                @Override
                protected List<Station> doInBackground(Void... params) {
                    List<Station> stationList = new Select().from(Station.class).execute();
                    return stationList;
                }

                @Override
                protected void onPostExecute(List<Station> stations) {
                    super.onPostExecute(stations);
                    if (isFinishing() || isCancelled()) return;
                    App.getApp().setStationList(stations);
                    startHomeActivity();
                }
            }.execute();

        } else if (!preferences.getBoolean(Const.PREF_WELCOME_SEEN, false)) {
            startWelcomeActivity();
        } else {
            startRegisterActivity();
        }
    }

    @Override
    protected void onDestroy() {
        if(stationsDbTask != null) stationsDbTask.cancel(true);
        super.onDestroy();
    }

    private void startWelcomeActivity() {
        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
        startActivity(welcomeIntent);
        finish();
    }

    private void startRegisterActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}