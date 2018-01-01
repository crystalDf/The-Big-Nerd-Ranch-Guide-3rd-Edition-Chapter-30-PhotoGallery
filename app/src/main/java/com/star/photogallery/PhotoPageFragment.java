package com.star.photogallery;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PhotoPageFragment extends VisibleFragment {

    private static final String ARG_URI = "photo_page_url";

    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static PhotoPageFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        PhotoPageFragment photoPageFragment = new PhotoPageFragment();
        photoPageFragment.setArguments(args);

        return photoPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            return;
        }

        mUri = getArguments().getParcelable(ARG_URI);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_page, container, false);

        mProgressBar = view.findViewById(R.id.fragment_photo_page_progress_bar);
        mProgressBar.setMax(100);

        mWebView = view.findViewById(R.id.fragment_photo_page_web_view);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

                if (appCompatActivity == null) {
                    return;
                }

                if (appCompatActivity.getSupportActionBar() == null) {
                    return;
                }

                appCompatActivity.getSupportActionBar().setSubtitle(title);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!request.getUrl().toString().startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                        startActivity(intent);

                        return true;
                    }
                }
                return false;
            }
        });

        mWebView.loadUrl(mUri.toString());
//        mWebView.loadUrl("https://cn.bing.com/");

        return view;
    }

    public boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }
}
