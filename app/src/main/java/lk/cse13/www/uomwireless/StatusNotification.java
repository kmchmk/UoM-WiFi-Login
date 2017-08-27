package lk.cse13.www.uomwireless;

import android.app.PendingIntent;
import android.content.Context;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class StatusNotification {
 /*<a href="https://developer.android.com/design/patterns/notifications.html">*/

    public static void notify(final Context context, final String title, final String text) {
        final Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)//R.drawable.ic_vpn_key_black_24dp)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(0);
    }
}
