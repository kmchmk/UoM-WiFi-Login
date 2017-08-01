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

import android.provider.Settings.Secure;


public class MainActivity extends AppCompatActivity {
    private Operations operations;
    public static FloatingActionButton loggingfb;
    public static boolean loggedIn = false;
    public static boolean screenShowing = false;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operations = new Operations();
        mainContext = MainActivity.this;

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
                if (info.getSSID().toString().equalsIgnoreCase("\"ChanakaWiFi\"")) {
                    if (loggedIn) {
                        operations.toast("Logging out...");
                        new BackgroundLogout(operations).execute();
                    } else {
                        operations.toast("Logging in...");
                        new BackgroundLogin(operations, 0).execute();
                    }
                } else {
                    operations.toast("Connect to UoM Wireless first");
                }

            }
        });
        operations.toast("Logging in...");
        new BackgroundLogin(operations, 0).execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        screenShowing = true;
    }

    protected void onStop() {
        super.onStop();
        screenShowing = false;
    }

}
