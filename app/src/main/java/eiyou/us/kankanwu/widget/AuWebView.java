package eiyou.us.kankanwu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eiyou.us.kankanwu.model.WebViewNoAdJs;
import eiyou.us.kankanwu.model.WebViewNoAdUrl;
import eiyou.us.kankanwu.utils.ComputeCallBack;
import eiyou.us.kankanwu.utils.SP;

/**
 * Created by Au on 2017/1/5.
 */

public class AuWebView extends WebView {

    private WebView wv;
    private Context context;
    Map<String, String> extraHeaders = new HashMap<String, String>();

    public AuWebView(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        wv = this;
        this.context = context;
        wv.setDrawingCacheEnabled(true);
        extraHeaders.put("X-Requested-With", "com.xtuone.android.syllabus");
        WebSettings webSetting = wv.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; Chitanda/Akari) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.0.0.58_r884092.501 NetType/WIFI");
        if (Build.VERSION.SDK_INT >= 21) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().sync();

        //        去广告
        WebViewNoAdJs.getWebViewNoAdJs(new ComputeCallBack() {
            @Override
            public void onComputeEnd(Object o) {
                SP.put(context, "webViewNoAdJs", o.toString());
            }
        });
        WebViewNoAdUrl.getWebViewNoAdUrl(new ComputeCallBack() {
            @Override
            public void onComputeEnd(Object o) {
                List<WebViewNoAdUrl> webViewNoAdUrls = (List<WebViewNoAdUrl>) o;
                Set<String> stringSet = new HashSet<>();
                for (WebViewNoAdUrl webViewNoAdUrl : webViewNoAdUrls) {
                    stringSet.add(webViewNoAdUrl.getWebViewNoAdUrl());
                }
                SP.put(context, "webViewNoAdUrl", stringSet);
            }
        });
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.setWebViewClient(new MyWebViewClient());
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url, extraHeaders);
            return true;
        }

        @Override
        public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
            Set<String> webViewNoAdUrls = SP.getSetString(context, "webViewNoAdUrl");
            if (webViewNoAdUrls.contains(s)) {
                return new com.tencent.smtt.export.external.interfaces.WebResourceResponse(null, null, null);
            } else {
                return super.shouldInterceptRequest(webView, s);
            }
        }
    }
    public class MyWebChromeClient extends WebChromeClient{
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i > 17) {
                try {
                    webView.loadUrl(IOUtils.toString(context.getAssets().open("jquery.txt"), StandardCharsets.UTF_8) + SP.getString(context, "webViewNoAdJs"));
                } catch (IOException e) {
                    webView.loadUrl("javascript:" + SP.getString(context, "webViewNoAdJs"));
                }
            }
        }
    }
}

