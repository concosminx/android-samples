package com.nimsoc.appdevelopment.webaccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nimsoc.appdevelopment.R;

public class WebViewFragment extends Fragment {
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.webview, container, false);
      WebView wv = view.findViewById(R.id.webview);
      wv.getSettings().setJavaScriptEnabled(true);
      wv.setWebViewClient(new ExampleWebViewClient());
      wv.loadUrl("https://www.google.com");
      return view;
   }

   private class ExampleWebViewClient extends WebViewClient {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
         view.loadUrl(request.getUrl().toString());
         return true;
      }
   }
}
