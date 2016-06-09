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
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
public abstract class BaseTask<T> extends Request<T> {
	protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    protected static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);


	protected Map<String, String> mBundle;
	
	public Map<String, String> getmBundle() {
		return mBundle;
	}

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    public void setmBundle(Map<String, String> mBundle) {
		this.mBundle = mBundle;
	}
	String tag;
    String subCategory;
    public String getSubCategory() {
        return subCategory;
    }
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
	JSONObject jsonResponse;
	JSONArray jsonArrayResponse;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public JSONObject getJsonResponse() {
		return jsonResponse;
	}
	public void setJsonResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	public void setJsonArrayResponse(JSONArray jsonResponse) {
		this.jsonArrayResponse = jsonResponse;
	}
	public JSONArray getJsonArrayResponse() {
		return jsonArrayResponse;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}

	String response;
	
	static final int MY_SOCKET_TIMEOUT_MS  = 10000;
	static final int DEFAULT_MAX_RETRIES   = 2;
	static final int DEFAULT_BACKOFF_MULT  = 2;
	
	public BaseTask(int method, String url, ErrorListener listener, String requestTag,Map<String, String> mParams) {
		super(method, url, listener);
		setShouldCache(false);
		
		this.tag = requestTag;
		this.setRetryPolicy(new DefaultRetryPolicy(
				MY_SOCKET_TIMEOUT_MS,
				DEFAULT_MAX_RETRIES,
				DEFAULT_BACKOFF_MULT));
	}
	
}
