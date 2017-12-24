package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import lk.cse13.www.uomwireless.Views.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class Operations {
    private static Toast toastvariable;

    public static void toast(String message) {
        if (isToastEnabled() || MainActivity.screenShowing || MainActivity.loginScreenShowing) {
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

    private static boolean isVibrationEnabled() {
        SharedPreferences preferences = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
        return (preferences.getBoolean("toast_enabled", true));
    }

    private static boolean isSoundEnabled() {
        SharedPreferences preferences = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
        return (preferences.getBoolean("toast_enabled", true));
    }


   /* public static String readFromFile(String file) {
        String ret = "";
        try {
            InputStream inputStream = MainActivity.mainContext.openFileInput(file);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ret = bufferedReader.readLine();
                inputStream.close();
            }
        } catch (IOException ignored) {
        }
        return ret;
    }
*/
    public static void writeToFile(String data, String file) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.mainContext.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException ignored) {
        }
    }

    public static void showNotification(String notification) {
        if (isNotificationEnabled()) {
            StatusNotification.notify(notification);
        }
    }

    public static void cancelNotification() {
        StatusNotification.cancel();
    }

    public static void savePreferences(String type, Boolean value) {
        SharedPreferences settings = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(type, value);
        editor.commit();
    }

    public static Boolean getPreferences(String type) {
//        SharedPreferences preferences = MainActivity.mainContext.getSharedPreferences("preferences", MODE_PRIVATE);
//        return preferences.getBoolean(type, true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getBoolean(type, true);
    }


    public static String getUsername() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("index", "");
    }

    public static String getPassword() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("password", "");
    }
}
