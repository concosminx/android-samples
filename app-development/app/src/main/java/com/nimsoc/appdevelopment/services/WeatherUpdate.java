package com.nimsoc.appdevelopment.services;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.nimsoc.appdevelopment.widgets.WeatherWidget;

public class WeatherUpdate {
   private String iba;

   public static final String KEY_IBA = "iba";
   public static final String WEATHER_UPDATE_INTENT = "com.nimsoc.appdevelopment.widgets.WEATHER_UPDATE_INTENT";

   public WeatherUpdate(String iba) {
      this.iba = iba;
   }

   public WeatherUpdate(Intent i) {
      Bundle extras = i.getExtras();
      this.iba = extras.getString(KEY_IBA);
   }

   public Intent toIntent() {
      Intent i = new Intent();
      i.setAction(WEATHER_UPDATE_INTENT); //nu merge cu asta ...
      i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      i.putExtra(KEY_IBA, this.iba);

      return i;
   }

   public String getIba() {
      return iba;
   }
}
