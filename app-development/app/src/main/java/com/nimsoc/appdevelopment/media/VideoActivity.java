package com.nimsoc.appdevelopment.media;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.VideoView;

import com.nimsoc.appdevelopment.R;

import java.io.File;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView vv;
    private CompoundButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vv = findViewById(R.id.videoView);
        button = findViewById(R.id.play_video);
        button.setOnClickListener(this);

        File video = new File(Environment.getExternalStorageDirectory(), "video.mp4");
        vv.setVideoPath(video.getAbsolutePath());
        //Uri videoUri = Uri.fromFile(video);
        //vv.setVideoURI(videoUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (vv.isPlaying()) {
            vv.pause();
        } else {
            vv.start();
        }
    }
}