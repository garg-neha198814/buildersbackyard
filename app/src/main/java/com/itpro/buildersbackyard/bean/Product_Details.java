package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class Product_Details {

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    @SerializedName("owner_name")
    @Expose
    private String owner_name;

    public String getOwner_pic() {
        return owner_pic;
    }

    public void setOwner_pic(String owner_pic) {
        this.owner_pic = owner_pic;
    }

    @SerializedName("owner_pic")
    @Expose
    private String owner_pic;

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


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("is_delete")
    @Expose
    private String is_delete;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @SerializedName("distance")
    @Expose

    private String distance;

    @SerializedName("product_images")
    @Expose
    private ArrayList<Images> product_images;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("location")
    @Expose
    private String location;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("longitude")
    @Expose
    private String longitude;


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


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}
