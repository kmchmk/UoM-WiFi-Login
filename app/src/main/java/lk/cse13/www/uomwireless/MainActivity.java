package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static FloatingActionButton loggingfb;
    public static boolean loggedIn = false;
    public static boolean screenShowing = false;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                if (Operations.isConnectedToUoMWireless()) {
                    if (loggedIn) {
                        Operations.toast("Logging out...");
                        new BackgroundLogout().execute();
                    } else {
                        Operations.toast("Logging in...");
                        new BackgroundLogin( 0).execute();
                    }
                } else {
                    Operations.toast("Connect to UoM Wireless first");
                }

            }
        });
        new BackgroundLogin( 0).execute();
        StatusNotification.cancel(mainContext);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Updates.downloadNewAPK();
            } else {
                Operations.toast("Couldn't download update without permission");
            }
        }
    }

    public void openSteps(View v) {
        Intent i = new Intent(getApplicationContext(), StepsActivity.class);
        startActivity(i);

    }

}
