package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    public static WebView webview;
    private Operations operations;
    public static FloatingActionButton loggingfb;
    public static boolean loggedIn = false;
    public static boolean screenShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operations = new Operations(getApplicationContext());
        webview = (WebView) findViewById(R.id.webView);

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                operations.toast("Touch disabled");
                return true;
            }
        });

        FloatingActionButton settingsfb = (FloatingActionButton) findViewById(R.id.settingsfb);
        settingsfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });


        loggingfb = (FloatingActionButton) findViewById(R.id.loggingfb);
        loggingfb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                if (info.getSSID().toString().equalsIgnoreCase("\"UoM_Wireless\"")) {
                    if (loggedIn) {
                        operations.toast("Logging out...");
                        new BackgroundLogout(MainActivity.this, operations).execute();
                    } else {
                        operations.toast("Logging in...");
                        new BackgroundLogin(MainActivity.this, operations).execute();
                    }
                } else {
                    operations.toast("Connect to UoM Wireless first");
                }

            }
        });
        operations.toast("Logging in...");
        new BackgroundLogin(this, operations).execute();




    }

    @Override
    protected void onResume(){
        super.onResume();
        screenShowing = true;
    }

    protected void onStop(){
        super.onStop();
        screenShowing = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        operations.toast("Loggin in...");
        new BackgroundLogin(this, operations).execute();
    }

}
