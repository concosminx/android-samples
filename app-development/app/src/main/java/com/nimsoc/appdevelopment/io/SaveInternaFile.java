package com.nimsoc.appdevelopment.io;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveInternaFile extends AsyncTask<Context, Void, Boolean> {

   private final String fileName;
   private final byte[] bytes;

   public SaveInternaFile(String fileName, byte[] bytes) {
      this.fileName = fileName;
      this.bytes = bytes;
   }

   @Override
   protected Boolean doInBackground(Context... contexts) {
      Context context = contexts[0];
      contexts = null;
      try (FileOutputStream fileOutputStream = context.openFileOutput(this.fileName, Context.MODE_PRIVATE)) {
         context = null;
         fileOutputStream.write(bytes);
         return Boolean.TRUE;
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return Boolean.FALSE;
   }
}
