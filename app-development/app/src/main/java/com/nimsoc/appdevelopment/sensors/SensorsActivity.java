package com.nimsoc.appdevelopment.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.nimsoc.appdevelopment.R;

public class SensorsActivity extends FragmentActivity {

    private SensorEventListener mProximityListener = new ProximityListener();
    private SensorEventListener mPressureListener = new PressureListener();
    private SensorEventListener mAccelListener = new AccelListener();
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private Sensor mPressureSensor;
    private Sensor mAccelSensor;
    private TextView mProximityInfo;
    private TextView mPressureInfo;
    private TextView mAccelInfo;
    private float mColorMultiplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mProximityInfo = findViewById(R.id.proximity_info);

        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mPressureInfo = findViewById(R.id.pressure_info);

        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelInfo = findViewById(R.id.accel_info);

        float max = mAccelSensor.getMaximumRange();
        mColorMultiplier = 255/max;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProximitySensor != null) {
            mSensorManager.unregisterListener(mProximityListener);
        }
        if (mPressureSensor != null) {
            mSensorManager.unregisterListener(mPressureListener);
        }
        if (mAccelSensor != null) {
            mSensorManager.unregisterListener(mAccelListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mProximitySensor != null) {
            mSensorManager.registerListener(mProximityListener, mProximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (mPressureSensor != null) {
            mSensorManager.registerListener(mPressureListener, mPressureSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (mAccelSensor != null) {
            mSensorManager.registerListener(mAccelListener, mAccelSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private class ProximityListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float cm = sensorEvent.values[0];
            mProximityInfo.setText("cm: " + cm);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class PressureListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float mb = sensorEvent.values[0];
            mPressureInfo.setText(mb + "mb of pressure ");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class AccelListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = Math.abs(sensorEvent.values[0]);
            float y = Math.abs(sensorEvent.values[1]);
            float z = Math.abs(sensorEvent.values[2]);

            int r = (int) (x * mColorMultiplier);
            int g = (int) (y * mColorMultiplier);
            int b = (int) (z * mColorMultiplier);

            mAccelInfo.setBackgroundColor(Color.rgb(r,g,b));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


}