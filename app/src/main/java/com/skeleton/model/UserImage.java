
package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model class {@link UserImage}
 */
public class UserImage implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("original")
    @Expose
    private String original;

    /**
     * Setter id
     *
     * @return : id
     */
    public String getId() {
        return id;
    }

    /**
     * setter
     *
     * @param id : id
     */
    public void setId(final String id) {
        this.id = id;
    }

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