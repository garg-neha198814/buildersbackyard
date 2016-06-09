package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 3/12/15.
 */
public class Find_Job_Details {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("user_id")
    @Expose
    private String UserId;

    @SerializedName("post_title")
    @Expose
    private String PostTitle;

    @SerializedName("position_needed")
    @Expose
    private String PositionNeeded;


    @SerializedName("certification_required")
    @Expose
    private String CertificationRequired;

    @SerializedName("specialties")
    @Expose
    private String Specialities;

    @SerializedName("zipcode")
    @Expose
    private String Zipcode;

    @SerializedName("contact_no")
    @Expose
    private String ContactNo;
    @SerializedName("strt_date")
    @Expose
    private String StartDate;

    @SerializedName("location")
    @Expose
    private String Location;

    @SerializedName("latitude")
    @Expose
    private String Latitude;

    @SerializedName("longitude")
    @Expose
    private String Longitude;

    @SerializedName("created_at")
    @Expose
    private String CreatedAt;
    @SerializedName("end_date")
    @Expose
    private String EndDate;



    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }





    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getPositionNeeded() {
        return PositionNeeded;
    }

    public void setPositionNeeded(String positionNeeded) {
        PositionNeeded = positionNeeded;
    }

    public String getCertificationRequired() {
        return CertificationRequired;
    }

    public void setCertificationRequired(String certificationRequired) {
        CertificationRequired = certificationRequired;
    }

    public String getSpecialities() {
        return Specialities;
    }

    public void setSpecialities(String specialities) {
        Specialities = specialities;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }


}
