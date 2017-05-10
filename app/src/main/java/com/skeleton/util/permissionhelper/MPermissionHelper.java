package com.skeleton.util.permissionhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.skeleton.R;
import com.skeleton.util.dialog.CustomAlertDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.skeleton.constant.AppConstant.REQ_CODE_DEFAULT_SETTINGS;

/**
 * Developer: Saurabh Verma
 * Dated: 07/03/17.
 */
public final class MPermissionHelper {

    /**
     * Empty Constructor
     * not called
     */
    private MPermissionHelper() {
    }

    //=================================== Methods for requesting permission ==============================

    /**
     * Request a set of permissions, showing a rationale if the system requests it.
     *
     * @param activity    {@link Activity} requesting permissions. Should implement {@link
     *                    ActivityCompat.OnRequestPermissionsResultCallback} or override {@link
     *                    FragmentActivity#onRequestPermissionsResult(int, String[], int[])} if
     *                    it extends from {@link FragmentActivity}.
     * @param rationale   a message explaining why the application needs this set of permissions,
     *                    will be displayed if the user rejects the request the first time.
     * @param requestCode request code to track this request, must be < 256.
     * @param perms       a set of permissions to be requested.
     */
    public static void requestPermissions(@NonNull final Activity activity,
                                          @NonNull final String rationale,
                                          final int requestCode,
                                          @NonNull final String... perms) {
        requestPermissions(
                activity,
                rationale,
                android.R.string.ok,
                android.R.string.cancel,
                requestCode,
                perms);

    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param activity       {@link Activity} requesting permissions. Should implement {@link
     *                       ActivityCompat.OnRequestPermissionsResultCallback} or override {@link
     *                       FragmentActivity#onRequestPermissionsResult(int, String[], int[])} if
     *                       it extends from {@link FragmentActivity}.
     * @param rationale      a message explaining why the application needs this set of permissions,
     *                       will be displayed if the user rejects the request the first time.
     * @param positiveButton custom text for positive button
     * @param negativeButton custom text for negative button
     * @param requestCode    request code to track this request, must be < 256.
     * @param perms          a set of permissions to be requested.
     * @see Manifest.permission
     */
    public static void requestPermissions(@NonNull final Activity activity,
                                          @NonNull final String rationale,
                                          @StringRes final int positiveButton,
                                          @StringRes final int negativeButton,
                                          final int requestCode,
                                          @NonNull final String... perms) {
        if (hasPermissions(activity, perms)) {
            notifyAlreadyHasPermissions(activity, requestCode, perms);
            return;
        }

        if (shouldShowRationale(activity, perms)) {
            showRationaleDialogActivity(
                    activity,
                    rationale,
                    positiveButton,
                    negativeButton,
                    requestCode,
                    perms);
        } else {
            ActivityCompat.requestPermissions(activity, perms, requestCode);
        }
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param fragment    {@link Fragment} requesting permissions. Should override {@link
     *                    Fragment#onRequestPermissionsResult(int, String[], int[])}.
     * @param rationale   the rationale
     * @param requestCode the request code
     * @param perms       the perms
     * @see #requestPermissions(Fragment, String, int, int, int, String...)
     */
    public static void requestPermissions(@NonNull final Fragment fragment,
                                          @NonNull final String rationale,
                                          final int requestCode,
                                          @NonNull final String... perms) {
        requestPermissions(
                fragment,
                rationale,
                android.R.string.ok,
                android.R.string.cancel,
                requestCode,
                perms);
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param fragment       {@link Fragment} requesting permissions. Should override {@link
     *                       Fragment#onRequestPermissionsResult(int, String[], int[])}.
     * @param rationale      the rationale
     * @param positiveButton the positive button
     * @param negativeButton the negative button
     * @param requestCode    the request code
     * @param perms          the perms
     * @see #requestPermissions(Activity, String, int, int, int, String...)
     */
    public static void requestPermissions(@NonNull final Fragment fragment,
                                          @NonNull final String rationale,
                                          @StringRes final int positiveButton,
                                          @StringRes final int negativeButton,
                                          final int requestCode,
                                          @NonNull final String... perms) {
        if (hasPermissions(fragment.getContext(), perms)) {
            notifyAlreadyHasPermissions(fragment, requestCode, perms);
            return;
        }

        if (shouldShowRationale(fragment, perms)) {
            showRationaleDialogFragment(fragment,
                    rationale,
                    positiveButton,
                    negativeButton,
                    requestCode,
                    perms);
        } else {
            fragment.requestPermissions(perms, requestCode);
        }
    }


    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context the calling context.
     * @param perms   one ore more permissions, such as {@link Manifest.permission#CAMERA}.
     * @return true if all permissions are already granted, false if at least one permission is not yet granted.
     * @see Manifest.permission
     */
    public static boolean hasPermissions(@NonNull final Context context, @NonNull final String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //Log.w(TAG, "hasPermissions: API version < M, returning true by default");
            // DANGER ZONE!!! Changing this will break the library.
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = ContextCompat.checkSelfPermission(context, perm)
                    == PackageManager.PERMISSION_GRANTED;
            if (!hasPerm) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param object Activity or Fragment
     * @param perms  list of permissions
     * @return true if the user has previously denied any of the {@code perms} and we should show a
     * rationale, false otherwise.
     */
    private static boolean shouldShowRationale(@NonNull final Object object,
                                               @NonNull final String[] perms) {
        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale =
                    shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
        }
        return shouldShowRationale;
    }

    /**
     * @param object Activity or Fragment
     * @param perm   permission
     * @return boolean
     */
    private static boolean shouldShowRequestPermissionRationale(@NonNull final Object object,
                                                                @NonNull final String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            throw new IllegalArgumentException("Object was neither an Activity nor a Fragment.");
        }
    }


    /**
     * @param object      Activity or Fragment
     * @param requestCode the request code
     * @param perms       list of permissions
     */
    private static void notifyAlreadyHasPermissions(final Object object,
                                                    final int requestCode,
                                                    @NonNull final String[] perms) {
        int[] grantResults = new int[perms.length];
        for (int i = 0; i < perms.length; i++) {
            grantResults[i] = PackageManager.PERMISSION_GRANTED;
        }
        onRequestPermissionsResult(true, object, requestCode, perms, grantResults, object);
    }

    /**
     * Handle the result of a permission request, should be called from the calling {@link
     * Activity}**'s {@link ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int,
     * String[], int[])}** method.
     * <p>
     * If any permissions were granted or denied, the {@code object} will receive the appropriate
     * callbacks through {@link PermissionCallbacks} and methods annotated with {@link
     * AfterPermissionGranted}** will be run if appropriate.
     *
     * @param object       activity or fragment
     * @param requestCode  requestCode argument to permission result callback.
     * @param permissions  permissions argument to permission result callback.
     * @param grantResults grantResults argument to permission result callback.
     * @param receivers    an array of objects that have a method annotated with {@link
     *                     AfterPermissionGranted} or implement {@link PermissionCallbacks}.
     */
    public static void onRequestPermissionsResult(@NonNull final Object object,
                                                  final int requestCode,
                                                  @NonNull final String[] permissions,
                                                  @NonNull final int[] grantResults,
                                                  @NonNull final Object... receivers) {
        onRequestPermissionsResult(false, object, requestCode, permissions, grantResults, receivers);
    }

    /**
     * Handle the result of a permission request, should be called from the calling {@link
     * Activity}**'s {@link ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int,
     * String[], int[])}** method.
     * <p>
     * If any permissions were granted or denied, the {@code object} will receive the appropriate
     * callbacks through {@link PermissionCallbacks} and methods annotated with {@link
     * AfterPermissionGranted}** will be run if appropriate.
     *
     * @param isPermissionPermanentlyDeniedCheck true if check permanent denied check
     * @param activityOrFragment                 object  activity or fragment
     * @param requestCode                        requestCode argument to permission result callback.
     * @param permissions                        permissions argument to permission result callback.
     * @param grantResults                       grantResults argument to permission result callback.
     * @param receivers                          an array of objects that have a method annotated with {@link
     *                                           AfterPermissionGranted} or implement {@link PermissionCallbacks}.
     */
    public static void onRequestPermissionsResult(final boolean isPermissionPermanentlyDeniedCheck,
                                                  @NonNull final Object activityOrFragment,
                                                  final int requestCode,
                                                  @NonNull final String[] permissions,
                                                  @NonNull final int[] grantResults,
                                                  @NonNull final Object... receivers) {
        // Make a collection of granted and denied permissions from the request.
        final List<String> granted = new ArrayList<>();
        final List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }


        if (isPermissionPermanentlyDeniedCheck
                && !denied.isEmpty()) {
            final Context context;
            if (activityOrFragment instanceof Fragment) {
                context = ((Fragment) activityOrFragment).getContext();
            } else {
                context = (Activity) activityOrFragment;
            }


            new CustomAlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.rationale_ask_again))
                    .setTitle(context.getString(R.string.title_settings_dialog))
                    .setCancelable(false)
                    .setPositiveButton(context.getString(android.R.string.ok), new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                        @Override
                        public void onClick() {
                            // Create app settings intent
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);

                            // Start for result
                            if (activityOrFragment instanceof Activity) {
                                ((Activity) activityOrFragment).startActivityForResult(intent, REQ_CODE_DEFAULT_SETTINGS);
                            } else {
                                ((Fragment) activityOrFragment).startActivityForResult(intent, REQ_CODE_DEFAULT_SETTINGS);
                            }
                        }
                    })
                    .setNegativeButton(context.getString(android.R.string.cancel), new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                        @Override
                        public void onClick() {
                            notifyCallbacks(requestCode, granted, denied, receivers);
                        }
                    })
                    .show();
        } else {
            notifyCallbacks(requestCode, granted, denied, receivers);
        }
    }

