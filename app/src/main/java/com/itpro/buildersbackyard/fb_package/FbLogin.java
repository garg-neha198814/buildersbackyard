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
package com.itpro.buildersbackyard.fb_package;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.itpro.buildersbackyard.fb_package.FbResult;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by Ripansharma
 */
public class FbLogin {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    FbResult fbResult;
    public FbLogin(final Activity context) {
        fbResult = (LoginActivity) context;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LoginActivity", "onSuccess Facebook Login" + loginResult);
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();

//                if(loginResult.getAccessToken().getPermissions().contains("publish_actions"))
                fbResult.onloginsuccess(loginResult);
//                else
//                    LoginManager.getInstance().logInWithPublishPermissions(context, Arrays.asList("publish_actions"));
            }
            @Override
            public void onCancel() {
                fbResult.onlogincancel();
            }
            @Override
            public void onError(FacebookException e) {
                Log.e("LoginActivity", "ERROR: " + e.getMessage());
                fbResult.onloginerror(e);
            }
        });
        facebookLogin(context);
    }
    public void facebookLogin(Activity context) {
        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile", "email"));
    }
    public FbLogin(Activity context, Uri uri) {
       fbResult = (LoginActivity) context;
        File f = new File(getRealPathFromURI(uri, context));
        String path = f.getAbsolutePath();
        String extension = getExtension(path);
        if (extension != null) {
            if (extension.contains("mp4") || extension.contains("3gp")) {
                shareVedio(uri, context);
            } else {
                shareimage(uri, context);
            }
        }
    }
    private String getRealPathFromURI(Uri contentURI, Activity context) {
        String result = "";
        try {
            Cursor cursor = context.getContentResolver().query(
                    contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local
                // file
                // path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "File not found.", Toast.LENGTH_LONG).show();
        }
        return result;
    }
    String getExtension(String fileName) {
        final String emptyExtension = "";
        if (fileName == null) {
            return emptyExtension;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return emptyExtension;
        }
        return fileName.substring(index + 1);
    }
    private void shareVedio(Uri vediouri, Activity context) {
        fbResult = (LoginActivity) context;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(context);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("Success", "SuccessReached");
            }
            @Override
            public void onCancel() {
                Log.d("Cancel", "CancelReached");
            }
            @Override
            public void onError(FacebookException e) {
                Log.d("Error", "ErrorReached");
            }
        });
        Uri videoFileUri = vediouri;
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoFileUri)
                .build();
        ShareVideoContent vediocontent = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();
        shareDialog.show(context, vediocontent);
    }
    private void shareimage(Uri imageuri, Activity context) {
        try {
            fbResult = (LoginActivity) context;
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(context);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.d("Success","SuccessReached");
                }
                @Override
                public void onCancel() {
                    Log.d("Cancel","CancelReached");
                }
                @Override
                public void onError(FacebookException e) {
                    Log.d("Error", "ErrorReached");
                }
            });
            Bitmap sharebitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageuri);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(sharebitmap)
                    .build();
            SharePhotoContent imagecontent = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(imagecontent);
            //   ShareApi.share(imagecontent,fbCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shareLink(Activity context, String title, String link, String Imagelink, String description) {
       fbResult = (LoginActivity) context;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(context);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("Success","SuccessReached");
            }
            @Override
            public void onCancel() {
                Log.d("Cancel","CancelReached");
            }
            @Override
            public void onError(FacebookException e) {
                Log.d("Error","ErrorReached");
            }
        });
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(
                        description)
                .setContentUrl(Uri.parse(link))
                .setImageUrl(Uri.parse(Imagelink))
                .build();
        shareDialog.show(context, linkContent);
    }
    public void OnActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
