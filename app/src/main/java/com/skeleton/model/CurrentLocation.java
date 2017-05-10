
package com.skeleton.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * {@link CurrentLocation}
 */
public class CurrentLocation implements Serializable {

    @SerializedName("coordinates")
    @Expose
    private List<Integer> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * list of coordinates
     *
     * @return : coordinates
     */
    public List<Integer> getCoordinates() {
        return coordinates;
    }

    /**
     * setter method
     *
     * @param coordinates : coordinates
     */
    public void setCoordinates(final List<Integer> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Getter method
     *
     * @return : type
     */
    public String getType() {
        return type;
    }

    /**
     * set type
     *
     * @param type : type
     */
    public void setType(final String type) {
        this.type = type;
    }

}