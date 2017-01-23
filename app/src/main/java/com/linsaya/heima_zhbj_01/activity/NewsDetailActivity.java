package com.linsaya.heima_zhbj_01.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by Administrator on 2017/1/22.
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private ImageButton ib_back;
    private ImageButton ib_share;
    private ImageButton ib_textsize;
    private WebView wb_content;
    private ProgressBar pb_web;
    private int mCurrentWhich = 2;
    private int mSelectWhich;
    private WebSettings mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initUi();
        initData();
        ShareSDK.initSDK(this);
    }

    private void initData() {
        String mUrl = getIntent().getStringExtra("url");

        wb_content.loadUrl(mUrl);
        mSetting = wb_content.getSettings();

        //支持页面缩放
        mSetting.setDisplayZoomControls(true);
        //支持JavaScript
        mSetting.setJavaScriptEnabled(true);
        //支持双击放大
        mSetting.setUseWideViewPort(true);

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
        ib_back.setOnClickListener(this);
        ib_share.setOnClickListener(this);
        ib_textsize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_share:
                //一键分享使用shareSdk的api
                showShare();
                break;
            case R.id.ib_textsize:
                setTextSize();
                break;

        }
    }

    private void setTextSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("调整字体大小");

        String[] item = new String[]{"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(item, mCurrentWhich, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                mSelectWhich = which;
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mSelectWhich) {
                    case 0:
                        mSetting.setTextZoom(200);
                        break;
                    case 1:
                        mSetting.setTextZoom(150);
                        break;
                    case 2:
                        mSetting.setTextZoom(100);
                        break;
                    case 3:
                        mSetting.setTextZoom(80);
                        break;
                    case 4:
                        mSetting.setTextZoom(50);
                        break;
                }
                mCurrentWhich = mSelectWhich;
            }
        });
        builder.show();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
