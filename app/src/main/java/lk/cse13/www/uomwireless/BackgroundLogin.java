package lk.cse13.www.uomwireless;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import lk.cse13.www.uomwireless.Views.MainActivity;

import static android.R.attr.password;


public class BackgroundLogin extends AsyncTask<String, Void, String> {
    private int trying;

    public BackgroundLogin(int trying) {
        this.trying = trying;
        if (MainActivity.screenShowing) {
            MainActivity.loginButton.setEnabled(false);
        }
    }


    @Override
    protected String doInBackground(String[] params) {
        Operations.cancelNotification();

        if (MainActivity.loginScreenShowing) {
            return "";
        }

        if (Operations.isConnectedToUoMWireless() || Operations.isConnectedToOtherSSID()) {

//            String username = Operations.readFromFile("username");
//            String password = Operations.readFromFile("password");
//
            String username = null;//this might give an error
            String password = null;//"    "
            String server_url = null;//"  "

            if(Operations.isConnectedToUoMWireless()) {
                username = Operations.getUsername();
                password = Operations.getPassword();
                server_url = "https://wlan.uom.lk/login.html";
            }
            else if(Operations.isConnectedToOtherSSID()){
                username = Operations.getOtherUsername();
                password = Operations.getOtherPassword();
                server_url = Operations.getOtherServer();
            }
            if (username == null || password == null || server_url==null) {
                trying = 10;
                return "Please complete your username, password or server url";
            } else if (Operations.isConnectedToUoMWireless() && username.length() < 7) {
                trying = 10;
                return "Index number is incorrect";
            }
            String responseString = "Couldn't log in. Open app to try again.";
            try {
                MyHttpClient httpClient = new MyHttpClient();

                HttpPost httpPost = new HttpPost(server_url);

                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("buttonClicked", "4"));
                para.add(new BasicNameValuePair("err_flag", "0"));
                para.add(new BasicNameValuePair("err_msg", ""));
                para.add(new BasicNameValuePair("info_flag", "0"));
                para.add(new BasicNameValuePair("info_msg", ""));
                para.add(new BasicNameValuePair("redirect_url", ""));
                para.add(new BasicNameValuePair("network_name", "Guest Network"));
                para.add(new BasicNameValuePair("username", username));//settings.getString("index", "")));
                para.add(new BasicNameValuePair("password", password));//settings.getString("password", "")));
                httpPost.setEntity(new UrlEncodedFormEntity(para));
                HttpResponse response = httpClient.execute(httpPost);

                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    if (Operations.isLoggedIn()) {
                        responseString = "Logged in";
                    }
                }
                return responseString;

            } catch (Exception e) {
                return responseString;
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        MainActivity.loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                    MainActivity.loggedIn = true;
                } else {
                    Operations.showNotification("Logged in successfully!");
                }
            } else {
                if (trying < 10) {
                    try {
                        Thread.sleep(trying * 500);
                        Operations.toast("Trying to login again...");
                        new BackgroundLogin(trying + 1).execute();
                    } catch (InterruptedException ignore) {
                    }
                } else {
                    Operations.showNotification(message);
                    Operations.toast(message);
                }
            }
        }
        if (MainActivity.screenShowing) {
            MainActivity.loginButton.setEnabled(true);
        }
    }

}


