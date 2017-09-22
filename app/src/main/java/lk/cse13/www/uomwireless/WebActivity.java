package lk.cse13.www.uomwireless;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;



public class WebActivity extends AppCompatActivity {
    private WebView webview;
    private String site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodle);
        site = getIntent().getExtras().getString("site");

        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new MyBrowser());
        webview.setWebChromeClient(new MyChromeBrowser());
        WebSettings settings = webview.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        login();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    private void login() {
        String username = Operations.readFromFile("username");
        String password = Operations.readFromFile("password");

        if(site.equals("online")) {
            String postData = "username=" + username + "&password=" + password;
            webview.postUrl("https://online.mrt.ac.lk/login/index.php", EncodingUtils.getBytes(postData, "BASE64"));
        }
        else if(site.equals("lms")){
            String postData = "LearnOrgUsername="+username+"&LearnOrgPassword="+password+"&LearnOrgLogin=Login";
            webview.postUrl("https://lms.mrt.ac.lk/login.php", EncodingUtils.getBytes(postData, "BASE64"));
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url.equals("https://online.mrt.ac.lk/login/index.php")) {
//                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
//                startActivity(i);
//            }
            if (Uri.parse(url).getHost().equals(site + ".mrt.ac.lk")) {
                return false;
            } else {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            webview.loadUrl("about:blank");
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(WebActivity.this);
            dlgAlert.setTitle("No internet connection!");
            dlgAlert.setMessage("Connect to the internet and try again!");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dlgAlert.create().show();
        }
    }


    private class MyChromeBrowser extends WebChromeClient {
        private ProgressDialog mProgress;

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgress == null) {
                mProgress = new ProgressDialog(WebActivity.this);
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
