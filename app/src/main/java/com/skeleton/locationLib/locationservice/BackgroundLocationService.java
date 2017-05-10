package com.skeleton.locationLib.locationservice;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.skeleton.R;
import com.skeleton.activity.SplashActivity;
import com.skeleton.locationLib.db.DatabaseHandler;
import com.skeleton.locationLib.location.LocationFetcher;
import com.skeleton.locationLib.locationlogger.LocationLoggerService;
import com.skeleton.locationLib.locationroute.RoutePoint;
import com.skeleton.locationLib.locationroute.RouteUtil;
import com.skeleton.util.Log;


/**
 * The type Background location service.
 */
public class BackgroundLocationService extends Service implements LocationFetcher.Listener {

    /**
     * The constant COPA_RESULT.
     */
    public static final String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
    private static final String SMOOTH_LOCATIONS = "Smooth Locations";
    private static final String RUNNING = "Running";


    // in m/s
    private static final float MINIMUM_SPEED_TO_CONSIDER = 0.5f;
    private static final float SPEED_MAX_FOR_MOVING = 22.0f;

    private static final int ACCURACY_FOR_MOVING = 20;
    private static final int ACCURACY_FOR_FLYING = 50;
    private static final int MAXIMUM_ACCURACY_FOR_CONSIDERATION = 50;
    private static final int MINIMUM_DISTANCE_FOR_CONSIDERATION = 20;
    private static final int LOCATION_UPDATE_INTERVAL = 5000;
    private static final int TIME_ONE_MILISECOND = 1000;
    private static final int NOTIFICATION_ID = 1947;
    private int accuracy = ACCURACY_FOR_MOVING;
    private Context context;
    private DatabaseHandler databaseHandler;
    private LocalBroadcastManager broadcaster;
    private LocationFetcher tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        broadcaster = LocalBroadcastManager.getInstance(this);
        databaseHandler = new DatabaseHandler(this);
//        tracker = new LocationFetcher(this, this, LocationFetcher.Accuracy.HIGH, LOCATION_UPDATE_INTERVAL, true, true);
        tracker = new LocationFetcher.Builder(this)
                .setCallback(this)
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .allowMockLocations(false)
                .repeat(true)
                .build();
        runAsForeground();

    }

    /**
     * Send update and update in route.
     *
     * @param location the location
     */
    public void sendUpdateAndUpdateInRoute(final Location location) {
        addRoutePointToRoute(location);
        if (LocationTracking.getInstance().getRoute(context).getLocationUpdatorOn()) {
            LocationLoggerService.updateLocationInService(context, location.getLatitude(),
                    location.getLongitude(), location.getSpeed(), location.getAccuracy(),
                    System.currentTimeMillis());
        }
        sendUpdate();
    }

    /**
     *
     */
    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(SMOOTH_LOCATIONS)
                .setContentText(RUNNING)
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);

    }

    /**
     * Add route point to route.
     *
     * @param location the location
     */
    public void addRoutePointToRoute(final Location location) {
        RoutePoint routePoint = new RoutePoint(location.getLatitude(),
                location.getLongitude(), System.currentTimeMillis(), location.getAccuracy());
        long entryId = databaseHandler.addLocation(routePoint);
        Log.i("Location entry into db", "pos at > " + entryId);
    }


    /**
     * @param intent  The Intent supplied to {@link android.content.Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     */
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!tracker.isConnected()) {
            tracker.start();
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        // Turn off the request flag
        if (tracker.isConnected()) {
            tracker.stop();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (tracker.isConnected()) {
            tracker.stop();
        }

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + TIME_ONE_MILISECOND,
                restartServicePendingIntent);

    }

    /**
     * Send update.
     */
    public void sendUpdate() {
        Intent intent = new Intent(COPA_RESULT);
        broadcaster.sendBroadcast(intent);
    }


    /**
     * Is running boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isRunning(final Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundLocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onNeedLocationPermission() {

    }


    @Override
    public void onLocationPermissionPermanentlyDeclined(final View.OnClickListener fromView,
                                                        final DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(final View.OnClickListener fromView, final DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNewLocationAvailable(final Location location) {
        if (location.getAccuracy() > MAXIMUM_ACCURACY_FOR_CONSIDERATION) {
            return;
        }

        // if it is first point then add it as first point of route and return
        if (LocationTracking.getInstance().getRoute(context).getRoutePoints().size() == 0) {
            // First Location
            sendUpdateAndUpdateInRoute(location);
            return;
        }
        // if speed is less then required minimum speed
        if (location.getSpeed() < MINIMUM_SPEED_TO_CONSIDER) {
            return;
        } else if (location.getSpeed() < SPEED_MAX_FOR_MOVING) {
            accuracy = ACCURACY_FOR_MOVING;
        } else {
            accuracy = ACCURACY_FOR_FLYING;
        }
        if (location.getAccuracy() > accuracy) {
            return;
        }
        if (RouteUtil.getDistance(LocationTracking.getInstance().getRoute(context).getLastLatLng(),
                new LatLng(location.getLatitude(), location.getLongitude())) < MINIMUM_DISTANCE_FOR_CONSIDERATION) {
            return;
        }
        sendUpdateAndUpdateInRoute(location);
    }

    @Override
    public void onMockLocationsDetected(final View.OnClickListener fromView,
                                        final DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(final LocationFetcher.ErrorType type, final String message) {

    }

}