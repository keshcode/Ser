package com.skeleton.locationLib.locationlogger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class LocationLoggerService extends IntentService {

    //parameters
    private static final String LATITUDE = "location.clicklabs.com.smothlocations.extra.LATITUDE";
    private static final String LONGITUDE = "location.clicklabs.com.smothlocations.extra.LONGITUDE";
    private static final String SPEED = "location.clicklabs.com.smothlocations.extra.SPEED";
    private static final String ACCURACY = "location.clicklabs.com.smothlocations.extra.ACCURACY";
    private static final String TIMESTAMP = "location.clicklabs.com.smothlocations.extra.TIMESTAMP";

    /**
     * Instantiates a new Location logger service.
     */
    public LocationLoggerService() {
        super(LocationLoggerService.class.getSimpleName());
    }


    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context   the context
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param speed     the speed
     * @param accuracy  the accuracy
     * @param timestamp the timestamp
     * @see IntentService
     */
    public static void updateLocationInService(final Context context, final Double latitude,
                                               final Double longitude, final float speed,
                                               final float accuracy, final Long timestamp) {
        Intent intent = new Intent(context, LocationLoggerService.class);
        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);
        intent.putExtra(SPEED, speed);
        intent.putExtra(ACCURACY, accuracy);
        intent.putExtra(TIMESTAMP, timestamp);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            final Double latitude = intent.getDoubleExtra(LATITUDE, 0.0);
            final Double longitude = intent.getDoubleExtra(LONGITUDE, 0.0);
            final Float speed = intent.getFloatExtra(SPEED, 0.0f);
            final Float accuracy = intent.getFloatExtra(ACCURACY, 0f);
            final Long timestamp = intent.getLongExtra(TIMESTAMP, 0L);
            updateLocationServerCall(latitude, longitude, speed, accuracy, timestamp);
        }
    }


    /**
     * Update location server call.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param speed     the speed
     * @param accuracy  the accuracy
     * @param timestamp the timestamp
     */
    public void updateLocationServerCall(final Double latitude, final Double longitude,
                                         final float speed, final float accuracy, final Long timestamp) {
        // Add your server call over here
        // Add your server call
        Log.v("Intent Service lat", latitude.toString());
        Log.v("Intent Service lng", longitude.toString());
        Log.v("Intent Service speed", speed + "");
        Log.v("Intent Service accuracy", accuracy + "");
        Log.v("Intent Service times", timestamp + "");
    }
}
