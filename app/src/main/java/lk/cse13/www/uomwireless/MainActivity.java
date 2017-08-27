package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static FloatingActionButton loggingfb;
    public static boolean loggedIn = false;
    public static boolean screenShowing = false;
    public static Context mainContext;
    public static Boolean loginScreenShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = MainActivity.this;

        ((TextView)findViewById(R.id.email)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.messenger)).setMovementMethod(LinkMovementMethod.getInstance());

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

    public void openSteps(View v) {
        Intent i = new Intent(getApplicationContext(), StepsActivity.class);
        startActivity(i);

    }

}
