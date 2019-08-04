package lk.cse13.www.uomwireless;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import lk.cse13.www.uomwireless.Views.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class Operations {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
            if (isConnectedToUoMWireless()) {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 10.10.31.254");//pinging nearest router
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } else if (isConnectedToOtherSSID()) {
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
        return info.getSSID().equalsIgnoreCase("\"" + otherssid + "\"");
    }

    public static boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            //For Oreo and previous Android versions, locations is not needed.
            return true;
        }
        LocationManager locationManager = (LocationManager) MainActivity.mainContext.getSystemService(Activity.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return requestLocationPermission();
        } else {
            toast("Enable location service first");
            MainActivity.mainContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return false;
        }
    }


    private static boolean requestLocationPermission() {
        int fineLocation = ContextCompat.checkSelfPermission(MainActivity.mainContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ContextCompat.checkSelfPermission(MainActivity.mainContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocation != PackageManager.PERMISSION_GRANTED && coarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) MainActivity.mainContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
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
