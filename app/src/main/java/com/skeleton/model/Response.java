
package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * {@link Response}
 */
public class Response implements Serializable {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * status code of user
     *
     * @return : statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Setter method
     *
     * @param statusCode : statusCode
     */
    public void setStatusCode(final Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Message
     *
     * @return : message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method
     *
     * @param message : message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * data
     *
     * @return : data
     */
    public Data getData() {
        return data;
    }

    /**
     * Setter method
     *
     * @param data : data
     */
    public void setData(final Data data) {
        this.data = data;
    }

}