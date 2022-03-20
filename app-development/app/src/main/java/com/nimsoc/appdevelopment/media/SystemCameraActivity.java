package com.nimsoc.appdevelopment.media;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.nimsoc.appdevelopment.R;

import java.io.File;

public class SystemCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAPTURE_PHOTO = 1;
    private Uri mOutputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_camera);

        findViewById(R.id.camera_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "test-" + System.currentTimeMillis() + ".jpg");
        mOutputFileUri = Uri.fromFile(file);
        i.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
        startActivityForResult(i, CAPTURE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_PHOTO) {
            if (data != null && data.hasExtra("data")) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                ImageView iv = findViewById(R.id.thumbnail);
                iv.setImageBitmap(thumbnail);
            }
        }
    }
}