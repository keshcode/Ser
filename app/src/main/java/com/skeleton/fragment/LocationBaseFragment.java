package com.skeleton.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.skeleton.locationLib.location.LocationConfig;
import com.skeleton.locationLib.location.LocationFetcher;
import com.skeleton.util.dialog.CustomAlertDialog;

/**
 * Created by cl-macbookair-71 on 4/21/17.
 */

public abstract class LocationBaseFragment extends BaseFragment implements LocationFetcher.Listener {
    private LocationFetcher locationFetcher;
    private boolean isLocationUpdateAlways = true;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("isRepeated >> ", "" + setLocationConfig().isRepeat());
        locationFetcher = new LocationFetcher.Builder(getActivity()).setCallback(this)
                .repeat(setLocationConfig().isRepeat())
                .allowMockLocations(setLocationConfig().isAllowMockLocations())
                .setInterval(setLocationConfig().getTimeInterval())
                .build();
    }

    @Override
    public void onNeedLocationPermission() {
        locationFetcher.requestLocationPermission();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(final View.OnClickListener fromView,
                                                        final DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Permission Permanent Decline")
                .setPositiveButton("Ok", fromDialog)
                .show();
    }

    @Override
    public void onNeedLocationSettingsChange() {

        new CustomAlertDialog.Builder(getActivity()).setMessage("You need to switch on the Location")
                .setPositiveButton("Ok", new CustomAlertDialog.CustomDialogInterface.OnClickListener() {
                    @Override
                    public void onClick() {
                        locationFetcher.changeLocationSettings();
                    }
                }).show();
    }

    @Override
    public void onFallBackToSystemSettings(final View.OnClickListener fromView, final DialogInterface.OnClickListener fromDialog) {

        new AlertDialog.Builder(getActivity())
                .setMessage("Switch the Location On")
                .setPositiveButton("Ok", fromDialog)
                .show();
    }

    /**
     * @param location the current user location
     */
    @Override
    public void onNewLocationAvailable(final Location location) {
        onLocationUpdate(location);
        if (!setLocationConfig().isRepeat()) {
            isLocationUpdateAlways = false;
            locationFetcher.stop();
        }
    }


    /**
     * @param fromView   OnClickListener to use with a view (e.g. a button), jumps to the development settings
     * @param fromDialog OnClickListener to use with a dialog, jumps to the development settings
     */
    @Override
    public void onMockLocationsDetected(final View.OnClickListener fromView, final DialogInterface.OnClickListener fromDialog) {
        Toast.makeText(getActivity(), "Mock Location Detected", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param type    the type of error that occurred
     * @param message a plain-text message with optional details
     */
    @Override
    public void onError(final LocationFetcher.ErrorType type, final String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (locationFetcher != null && isLocationUpdateAlways) {
            locationFetcher.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationFetcher != null) {
            locationFetcher.stop();
        }
    }


    /**
     * @param requestCode  Request Code
     * @param permissions  Permission Required
     * @param grantResults Permission results
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        locationFetcher.onPermissionsUpdated(requestCode, grantResults);
    }

    /**
     * @param requestCode Request Code
     * @param resultCode  Result Code
     * @param data        Intent Data
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        locationFetcher.onActivityResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param location User current location
     */
    public abstract void onLocationUpdate(final Location location);

    /**
     * @return true or false to check location is repeated ot not
     */
    public abstract LocationConfig setLocationConfig();

}
