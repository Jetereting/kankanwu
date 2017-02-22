package eiyou.us.kankanwu.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eiyou.us.kankanwu.R;
import eiyou.us.kankanwu.widget.AuWebView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.wv)
    eiyou.us.kankanwu.widget.AuWebView wv;

    boolean exit = false;
    AuWebView.MyWebChromeClient webChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        webViewInit();
    }

    public void webViewInit() {
        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        wv.loadUrl("http://m.jiakaobaodian.com/");
        wv.setWebViewClient(new AuWebView(getApplicationContext(), null).new MyWebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }
        });
        webChromeClient = new AuWebView(getApplicationContext(), null).new MyWebChromeClient() {

        };
        wv.setWebChromeClient(webChromeClient);
        if(TbsVideo.canUseTbsPlayer(getApplicationContext())){
            TbsVideo.openVideo(getApplicationContext(),"视频地址");
        }else {
            Toast.makeText(this, "不能播", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            if (exit) {
                wv.destroy();
                super.onBackPressed();
            } else {
                exit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }
}
