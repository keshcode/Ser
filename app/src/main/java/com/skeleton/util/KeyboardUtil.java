package com.skeleton.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */
public final class KeyboardUtil {
    /**
     * Empty Constructor
     * not called
     */
    private KeyboardUtil() {
    }

    /**
     * Show keyboard.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void showKeyboard(final Activity activity, final View view) {
        if (activity != null) {
            if (view != null) {
                view.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    /**
     * Hide keyboard.
     *
     * @param activity the activity
     */
    public static void hideKeyboard(final Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                activity.getCurrentFocus().clearFocus();
            }
        }
    }

    /**
     * Hide keyboard.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void hideKeyboard(final Activity activity, final View view) {
        if (activity != null) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } else {
                hideKeyboard(activity);
            }
        }
    }
}
