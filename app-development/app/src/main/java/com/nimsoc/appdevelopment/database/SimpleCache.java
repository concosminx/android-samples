package com.nimsoc.appdevelopment.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;

public class SimpleCache {

   private static final String TAG = "SimpleCache";
   private final static long DEFAULT_EXPIRATON = DateUtils.MINUTE_IN_MILLIS * 5;

   private final String mKey;
   private final String mValue;
   private final long mTimestamp;

   public SimpleCache(String key, String value) {
      this(key, value, System.currentTimeMillis());
   }

   public SimpleCache(String key, String value, long timestamp) {
      this.mKey = key;
      this.mValue = value;
      this.mTimestamp = timestamp;
   }

   public String getValue() {
      return mValue;
   }

   public boolean hasExpired() {
      return System.currentTimeMillis() > this.mTimestamp + DEFAULT_EXPIRATON;
   }

   public static SimpleCache loadSimpleCache(Context context, String key) {
      DatabaseHelper dh = new DatabaseHelper(context);
      SimpleCache simpleCache = dh.loadSimpleCache(key);
      dh.close();
      return simpleCache;
   }

   public boolean save(Context context) {
      DatabaseHelper dh = new DatabaseHelper(context);
      boolean result = dh.replaceSimpleCache(this);
      dh.close();
      return result;
   }

   private static class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

      private static final String COLUMN_KEY = "akey";
      private static final String COLUMN_VALUE = "value";
      private static final String COLUMN_TIMESTAMP = "timestamp";

      private static final String DATABASE_NAME = "simplecache.db";
      private static final int DATABASE_VERSION = 1;
      private static String TABLE_NAME = "simplecache";

      private static final String CREATE_TABLE = "create table " + TABLE_NAME +
              "( " +
              _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
              COLUMN_KEY + " TEXT UNIQUE NOT NULL, " +
              COLUMN_VALUE + " TEXT NOT NULL, " +
              COLUMN_TIMESTAMP + " INTEGER NOT NULL " +
              " ); ";

      public DatabaseHelper(@Nullable Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL(CREATE_TABLE);
      }

      @Override
      public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
         sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(sqLiteDatabase);
      }

      public SimpleCache loadSimpleCache(String key) {
         final SQLiteDatabase db;
         try {
            db = getReadableDatabase();
         } catch (SQLiteException e) {
            Log.w(TAG, "replaceSimpleCache: Failed to get writeable database", e);
            e.printStackTrace();
            return null;
         }

         String []columns = new String[] {COLUMN_KEY, COLUMN_VALUE, COLUMN_TIMESTAMP};
         String selection = COLUMN_KEY + " = ? ";

         Cursor cursor = null;
         try {
            cursor = db.query(TABLE_NAME, columns, selection, new String[] {key}, null, null, null);
            if (cursor == null) {
               return null;
            }
            if (!cursor.moveToFirst()) {
               cursor.close();
               return null;
            }

            @SuppressLint("Range")
            String returnedKey = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
            @SuppressLint("Range")
            String returnedValue = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
            @SuppressLint("Range")
            long returnedTimestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));

            return new SimpleCache(returnedKey, returnedValue, returnedTimestamp);
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (cursor != null) {
               cursor.close();
               cursor = null;
            }
            db.close();
         }
         return null;
      }

      public boolean replaceSimpleCache(SimpleCache simpleCache) {
         final SQLiteDatabase db;
         try {
            db = getWritableDatabase();
         } catch (SQLiteException e) {
            Log.w(TAG, "replaceSimpleCache: Failed to get writeable database", e);
            e.printStackTrace();
            return false;
         }

         ContentValues cv = new ContentValues();
         cv.put(COLUMN_KEY, simpleCache.mKey);
         cv.put(COLUMN_VALUE, simpleCache.mValue);
         cv.put(COLUMN_TIMESTAMP, simpleCache.mTimestamp);

         boolean result = db.replace(TABLE_NAME, null, cv) != -1;
         db.close();
         return result;
      }
   }
}
