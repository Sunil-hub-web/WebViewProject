package com.Brahma.webviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.Brahma.SessionManager;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    String currenturl = "";
    private Boolean exit = false;

    SessionManager sessionManager;
    String id;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        webView = findViewById(R.id.webView);
try {
    id = sessionManager.getFCMId();
    webview("http://brahmarealty.com/app/home.php");

    Log.d("webviewid", id);
}catch (Exception e){
    webview("http://brahmarealty.com/app/");
}




    }

    public void webview(String url){
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                if(url.equalsIgnoreCase("http://brahmarealty.com/app/home.php")){

                    sessionManager.setFCMId("1");
                }

                Log.d("url",url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("hjykrjterew", "your current url when webpage loading.. finish" + url);
                currenturl = url;
                CookieSyncManager.getInstance().sync();
                super.onPageFinished(view, url);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress < 100) {
                    progressDialog.show();
//                    progressbar.showDialog();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
//                    progressbar.hideDialog();
                }
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()
                        == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    //handler.sendEmptyMessage(1);

                    if (currenturl.equalsIgnoreCase("http://brahmarealty.com/app/") || currenturl.equalsIgnoreCase("http://brahmarealty.com/app/")) {
                        if (exit) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent); // finish activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Press Back again to Exit.",
                                    Toast.LENGTH_SHORT).show();
                            exit = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    exit = false;
                                }
                            }, 3 * 1000);
                        }
                    } else {
                        webView.goBack();
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack();

        }else{
            super.onBackPressed();

            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
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