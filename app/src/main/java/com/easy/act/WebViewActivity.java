package com.easy.act;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.easy.act.base.BaseFragmentActivity;
import com.easy.android.R;
import com.easy.ui.FloatView;

import butterknife.BindView;
import butterknife.ButterKnife;
import comm.helper.DisplayHelper;
import comm.helper.ScreenHelper;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/9/15 10:39
 * 传参：
 * 返回:
 */
public class WebViewActivity extends BaseFragmentActivity{
    @BindView(R.id.webview)
    public WebView mWebView;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);
        ButterKnife.bind(this);
//        mWebView = (WebView) findViewById(R.id.webview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); }
        //不使用缓存：
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new CallBack(), "knms");
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.loadUrl("http://h5.cache.kebuyer.com/turntableMovie_20170915102205/index_re.html?planName=0915roll");

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:window.knms.getHtmlContent('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });


        FloatView ivPjyl = (FloatView) findViewById(R.id.floatview);
        FrameLayout.LayoutParams layoutParamss = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamss.leftMargin = ScreenHelper.getScreenWidth(this) - DisplayHelper.dp2px(this,100);
        layoutParamss.topMargin = DisplayHelper.dp2px(this,90);
        ivPjyl.setLayoutParams(layoutParamss);
        ivPjyl.setUrl("");
        ivPjyl.setVisibility(View.VISIBLE);
    }
    final public class CallBack {
        @JavascriptInterface
        public void systemCopy(String content) {

        }
        @JavascriptInterface
        public String getCookie() {
            return "";
        }
        @JavascriptInterface
        public void getShareImageSource(String shareContent, String shareImg) {
            //组装分享对象
        }
        @JavascriptInterface
        public void getHtmlContent(String content){
            Log.i("html",content);
        }
    }

}
