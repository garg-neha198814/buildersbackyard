package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10/12/15.
 */
public class ViewHired {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String Name;
    @SerializedName("trade")
    @Expose
    private String Trade;
    @SerializedName("certification")
    @Expose
    private String Certification;
    @SerializedName("specialties")
    @Expose
    private String Specialities;
    @SerializedName("location")
    @Expose
    private String Location;
    @SerializedName("availability")
    @Expose
    private String Availability;
    @SerializedName("latitude")
    @Expose
    private String Latitude;
    @SerializedName("longitude")
    @Expose
    private String Longitude;
    @SerializedName("profile_pic")
    @Expose
    private String ProfilePic;

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTrade() {
        return Trade;
    }

    public void setTrade(String trade) {
        Trade = trade;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }

    public String getSpecialities() {
        return Specialities;
    }

    public void setSpecialities(String specialities) {
        Specialities = specialities;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
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

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    @SerializedName("created_at")
    @Expose
    private String CreatedAt;
}
