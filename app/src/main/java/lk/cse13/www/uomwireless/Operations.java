package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class Operations {
    private static Toast toastvariable;

    public static void toast(String message) {
        if (isToastEnabled()) {
            if (toastvariable != null) {
                toastvariable.cancel();
            }
            toastvariable = Toast.makeText(MainActivity.mainContext, message, Toast.LENGTH_SHORT);
            toastvariable.show();
        }
    }

    public static boolean isLoggedIn() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 10.10.31.254");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isConnectedToUoMWireless() {
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"");
    }

    public static boolean isNotificationEnabled() {
        SharedPreferences preferences = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
        return (preferences.getBoolean("notification_enabled", true));
    }

    private static boolean isToastEnabled() {
        SharedPreferences preferences = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
        return (preferences.getBoolean("toast_enabled", true));
    }
}
