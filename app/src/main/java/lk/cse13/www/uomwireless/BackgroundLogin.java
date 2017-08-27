package lk.cse13.www.uomwireless;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import static android.content.Context.MODE_PRIVATE;


public class BackgroundLogin extends AsyncTask<String, Void, String> {
    private int trying;

    public BackgroundLogin( int trying) {
        this.trying = trying;
        if (MainActivity.screenShowing) {
            MainActivity.loggingfb.setEnabled(false);
        }
    }


    @Override
    protected String doInBackground(String[] params) {
        StatusNotification.cancel(MainActivity.mainContext);
        if (Operations.isConnectedToUoMWireless()) {
            try {
                MyHttpClient httpClient = new MyHttpClient();

                HttpPost httpPost = new HttpPost("https://wlan.uom.lk/login.html");

                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("buttonClicked", "4"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));
                para.add(new BasicNameValuePair("info_flag", "0"));
                para.add(new BasicNameValuePair("info_msg", ""));
                para.add(new BasicNameValuePair("redirect_url", ""));
                para.add(new BasicNameValuePair("network_name", "Guest Network"));
//                SharedPreferences settings = MainActivity.mainContext.getSharedPreferences("index_password", MODE_PRIVATE);
                para.add(new BasicNameValuePair("username", Operations.readFromFile("username")));//settings.getString("index", "")));
                para.add(new BasicNameValuePair("password", Operations.readFromFile("password")));//settings.getString("password", "")));
                httpPost.setEntity(new UrlEncodedFormEntity(para));
                HttpResponse response = httpClient.execute(httpPost);
                String responseString = "Couldn't log in";
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    if (Operations.isLoggedIn()) {
                        responseString = "Logged in";
                    }
                }
                return responseString;

            } catch (Exception e) {
                return "Error 134";
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String message) {
        if (!message.equals("")) {//this means not connected to UoM Wireless. So ignore.
            Operations.toast(message);

            if (message.equals("Logged in")) {
                if (MainActivity.screenShowing) {
                    MainActivity.loggingfb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    MainActivity.loggedIn = true;
                }

                else if (Operations.isNotificationEnabled()) {
                    StatusNotification.notify(MainActivity.mainContext, "Status:", "Logged in successfully!");
                }

                new Statistics().execute();

            } else {
                if (trying < 10) {
                    try {
                        Thread.sleep(trying * 500);
                        Operations.toast("Trying to login again...");
                        new BackgroundLogin( trying + 1).execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    StatusNotification.notify(MainActivity.mainContext, "Error:", "Couldn't log in!");
                }
            }
        }
        if (MainActivity.screenShowing) {
            MainActivity.loggingfb.setEnabled(true);
        }
    }

}


