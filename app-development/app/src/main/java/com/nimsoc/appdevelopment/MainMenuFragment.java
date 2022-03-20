package com.nimsoc.appdevelopment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.nimsoc.appdevelopment.io.ReadInternalFile;
import com.nimsoc.appdevelopment.io.SaveInternaFile;
import com.nimsoc.appdevelopment.location.LocationActivity;
import com.nimsoc.appdevelopment.media.AudioActivity;
import com.nimsoc.appdevelopment.media.SystemCameraActivity;
import com.nimsoc.appdevelopment.media.VideoActivity;
import com.nimsoc.appdevelopment.phone.PhoneActivity;
import com.nimsoc.appdevelopment.preferences.OurPreferenceActivity;
import com.nimsoc.appdevelopment.receivers.BatteryMonitorActivity;
import com.nimsoc.appdevelopment.sensors.SensorsActivity;
import com.nimsoc.appdevelopment.services.WeatherService;
import com.nimsoc.appdevelopment.webaccess.CustomHttpHandlingActivity;
import com.nimsoc.appdevelopment.webaccess.WebViewExample;

public class MainMenuFragment extends Fragment implements View.OnClickListener {
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.mainmenu, container, false);
      LinearLayout ct = view.findViewById(R.id.button_container);
      for (int i=0; i<ct.getChildCount(); i++) {
         ct.getChildAt(i).setOnClickListener(this);
      }

//      view.findViewById(R.id.webview_button).setOnClickListener(this);
//      view.findViewById(R.id.custom_http_button).setOnClickListener(this);
//      view.findViewById(R.id.weather_service_button).setOnClickListener(this);
//      view.findViewById(R.id.batery_monitor_button).setOnClickListener(this);
//      view.findViewById(R.id.preferences_button).setOnClickListener(this);
//      view.findViewById(R.id.save_internal_button).setOnClickListener(this);
//      view.findViewById(R.id.read_internal_button).setOnClickListener(this);
//      view.findViewById(R.id.telephony_button).setOnClickListener(this);
      return view;
   }

   @Override
   public void onClick(View view) {
      Intent i = null;
      switch (view.getId()) {
         case R.id.webview_button:
            i = new Intent(getActivity(), WebViewExample.class);
            break;
         case R.id.custom_http_button:
            i = new Intent(getActivity(), CustomHttpHandlingActivity.class);
            break;
         case R.id.batery_monitor_button:
            i = new Intent(getActivity(), BatteryMonitorActivity.class);
            break;
         case R.id.preferences_button:
            i = new Intent(getActivity(), OurPreferenceActivity.class);
            break;
         case R.id.telephony_button:
            i = new Intent(getActivity(), PhoneActivity.class);
            break;
         case R.id.sensor_button:
            i = new Intent(getActivity(), SensorsActivity.class);
            break;
         case R.id.weather_service_button:
            Intent serviceIntent = new Intent(getActivity(), WeatherService.class);
            getActivity().startService(serviceIntent);
            break;
         case R.id.audio_button_activity:
            i = new Intent(getActivity(), AudioActivity.class);
            break;
         case R.id.video_button_activity:
            i = new Intent(getActivity(), VideoActivity.class);
            break;
         case R.id.photo_button_activity:
            i = new Intent(getActivity(), SystemCameraActivity.class);
            break;
         case R.id.location_button_activity:
            i = new Intent(getActivity(), LocationActivity.class);
            break;
         case R.id.save_internal_button:
            new SaveInternaFile("testfile", "This is the file content".getBytes()).execute(getActivity());
            break;
         case R.id.read_internal_button:
            new ReadInternalFile("testfile") {
               @Override
               protected void onPostExecute(String s) {
                  Activity activity = getActivity();
                  if (activity != null) {
                     Toast.makeText(activity, "Read file:\n" + s, Toast.LENGTH_LONG).show();
                  }
               }
            }.execute(getActivity());
            break;

      }
      if (i != null) {
         startActivity(i);
      }
   }
}
