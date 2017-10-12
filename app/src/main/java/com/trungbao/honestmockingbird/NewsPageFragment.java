package com.trungbao.honestmockingbird;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trungbao.honestmockingbird.model.News;

/**
 * Created by baotrungtn on 10/12/17.
 */

public class NewsPageFragment extends Fragment {
    private static final String NEWS = "com.trungbao.honestmockingbird.NEWS";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mOpenBrowserTextView;
    private News mNews;

    public static NewsPageFragment newInstance(News news) {
        Bundle args = new Bundle();
        args.putSerializable(NEWS, news);
        NewsPageFragment fragment = new NewsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = (News) getArguments().getSerializable(NEWS);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_page, container, false);

        mWebView = (WebView) v.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    mOpenBrowserTextView.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mOpenBrowserTextView.setVisibility(View.GONE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if (activity != null && activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setSubtitle(title);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mNews.getUrl());


        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mProgressBar.setMax(100);

        mOpenBrowserTextView = (TextView) v.findViewById(R.id.open_browser);
        mOpenBrowserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                startActivity(i);
            }
        });

        return v;
    }

    public boolean webViewGoBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return false;
    }
}
