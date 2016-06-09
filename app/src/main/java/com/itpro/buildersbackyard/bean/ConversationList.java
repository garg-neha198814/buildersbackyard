package com.itpro.buildersbackyard.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/12/15.
 */
public class ConversationList {

    public String getProduct_job_hire_name() {
        return product_job_hire_name;
    }

    public void setProduct_job_hire_name(String product_job_hire_name) {
        this.product_job_hire_name = product_job_hire_name;
    }

    @SerializedName("product_job_hire_name")
    @Expose
    private String product_job_hire_name;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("conversation_id")
    @Expose
    private String conversationId;
    @SerializedName("last_message")
    @Expose
    private String lastMessage;
    @SerializedName("last_message_user")
    @Expose
    private String lastMessageUser;
    @SerializedName("type")
    @Expose
    private String Type;
    @SerializedName("message_type")
    @Expose
    private String messageType;
    @SerializedName("last_message_at")
    @Expose
    private String lastMessageAt;

    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("friend_name")
    @Expose
    private String friendName;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageUser() {
        return lastMessageUser;
    }

    public void setLastMessageUser(String lastMessageUser) {
        this.lastMessageUser = lastMessageUser;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }


}
