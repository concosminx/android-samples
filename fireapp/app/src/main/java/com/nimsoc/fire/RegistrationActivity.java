package com.nimsoc.fire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class RegistrationActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    EditText etName;

    FirebaseAuth mAuth;
    DatabaseReference database;
    StorageReference storageReference;

    Bitmap bitmapCroppedImage;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String userImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etEmail = findViewById(R.id.etEmailRegistration);
        etPassword = findViewById(R.id.etPasswordRegistration);
        etName = findViewById(R.id.etName);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void btnRegisterAction(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        if (!"".equals(email) && !"".equals(password) && !"".equals(name)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        insertUserDataIntoFirebase();
                        User user = new User(name, email, userImageUrl);
                        database.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegistrationActivity.this, "Registration succeeded!", Toast.LENGTH_SHORT).show();
                    } else {
                        //task.getException().printStackTrace();
                        Toast.makeText(RegistrationActivity.this, "Unexpected error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(RegistrationActivity.this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(16, 9).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImage = activityResult.getUri();
                File imageFile = new File(croppedImage.getPath());
                try {
                    bitmapCroppedImage = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(60)
                            .compressToBitmap(imageFile);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapCroppedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertUserDataIntoFirebase() {
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("user_pic").child(mAuth.getCurrentUser().getUid() + ".jpg");
        UploadTask uploadTask = storageReference.putBytes(byteArray);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    userImageUrl = task.getResult().getStorage().getDownloadUrl().toString();
                    Toast.makeText(RegistrationActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void btnAddImage(View view) {
        goToGallery();
    }
}
