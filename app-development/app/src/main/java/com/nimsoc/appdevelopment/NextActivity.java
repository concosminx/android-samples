package com.nimsoc.appdevelopment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.TextView;

public class NextActivity extends FragmentActivity {

    private TextView resultTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        resultTextView = findViewById(R.id.resultTextView);

        //FragmentManager supportFragmentManager = getSupportFragmentManager();
        AsyncTask myTask = new LongRunningClass().execute(new String[] {"This is it!", "We need peace!", "Are we there yet?"});
        myTask.cancel(false);

        //Handler handler = new Handler();
        //handler.postDelayed(new DelayedRunnable(), DateUtils.SECOND_IN_MILLIS * 5);
    }

    private class DelayedRunnable implements Runnable {

        @Override
        public void run() {
            resultTextView.setText("In Progress");
        }
    }

    private class LongRunningClass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            if (isCancelled()) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String s: params) {
                StringBuilder tmp = new StringBuilder(s);
                sb.append(tmp.reverse()).append("\n");

                final long time  = System.currentTimeMillis();
                while (time + (DateUtils.SECOND_IN_MILLIS * 1) > System.currentTimeMillis()) {
                    //Do nothing
                };

                i++;

                float progress = (float)i / (float) params.length * 100;
                progress = (float) Math.floor(progress);
                publishProgress("Progress " + progress + "%");
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultTextView.setText(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            resultTextView.setText(values[0]);
        }

        @Override
        protected void onCancelled(String result) {
            resultTextView.setText("Canceled!");
        }
    }
}