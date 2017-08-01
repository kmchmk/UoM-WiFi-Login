package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class BackgroundLogout extends AsyncTask<String, Void, String> {
    private Operations operations;
    public BackgroundLogout(Operations operations) {
        this.operations = operations;
    }

    @Override
    protected String doInBackground(String[] params) {
        Log.i("qqq","inside logout");
        WifiManager wifiManager = (WifiManager) MainActivity.mainContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        Log.i("qqq","21");
        if (info.getSSID().toString().equalsIgnoreCase("\"ChanakaWiFi\"")) {
            Log.i("qqq","22");
            try {

                MyHttpClient httpClient = new MyHttpClient();

                HttpPost httpPost = new HttpPost("https://wlan.uom.lk/logout.html");

                List<NameValuePair> para = new ArrayList<NameValuePair>();

                para.add(new BasicNameValuePair("userStatus", "1"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));

                httpPost.setEntity(new UrlEncodedFormEntity(para));
                Log.i("qqq","23");
                HttpResponse response = httpClient.execute(httpPost);
                Log.i("qqq","24");
                String responseString = "Couldn't log out";
                StatusLine statusLine = response.getStatusLine();

                Log.i("qqq","25");
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                    HttpURLConnection urlc = (HttpURLConnection) new URL("http://www.google.com").openConnection();
                    urlc.setConnectTimeout(1000);
                    urlc.connect();
                    if (urlc.getResponseCode() != 200) {//test this
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
        if(message.equals("Logged out")) {
            MainActivity.loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            MainActivity.loggedIn = false;
        }
    }


}

