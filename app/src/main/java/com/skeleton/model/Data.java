package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * {@link Data}
 */
public class Data implements Serializable {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("userDetails")
    @Expose
    private UserDetails userDetails;

    /**
     * accessToken
     *
     * @return : accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * setter method
     *
     * @param accessToken : accessToken
     */
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * userDetails
     *
     * @return : userDetails
     */
    public UserDetails getUserDetails() {
        return userDetails;
    }

    /**
     * Setter method
     *
     * @param userDetails : userDetails
     */
    public void setUserDetails(final UserDetails userDetails) {
        this.userDetails = userDetails;
    }

}