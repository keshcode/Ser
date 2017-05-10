package com.skeleton.locationLib.locationroute;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by cl-macmini-33 on 19/12/16.
 */
public final class RouteUtil {
    private static final int TIME_MINUTE = 60;
    private static final int TIME_MILI_SECONDS = 1000;

    /**
     * Empty Constructor
     * not called
     */
    private RouteUtil() {
    }


    /**
     * Gets distance.
     *
     * @param fstLatLng    the fst lat lng
     * @param secondLatLng the second lat lng
     * @return the distance
     */
    public static double getDistance(final LatLng fstLatLng, final LatLng secondLatLng) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(fstLatLng.latitude);
        locationA.setLongitude(fstLatLng.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(secondLatLng.latitude);
        locationB.setLongitude(secondLatLng.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;
    }

    /**
     * Gets route distance.
     *
     * @param routePoints list of route points
     * @return Total route distance in meters
     */
    public static Double getRouteDistance(final List<RoutePoint> routePoints) {
        Double distance = 0.0;
        LatLng dummyLastLatLng = null;
        for (RoutePoint routePoint : routePoints) {
            if (dummyLastLatLng == null) {
                dummyLastLatLng = new LatLng(routePoint.getLatitude(), routePoint.getLongitude());
            }

            distance = distance + getDistance(dummyLastLatLng, new LatLng(routePoint.getLatitude(),
                    routePoint.getLongitude()));
            dummyLastLatLng = new LatLng(routePoint.getLatitude(), routePoint.getLongitude());
        }
        return distance;
    }

    /**
     * Gets route time.
     *
     * @param route the route
     * @return time in mins
     */
    public static Long getRouteTime(final Route route) {
        if (!route.getHasStarted()) {
            return 0L;
        }
        if (!route.getHasEnded()) {
            return (System.currentTimeMillis() - route.getRoutePoints().get(0).getTimestamp()) / (TIME_MINUTE * TIME_MILI_SECONDS);
        } else {
            if (route.getRoutePoints().size() > 1) {
                return (route.getRoutePoints().get(route.getRoutePoints().size() - 1)
                        .getTimestamp() - route.getRoutePoints().get(0).getTimestamp()) / (TIME_MINUTE * TIME_MILI_SECONDS);
            } else {
                return 0L;
            }
        }
    }

    /**
     * Gets bearing.
     *
     * @param start the start
     * @param end   the end
     * @return the bearing
     */
    public static double getBearing(final LatLng start, final LatLng end) {
        Location startPoint = new Location("S");
        startPoint.setLatitude(start.latitude);
        startPoint.setLongitude(start.longitude);

        Location endPoint = new Location("E");
        endPoint.setLatitude(end.latitude);
        endPoint.setLongitude(end.longitude);

        double longitude1 = startPoint.getLongitude();
        double latitude1 = Math.toRadians(startPoint.getLatitude());

        double longitude2 = endPoint.getLongitude();
        double latitude2 = Math.toRadians(endPoint.getLatitude());

        double longDiff = Math.toRadians(longitude2 - longitude1);

        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2)
                - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return Math.toDegrees(Math.atan2(y, x));
    }
}
