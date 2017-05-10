package com.skeleton.util.permissionhelper;

/**
 * Developer: Saurabh Verma
 * Dated: 07/03/17.
 */

import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * The interface Permission callbacks.
 */
public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * On permissions granted.
     *
     * @param requestCode the request code
     * @param perms       the perms
     */
    void onPermissionsGranted(int requestCode, List<String> perms);

    /**
     * On permissions denied.
     *
     * @param requestCode the request code
     * @param perms       the perms
     */
    void onPermissionsDenied(int requestCode, List<String> perms);

}