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


import static lk.cse13.www.uomwireless.MainActivity.mainContext;


public class BackgroundLogin extends AsyncTask<String, Void, String> {
    private Operations operations;
    private int trying;

    public BackgroundLogin(Operations operations, int trying) {
        this.operations = operations;
        this.trying = trying;
        if (MainActivity.screenShowing) {
            MainActivity.loggingfb.setEnabled(false);
        }
    }


    @Override
    protected String doInBackground(String[] params) {
        WifiManager wifiManager = (WifiManager) mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getSSID().equalsIgnoreCase("\"UoM_Wireless\"")) {


//            This takes time before login in
//            if (isLoggedIn()) {
//                return "Already logged in";
//            }

            try {
                MyHttpClient httpClient = new MyHttpClient();

                HttpPost httpPost = new HttpPost("https://wlan.uom.lk/login.html");

                List<NameValuePair> para = new ArrayList<NameValuePair>();
                para.add(new BasicNameValuePair("buttonClicked", "4"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));
                para.add(new BasicNameValuePair("info_flag", "0"));
                para.add(new BasicNameValuePair("info_msg", ""));
                para.add(new BasicNameValuePair("redirect_url", ""));
                para.add(new BasicNameValuePair("network_name", "Guest Network"));
                para.add(new BasicNameValuePair("username", operations.readFromFile("ind")));
                para.add(new BasicNameValuePair("password", operations.readFromFile("psd")));
                httpPost.setEntity(new UrlEncodedFormEntity(para));
                HttpResponse response = httpClient.execute(httpPost);
                String responseString = "Couldn't log in";
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    if (isLoggedIn()) {
                        responseString = "Logged in";
                    }
                }
                return responseString;

            } catch (Exception e) {
                return "Error 1";
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String message) {
        if (!message.equals("")) {//this means not connected to UoM Wireless. So ignore.
            operations.toast(message);
            if (message.equals("Logged in")){// || message.equals("Already logged in")) { //Look at line 46
                if (MainActivity.screenShowing) {
                    MainActivity.loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    MainActivity.loggedIn = true;
                }
                new Updates(operations).execute();

            } else {
                if (trying < 10) {
                    try {
                        Thread.sleep(trying * 100);
                        operations.toast("Trying to login again...");
                        new BackgroundLogin(operations, trying + 1).execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (MainActivity.screenShowing) {
            MainActivity.loggingfb.setEnabled(true);
        }
    }

    Boolean isLoggedIn() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 10.10.31.254");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            return false;
        }
    }

}


