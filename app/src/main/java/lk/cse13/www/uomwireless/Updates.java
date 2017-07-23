package lk.cse13.www.uomwireless;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.net.URI;


class Updates extends AsyncTask<String, Void, String> {
    private Operations operations;

    public Updates(Operations operations) {
        this.operations = operations;
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
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String message) {
        if (!message.equals("")) {
            operations.writeToFile(message, "newVersion");
        }
    }
}

