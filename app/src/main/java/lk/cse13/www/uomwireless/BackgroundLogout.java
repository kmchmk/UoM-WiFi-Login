package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class BackgroundLogout extends AsyncTask<String, Void, String> {
    private Operations operations;
    public BackgroundLogout(Operations operations) {
        this.operations = operations;
    }

    @Override
    protected String doInBackground(String[] params) {
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getSSID().toString().equalsIgnoreCase("\"UoM_Wireless\"")) {
            try {

                MyHttpClient httpClient = new MyHttpClient();

                HttpPost httpPost = new HttpPost("https://wlan.uom.lk/logout.html");

                List<NameValuePair> para = new ArrayList<NameValuePair>();

                para.add(new BasicNameValuePair("userStatus", "1"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));

                httpPost.setEntity(new UrlEncodedFormEntity(para));
                HttpResponse response = httpClient.execute(httpPost);
                String responseString = "Couldn't log out";
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    Runtime runtime = Runtime.getRuntime();
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 10.10.31.254");
                    int exitValue = ipProcess.waitFor();
                    if (exitValue != 0) {
                        responseString = "Logged out";
                    }
                }
                return responseString;
            } catch (Exception e) {
                return "Error 451";
            }
        } else {
            return "Connect to UoM Wireless first";
        }
    }

    @Override
    protected void onPostExecute(String message) {
        operations.toast(message);
        if (message.equals("Logged out")) {
            MainActivity.loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            MainActivity.loggedIn = false;
        }
    }
}

