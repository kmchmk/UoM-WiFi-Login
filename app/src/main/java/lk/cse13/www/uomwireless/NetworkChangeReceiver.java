package lk.cse13.www.uomwireless;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.mainContext = context;
        NetworkInfo netinfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (netinfo != null && netinfo.isConnected()) {
            new BackgroundLogin(0).execute();
        } else {
            Operations.cancelNotification();
        }
    }

}