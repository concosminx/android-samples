package com.nimsoc.appdevelopment.widgets;

import static com.nimsoc.appdevelopment.services.WeatherUpdate.KEY_IBA;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.nimsoc.appdevelopment.MainActivity;
import com.nimsoc.appdevelopment.R;
import com.nimsoc.appdevelopment.services.WeatherService;
import com.nimsoc.appdevelopment.services.WeatherUpdate;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String TAG = "WeatherWidget";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(TAG, "updateAppWidget: am trecut pe aici ...");
        
        String widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews remoteViews = getRemoteViews(context, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        Intent serviceIntent = new Intent(context, WeatherService.class);
        context.startService(serviceIntent);

        Log.d(TAG, "updateAppWidget: am lansat service-ul");

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: intent.action " + intent.getAction());

        //if (intent.getAction().equals(WeatherUpdate.WEATHER_UPDATE_INTENT)) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(KEY_IBA)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean showCity = prefs.getBoolean("show_city", true);
            WeatherUpdate update = new WeatherUpdate(intent);
            Log.d(TAG, "onReceive: iba = " + update.getIba());
            RemoteViews remoteViews = getRemoteViews(context, (showCity ? "withCity" : "noCity") + update.getIba());

            // Instruct the widget manager to update the widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, WeatherWidget.class);
            appWidgetManager.updateAppWidget(cn, remoteViews);
        } else {
          super.onReceive(context, intent);
        }
    }

    private RemoteViews getRemoteViews(Context context, String textViewContent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.appwidget_text, textViewContent);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.weather_widget_root, pendingIntent);

        return views;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}