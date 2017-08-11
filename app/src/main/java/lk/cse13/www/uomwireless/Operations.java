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

    public void writeToFile(String data, String file) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.mainContext.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            toast("Cannot save username or password");
        }
    }

    public String readFromFile(String file) {

        String ret = "";
        try {
            InputStream inputStream = MainActivity.mainContext.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            toast("Username or Password not found");
        } catch (IOException e) {
            toast("Cannot read username or password");
        }

        return ret;
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
        if (info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"")) {
            return true;
        } else {
            return false;
        }
    }
}
