package com.skeleton.locationLib.locationservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.skeleton.database.CommonData;


/**
 * The type Boot receiver.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // Make sure we are getting the right intent
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (CommonData.getAccessToken() != null) {
                context.startService(new Intent(context, BackgroundLocationService.class));
            }
        } else {
            //Received unexpected intent
            Log.e(TAG, intent.toString());
        }
    }
}