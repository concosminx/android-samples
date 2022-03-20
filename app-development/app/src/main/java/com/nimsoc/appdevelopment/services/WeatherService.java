package com.nimsoc.appdevelopment.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nimsoc.appdevelopment.webaccess.HttpDownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherService extends Service {

   private static final String TAG = "WeatherService";
   private static final String WEATHER_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita";
   String strIBA = null;

   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //return super.onStartCommand(intent, flags, startId);

      new WeatherDownloadTask().execute(WEATHER_URL);

      return START_NOT_STICKY;
   }

   private class WeatherDownloadTask extends HttpDownloadTask {
      @Override
      protected void onPostExecute(String s) {
         try {
            JSONObject json = new JSONObject(s);
            JSONArray drinks = json.getJSONArray("drinks");
            json = drinks.getJSONObject(0);

            strIBA = json.getString("strIBA");

         } catch (JSONException e) {
            e.printStackTrace();
         }

         Log.d(TAG, "onPostExecute: strIBA = " + strIBA);

         WeatherUpdate update = new WeatherUpdate(strIBA);
         sendBroadcast(update.toIntent());



         Log.d(TAG, "onPostExecute: am facut broadCast");

         stopSelf();
      }
   }
}
