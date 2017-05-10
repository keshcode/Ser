package com.skeleton.locationLib.locationservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.skeleton.locationLib.db.DatabaseHandler;
import com.skeleton.locationLib.locationroute.Route;
import com.skeleton.locationLib.locationroute.RouteUtil;


/**
 * Created by cl-macmini-33 on 19/12/16.
 */
public final class LocationTracking {
    private static LocationTracking ourInstance = new LocationTracking();
    private BroadcastReceiver receiver;
    private RouteUpdateListener routeUpdateListener;
    //LatLng lastLntLng;
    //LatLng newLngLng;
    ////  Route route = new Route();
    // Activity context;


    /**
     *
     */
    private LocationTracking() {
        setUpLocationReceiver();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
//   setUpLocationReceiver();
    public static LocationTracking getInstance() {

        return ourInstance;
    }



    /**
     * Start tracking.
     *
     * @param context the context
     */
    public void startTracking(final Activity context) {

        if (!BackgroundLocationService.isRunning(context)) {
            context.startService(new Intent(context, BackgroundLocationService.class));
        }

    }


    /**
     * Start tracking with location logger.
     *
     * @param context the context
     */
    public void startTrackingWithLocationLogger(final Context context) {

        if (!BackgroundLocationService.isRunning(context)) {
            context.startService(new Intent(context, BackgroundLocationService.class));
        }
    }

    /**
     * Is tracking started boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public Boolean isTrackingStarted(final Context context) {
        return BackgroundLocationService.isRunning(context);
    }


    /**
     * Gets route.
     *
     * @param context the context
     * @return the route
     */
    public Route getRoute(final Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
//        Route route = new Route();
//        route.setRoutePoints(databaseHandler.getAllLocationData());
        return databaseHandler.getBookingRouteData();

    }

    /**
     * Stop tracking.
     *
     * @param context the context
     */
    public void stopTracking(final Context context) {

        if (BackgroundLocationService.isRunning(context)) {

            DatabaseHandler databaseHandler = new DatabaseHandler(context);

            if (getRoute(context).getRoutePoints().size() == 0) {
                routeUpdateListener.onRouteCompleted(null);
            } else if (routeUpdateListener != null) {
                routeUpdateListener.onRouteCompleted(getRoute(context));
            }

            databaseHandler.clearLocationTable();
            context.stopService(new Intent(context, BackgroundLocationService.class));

        }

    }

    /**
     * Sets listener.
     *
     * @param context              the context
     * @param mRouteUpdateListener the route update listener
     */
    public void setListener(final Context context, final RouteUpdateListener mRouteUpdateListener) {
        this.routeUpdateListener = mRouteUpdateListener;
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                new IntentFilter(BackgroundLocationService.COPA_RESULT)
        );
    }

    /**
     * Gets existing route update.
     *
     * @param context the context
     */
    public void getExistingRouteUpdate(final Activity context) {
        if (getRoute(context).getHasStarted() && !getRoute(context).getHasEnded()) {
            startTracking(context);
            if (getRoute(context).getRoutePoints().size() > 0) {
                routeUpdateListener.onExistingRoute(getRoute(context));
            } else {
                routeUpdateListener.onExistingRoute(null);
            }
        }
    }

    /**
     * Remove listener.
     *
     * @param context the context
     */
    public void removeListener(final Activity context) {
        routeUpdateListener = null;
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }


    /**
     *
     */
    private void setUpLocationReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {

                Log.v("location received = ", intent.toString());

                if (routeUpdateListener != null) {
                    Route route = getRoute(context);
                    if (route.getRoutePoints().size() > 0) {
                        routeUpdateListener.onRouteLocations(
                                route.getStartingLatLng() == null ? null : route.getStartingLatLng(),
                                route.getLastLatLng(),
                                route.getCurrentLatLng(),
                                RouteUtil.getBearing(route.getLastLatLng(), route.getCurrentLatLng())
                        );
                    }
                }
            }
        };
    }

}
