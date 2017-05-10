package com.skeleton.locationLib.locationservice;

import com.google.android.gms.maps.model.LatLng;
import com.skeleton.locationLib.locationroute.Route;


/**
 * Created by cl-macmini-33 on 19/12/16.
 */
public interface RouteUpdateListener {
    /**
     * On route locations.
     *
     * @param routeStartLocation the route start location
     * @param lastLatLng         the last lat lng
     * @param currentLatLng      the current lat lng
     * @param bearing            the bearing
     */
    void onRouteLocations(LatLng routeStartLocation, LatLng lastLatLng, LatLng currentLatLng, double bearing);

    /**
     * On route completed.
     *
     * @param route the route
     */
    void onRouteCompleted(Route route);

    /**
     * On existing route.
     *
     * @param route the route
     */
    void onExistingRoute(Route route);
}
