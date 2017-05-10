package com.skeleton.locationLib.location;

/**
 * Location Config Class
 */
public class LocationConfig {
    //Interval to update the location
    private long timeInterval;
    private boolean isRepeat = false;
    private boolean allowMockLocations = false;

    /**
     * Default Constructor
     */
    public LocationConfig() {

    }

    /**
     * @param time to Update location every
     * @return Config
     */
    public LocationConfig setTimeInterval(final long time) {
        timeInterval = time;
        return this;
    }

    /**
     * @param isRepeated repeat the location or not
     * @return Config
     */
    public LocationConfig setRepeated(final boolean isRepeated) {
        isRepeat = isRepeated;
        return this;
    }

    /**
     * @param isMockAllow true of false
     * @return Config
     */
    public LocationConfig allowMockLocations(final boolean isMockAllow) {
        allowMockLocations = isMockAllow;
        return this;
    }

    /**
     * @return time interval to get location
     */
    public long getTimeInterval() {
        return timeInterval;
    }

    /**
     * @return is repeat or not
     */
    public boolean isRepeat() {
        return isRepeat;
    }

    /**
     * @return mock allow or not
     */
    public boolean isAllowMockLocations() {
        return allowMockLocations;
    }
}