    /**
     * @param requestCode requestCode argument to permission result callback.
     * @param granted     list of granted permission
     * @param denied      list of denied permission
     * @param receivers   an array of objects that have a method annotated with {@link
     *                    AfterPermissionGranted} or implement {@link PermissionCallbacks}.
     */
    private static void notifyCallbacks(final int requestCode,
                                        final List<String> granted,
                                        final List<String> denied,
                                        @NonNull final Object... receivers) {
        // iterate through all receivers
        for (Object object : receivers) {
            // Report granted permissions, if any.
            if (!granted.isEmpty()) {
                if (object instanceof PermissionCallbacks) {
                    ((PermissionCallbacks) object).onPermissionsGranted(requestCode, granted);
                }
            }

            // Report denied permissions, if any.
            if (!denied.isEmpty()) {
                if (object instanceof PermissionCallbacks) {
                    ((PermissionCallbacks) object).onPermissionsDenied(requestCode, denied);
                }
            }

            // If 100% successful, call annotated methods
            if (!granted.isEmpty() && denied.isEmpty()) {
                runAnnotatedMethods(object, requestCode);
            }
        }
    }


    /**
     * @param activity       {@link Activity} requesting permissions. Should implement {@link
     *                       ActivityCompat.OnRequestPermissionsResultCallback} or override {@link
     *                       FragmentActivity#onRequestPermissionsResult(int, String[], int[])} if
     *                       it extends from {@link FragmentActivity}.
     * @param rationale      a message explaining why the application needs this set of permissions,
     *                       will be displayed if the user rejects the request the first time.
     * @param positiveButton custom text for positive button
     * @param negativeButton custom text for negative button
     * @param requestCode    request code to track this request, must be < 256.
     * @param perms          a set of permissions to be requested.
     */
    private static void showRationaleDialogActivity(@NonNull final Activity activity,
                                                    @NonNull final String rationale,
                                                    @StringRes final int positiveButton,
                                                    @StringRes final int negativeButton,
                                                    final int requestCode,
                                                    @NonNull final String... perms) {
        new CustomAlertDialog.Builder(activity)
                .setMessage(rationale)
                .setCancelable(false)
                .setPositiveButton(positiveButton, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                        ActivityCompat.requestPermissions(activity, perms, requestCode);
                    }
                })
                .setNegativeButton(negativeButton, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                        //notifyPermissionDenied();
                    }
                })
                .show();

    }

    /**
     * @param fragment       {@link Fragment} requesting permissions. Should override {@link
     *                       Fragment#onRequestPermissionsResult(int, String[], int[])}.
     * @param rationale      the rationale
     * @param positiveButton the positive button
     * @param negativeButton the negative button
     * @param requestCode    the request code
     * @param perms          the perms
     */
    private static void showRationaleDialogFragment(@NonNull final Fragment fragment,
                                                    @NonNull final String rationale,
                                                    @StringRes final int positiveButton,
                                                    @StringRes final int negativeButton,
                                                    final int requestCode,
                                                    @NonNull final String... perms) {
        new CustomAlertDialog.Builder(fragment.getContext())
                .setMessage(rationale)
                .setCancelable(false)
                .setPositiveButton(positiveButton, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                        fragment.requestPermissions(perms, requestCode);
                    }
                })
                .setNegativeButton(negativeButton, new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                        //notifyPermissionDenied();
                    }
                })
                .show();

    }

    /**
     * Check if at least one permission in the list of denied permissions has been permanently
     * denied (user clicked "Never ask again").
     *
     * @param activity          {@link Activity} requesting permissions.
     * @param deniedPermissions list of denied permissions, usually from {@link
     *                          PermissionCallbacks#onPermissionsDenied(int, List)}
     * @return {@code true} if at least one permission in the list was permanently denied.
     */
    private static boolean somePermissionPermanentlyDenied(@NonNull final Activity activity,
                                                           @NonNull final List<String> deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (permissionPermanentlyDenied(activity, deniedPermission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if at least one permission in the list of denied permissions has been permanently
     * denied (user clicked "Never ask again").
     *
     * @param fragment          {@link Fragment} requesting permissions.
     * @param deniedPermissions list of denied permissions, usually from {@link
     *                          PermissionCallbacks#onPermissionsDenied(int, List)}
     * @return {@code true} if at least one permission in the list was permanently denied.
     */
    private static boolean somePermissionPermanentlyDenied(@NonNull final Fragment fragment,
                                                           @NonNull final List<String> deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (permissionPermanentlyDenied(fragment, deniedPermission)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Check if a permission has been permanently denied (user clicked "Never ask again").
     *
     * @param activity         {@link Activity} requesting permissions.
     * @param deniedPermission denied permission.
     * @return {@code true} if the permissions has been permanently denied.
     */
    private static boolean permissionPermanentlyDenied(@NonNull final Activity activity,
                                                       @NonNull final String deniedPermission) {
        return !shouldShowRequestPermissionRationale(activity, deniedPermission);
    }

    /**
     * Check if a permission has been permanently denied (user clicked "Never ask again").
     *
     * @param fragment         {@link Fragment} requesting permissions.
     * @param deniedPermission denied permission.
     * @return {@code true} if the permissions has been permanently denied.
     */
    private static boolean permissionPermanentlyDenied(@NonNull final Fragment fragment,
                                                       @NonNull final String deniedPermission) {
        return !shouldShowRequestPermissionRationale(fragment, deniedPermission);
    }


    /**
     * @param object      Activity or Fragment
     * @param requestCode request code
     */
    private static void runAnnotatedMethods(@NonNull final Object object, final int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)) {
            clazz = clazz.getSuperclass();
        }

        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(AfterPermissionGranted.class)) {
                    // Check for annotated methods with matching request code.
                    AfterPermissionGranted ann = method.getAnnotation(AfterPermissionGranted.class);
                    if (ann.value() == requestCode) {
                        // Method must be void so that we can invoke it
                        if (method.getParameterTypes().length > 0) {
                            throw new RuntimeException(
                                    "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
                        }

                        try {
                            // Make method accessible if private
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            method.invoke(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * @param object Activity or Fragment
     * @return boolean
     */
    private static boolean isUsingAndroidAnnotations(@NonNull final Object object) {
        if (!object.getClass().getSimpleName().endsWith("_")) {
            return false;
        }
        try {
            Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
            return clazz.isInstance(object);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


}
