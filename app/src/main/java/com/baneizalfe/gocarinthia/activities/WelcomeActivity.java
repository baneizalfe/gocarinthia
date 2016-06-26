package com.baneizalfe.gocarinthia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;

import com.baneizalfe.gocarinthia.Const;
import com.baneizalfe.gocarinthia.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class WelcomeActivity extends AppIntro2 {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        addSlide(AppIntro2Fragment.newInstance("", Html.fromHtml("<b>Welcome<br>Wilkommen<br>Dobrodosli</b>"), R.drawable.welcomescreen01, Color.parseColor("#27a9e1")));
        addSlide(AppIntro2Fragment.newInstance(Html.fromHtml("LOCATION PERMISSION"), "Information about nearest station", R.drawable.welcomescreen02, Color.parseColor("#eeeeee"), Color.parseColor("#4a5060"), Color.parseColor("#4a5060")));
        addSlide(AppIntro2Fragment.newInstance("Bluetooth permissions", "Each vehicle needs to communicate with user", R.drawable.welcomescreen03, Color.parseColor("#27a9e1")));
        addSlide(AppIntro2Fragment.newInstance(Html.fromHtml("GREAT SUCCESS!"), Html.fromHtml("You are all set.\nLet's use the app!"), R.drawable.welcomescreen04, Color.parseColor("#eeeeee"), Color.parseColor("#4a5060"), Color.parseColor("#4a5060")));

        setImageSkipButton(null);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        preferences.edit().putBoolean(Const.PREF_WELCOME_SEEN, true).apply();

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
