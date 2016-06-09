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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Bean class for server response
 * Created by Ripansharma on 20/04/15.
 */


public class ResponseBean {




    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("user_detail")
    @Expose
    private userDetails details;

    @SerializedName("data")
    @Expose
    private Data data;

    public ArrayList<Find_Job_Details> getMy_job() {
        return my_job;
    }

    public void setMy_job(ArrayList<Find_Job_Details> my_job) {
        this.my_job = my_job;
    }
    @SerializedName("my_job")
    @Expose
    private ArrayList<Find_Job_Details> my_job;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @SerializedName("view_profie")
    @Expose
    private Profile profile;



    private Product_Details product_details;
    private ArrayList<MyAdds_Data> current_add;

    public ArrayList<Notification_List> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<Notification_List> notification) {
        this.notification = notification;
    }
    @SerializedName("notification")
    @Expose
    private ArrayList<Notification_List> notification;


    private ArrayList<Product_Data> product_data;



    public ArrayList<Searched_Data> getSearch_product() {
        return search_product;
    }

    public void setSearch_product(ArrayList<Searched_Data> search_product) {
        this.search_product = search_product;
    }

    private ArrayList<Searched_Data> search_product;

    public ArrayList<Find_Job_Details> getFindJob() {
        return findJob;
    }

    public void setFindJob(ArrayList<Find_Job_Details> findJob) {
        this.findJob = findJob;
    }
    @SerializedName("user_jobs")
    @Expose
    private ArrayList<Find_Job_Details> findJob;

    public ArrayList<Find_Candidate> getFind_candidate() {
        return find_candidate;
    }

    public void setFind_candidate(ArrayList<Find_Candidate> find_candidate) {
        this.find_candidate = find_candidate;
    }

    private ArrayList<Find_Candidate> find_candidate;
    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public userDetails getDetails() {
        return details;
    }

    public void setDetails(userDetails details) {
        this.details = details;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ArrayList<MyAdds_Data> getCurrent_add() {
        return current_add;
    }

    public void setCurrent_add(ArrayList<MyAdds_Data> current_add) {
        this.current_add = current_add;
    }

    public Product_Details getProduct_details() {
        return product_details;
    }

    public void setProduct_details(Product_Details product_details) {
        this.product_details = product_details;
    }




    public ArrayList<Product_Data> getProduct_data() {
        return product_data;
    }

    public void setProduct_data(ArrayList<Product_Data> product_data) {
        this.product_data = product_data;
    }
    public ArrayList<MessagesData> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessagesData> messages) {
        this.messages = messages;
    }

    @SerializedName("messages")
    @Expose
    private ArrayList<MessagesData> messages;


    public Conversation_Id getConversationObj() {
        return conversationObj;
    }

    public void setConversationObj(Conversation_Id conversationObj) {
        this.conversationObj = conversationObj;
    }

    @SerializedName("conversation_id")
    @Expose
    private Conversation_Id conversationObj;




    @SerializedName("conversations")
    @Expose
    private ArrayList<ConversationList> conversationsList;
    public ArrayList<ConversationList> getConversationsList() {
        return conversationsList;
    }

    public void setConversationsList(ArrayList<ConversationList> conversationsList) {
        this.conversationsList = conversationsList;
    }

    public ViewHired getViewHired() {
        return viewHired;
    }

    public void setViewHired(ViewHired viewHired) {
        this.viewHired = viewHired;
    }

    @SerializedName("view_hired")
    @Expose
    private ViewHired viewHired;

    public String getGetHiredStatus() {
        return getHiredStatus;
    }

    public void setGetHiredStatus(String getHiredStatus) {
        this.getHiredStatus = getHiredStatus;
    }

    @SerializedName("get_hired")
    @Expose
    private String getHiredStatus;

    public String getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }

    @SerializedName("block")
    @Expose
    private String blockStatus;

}
