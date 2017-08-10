package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
                if (info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"")) {
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
        //operations.toast("Logging in...");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Updates.downloadNewAPK();
            } else {
                operations.toast("Couldn't download update without permission");
            }
        }
    }


}
