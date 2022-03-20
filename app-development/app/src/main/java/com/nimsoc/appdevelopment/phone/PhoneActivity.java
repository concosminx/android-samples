package com.nimsoc.appdevelopment.phone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.nimsoc.appdevelopment.R;

public class PhoneActivity extends FragmentActivity implements View.OnClickListener {

    private BroadcastReceiver mSentNotificationReceiver = new SentNotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        findViewById(R.id.send_app_call).setOnClickListener(this);
        findViewById(R.id.send_system_call).setOnClickListener(this);
        findViewById(R.id.send_app_sms).setOnClickListener(this);
        findViewById(R.id.send_system_sms).setOnClickListener(this);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        sb.append(tm.getLine1Number()).append("\n");
        sb.append(tm.getNetworkOperatorName()).append("\n");
        ((TextView)findViewById(R.id.phone_info)).setText(sb.toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_app_call:
                Intent appCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:5558989"));
                startActivity(appCallIntent);
                break;
            case R.id.send_system_call:
                Intent systemCallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:5558989"));
                startActivity(systemCallIntent);
                break;
            case R.id.send_app_sms:
                String phoneNumber = "5558989";
                String messageBody = "Sssss!";
                SmsManager sm = SmsManager.getDefault();

                registerReceiver(mSentNotificationReceiver, new IntentFilter("my_special_action"));
                Intent sentIntent = new Intent("my_special_action");
                PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, sentIntent, 0);

                sm.sendTextMessage(phoneNumber, null, messageBody, sentPendingIntent, null);
                ((TextView)findViewById(R.id.phone_info)).setText("Possibly sent message!");
                break;
            case R.id.send_system_sms:
                Intent systemSMSIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:5558989"));
                systemSMSIntent.putExtra("sms_body", "Hello from Android App!");
                startActivity(systemSMSIntent);
                break;
        }
    }

    private class SentNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView tv = ((TextView)findViewById(R.id.phone_info));
            if (getResultCode() == Activity.RESULT_OK) {
                tv.setText("Message sent ok!");
            } else {
                tv.setText("Message could not be sent!");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSentNotificationReceiver);
    }
}