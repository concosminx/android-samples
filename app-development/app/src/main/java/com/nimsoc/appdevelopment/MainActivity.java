package com.nimsoc.appdevelopment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment f = new MainMenuFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(android.R.id.content, f);
            ft.commit();
        }

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putLong("Some key", System.currentTimeMillis());
//        edit.apply();

    }

    public void goToNext(View view) {
        //Intent i = new Intent(this, NextActivity.class);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        startActivity(i);
    }
}