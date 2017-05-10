package com.skeleton.fcm;

import android.os.Handler;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.skeleton.database.CommonData;
import com.skeleton.util.Log;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIdService.class.getName();
    private static final int FCM_CALL_TIMEOUT = 20000;
    private static FCMTokenInterface fcmTokenCallback;
    private static Handler handlerOs = new Handler();

    /**
     * Sets callback.
     *
     * @param callback the callback
     */
    public static void setCallback(final FCMTokenInterface callback) {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            CommonData.updateFCMToken(token);
            callback.onTokenReceived(token);
            return;
        }
        fcmTokenCallback = callback;
        startHandler();
    }

    /**
     * Retry.
     *
     * @param callback the callback
     */
    public static void retry(final FCMTokenInterface callback) {
        setCallback(callback);
    }

    /**
     *
     */
    private static void startHandler() {
        handlerOs.postDelayed(new Runnable() {
            @Override
            public void run() {
                fcmTokenCallback.onFailure();
                fcmTokenCallback = null;
            }
        }, FCM_CALL_TIMEOUT);
    }

    /**
     *
     */
    private static void clearHandler() {
        handlerOs.removeCallbacksAndMessages(null);
    }

    /**
     * The Application's current Instance ID token is no longer valid and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or after a refresh this is
        // where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, token);
        if (token != null) {
            CommonData.updateFCMToken(token);
        }
        if (token != null && fcmTokenCallback != null) {
            fcmTokenCallback.onTokenReceived(token);
            fcmTokenCallback = null;
            clearHandler();
        }
    }
}
