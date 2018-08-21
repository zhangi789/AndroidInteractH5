package com.android.interact.h5.cn;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private WebView mWebView;

    private Button btn_1, btn_2, btn_3, btn_4, btn_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);


        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);

        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);


        mWebView.loadUrl("file:///android_asset/dj.html");//加载本地asset下面的js_java_interaction.html文件
        //mWebView.loadUrl("https://www.baidu.com/");

        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        /**
         *   AndroidAndJSInterface类 功能 提供Js调用安卓里面的方法
         */
        mWebView.addJavascriptInterface(new AndroidAndJSInterface(), "android");
        mWebView.setWebViewClient(new WebViewClient());


        /**
         *  由于设置了弹窗检验调用结果,所以需要支持js对话框
         *  webview只是载体，内容的渲染需要使用webviewChromClient类去实现
         *  通过设置WebChromeClient对象处理JavaScript的对话框
         *  设置响应js 的Alert()函数
         *
         *  普通直接设置即可    mWebView.setWebChromeClient(new WebChromeClient());
         */
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("通知");
                b.setMessage("安卓访问h5里面js方法" + "\r\n" + "message " + message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });


    }

    /**
     * 类的作用
     * <p>
     * 提供方法用于   h5 处理安卓端的逻辑
     */
    public class AndroidAndJSInterface {

        @JavascriptInterface//
        public void setJump(String name) {

            Log.i("TAG", name + " ------- ");
            //无参数跳转
            startActivity(new Intent(MainActivity.this, TwoActivity.class));
        }


    }

    //点击按钮，访问H5里带返回值的方法
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                //安卓调用js 无参方法
                mWebView.loadUrl("javascript:Noarguments()");//访问H5里带参数的方法，alertMessage(message)为H5里的方法
                break;

            case R.id.btn_2:

                //当出入变量名时，需要用转义符隔开
                String content = "张三";
                mWebView.loadUrl("javascript:appToh5(\"" + content + "\")");//访问H5里带参数的方法，alertMessage(message)为H5里的方法
                break;

            case R.id.btn_3:
                // 只需要将第一种方法的loadUrl()换成下面该方法即可
                mWebView.evaluateJavascript("add()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.i("TAG", "value " + value);
                    }
                });


                break;
            case R.id.btn_4:

                //Android调用有返回值js方法，安卓4.4以上才能用这个方法
                mWebView.evaluateJavascript("sum(100,150)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.i("TAG", "value " + value);
                    }
                });

                break;
            case R.id.btn_5:


startActivity(new Intent(MainActivity.this,WebActivity.class));


                break;
        }


    }


}
