package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.itpro.buildersbackyard.R;

/**
 * Created by root on 25/11/15.
 */
public class TermsOfService extends Fragment  {


   private WebView mWebview;

    private String urlToOpen = "http://bsslife.com/iboard/app/termsconditions";

    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_terms_of_service, container, false);

        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void inflateViews() {
        mWebview = (WebView) view.findViewById(R.id.webview);


        mWebview.setWebViewClient(new MyBrowser());
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setAppCacheEnabled(false);
        mWebview.loadUrl(urlToOpen);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}