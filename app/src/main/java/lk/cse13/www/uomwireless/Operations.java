package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;
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

    public static boolean isConnectedToInternet() {
        try {
            Runtime runtime = Runtime.getRuntime();
            if(isConnectedToUoMWireless()) {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 10.10.31.254");//pinging nearest router
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            }else if(isConnectedToOtherSSID()){
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 8.8.8.8");//pinging google's DNS server
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static boolean isConnectedToUoMWireless() {
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"");
    }


    public static String getOtherSSID() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("otherssid", "");
    }

    public static boolean isConnectedToOtherSSID() {
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String otherssid = getOtherSSID();
        return info.getSSID().equalsIgnoreCase("\""+otherssid+"\"");
    }




    private static boolean isNotificationEnabled() {
        return getPreferences("notification_enabled");
    }

    private static boolean isToastEnabled() {
        return getPreferences("toast_enabled");
    }

    public static boolean isVibrationEnabled() {
        return getPreferences("vibration_enabled");
    }

    public static boolean isSoundEnabled() {
        return getPreferences("sound_enabled");
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
        editor.apply();
    }

    private static Boolean getPreferences(String type) {
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

    public static String getOtherUsername() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("otherusername", "");
    }

    public static String getOtherPassword() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("otherpassword", "");
    }

    public static String getOtherServer() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainContext);
        return sharedPref.getString("otherserver", "");
    }
}
