package lk.cse13.www.uomwireless;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;


class Updates extends AsyncTask<String, Void, String> {
    private Context context;

    public Updates(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(String[] params) {
        String versionURL = "http://13.58.202.127/UoM_Wireless_App/version.php";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(new URI(versionURL)));

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                return responseString;
            } else {
                response.getEntity().getContent().close();
                return "false";
            }
        } catch (Exception e) {
            return "false";
        }
    }

    @Override
    protected void onPostExecute(String message) {
        if (!message.equals("false")) {

            int thisAppVersion = 0;//change this everytime updating the app

            try {
                if (new JSONObject(message).getInt("newversion") > thisAppVersion) {
                    Log.i("qqq","1");
                    JSONObject jsonObject = new JSONObject(message);
                    Log.i("qqq","2");
                    final String apkurl = jsonObject.getString("apkurl");
                    Log.i("qqq","3");
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                    Log.i("qqq","4");
                    dlgAlert.setMessage(jsonObject.getString("message"));
                    Log.i("qqq","5");
                    dlgAlert.setTitle(jsonObject.getString("title"));
                    Log.i("qqq","6");
                    dlgAlert.setPositiveButton(jsonObject.getString("positivebutton"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apkurl)));
                        }
                    });
                    Log.i("qqq","7");
                    dlgAlert.setNegativeButton(jsonObject.getString("negativebutton"), null);
                    Log.i("qqq","8");
                    dlgAlert.create();;
                    Log.i("qqq","9");
                    dlgAlert.show();
                    Log.i("qqq","10");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

