package com.skeleton.constant;

/**
 * Developer: Saurabh Verma
 * Dated: 19-02-2017.
 */

public interface AppConstant {
    String DEVICE_TYPE = "ANDROID";

    //Intent Filter
    String NOTIFICATION_RECEIVED = "notification_received";

    //
    int SESSION_EXPIRED = 401;

    //Request code
    int REQ_CODE_DEFAULT_SETTINGS = 16061;
    int REQ_CODE_PLAY_SERVICES_RESOLUTION = 16061;
    int REQ_CODE_SCREEN_OVERLAY = 10101;

    //USER_SIGNUP_KEYS
    String KEY_FRAGMENT_FNAME = "firstName";
    String KEY_FRAGMENT_LNAME = "lastName";
    String KEY_FRAGMENT_DOB = "dob";
    String KEY_FRAGMENT_COUNTRY_CODE = "countryCode";
    String KEY_FRAGMENT_PHONE = "phoneNo";
    String KEY_FRAGMENT_EMAIL = "email";
    String KEY_FRAGMENT_PASSWORD = "password";
    String KEY_FRAGMENT_LANGUAGE = "language";
    String KEY_FRAGMENT_DEVICE_TYPE = "deviceType";
    String KEY_FRAGMENT_DEVICE_TOKEN = "deviceToken";
    String KEY_FRAGMENT_APP_VERSION = "appVersion";
    String KEY_FRAGMENT_GENDER = "gender";
    String KEY_FRAGMENT_ORIENTATION = "orientation";
    String KEY_FRAGMENT_PROFILE_PIC = "profilePic";


    //USER_SIGNUP_VALUES
    String VALUE_FRAGMENT_LANGUAGE = "EN";
    String VALUE_FRAGMENT_DEVICE_TYPE = "ANDROID";
    String VALUE_RAGMENT_DEVICE_TOKEN = "token";
    int VALUE_FRAGMENT_APP_VERSION = 100;
}
