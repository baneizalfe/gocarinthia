package com.baneizalfe.gocarinthia;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.baneizalfe.gocarinthia.stations.Station;
import com.baneizalfe.gocarinthia.user.AuthToken;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;

import java.util.List;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class App extends Application {

    private static App app;

    private Gson gson;
    private SharedPreferences preferences;

    private AuthToken authToken;
    private List<Station> stationList;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        gson = new Gson();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Dexter.initialize(this);
        ActiveAndroid.initialize(this);
    }

    public void setAuthToken(AuthToken authToken) {

        this.authToken = authToken;
        preferences.edit().putString(Const.PREF_TOKEN, gson.toJson(authToken)).apply();
    }

    public AuthToken getAuthToken() {

        if (authToken == null) {
            String tokenStr = preferences.getString(Const.PREF_TOKEN, null);
            if (!TextUtils.isEmpty(tokenStr)) {
                try {
                    authToken = gson.fromJson(tokenStr, AuthToken.class);
                } catch (Exception e) {

                }
            }
        }
        return authToken;
    }

    public static App getApp() {
        return app;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public List<Station> getStationList() {
        return this.stationList;
    }

    public void setPaymentSet(boolean paymentSet) {
        preferences.edit().putBoolean(Const.PREF_PAYMENT_ADDED, paymentSet).apply();
    }

    public boolean getPaymentSet() {
        return preferences.getBoolean(Const.PREF_PAYMENT_ADDED, false);
    }
}
