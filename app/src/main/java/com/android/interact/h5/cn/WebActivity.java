package com.android.interact.h5.cn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author 张海洋
 * @Date on 2018/08/21.
 * @org 上海相舆科技有限公司
 * @describe
 */


public class WebActivity extends AppCompatActivity {

    WebView mWebview;
    WebSettings mWebSettings;

    TextView mtitle;
    private ProgressBar mPbNet;
    ImageView mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mtitle = (TextView) findViewById(R.id.tv_title);
        mPbNet = (ProgressBar) findViewById(R.id.pb_net);

        mWebview = (WebView) findViewById(R.id.webView1);


        mWebSettings = mWebview.getSettings();

        mWebview.loadUrl("http://www.baidu.com/");


        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");


            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {


            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {


            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                mtitle.setText(title);
            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 0) {
                    mPbNet.setVisibility(View.VISIBLE);
                }

                if (newProgress == 100) {
                    mPbNet.setVisibility(View.GONE);
                }
                Log.i("newProgress", "progress:" + newProgress);
                mPbNet.setProgress(newProgress);
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}
