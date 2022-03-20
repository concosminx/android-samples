package com.nimsoc.appdevelopment.media;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nimsoc.appdevelopment.R;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        Button btn = findViewById(R.id.audio_button);
        btn.setEnabled(true);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.mata);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    view.setEnabled(true);
                }
            });
        }
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}