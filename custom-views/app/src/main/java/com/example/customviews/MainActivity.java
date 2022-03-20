package com.example.customviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li = LayoutInflater.from(this);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.activity_main, null);
        setContentView(ll);

        CustomLayout authorBox = new CustomLayout(this);
        authorBox.setMainText("John Doe");
        authorBox.setSubText("President of Romania");
        ll.addView(authorBox);

        authorBox = new CustomLayout(this);
        authorBox.setMainText("Andy Doe");
        authorBox.setSubText("VP of Romania");
        ll.addView(authorBox);

    }
}