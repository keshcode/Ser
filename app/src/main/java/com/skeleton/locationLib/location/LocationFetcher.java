package com.skeleton.locationLib.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * A helper class that monitors the available location info on behalf of a requesting activity or application.
 */
public class LocationFetcher
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * Delivers relevant events required to obtain (valid) location info.
     */
    public interface Listener {
        /**
         * Called when the user needs to grant the app location permission at run time.
         * This is only necessary on newer Android systems (API level >= 23).
         * If you want to show some explanation up front, do that, then call {@link #requestLocationPermission()}.
         * Alternatively, you can call, which will request the
         * location permission right away and invoke only if the user declines.
         * Both methods will bring up the system permission dialog.
         */
        void onNeedLocationPermission();


        /**
         * Called when the user has declined the location permission at least twice or has declined once and checked
         * "Don't ask again" (which will cause the system to permanently decline it).
         * You can show some sort of message that explains that the user will need to go to the app settings
         * to enable the permission. You may use the preconfigured OnClickListeners to send the user to the app
         * settings page.
         *
         * @param fromView   OnClickListener to use with a view (e.g. a button), jumps to the app settings
         * @param fromDialog OnClickListener to use with a dialog, jumps to the app settings
         */
        void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog);

        /**
         * Called when a change of the location provider settings is necessary.
         * You can optionally show some informative dialog and then request the settings change with
         * {@link #changeLocationSettings()}.
         */
        void onNeedLocationSettingsChange();

        /**
         * In certain cases where the user has switched off location providers, changing the location settings from
         * within the app may not work. The LocationFetcher will attempt to detect these cases and offer a redirect to
         * the system location settings, where the user may manually enable on location providers before returning to
         * the app.
         * You can prompt the user with an appropriate message (in a view or a dialog) and use one of the provided
         * OnClickListeners to jump to the settings.
         *
         * @param fromView   OnClickListener to use with a view (e.g. a button), jumps to the location settings
         * @param fromDialog OnClickListener to use with a dialog, jumps to the location settings
         */
        void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog);

        /**
         * Called when a new and valid location is available.
         * If you chose to reject mock locations, this method will only be called when a real location is available.
         *
         * @param location the current user location
         */
        void onNewLocationAvailable(Location location);

        /**
         * Called when the presence of mock locations was detected and {@link #allowMockLocations} is {@code false}.
         * You can use this callback to scold the user or do whatever. The user can usually disable mock locations by
         * either switching off a running mock location app (on newer Android systems) or by disabling mock location
         * apps altogether. The latter can be done in the phone's development settings. You may show an appropriate
         * message and then use one of the provided OnClickListeners to jump to those settings.
         *
         * @param fromView   OnClickListener to use with a view (e.g. a button), jumps to the development settings
         * @param fromDialog OnClickListener to use with a dialog, jumps to the development settings
         */
        void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog);

        /**
         * Called when an error has occurred.
         *
         * @param type    the type of error that occurred
         * @param message a plain-text message with optional details
         */
        void onError(ErrorType type, String message);
    }

    /**
     * Possible values for the desired location accuracy.
     */
    public enum Accuracy {
        /**
         * Highest possible accuracy, typically within 30m
         */
        HIGH,
        /**
         * Medium accuracy, typically within a city block / roughly 100m
         */
        MEDIUM,
        /**
         * City-level accuracy, typically within 10km
         */
        LOW,
        /**
         * Variable accuracy, purely dependent on updates requested by other apps
         */
        PASSIVE
    }

    /**
     * The enum Error type.
     */
    public enum ErrorType {
        /**
         * An error with the user's location settings
         */
        SETTINGS,
        /**
         * An error with the retrieval of location info
         */
        RETRIEVAL
    }

    public static final int REQUEST_LOCATION_PERMISSION = 1111;
    private static final String TAG = LocationFetcher.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 0;
    private static final int TIME_INTERVAL = 10000;
    private static final int ONE_MILISECOND = 1000;
    private static final int SDK = 18;
    private static final int TWENTY = 20;
    private static final int NUM_GOOD_READINGS = 1000000;
    private static final int M = 23;
    // Parameters
    private Context context;
    private Activity activity;
    private Listener listener;
    private int priority;
    private long updateInterval;
    private boolean allowMockLocations;
    private boolean isRepeated;
    private boolean logs;
    private boolean quiet;

    // Internal state
    private boolean permissionGranted;
    private boolean locationRequested;
    private boolean locationStatusOk;
    private boolean changeSettings;
    private boolean updatesRequested;
    private Location bestLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Status locationStatus;
    private boolean mockLocationsEnabled;
    private int numTimesPermissionDeclined;

    // Mock location rejection
    private Location lastMockLocation;
    private int numGoodReadings;

    /**
     *
     */
    public static class Builder {
        //Context of the activity
        private Context context;

        //Interval to update the location
        private long timeInterval = ONE_MILISECOND;
        private boolean isRepeat = false;
        private boolean allowMockLocations = false;
        private Listener listener;
        //Location Fetcher
        private LocationFetcher locationFetcher;

        /**
         * @param context Context of the activity
         */
        public Builder(final Context context) {
            this.context = context;
        }

        /**
         * @param mTimeInterval Interval to update the location
         * @return Builder
         */
        public Builder setInterval(final long mTimeInterval) {
            this.timeInterval = mTimeInterval;
            return this;
        }


        /**
         * @param mListener Listener where send the updates
         * @return Builder
         */
        public Builder setCallback(final Listener mListener) {
            this.listener = mListener;
            return this;
        }

        /**
         * @param isLocRepeat true or false to repeat location
         * @return return builder
         */
        public Builder repeat(final boolean isLocRepeat) {
            this.isRepeat = isLocRepeat;
            return this;
        }

        /**
         *
         * @param isMockAllow true or false
         * @return Builder
         */
        public Builder allowMockLocations(final boolean isMockAllow) {
            this.allowMockLocations = isMockAllow;
            return this;
        }

        /**
         * @return Builder
         */
        public LocationFetcher build() {
            locationFetcher = new LocationFetcher(context,
                    listener,
                    Accuracy.HIGH,
                    timeInterval,
                    allowMockLocations,
                    isRepeat);
            return locationFetcher;
        }

        /**
         *
         */
        public void disconnect() {
            if (locationFetcher != null) {
                locationFetcher.stop();
            }
        }
    }

    private DialogInterface.OnClickListener onGoToLocationSettingsFromDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            if (activity != null) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    private View.OnClickListener onGoToLocationSettingsFromView = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (activity != null) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    private DialogInterface.OnClickListener onGoToDevSettingsFromDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            if (activity != null) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    private View.OnClickListener onGoToDevSettingsFromView = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (activity != null) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    private DialogInterface.OnClickListener onGoToAppSettingsFromDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            if (activity != null) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    private View.OnClickListener onGoToAppSettingsFromView = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (activity != null) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            } else if (!quiet) {
                Log.e(TAG, "Need to launch an intent, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
        }
    };

    /**
     * The On location settings received.
     */
    private ResultCallback<LocationSettingsResult> onLocationSettingsReceived = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(@NonNull final LocationSettingsResult result) {
            locationRequested = true;
            locationStatus = result.getStatus();
            switch (locationStatus.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    locationStatusOk = true;
                    checkInitialLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    locationStatusOk = false;
                    changeSettings = true;
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    locationStatusOk = false;
                    break;
                default:
                    break;
            }
            acquireLocation();
        }
    };

    /**
     * Constructs a LocationFetcher instance that will listen for valid location updates.
     *
     * @param context            the context of the application or activity that wants to receive location updates
     * @param listener           a listener that will receive location-related events
     * @param accuracy           the desired accuracy of the loation updates
     * @param updateInterval     the interval (in milliseconds) at which the activity can process updates
     * @param allowMockLocations whether or not mock locations are acceptable
     * @param isRepeated         whether you want to receive update location on repeat
     */
    public LocationFetcher(final Context context,
                           final Listener listener,
                           final Accuracy accuracy,
                           final long updateInterval,
                           final boolean allowMockLocations,
                           final boolean isRepeated) {
        this.context = context;
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        this.listener = listener;
        switch (accuracy) {
            case HIGH:
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
                break;
            case MEDIUM:
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                break;
            case LOW:
                priority = LocationRequest.PRIORITY_LOW_POWER;
                break;
            case PASSIVE:
            default:
                priority = LocationRequest.PRIORITY_NO_POWER;
        }
        this.updateInterval = updateInterval;
        this.allowMockLocations = allowMockLocations;
        this.isRepeated = isRepeated;

        // Set up the Google API client
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Makes the LocationFetcher print info log messages.
     *
     * @param log whether or not the LocationFetcher should print logs log messages.
     */
    public void setLogEnable(final boolean log) {
        this.logs = log;
    }

    /**
     * Mutes/unmutes all log output.
     * You may want to mute the LocationFetcher in production.
     *
     * @param quiet whether or not to disable all log output (including errors).
     */
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * Starts the LocationFetcher and makes it subscribe to valid location updates.
     * Call this method when your application or activity becomes awake.
     */
    public void start() {
        checkMockLocations();
        googleApiClient.connect();
    }

    /**
     * Updates the active Activity for which the LocationFetcher manages location updates.
     * When you want the LocationFetcher to start and stop with your overall application, but service different
     * activities, call this method at the end of your {@link Activity#onResume()} implementation.
     *
     * @param mActivity the activity that wants to receive location updates
     * @param mListener a listener that will receive location-related events
     */
    public void register(final Activity mActivity, final Listener mListener) {
        this.activity = mActivity;
        this.listener = mListener;
        checkInitialLocation();
        acquireLocation();
    }

    /**
     * Stops the LocationFetcher and makes it unsubscribe from any location updates.
     * Call this method right before your application or activity goes to sleep.
     */
    public void stop() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        permissionGranted = false;
        locationRequested = false;
        locationStatusOk = false;
        updatesRequested = false;
    }

    /**
     * Clears the active Activity and its listener.
     * Until you register a new activity and listener, the LocationFetcher will silently produce error messages.
     * When you want the LocationFetcher to start and stop with your overall application, but service different
     * activities, call this method at the beginning of your {@link Activity#onPause()} implementation.
     */
    public void unregister() {
        this.activity = null;
        this.listener = null;
    }

    /**
     * In rare cases (e.g. after losing connectivity) you may want to reset the LocationFetcher and have it start
     * from scratch. Use this method to do so.
     */
    public void reset() {
        permissionGranted = false;
        locationRequested = false;
        locationStatusOk = false;
        updatesRequested = false;
        acquireLocation();
    }


    /**
     * Get Last location
     *
     * @return location object
     */
    public Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        if (!isConnected()) {
            return bestLocation;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        bestLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        onLocationChanged(bestLocation);
        return bestLocation;
    }

    /**
     * Is connected boolean.
     *
     * @return boolean boolean
     */
    public boolean isConnected() {
        if (googleApiClient == null) {
            return false;
        } else {
            return googleApiClient.isConnected();
        }
    }

    /**
     * Returns the best valid location currently available.
     * Usually, this will be the last valid location that was received.
     *
     * @return the best valid location
     */
    public Location getBestLocation() {
        return bestLocation;
    }


    /**
     * Brings up a system dialog asking the user to give location permission to the app.
     */
    public void requestLocationPermission() {
        if (activity == null) {
            if (!quiet) {
                Log.e(TAG, "Need location permission, but no activity is registered! "
                        + "Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
            return;
        }
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    /**
     * Call this method at the end of your {@link Activity#onRequestPermissionsResult} implementation to notify the
     * LocationFetcher of an update in permissions.
     *
     * @param requestCode  the request code returned to the activity (simply pass it on)
     * @param grantResults the results array returned to the activity (simply pass it on)
     * @return {@code true} if the location permission was granted, {@code false} otherwise
     */
    public boolean onPermissionsUpdated(final int requestCode, final int[] grantResults) {
        if (requestCode != REQUEST_LOCATION_PERMISSION) {
            return false;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            acquireLocation();
            return true;
        } else {
            numTimesPermissionDeclined++;
            if (!quiet) {
                Log.i(TAG, "Location permission request denied.");
            }
            if (numTimesPermissionDeclined >= 2 && listener != null) {
                listener.onLocationPermissionPermanentlyDeclined(onGoToAppSettingsFromView,
                        onGoToAppSettingsFromDialog);
            }
            return false;
        }
    }

    /**
     * Call this method at the end of your {@link Activity#onActivityResult} implementation to notify the
     * LocationFetcher of a change in location provider settings.
     *
     * @param requestCode the request code returned to the activity (simply pass it on)
     * @param resultCode  the result code returned to the activity (simply pass it on)
     */
    public void onActivityResult(final int requestCode, final int resultCode) {
        if (requestCode != REQUEST_CHECK_SETTINGS) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            changeSettings = false;
            locationStatusOk = true;
        }
        acquireLocation();
    }

    /**
     * Brings up an in-app system dialog that requests a change in location provider settings.
     * The settings change may involve switching on GPS and/or network providers and depends on the accuracy and
     * update interval that was requested when constructing the LocationFetcher.
     * Call this method only from within {@link Listener#onNeedLocationSettingsChange()}.
     */
    public void changeLocationSettings() {
        if (locationStatus == null) {
            return;
        }
        if (activity == null) {
            if (!quiet) {
                Log.e(TAG, "Need to resolve location status issues, but no activity is "
                        + "registered! Specify a valid activity when constructing " + TAG
                        + " or register it explicitly with register().");
            }
            return;
        }
        try {
            locationStatus.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            if (!quiet) {
                Log.e(TAG, "Error while attempting to resolve location status issues:\n"
                        + e.toString());
            }
            if (listener != null) {
                listener.onError(ErrorType.SETTINGS, "Could not resolve location settings issue:\n"
                        + e.getMessage());
            }
            changeSettings = false;
            acquireLocation();
        }
    }

    /**
     *
     */
    private void acquireLocation() {
        if (!permissionGranted) {
            checkLocationPermission();
        }
        if (!permissionGranted) {
            if (numTimesPermissionDeclined >= 2) {
                return;
            }
            if (listener != null) {
                listener.onNeedLocationPermission();
            } else if (!quiet) {
                Log.e(TAG, "Need location permission, but no listener is registered! "
                        + "Specify a valid listener when constructing " + TAG
                        + " or register it explicitly with register().");
            }
            return;
        }
        if (!locationRequested) {
            requestLocation();
            return;
        }
        if (!locationStatusOk) {
            if (changeSettings) {
                if (listener != null) {
                    listener.onNeedLocationSettingsChange();
                } else if (!quiet) {
                    Log.e(TAG, "Need location settings change, but no listener is "
                            + "registered! Specify a valid listener when constructing " + TAG
                            + " or register it explicitly with register().");
                }
            } else {
                checkProviders();
            }
            return;
        }
        if (!updatesRequested) {
            requestLocationUpdates();
            // Check back in a few
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    acquireLocation();
                }
            }, TIME_INTERVAL);
            return;
        }

        if (!checkLocationAvailability()) {
            // Something is wrong - probably the providers are disabled.
            checkProviders();
        }
    }

    /**
     * Check initial location.
     */
    protected void checkInitialLocation() {
        if (!googleApiClient.isConnected() || !permissionGranted || !locationRequested || !locationStatusOk) {
            return;
        }
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            onLocationChanged(location);
        } catch (SecurityException e) {
            if (!quiet) {
                Log.e(TAG, "Error while requesting last location:\n "
                        + e.toString());
            }
            if (listener != null) {
                listener.onError(ErrorType.RETRIEVAL, "Could not retrieve initial location:\n"
                        + e.getMessage());
            }
        }
    }

    /**
     *
     */
    private void checkMockLocations() {
        // Starting with API level >= 18 we can (partially) rely on .isFromMockProvider()
        // (http://developer.android.com/reference/android/location/Location.html#isFromMockProvider%28%29)
        // For API level < 18 we have to check the Settings.Secure flag
        if (Build.VERSION.SDK_INT < SDK
                && !"0".equals(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION))) {
            mockLocationsEnabled = true;
            if (listener != null) {
                listener.onMockLocationsDetected(onGoToDevSettingsFromView, onGoToDevSettingsFromDialog);
            }
        } else {
            mockLocationsEnabled = false;
        }
    }

    /**
     *
     */
    private void checkLocationPermission() {
        permissionGranted = Build.VERSION.SDK_INT < M
                || ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     */
    private void requestLocation() {
        if (!googleApiClient.isConnected() || !permissionGranted) {
            return;
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(priority);
        locationRequest.setInterval(updateInterval);
        locationRequest.setFastestInterval(updateInterval);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                .setResultCallback(onLocationSettingsReceived);
    }

    /**
     * @return boolean
     */
    private boolean checkLocationAvailability() {
        if (!googleApiClient.isConnected() || !permissionGranted) {
            return false;
        }
        try {
            LocationAvailability la = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            return la != null && la.isLocationAvailable();
        } catch (SecurityException e) {
            if (!quiet) {
                Log.e(TAG, "Error while checking location availability:\n " + e.toString());
            }
            if (listener != null) {
                listener.onError(ErrorType.RETRIEVAL, "Could not check location availability:\n"
                        + e.getMessage());
            }
            return false;
        }
    }

    /**
     *
     * @return return value of provide i.e true or false
     */
    private boolean checkProviders() {
        // Do it the old fashioned way
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        if (listener != null) {
            listener.onFallBackToSystemSettings(onGoToLocationSettingsFromView, onGoToLocationSettingsFromDialog);
        } else if (!quiet) {
            Log.e(TAG, "Location providers need to be enabled, but no listener is "
                    + "registered! Specify a valid listener when constructing " + TAG
                    + " or register it explicitly with register().");
        }
        return false;
    }

    /**
     *
     */
    private void requestLocationUpdates() {
        if (!googleApiClient.isConnected() || !permissionGranted || !locationRequested) {
            return;
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            updatesRequested = true;
        } catch (SecurityException e) {
            if (!quiet) {
                Log.e(TAG, "Error while requesting location updates:\n "
                        + e.toString());
            }
            if (listener != null) {
                listener.onError(ErrorType.RETRIEVAL, "Could not request location updates:\n"
                        + e.getMessage());
            }
        }
    }


    /**
     * @param location object of location
     * @return boolean
     */
    private boolean isLocationPlausible(final Location location) {
        if (location == null) {
            return false;
        }

        boolean isMock = mockLocationsEnabled || (Build.VERSION.SDK_INT >= SDK && location.isFromMockProvider());
        if (isMock) {
            lastMockLocation = location;
            numGoodReadings = 0;
        } else {
            // Prevent overflow
            numGoodReadings = Math.min(numGoodReadings + 1, NUM_GOOD_READINGS);
        }

        // We only clear that incident record after a significant show of good behavior
        if (numGoodReadings >= TWENTY) {
            lastMockLocation = null;
        }

        // If there's nothing to compare against, we have to trust it
        if (lastMockLocation == null) {
            return true;
        }

        // And finally, if it's more than 1km away from the last known mock, we'll trust it
        double d = location.distanceTo(lastMockLocation);
        return d > ONE_MILISECOND;
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        acquireLocation();
    }

    @Override
    public void onConnectionSuspended(final int i) {
    }

    @Override
    public void onLocationChanged(final Location location) {
        if (location == null) {
            return;
        }
        boolean plausible = isLocationPlausible(location);
        if (logs && !quiet) {
            Log.i(TAG, location.toString()
                    + (plausible ? " -> plausible" : " -> not plausible"));
        }

        if (!allowMockLocations && !plausible) {
            if (listener != null) {
                listener.onMockLocationsDetected(onGoToDevSettingsFromView,
                        onGoToDevSettingsFromDialog);
            }
            return;
        }

        bestLocation = location;
        if (listener != null) {
            listener.onNewLocationAvailable(location);
            if (!isRepeated) {
                stop();
            }
        } else if (!quiet) {
            Log.w(TAG, "New location is available, but no listener is registered!\n"
                    + "Specify a valid listener when constructing " + TAG
                    + " or register it explicitly with register().");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        if (!quiet) {
            Log.e(TAG, "Error while trying to connect to Google API:\n"
                    + connectionResult.getErrorMessage());
        }
        if (listener != null) {
            listener.onError(ErrorType.RETRIEVAL, "Could not connect to Google API:\n"
                    + connectionResult.getErrorMessage());
        }
    }
}