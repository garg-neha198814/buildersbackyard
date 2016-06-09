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
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;


/**
 * Request Manager Class for Managing the Network Requests
 */
public class RequestManager {

    Bitmap bitmap = null;

    private static RequestManager mRequestManager;

    /**
     * Queue which Manages the Network Requests :-)
     */
    private static RequestQueue mRequestQueue;

    // ImageLoader Instance
    private static ImageLoader mImageLoader;

    private RequestManager() {

    }

    public static RequestManager get(Context context) {

        if (mRequestManager == null)
            mRequestManager = new RequestManager();

        return mRequestManager;
    }

    /**
     * @param context application context
     */
    public static RequestQueue getnstance(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;

    }

    public static ImageLoader getImageLoader() {

        if (mImageLoader == null)
            mImageLoader = new ImageLoader(mRequestQueue, new ImageCache() {

                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(
                        10);

                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);

                }

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });

        return mImageLoader;

    }

//	/**
//	 * 
//	 * @param context
//	 * @param imageView
//	 * @param imgUrl
//	 * @param imagerequest
//	 */
//
//	public Bitmap requestImage(Context context, final ImageView imageView,
//			final String imgUrl) {
//
//		imageView.setTag(imgUrl);
//
//		ImageRequest request = new ImageRequest(imgUrl, new Listener<Bitmap>() {
//
//			@Override
//			public void onResponse(Bitmap bm) {
//
//				imageView.setImageBitmap(RoundedCornerImage
//						.getRoundedCornerBitmap(bm));
//			}
//		},
//
//		0, 0, Config.ARGB_8888, new ErrorListener() {
//
//			public void onErrorResponse(VolleyError error) {
//				error.printStackTrace();
//			}
//
//		});
//		RequestManager.getnstance(context).add(request);
//		return bitmap;
//
//	}

//	public Bitmap requestCircularImage(Context context,
//			final FWCircularImageView imageView, final String imgUrl) {
//
//		imageView.setTag(imgUrl);
//
//		ImageRequest request = new ImageRequest(imgUrl, new Listener<Bitmap>() {
//
//			@Override
//			public void onResponse(Bitmap bm) {
//				Log.e("requestCircularImage", "Setting image");
//				imageView.setImageBitmap(bm);
//			}
//		},
//
//		0, 0, Config.ARGB_8888, new ErrorListener() {
//
//			public void onErrorResponse(VolleyError error) {
//				Log.e("requestCircularImage", "Error " + error.toString());
//				error.printStackTrace();
//			}
//
//		});
//		RequestManager.getnstance(context).add(request);
//		return bitmap;
//
//	}

}
