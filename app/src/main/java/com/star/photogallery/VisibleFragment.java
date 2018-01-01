package com.star.photogallery;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class VisibleFragment extends Fragment {

    private static final String TAG = "VisibleFragment";

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(PollServiceUtils.ACTION_SHOW_NOTIFICATION);

        if (getActivity() == null) {
            return;
        }

        getActivity().registerReceiver(mOnShowNotification, intentFilter,
                PollServiceUtils.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity() == null) {
            return;
        }

        getActivity().unregisterReceiver(mOnShowNotification);
    }
}
