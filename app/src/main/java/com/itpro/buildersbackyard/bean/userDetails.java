package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 2/12/15.
 */
public class userDetails {

    @SerializedName("first_name")
    @Expose
    private String FirstName;
    @SerializedName("last_name")
    @Expose
    private String LastName;
    @SerializedName("phone_number")
    @Expose
    private String PhoneNumber;
    @SerializedName("is_social")
    @Expose
    private Integer IsSocial;


    @SerializedName("id")
    @Expose
    private String UserId;
    @SerializedName("email")
    @Expose
    private String Email;
    @SerializedName("profile_pic")
    @Expose
    private String ProfilePic;
    @SerializedName("socail_id")
    @Expose
    private String SocailId;
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Integer getIsSocial() {
        return IsSocial;
    }

    public void setIsSocial(Integer isSocial) {
        IsSocial = isSocial;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getSocailId() {
        return SocailId;
    }

    public void setSocailId(String socailId) {
        SocailId = socailId;
    }

}
