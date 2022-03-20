package com.nimsoc.appdevelopment.webaccess;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nimsoc.appdevelopment.R;
import com.nimsoc.appdevelopment.webaccess.HttpDownloadTask;

public class CustomHttHandlerFragment extends Fragment {

   private TextView textView;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.scrolling_textview, container, false);

      textView = view.findViewById(R.id.textViewScroll);
      new Task().execute("https://ro.wikipedia.org/wiki/Wizz_Air");
      return view;
   }

   @Override
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

   }

   private class Task extends HttpDownloadTask {
      @Override
      protected void onPostExecute(String s) {
         if (TextUtils.isEmpty(s)) {
            textView.setText(R.string.generic_error);
         } else {
            textView.setText(Html.fromHtml(s));
         }
      }
   }
}
