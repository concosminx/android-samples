package com.example.dictionary.threading;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.dictionary.models.Word;
import com.example.dictionary.persistence.AppDatabase;
import com.example.dictionary.util.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class RetrieveWordsRunnable implements Runnable {

    private static final String TAG = "RetrieveWordsRunnable";

    private WeakReference<Handler>  mMainThreadHandler;
    private AppDatabase mDb;
    private String mQuery;

    public RetrieveWordsRunnable(Context context, Handler mainThreadHandler, String query) {
        mMainThreadHandler = new WeakReference<>(mainThreadHandler);
        mDb = AppDatabase.getDatabase(context);
        mQuery = query;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: retrieving words. This is from thread: " + Looper.myLooper().getThread().getName());
        ArrayList<Word> words = new ArrayList<>(mDb.wordDataDao().getWords(mQuery));
        Message message = null;
        if(words.size() > 0){
            message = Message.obtain(null, Constants.WORDS_RETRIEVE_SUCCESS);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("words_retrieve", words);
            message.setData(bundle);
        }
        else{
            message = Message.obtain(null, Constants.WORDS_RETRIEVE_FAIL);
        }
        mMainThreadHandler.get().sendMessage(message);
    }
}


