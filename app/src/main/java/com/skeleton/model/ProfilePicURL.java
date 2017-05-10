package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * {@link ProfilePicURL}
 */
public class ProfilePicURL implements Serializable {

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("original")
    @Expose
    private String original;

    /**
     * thumbnail
     *
     * @return : thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * thumbnail
     *
     * @param thumbnail : thumbnail
     */
    public void setThumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * original
     *
     * @return : original
     */
    public String getOriginal() {
        return original;
    }

    /**
     * original
     *
     * @param original : original
     */
    public void setOriginal(final String original) {
        this.original = original;
    }

}