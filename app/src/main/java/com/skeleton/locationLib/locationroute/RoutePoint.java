package com.skeleton.locationLib.locationroute;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cl-macmini-33 on 19/12/16.
 */
public class RoutePoint {
    private double latitude;
    private double longitude;
    private Long timestamp;
    private Float accuracy;

    /**
     * Instantiates a new Route point.
     *
     * @param latitude      the latitude
     * @param longitude     the longitude
     * @param timestamp     the timestamp
     * @param accuracy      the accuracy
     */
    public RoutePoint(final double latitude,
                      final double longitude, final Long timestamp,
                      final Float accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
    }


    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets accuracy.
     *
     * @return the accuracy
     */
    public Float getAccuracy() {
        return accuracy;
    }


    /**
     * Gets lat lng.
     *
     * @return the lat lng
     */
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

}
