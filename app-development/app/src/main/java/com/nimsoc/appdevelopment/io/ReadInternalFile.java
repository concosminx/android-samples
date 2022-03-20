package com.nimsoc.appdevelopment.io;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;

public class ReadInternalFile extends AsyncTask<Context, Void, String> {

   private final String fileName;
   public ReadInternalFile(String fileName) {
      this.fileName = fileName;
   }

   @Override
   protected String doInBackground(Context... contexts) {
      Context context = contexts[0];
      contexts = null;
      try (FileInputStream fis = context.openFileInput(this.fileName)) {
         context = null;
         StringBuilder sb = new StringBuilder();
         byte[] buffer = new byte[1024];
         while (fis.read(buffer) != -1) {
            sb.append(new String(buffer));
         }
         return sb.toString();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }
}
