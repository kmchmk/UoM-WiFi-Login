package lk.cse13.www.uomwireless;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

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


public class BackgroundLogin extends AsyncTask<String, Void, Integer> {
    private int trying;

    public BackgroundLogin(int trying) {
        this.trying = trying;
        if (MainActivity.screenShowing) {
            MainActivity.loginButton.setEnabled(false);
        }
    }


    @Override
    protected Integer doInBackground(String[] params) {
        Operations.cancelNotification();

        if (MainActivity.loginScreenShowing) {//User is in the settings page - Shouldn't do anything
            return 0;//Do nothing
        }

        if (Operations.isConnectedToUoMWireless() || Operations.isConnectedToOtherSSID()) {

            String username = null;//this might give an error
            String password = null;//"    "
            String server_url = null;//"  "

            if (Operations.isConnectedToUoMWireless()) {
                username = Operations.getUsername();
                password = Operations.getPassword();
                server_url = "https://wlan.uom.lk/login.html";
            } else if (Operations.isConnectedToOtherSSID()) {
                username = Operations.getOtherUsername();
                password = Operations.getOtherPassword();
                server_url = Operations.getOtherServer();
            }
            if (username == null || password == null || server_url == null) {
                return 1;
            } else if (Operations.isConnectedToUoMWireless() && username.length() < 7) {
                return 2;
            }

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
                    if (Operations.isConnectedToInternet()) {
                        return 4;
                    } else {
                        return 6;
                    }
                } else {
                    if (Operations.isConnectedToInternet()) {
                        return 5;
                    } else {
                        return 3;
                    }
                }

            } catch (Exception e) {
                return 3;
            }
        }
        return 0;//Do nothing
    }



//            0 = ""//Do nothing
//            1 = "Please complete your username, password or server url"
//            2 = "Index number is incorrect"
//            3 = "Couldn't log in".// Open app to try again."
//            4 = "Logged in"
//            5 = "Internet connected"
//            6 = "Logged in. Internet not working"

    @Override
    protected void onPostExecute(Integer message) {

        if (message == 0) {
            /*ignore*/
        }
        else if (message == 1) {
            Operations.toast("Please complete your username, password or server url");
        } else if (message == 2) {
            Operations.toast("Index number is incorrect");
        } else if (message == 3) {
            if (trying < 5) {
                try {
                    Thread.sleep(trying * 500);
                    Operations.toast("Trying to login again...");
                    new BackgroundLogin(trying + 1).execute();
                } catch (InterruptedException ignore) {
                }
            } else {
                Operations.showNotification("Couldn't log in. Open app to try again.");
                Operations.toast("Couldn't log in. Open app to try again.");
            }
        } else if (message == 4) {
            Operations.toast("Logged in");
            if (MainActivity.screenShowing) {
                MainActivity.loginButton.setText("Click here lo logout");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    MainActivity.loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                }
                MainActivity.loggedIn = true;
            } else {
                Operations.showNotification("Logged in successfully!");
            }
        } else if (message == 5) {
            Operations.toast("Internet connected");
            if (MainActivity.screenShowing) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    MainActivity.loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                }
            } else {
                Operations.showNotification("Couldn't login, but internet is working");
            }
        } else if (message == 6) {
            Operations.toast("Internet is not working");
            if (!MainActivity.screenShowing) {
                Operations.showNotification("Internet is not working.");
            }
        } else {
            Operations.toast("Undefined error occured");
        }


//        if (message != 0) {//this means not connected to UoM Wireless. So ignore.
//            Operations.toast(message);
//
//            if (message.equals("Logged in")) {
////                if (MainActivity.screenShowing) {
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        MainActivity.loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
////                    }
////                    MainActivity.loggedIn = true;
////                } else {
////                    Operations.showNotification("Logged in successfully!");
////                }
//            } else {
////                if (trying < 5) {
////                    try {
////                        Thread.sleep(trying * 500);
////                        Operations.toast("Trying to login again...");
////                        new BackgroundLogin(trying + 1).execute();
////                    } catch (InterruptedException ignore) {
////                    }
////                } else {
////                    Operations.showNotification(message);
////                    Operations.toast(message);
////                }
//            }
//        }
        if (MainActivity.screenShowing) {
            MainActivity.loginButton.setEnabled(true);
        }
    }


}


