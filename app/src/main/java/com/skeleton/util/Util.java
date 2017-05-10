package com.skeleton.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.skeleton.R;
import com.skeleton.activity.SplashActivity;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */

public final class Util {

    public static final int TEN = 10;

    /**
     * Empty Constructor
     * not called
     */
    private Util() {
    }

    /**
     * Method to check if internet is connected
     *
     * @param context context of calling class
     * @return true if connected to any network else return false
     */
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (context.getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Method to show toast
     *
     * @param message  that disaply in the Toast
     * @param mContext context of calling activity or fragment
     */
    public static void showToast(final Context mContext, final String message) {
        if (mContext == null
                || message == null
                || message.isEmpty()) {
            return;
        }
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param mContext context of calling activity or fragment
     */
    public static void restartAppOnSessionExpired(final Context mContext) {
        Util.showToast(mContext, mContext.getString(R.string.session_expired));
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * @param context of calling activity or fragment
     * @return pixel scale factor
     */
    private static float getPixelScaleFactor(final Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT;
    }


    /**
     * Method to convert dp into pixel
     *
     * @param context of calling activity or fragment
     * @param dp      dp value that need to convert into px
     * @return px converted dp into px
     */
    public static int dpToPx(final Context context, final int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }

    /**
     * Method to convert pixel into dp
     *
     * @param context of calling activity or fragment
     * @param px      pixel value that need to convert into dp
     * @return dp converted px into dp
     */
    public static int pxToDp(final Context context, final int px) {
        int dp = Math.round(px / getPixelScaleFactor(context));
        return dp;
    }
}
