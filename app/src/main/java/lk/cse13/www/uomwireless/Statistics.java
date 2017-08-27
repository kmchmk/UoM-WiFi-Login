package lk.cse13.www.uomwireless;

import android.bluetooth.BluetoothAdapter;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.provider.Settings.Secure;

class Statistics extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String[] params) {
        String device_id = Secure.getString(MainActivity.mainContext.getContentResolver(), Secure.ANDROID_ID);
        String device_model = Build.MODEL;
        String device_name = BluetoothAdapter.getDefaultAdapter().getName();
        String statisticsURL = "http://13.58.202.127/UoM_Wireless_App/statistics.php";//?device_id=" + device_id + "&device_model=" + device_model + "&device_name=" + device_name;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(statisticsURL);
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("device_id", device_id));
            para.add(new BasicNameValuePair("device_model", device_model));
            para.add(new BasicNameValuePair("device_name", device_name));
            httpPost.setEntity(new UrlEncodedFormEntity(para, "UTF-8"));
            httpclient.execute(httpPost);
        } catch (Exception ignore) {
        }
        return null;
    }
}

