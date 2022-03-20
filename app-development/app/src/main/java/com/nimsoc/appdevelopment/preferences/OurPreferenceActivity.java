package com.nimsoc.appdevelopment.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.nimsoc.appdevelopment.R;
import com.nimsoc.appdevelopment.services.WeatherService;

public class OurPreferenceActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.our_preferences);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//      if ("show_city".equals(key)) {
//          boolean value = sharedPreferences.getBoolean("show_city", false);
//          if (value) {
//              Toast.makeText(this, "Good Choice!", Toast.LENGTH_SHORT).show();
//          }
//      }
        Intent serviceIntent = new Intent(this, WeatherService.class);
        startService(serviceIntent);
    }
}