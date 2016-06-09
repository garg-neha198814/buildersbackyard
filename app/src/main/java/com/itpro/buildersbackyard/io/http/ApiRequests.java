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
package com.itpro.buildersbackyard.io.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.UrlConstants;

import java.util.Map;


/**
 * Created by Ripansharma
 */
public class ApiRequests {
    private RequestQueue mRequestQueue;
    private static ApiRequests apiRequests = null;

    private SharedPreferences pref;

    public static ApiRequests getInstance() {
        if (apiRequests == null) {
            apiRequests = new ApiRequests();
            return apiRequests;
        }
        return apiRequests;
    }

    /*
    * Login Api Method
    * */
    public void userLogin(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.LOGIN_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_login = new HttpRequests(Request.Method.POST, url, error, appRequest, "Use_login", mParams);
            User_login.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_login);
            mRequestQueue.add(User_login);
            appRequest.onRequestStarted(User_login);
        }
    }

    /*
    * userRegistration  Api Method
    * */
    public void userRegistration(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.REGISTER_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_register = new HttpRequests(Request.Method.POST, url, error, appRequest, "Use_reg", mParams);
            User_register.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_register);
            mRequestQueue.add(User_register);
            appRequest.onRequestStarted(User_register);
        }
    }

    /*
      * forgotPassword  Api Method
      * */
    public void forgotPassword(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.FORGOT_PWD_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_forgot_pwd = new HttpRequests(Request.Method.POST, url, error, appRequest, "Use_forget_pwd", mParams);
            User_forgot_pwd.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            System.out.println("forgot password called" + url);
            error.setRequestLister(appRequest, User_forgot_pwd);
            mRequestQueue.add(User_forgot_pwd);
            appRequest.onRequestStarted(User_forgot_pwd);
        }
    }

    public void getproductList(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_PRODUCTLIST_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_login = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_productlist", mParams);
            User_login.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_login);
            mRequestQueue.add(User_login);
            appRequest.onRequestStarted(User_login);
        }
    }

    public void getproductlist_item(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_PRODUCTLIST_ITEM__URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_login = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_productlist_item", mParams);
            User_login.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_login);
            mRequestQueue.add(User_login);
            appRequest.onRequestStarted(User_login);
        }

    }

    public void deleteAdd(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.DELETE_PRODUCT;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_login = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_productlist", mParams);
            User_login.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_login);
            mRequestQueue.add(User_login);
            appRequest.onRequestStarted(User_login);
        }

    }

    public void getMyAdds(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_MYADDS;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests User_login = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_productlist", mParams);
            User_login.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Log.e("url", url + "");
            error.setRequestLister(appRequest, User_login);
            mRequestQueue.add(User_login);
            appRequest.onRequestStarted(User_login);
        }

    }


    /*
     * Upload Image  Api Method
     * */
    public void uploadImage(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.UPLOAD_IMAGE_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests upload_image = new HttpRequests(Request.Method.POST, url, error, appRequest, "upload_image", mParams);
            upload_image.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, upload_image);
            mRequestQueue.add(upload_image);
            appRequest.onRequestStarted(upload_image);
        }
    }

    /*
      * Add Post Api Method
      * */
    public void addPost(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.ADD_POST_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests add_post = new HttpRequests(Request.Method.POST, url, error, appRequest, "add_post", mParams);
            add_post.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, add_post);
            mRequestQueue.add(add_post);
            appRequest.onRequestStarted(add_post);
        }
    }

    /*
         *Delete Uploaded Images On back press Api Methhod*/
    public void deleteUploadImages(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.DELETE_UPLOAD_IMAGES_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests deleteImages = new HttpRequests(Request.Method.POST, url, error, appRequest, "deleteImages", mParams);
            deleteImages.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, deleteImages);
            mRequestQueue.add(deleteImages);
            appRequest.onRequestStarted(deleteImages);
        }
    }

    /*
        *Post New Job Api Methhod*/
    public void postNewJob(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.POST_NEW_JOB_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests post_new_job = new HttpRequests(Request.Method.POST, url, error, appRequest, "post_new_job", mParams);
            post_new_job.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, post_new_job);
            mRequestQueue.add(post_new_job);
            appRequest.onRequestStarted(post_new_job);
        }
    }

    /*
       *Get Job Details Api Methhod*/
    public void getJobs(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_JOB_DETAILS_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_job_details = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_job_details", mParams);
            get_job_details.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_job_details);
            mRequestQueue.add(get_job_details);
            appRequest.onRequestStarted(get_job_details);
        }
    }

    public void addpostgethired(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.ADDPOST_GETHIRED;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests add_post_get_hired = new HttpRequests(Request.Method.POST, url, error, appRequest, "add_post_get_hired", mParams);
            add_post_get_hired.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, add_post_get_hired);
            mRequestQueue.add(add_post_get_hired);
            appRequest.onRequestStarted(add_post_get_hired);
        }
    }

    public void getCandidateList(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GETCANDIDATES_LIST;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests deleteImages = new HttpRequests(Request.Method.POST, url, error, appRequest, "deleteImages", mParams);
            deleteImages.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, deleteImages);
            mRequestQueue.add(deleteImages);
            appRequest.onRequestStarted(deleteImages);
        }
    }

    public void searchCandidate(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.SEARCH_CANDIDATE;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests deleteImages = new HttpRequests(Request.Method.POST, url, error, appRequest, "deleteImages", mParams);
            deleteImages.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, deleteImages);
            mRequestQueue.add(deleteImages);
            appRequest.onRequestStarted(deleteImages);
        }
    }

    public void getMyJobs(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_MY_JOBS_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_my_jobs = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_my_jobs", mParams);
            get_my_jobs.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_my_jobs);
            mRequestQueue.add(get_my_jobs);
            appRequest.onRequestStarted(get_my_jobs);
        }
    }

    public void EditProduct(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.EDITPRODUCT;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_my_jobs = new HttpRequests(Request.Method.POST, url, error, appRequest, "edit_product", mParams);
            get_my_jobs.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_my_jobs);
            mRequestQueue.add(get_my_jobs);
            appRequest.onRequestStarted(get_my_jobs);
        }
    }

    public void generateMessage(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GENERATE_MESSAGE;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_my_jobs = new HttpRequests(Request.Method.POST, url, error, appRequest, "generate_message", mParams);
            get_my_jobs.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_my_jobs);
            mRequestQueue.add(get_my_jobs);
            appRequest.onRequestStarted(get_my_jobs);
        }
    }

    public void generateConversationId(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GENERATE_CONVERSATION_ID_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests conversation_id = new HttpRequests(Request.Method.POST, url, error, appRequest, "conversation_id", mParams);
            conversation_id.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, conversation_id);
            mRequestQueue.add(conversation_id);
            appRequest.onRequestStarted(conversation_id);
        }
    }

    public void SendMessage(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.SEND_MESSAGE_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests send_message = new HttpRequests(Request.Method.POST, url, error, appRequest, "send_message", mParams);
            send_message.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, send_message);
            mRequestQueue.add(send_message);
            appRequest.onRequestStarted(send_message);
        }
    }

    public void GetMessages(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_MESSAGES_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_messages = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_messages", mParams);
            get_messages.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_messages);
            mRequestQueue.add(get_messages);
            appRequest.onRequestStarted(get_messages);
        }
    }

    public void GetMessagesList(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_MESSAGES_LIST_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_messages_list = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_messages_list", mParams);
            get_messages_list.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_messages_list);
            mRequestQueue.add(get_messages_list);
            appRequest.onRequestStarted(get_messages_list);
        }
    }

    public void GetNotificationList(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_NOTIFICATION_LIST_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_notification_list = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_notification_list", mParams);
            get_notification_list.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_notification_list);
            mRequestQueue.add(get_notification_list);
            appRequest.onRequestStarted(get_notification_list);
        }
    }

    public void getHiredStatus(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.GET_HIRED_STATUS_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_hired_status = new HttpRequests(Request.Method.POST, url, error, appRequest, "get_hired_status", mParams);
            get_hired_status.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_hired_status);
            mRequestQueue.add(get_hired_status);
            appRequest.onRequestStarted(get_hired_status);
        }
    }
    public void contactUs(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.CONTACT_US_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests contact_us = new HttpRequests(Request.Method.POST, url, error, appRequest, "contact_us", mParams);
            contact_us.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, contact_us);
            mRequestQueue.add(contact_us);
            appRequest.onRequestStarted(contact_us);
        }
    }
    public void deleteAccount(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.DELETE_ACCOUNT_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests delete_Account = new HttpRequests(Request.Method.POST, url, error, appRequest, "delete_Account", mParams);
            delete_Account.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, delete_Account);
            mRequestQueue.add(delete_Account);
            appRequest.onRequestStarted(delete_Account);
        }
    }
    public void changePassword(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.CHANGE_PASSWORD_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests change_password = new HttpRequests(Request.Method.POST, url, error, appRequest, "change_password", mParams);
            change_password.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, change_password);
            mRequestQueue.add(change_password);
            appRequest.onRequestStarted(change_password);
        }
    }

    public void EditProfile(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.EDITPROFILE;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests edit_profile = new HttpRequests(Request.Method.POST, url, error, appRequest, "edit_profile", mParams);
            edit_profile.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, edit_profile);
            mRequestQueue.add(edit_profile);
            appRequest.onRequestStarted(edit_profile);
        }
    }

    public void ViewProfile(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.VIEWPROFILE;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests view_profile = new HttpRequests(Request.Method.POST, url, error, appRequest, "view_profile", mParams);
            view_profile.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, view_profile);
            mRequestQueue.add(view_profile);
            appRequest.onRequestStarted(view_profile);
        }
    }
    public void getLatLngFromZip(Context context, AppRequest appRequest, Map<String, String> mParams, String zipCode) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+zipCode+"&sensor=true";
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests change_password = new HttpRequests(Request.Method.POST, url, error, appRequest, "getLatLngFromZip", mParams);
            change_password.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, change_password);
            mRequestQueue.add(change_password);
            appRequest.onRequestStarted(change_password);
        }
    }

    public void delete_conversation(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = "http://52.22.80.246/builders_backyard/app/delete_conversation";
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests change_password = new HttpRequests(Request.Method.POST, url, error, appRequest, "delete_conversation", mParams);
            change_password.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, change_password);
            mRequestQueue.add(change_password);
            appRequest.onRequestStarted(change_password);
        }
    }


    public void delete_message(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {
            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = "http://52.22.80.246/builders_backyard/app/delete_message";
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests change_password = new HttpRequests(Request.Method.POST, url, error, appRequest, "delete_message", mParams);
            change_password.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, change_password);
            mRequestQueue.add(change_password);
            appRequest.onRequestStarted(change_password);
        }
    }
    public void DeleteNotification(Context context, AppRequest appRequest, Map<String, String> mParams) {
        if (context != null) {

            mRequestQueue = RequestManager.getnstance(context);
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0); // 0 - for private mode
            String url = UrlConstants.DELETE_NOTIFICATION_URL;
            VolleyErrorListener error = new VolleyErrorListener();
            HttpRequests get_messages = new HttpRequests(Request.Method.POST, url, error, appRequest, "delete_notification", mParams);
            get_messages.setHeaders("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            error.setRequestLister(appRequest, get_messages);
            mRequestQueue.add(get_messages);
            appRequest.onRequestStarted(get_messages);
        }
    }
    /**
     * Will be reponsible for listening errors
     * <p/>
     * *
     */
    class VolleyErrorListener implements Response.ErrorListener {
        private AppRequest listener;
        private BaseTask<?> task;

        void setRequestLister(AppRequest listener, BaseTask<?> task) {
            this.listener = listener;
            this.task = task;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("error", error + "");
            listener.onRequestFailed(task);
            //RequestListener.getInstance().onRequestFailed(mContext,error,requestTag);
        }
    }
}