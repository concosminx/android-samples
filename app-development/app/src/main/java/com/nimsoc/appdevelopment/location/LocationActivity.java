package com.nimsoc.appdevelopment.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.nimsoc.appdevelopment.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private LocationManager mLocationManager;
    private String mProvider;
    private TextView mAddressTextView;
    private TextView mLocationTextView;
    private boolean mIsReverseGeocoding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mLocationTextView = findViewById(R.id.location);
        mAddressTextView = findViewById(R.id.address);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(mProvider);
        updateLocationView(location);
    }

    private void updateLocationView(Location location) {
        if (location == null) {
            mLocationTextView.setText("Null location");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(location.getProvider())
                .append(": ")
                .append(location.getLatitude())
                .append(", ")
                .append(location.getLongitude())
                .append(" (")
                .append(location.getAccuracy())
                .append(" ) ");

        mLocationTextView.setText(sb.toString());

        if (!mIsReverseGeocoding) {
            mIsReverseGeocoding = true;
            new ReverseGeocoderTask().execute(location);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationManager.isProviderEnabled(mProvider)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(mProvider, 0, 0, this);
        } else {
            mLocationTextView.setText("LocationProvider disabled: " + mProvider);
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    private class ReverseGeocoderTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... locations) {
            Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
            Location location = locations[0];
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && !addresses.isEmpty()) {
                Address a = addresses.get(0);
                return a.getLocality();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (TextUtils.isEmpty(s)) {
                mAddressTextView.setText("Could not reverse geocode");
            } else {
                mAddressTextView.setText(s);
            }
            mIsReverseGeocoding = false;
        }
    }
}