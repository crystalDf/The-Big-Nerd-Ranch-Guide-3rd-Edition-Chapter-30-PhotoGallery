package com.star.photogallery;


import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

public class PollServiceUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "channelId";

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.star.photogallery.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE = "com.star.photogallery.PRIVATE";

    public static final String REQUEST_CODE_KEY = "REQUEST_CODE_KEY";
    private static final int REQUEST_CODE_VALUE = 0;

    public static final String NOTIFICATION_KEY = "NOTIFICATION";

    public static void pollFlickr(Context context) {

        String tag = context.getClass().getName();

        if (!isNetworkAvailableAndConnected(context)) {
            return;
        }

        String query = QueryPreferences.getStoredQuery(context);
        String lastResultId = QueryPreferences.getLastResultId(context);

        List<GalleryItem> items;

        if (query == null) {
            items = new FlickrFetchr().getRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }

        if (items.size() == 0) {
            return;
        }

        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.i(tag, "Got an old result: " + resultId);
        } else {
            Log.i(tag, "Got an new result: " + resultId);

            Resources resources = context.getResources();
            Intent i = PhotoGalleryActivity.newIntent(context);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(context, REQUEST_CODE_VALUE, notification);
        }

        QueryPreferences.setLastResultId(context, resultId);
    }

    private static void showBackgroundNotification(
            Context context, int requestCode, Notification notification) {
        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(REQUEST_CODE_KEY, requestCode);
        intent.putExtra(NOTIFICATION_KEY, notification);
        context.sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    private static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
