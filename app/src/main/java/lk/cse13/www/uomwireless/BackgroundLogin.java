package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static lk.cse13.www.uomwireless.MainActivity.mainContext;


public class BackgroundLogin extends AsyncTask<String, Void, String> {
    private Operations operations;
    private int trying;

    public BackgroundLogin(Operations operations, int trying) {
        this.operations = operations;
        this.trying = trying;
    }


    @Override
    protected String doInBackground(String[] params) {
        Log.i("qqq", "1");
        WifiManager wifiManager = (WifiManager) mainContext.getSystemService(Context.WIFI_SERVICE);
        //Log.i("qqq","2");
        WifiInfo info = wifiManager.getConnectionInfo();
        //Log.i("qqq","3");
//        if (info.getSSID().toString().equalsIgnoreCase("\"UoM_Wireless\"")) {
        if (info.getSSID().toString().equalsIgnoreCase("\"ChanakaWiFi\"")) {
            Log.i("qqq", "4");
            try {

                MyHttpClient httpClient = new MyHttpClient();

                //HttpPost httpPost = new HttpPost("https://wlan.uom.lk/login.html");
                HttpPost httpPost = new HttpPost("http://13.58.202.127/");//delete this

                List<NameValuePair> para = new ArrayList<NameValuePair>();
                Log.i("qqq", "5");
                para.add(new BasicNameValuePair("buttonClicked", "4"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));
                para.add(new BasicNameValuePair("info_flag", "0"));
                para.add(new BasicNameValuePair("info_msg", ""));
                para.add(new BasicNameValuePair("redirect_url", ""));
                para.add(new BasicNameValuePair("network_name", "Guest Network"));
                Log.i("qqq", "6");
                para.add(new BasicNameValuePair("username", operations.readFromFile("ind")));
                Log.i("qqq", "7");
                para.add(new BasicNameValuePair("password", operations.readFromFile("psd")));
                Log.i("qqq", "8");
                httpPost.setEntity(new UrlEncodedFormEntity(para));
                Log.i("qqq", "9");
                HttpResponse response = httpClient.execute(httpPost);
                Log.i("qqq", "10");
                String responseString = "Couldn't log in";
                Log.i("qqq", "11");
                StatusLine statusLine = response.getStatusLine();
                Log.i("qqq", "12");

                Log.i("qqq", "13");
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    //check internet connection // use internel ip 10.10.31.254
                    HttpURLConnection urlc = (HttpURLConnection) new URL("http://www.google.com").openConnection();
                    urlc.setConnectTimeout(1000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        responseString = "Logged in";
                    }
                }
                return responseString;

            } catch (Exception e) {
                Log.i("qqq", e.toString());
                return "Error 1";
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String message) {
        operations.toast(message);//delete this
        if (!message.equals("")) {//this means not connected to UoM Wireless. So ignore.
            operations.toast(message);
            Log.i("qqq", "-3");
            if (message.equals("Logged in")) {
                Log.i("qqq", "-4");
                if (MainActivity.screenShowing) {
                    Log.i("qqq", "-5");
                    MainActivity.loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    MainActivity.loggedIn = true;
                    new Updates(operations).execute();
                }
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
    }


}


