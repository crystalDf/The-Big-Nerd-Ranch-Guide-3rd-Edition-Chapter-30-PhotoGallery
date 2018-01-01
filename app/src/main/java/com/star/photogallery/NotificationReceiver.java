package com.star.photogallery;


import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "received result: " + getResultCode());

        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }

        int requestCode = intent.getIntExtra(PollServiceUtils.REQUEST_CODE_KEY, 0);

        Notification notification = intent.getParcelableExtra(PollServiceUtils.NOTIFICATION_KEY);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(requestCode, notification);
    }
}
