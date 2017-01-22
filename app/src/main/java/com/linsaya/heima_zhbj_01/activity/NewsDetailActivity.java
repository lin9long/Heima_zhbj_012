package com.linsaya.heima_zhbj_01.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.linsaya.heima_zhbj_01.R;

/**
 * Created by Administrator on 2017/1/22.
 */

public class NewsDetailActivity extends Activity {

    private ImageButton ib_back;
    private ImageButton ib_share;
    private ImageButton ib_textsize;
    private WebView wb_content;
    private ProgressBar pb_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initUi();
        initData();
    }

    private void initData() {
        wb_content.loadUrl("http://www.baidu.com");
        WebSettings setting = wb_content.getSettings();

        //支持页面缩放
        setting.setDisplayZoomControls(true);
        //支持JavaScript
        setting.setJavaScriptEnabled(true);
        //支持双击放大
        setting.setUseWideViewPort(true);

        //跳到上一个页面
        wb_content.goBack();
        //跳到下一个页面
        wb_content.goForward();


        wb_content.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载啦");
                pb_web.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页开始结束啦");
                pb_web.setVisibility(View.INVISIBLE);
            }

            //强制在webview中加载网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wb_content.loadUrl(url);
                return true;
            }
        });

        wb_content.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("当前网页加载进度:" + newProgress);
                pb_web.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("当前加载网页标题:" + title);
            }
        });
    }

    private void initUi() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_share = (ImageButton) findViewById(R.id.ib_share);
        ib_textsize = (ImageButton) findViewById(R.id.ib_textsize);
        wb_content = (WebView) findViewById(R.id.wv_content);
        pb_web = (ProgressBar) findViewById(R.id.pb_web);
        pb_web.setVisibility(View.INVISIBLE);
    }
}
