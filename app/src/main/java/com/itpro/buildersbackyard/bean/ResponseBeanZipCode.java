package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rakshit on 22/12/15.
 */
public class ResponseBeanZipCode {

    public ArrayList<ZipCodeResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<ZipCodeResults> results) {
        this.results = results;
    }

    @SerializedName("results")
    @Expose
    ArrayList<ZipCodeResults> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    String status;



}
