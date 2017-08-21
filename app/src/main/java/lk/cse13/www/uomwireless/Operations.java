package lk.cse13.www.uomwireless;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Operations {
    private static Toast toastvariable;

    public static void toast(String message) {
        if (toastvariable != null) {
            toastvariable.cancel();
        }
        toastvariable = Toast.makeText(MainActivity.mainContext, message, Toast.LENGTH_SHORT);
        toastvariable.show();
    }

    public boolean isLoggedIn() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 10.10.31.254");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isConnectedToUoMWireless() {
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"");
    }

    public boolean isHuawei(){
        return true;
    }
}
