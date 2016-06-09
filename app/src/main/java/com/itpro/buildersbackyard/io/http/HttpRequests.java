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

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.itpro.buildersbackyard.io.listener.AppRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ripansharma
 */
public class HttpRequests extends BaseTask<JSONObject> {
    private Map<String, String> headers = new HashMap<String, String>();
    private Context mContext;
    private AppRequest appRequest;
    private Map<String, String> mParams = new HashMap();;
    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }
    public void setHeaders(String title, String content) {
        headers.put(title, content);
    }
    public HttpRequests(int method, String url, Response.ErrorListener listener,
                        AppRequest appRequest, String requestTag, Map<String, String> mParams) {
        super(method, url, listener, requestTag,mParams);
        //app_version, api_version, type, password,email, platform,device_id
        this.appRequest = appRequest;
        //entity =new MultipartEntity();

        this.mParams=mParams;
    }
    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }
    @Override
    protected void deliverResponse(JSONObject response) {
        setJsonResponse(response);
       appRequest.onRequestCompleted(this);
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject json = null;
        try {
            json = new JSONObject(new String(response.data));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }
}
