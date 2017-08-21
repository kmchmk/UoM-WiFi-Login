package lk.cse13.www.uomwireless;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


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

import android.provider.Settings.Secure;
import android.util.Log;


class Updates extends AsyncTask<String, Void, Boolean> {
    private static String apkurl = "";
    private String responseString;

    @Override
    protected Boolean doInBackground(String[] params) {
        String android_id = Secure.getString(MainActivity.mainContext.getContentResolver(), Secure.ANDROID_ID);
        String versionURL = "http://13.58.202.127/UoM_Wireless_App/version.php?device=" + android_id;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(new URI(versionURL)));

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
                return true;
            } else {
                response.getEntity().getContent().close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean succesful) {
        if (succesful) {

            //change the version in build.gradle everytime updating the app

            try {
                if (new JSONObject(responseString).getInt("newversion") > BuildConfig.VERSION_CODE) {
                    JSONObject jsonObject = new JSONObject(responseString);
                    apkurl = jsonObject.getString("apkurl");
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.mainContext);
                    dlgAlert.setMessage(jsonObject.getString("message"));
                    dlgAlert.setTitle(jsonObject.getString("title"));
                    dlgAlert.setPositiveButton(jsonObject.getString("positivebutton"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.mainContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apkurl)));
                            if (isStoragePermissionGranted()) {
                                downloadNewAPK();
                            }
                        }
                    });
                    dlgAlert.setNegativeButton(jsonObject.getString("negativebutton"), null);
                    dlgAlert.create();
                    if (MainActivity.screenShowing) {
                        dlgAlert.show();
                    } else {
                        StatusNotification.notify(MainActivity.mainContext, "Update:", "New update is available for 'UoM Wireless' application!");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (MainActivity.mainContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) MainActivity.mainContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public static void downloadNewAPK() {

        String apk = "UoM_Wireless.apk";
        String description = "Install this after downloading.";
        Uri destinationFile = Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + apk);

        DownloadManager downloadmanager = (DownloadManager) MainActivity.mainContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(apkurl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(apk);
        request.setDescription(description);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(destinationFile);
        downloadmanager.enqueue(request);
    }
}

