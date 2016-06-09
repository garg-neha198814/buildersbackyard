package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rakshit on 22/12/15.
 */
public class ZipCodeResults {


    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @SerializedName("formatted_address")
    @Expose
    String formatted_address;

    @SerializedName("place_id")
    @Expose
    String place_id;

    @SerializedName("geometry")
    @Expose
    Geometry geometry;


}
