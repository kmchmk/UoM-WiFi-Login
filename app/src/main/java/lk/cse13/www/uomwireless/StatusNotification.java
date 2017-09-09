package lk.cse13.www.uomwireless;

import android.app.PendingIntent;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class StatusNotification {
    public static void notify(final String text) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.mainContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You can disable or turn off sound/vibration for this notification. Open the app and go to settings."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(MainActivity.mainContext, 0, new Intent(MainActivity.mainContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        Boolean vibrationEnabled = Operations.getPreferences("vibration_enabled");
        Boolean soundEnabled = Operations.getPreferences("sound_enabled");
        if (soundEnabled && !vibrationEnabled) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        else if (!soundEnabled && vibrationEnabled) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        else if (soundEnabled && vibrationEnabled) {
            builder.setDefaults(Notification.DEFAULT_ALL);
        }
        notify(MainActivity.mainContext, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel() {
        final NotificationManager nm = (NotificationManager) MainActivity.mainContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(0);
    }
}
