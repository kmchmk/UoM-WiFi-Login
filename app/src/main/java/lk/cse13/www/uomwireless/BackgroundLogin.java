package lk.cse13.www.uomwireless;

import android.content.Context;
import android.os.AsyncTask;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
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


public class BackgroundLogin extends AsyncTask<String, Void, String> {
    private Operations operations;
private Context context;
    public BackgroundLogin(Context context) {
        this.context = context;
        operations = new Operations(context);
    }

    @Override
    protected String doInBackground(String[] params) {

        HttpResponse response = null;

        try {

            MyHttpClient httpClient = new MyHttpClient();

            HttpPost httpPost = new HttpPost("https://wlan.uom.lk/login.html");

            List<NameValuePair> para = new ArrayList<NameValuePair>();

            para.add(new BasicNameValuePair("buttonClicked", "4"));
            para.add(new BasicNameValuePair("err_flag", "0"));
            para.add(new BasicNameValuePair("err_msg", ""));
            para.add(new BasicNameValuePair("info_flag", "0"));
            para.add(new BasicNameValuePair("info_msg", ""));
            para.add(new BasicNameValuePair("redirect_url", ""));
            para.add(new BasicNameValuePair("network_name", "Guest Network"));
            para.add(new BasicNameValuePair("username", operations.readFromFile("ind")));
            para.add(new BasicNameValuePair("password", operations.readFromFile("psd")));

            httpPost.setEntity(new UrlEncodedFormEntity(para));


            response = httpClient.execute(httpPost);
            String responseString = "Nothing";
            StatusLine statusLine = response.getStatusLine();
            Toast.makeText(context,"Logging out...",Toast.LENGTH_LONG).show();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            }
            return responseString;
        } catch (Exception e) {
            return e.toString();
        }

    }

    @Override
    protected void onPostExecute(String message) {
//        Log.i("get", message);
    }


}


class MyHttpClient extends DefaultHttpClient {
    TrustManager easyTrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(
                X509Certificate[] chain,
                String authType) {
        }

        @Override
        public void checkServerTrusted(
                X509Certificate[] chain,
                String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", newSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }


    private MySSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            try {
                trusted.load(null, null);

            } finally {
            }

            MySSLSocketFactory sslfactory = new MySSLSocketFactory(trusted);
            sslfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return sslfactory;
        } catch (Exception e) {
            throw new AssertionError(e);
        }

    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}