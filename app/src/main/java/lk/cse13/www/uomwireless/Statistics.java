package lk.cse13.www.uomwireless;

import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

import android.provider.Settings.Secure;


class Statistics extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String[] params) {
        String android_id = Secure.getString(MainActivity.mainContext.getContentResolver(), Secure.ANDROID_ID);
        String statisticsURL = "http://13.58.202.127/UoM_Wireless_App/statistics.php?device=" + android_id;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.execute(new HttpGet(new URI(statisticsURL)));
        } catch (Exception e) {
        }
        return null;
    }
}

