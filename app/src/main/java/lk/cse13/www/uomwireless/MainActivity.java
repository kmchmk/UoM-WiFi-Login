package lk.cse13.www.uomwireless;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.net.InetAddress;


public class MainActivity extends AppCompatActivity {
    public static WebView webview;
    private Operations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operations = new Operations(getApplicationContext());
        webview = (WebView) findViewById(R.id.webView);
        webview.clearCache(true);
        webview.setWebViewClient(new MyBrowser());
        webview.setWebChromeClient(new MyChromeBrowser());
        webview.getSettings().setJavaScriptEnabled(true);

        FloatingActionButton settingsfb = (FloatingActionButton) findViewById(R.id.settingsfb);
        settingsfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });


        final FloatingActionButton loggingfb = (FloatingActionButton) findViewById(R.id.loggingfb);
        loggingfb.setOnClickListener(new View.OnClickListener() {
            boolean loggedIn = true;
            @Override
            public void onClick(View view) {
                if (loggedIn) {
                    logout();
                    loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    loggedIn = false;
                    Toast.makeText(getApplicationContext(),"Logging out...",Toast.LENGTH_LONG).show();
                } else {
                    login();
                    loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    loggedIn = true;
                    Toast.makeText(getApplicationContext(),"Logging in...",Toast.LENGTH_LONG).show();
                }

            }
        });
        new BackgroundLogin(getApplicationContext()).execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new BackgroundLogin(getApplicationContext()).execute();
    }






    public void login() {

        String index = operations.readFromFile("ind");
        String password = operations.readFromFile("psd");


        String login = "buttonClicked=4&err_flag=0&err_msg=&info_flag=0&info_msg=&redirect_url=&network_name=Guest%20Network&username=" + index + "&password=" + password;
        webview.postUrl("https://wlan.uom.lk/login.html", EncodingUtils.getBytes(login, "BASE64"));
    }
    private void logout() {
        String logout = "userStatus=1&err_flag=0&err_msg=";
        webview.postUrl("https://wlan.uom.lk/logout.html", EncodingUtils.getBytes(logout, "BASE64"));
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!Uri.parse(url).getHost().equals("www.mrt.ac.lk")) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }


    }

    private class MyChromeBrowser extends WebChromeClient {
        private ProgressDialog mProgress;

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgress == null) {
                mProgress = new ProgressDialog(MainActivity.this);
                mProgress.show();
            }
            mProgress.setMessage("Loading");
            if (progress == 100) {
                mProgress.dismiss();
                mProgress = null;
            }
        }
    }
}