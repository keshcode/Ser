package com.skeleton.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skeleton.R;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */
public final class NetworkUtil {
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;

    /**
     * Empty Constructor
     * not called
     */
    private NetworkUtil() {
    }


    /**
     * Gets connectivity status.
     *
     * @param context the context
     * @return the connectivity status
     */
    public static int getConnectivityStatus(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    /**
     * Gets connectivity status string.
     *
     * @param context the context
     * @return the connectivity status string
     */
    public static String getConnectivityStatusString(final Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = context.getString(R.string.msg_wifi_enabled);
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = context.getString(R.string.msg_mobile_data_enabled);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.msg_not_connected_to_internet);
        }
        return status;
    }
}
