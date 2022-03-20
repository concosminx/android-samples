package com.nimsoc.appdevelopment.webaccess;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

public class HttpDownloadTask extends AsyncTask<String, Void, String> {
   @Override
   protected String doInBackground(String... urls) {
      if (urls.length != 1) {
         throw new InvalidParameterException("urls must be of length of 1; it was: " + urls.length);
      }
      if (TextUtils.isEmpty(urls[0])) {
         throw new InvalidParameterException("urls must not be null or empty string");
      }

      HttpClient client = new DefaultHttpClient();
      HttpGet get = new HttpGet(urls[0]);
      try {
         HttpResponse response = client.execute(get);
         if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }
}
