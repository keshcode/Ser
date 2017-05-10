package com.skeleton.locationLib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skeleton.locationLib.locationroute.Route;
import com.skeleton.locationLib.locationroute.RoutePoint;
import com.skeleton.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Database handler.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "locationDB";

    // Contacts table name
    private static final String TABLE_LOCATION = "driverLocations";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String TIME_STAMP = "timestamp";
    private static final String ACCURACY = "accuracy";

    /**
     * Instantiates a new Database handler.
     *
     * @param context the context
     */
    public DatabaseHandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(final SQLiteDatabase db) {

        String createDriverBookingLoc = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LAT + " VARCHAR," + LNG + " VARCHAR," + TIME_STAMP + " VARCHAR," + ACCURACY
                + " VARCHAR" + ")";

        db.execSQL(createDriverBookingLoc);

    }

    //Upgrading database
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        // Create tables again
        onCreate(db);
    }


    /**
     * Add driver new Location.
     *
     * @param routePoint the route point
     * @return the long
     */
    public long addLocation(final RoutePoint routePoint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LAT, routePoint.getLatitude());
        values.put(LNG, routePoint.getLongitude());
        values.put(TIME_STAMP, routePoint.getTimestamp());
        values.put(ACCURACY, routePoint.getAccuracy());

        // Inserting Row
        long index = db.insert(TABLE_LOCATION, null, values);

        // Closing database connection
        db.close();

        return index;
    }

    /**
     * Gets all route points.
     *
     * @return the all location data
     */
    public List<RoutePoint> getAllLocationData() {
        List<RoutePoint> driverLocList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RoutePoint driverDBData = new RoutePoint(Double.valueOf(cursor.getString(1)), Double.valueOf(cursor.getString(1)),
                        Long.valueOf(cursor.getString(3)), Float.valueOf(cursor.getString(4)));

                // Adding Location to list
                driverLocList.add(driverDBData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return driverLocList;
    }

    /**
     * Gets total loc count.
     * Getting contacts Count
     *
     * @return the total loc count
     */
    public int getTotalLocCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    /**
     * Gets booking route data.
     *
     * @return the booking route data
     */
// Getting All Route Points
    public Route getBookingRouteData() {
        Route route = new Route();
        SQLiteDatabase db = this.getReadableDatabase();

        List<RoutePoint> driverLocList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.v("CURSOR COUNT", " >> " + cursor.getCount());
        Log.v("CURSOR COLUMN COUNT", " >> " + cursor.getColumnCount());

        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    RoutePoint driverDBData = new RoutePoint(Double.valueOf(cursor.getString(1)), Double.valueOf(cursor.getString(1)),
                            Long.valueOf(cursor.getString(3)), Float.valueOf(cursor.getString(4)));
                    // Adding Location to list
                    driverLocList.add(driverDBData);
                } while (cursor.moveToNext());
            }
        }
        route.setRoutePoints(driverLocList);
        // return contact list
        return route;
    }

    /**
     * Clear booking table.
     */
// Deleting single contact
    public void clearLocationTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATION, null, null);
        db.close();
    }

}