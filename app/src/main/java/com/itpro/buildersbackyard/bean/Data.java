/*
 *
 *  * Copyright (c) 2015, 360ITPRO and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions
 *  * are met:
 *  *
 *  *   - Redistributions of source code must retain the above copyright
 *  *     notice, this list of conditions and the following disclaimer.
 *  *
 *  *   - Redistributions in binary form must reproduce the above copyright
 *  *     notice, this list of conditions and the following disclaimer in the
 *  *     documentation and/or other materials provided with the distribution.
 *  *
 *  */
package com.itpro.buildersbackyard.bean;

/**
 * Created by Ripan on 6/10/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("product_images")
    @Expose
    private ArrayList<Images> product_images;






    @SerializedName("product_id")
    @Expose
    private String product_id;



    @SerializedName("created_at")
    @Expose
    private String created_at;




    @SerializedName("create_at")
    @Expose
    private String create_at;


    @SerializedName("zipcode")
    @Expose
    private String zipcode;



    @SerializedName("category")
    @Expose
    private String category;




    @SerializedName("additional_info")
    @Expose
    private String additional_info;


    @SerializedName("decription")
    @Expose
    private String decription;


    @SerializedName("owner_id")
    @Expose
    private String owner_id;


    @SerializedName("price")
    @Expose
    private String price;



    @SerializedName("name")
    @Expose
    private String name;




    @SerializedName("is_delete")
    @Expose
    private String is_delete;


    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    @SerializedName("message")
    @Expose
    private String message;

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }


    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }


    public ArrayList<Images> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ArrayList<Images> product_images) {
        this.product_images = product_images;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}