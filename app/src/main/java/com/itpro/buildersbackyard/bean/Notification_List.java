package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/12/15.
 */
public class Notification_List {

    @SerializedName("notification_id")
    @Expose
    private String notification_id;


    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("message_type")
    @Expose
    private String message_type;


    @SerializedName("created_at")
    @Expose
    private String created_at;


    @SerializedName("notification_type")
    @Expose
    private String notification_type;


    @SerializedName("sender_id")
    @Expose
    private String sender_id;


    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    @SerializedName("sender_name")
    @Expose
    private String sender_name;


    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    @SerializedName("alrt")
    @Expose
    private String alert;


}
