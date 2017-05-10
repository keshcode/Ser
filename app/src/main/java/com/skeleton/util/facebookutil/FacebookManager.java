package com.skeleton.util.facebookutil;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.skeleton.R;
import com.skeleton.util.Log;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;


/**
 * Developer: Saurabh Verma
 * Dated: 09/11/16.
 */
public class FacebookManager {
    private static final String TAG = FacebookManager.class.getSimpleName();

    private static final Collection<String> PERMISSIONS_LIST = Arrays.asList("public_profile", "user_friends", "email");

    //fields
    private static final String FIELDS = "fields";
    private static final String FIELDS_LIST = "id,first_name,last_name,name,email,gender,picture";
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String GENDER = "gender";



    private Activity mContext;

    /**
     * Instantiates a new Facebook manager.
     *
     * @param mContext the m context
     */
    public FacebookManager(final Activity mContext) {
        this.mContext = mContext;
    }


    /**
     * Gets fb user details.
     *
     * @param mCallbackManager        the m callback manager
     * @param facebookResponseHandler the facebook response handler
     */
    public void getFbUserDetails(final CallbackManager mCallbackManager, final FacebookResponseHandler facebookResponseHandler) {
        try {
            LoginManager.getInstance().logInWithReadPermissions(mContext, PERMISSIONS_LIST);
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(final JSONObject object,
                                                        final GraphResponse response) {
                                    if (response.getError() != null) {
                                        //error
                                        Log.e(TAG, response.getError().toString());
                                    } else {
                                        try {
                                            String email = "";
                                            if (object.has(EMAIL)) {
                                                email = object.getString(EMAIL);
                                            }


                                            // setFbData(object.getString("id"),
                                            // object.getString("first_name"),
                                            // object.getString("last_name"),
                                            // email,
                                            // "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large",
                                            // object.getString("gender"));
                                            Log.i(TAG, object + "");
                                            SocialUserDetails mSocialUserDetails = new SocialUserDetails(object.getString(ID),
                                                    object.getString(FIRST_NAME),
                                                    object.getString(LAST_NAME),
                                                    email,
                                                    object.getString(GENDER),
                                                    getUserProfileImageUrl(object.getString(ID)));
                                            facebookResponseHandler.onSuccess(mSocialUserDetails);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString(FIELDS, FIELDS_LIST);
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    facebookResponseHandler.onCancel(mContext.getString(R.string.error_fb_login_failed));
                }

                @Override
                public void onError(final FacebookException e) {
                    facebookResponseHandler.onError(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id facbook id
     * @return url of user fb image
     */
    private String getUserProfileImageUrl(final String id) {
        return "https://graph.facebook.com/" + id + "/picture?type=large";
    }
}
