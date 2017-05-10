package com.skeleton.util.facebookutil;

import com.facebook.FacebookException;


/**
 * Developer: Saurabh Verma
 * Dated: 09/11/16.
 */
public interface FacebookResponseHandler {

    /**
     * On success.
     *
     * @param fbUserDetails the fb user details
     */
    void onSuccess(SocialUserDetails fbUserDetails);

    /**
     * On cancel.
     *
     * @param msg the msg
     */
    void onCancel(String msg);

    /**
     * On error.
     *
     * @param e the e
     */
    void onError(FacebookException e);
}
