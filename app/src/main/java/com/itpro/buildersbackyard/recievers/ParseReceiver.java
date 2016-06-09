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
package com.itpro.buildersbackyard.recievers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;


import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.activity.HomeActivity;
import com.itpro.buildersbackyard.ui.activity.MessagesHome;
import com.itpro.buildersbackyard.ui.activity.SplashActivity;
import com.itpro.buildersbackyard.ui.fragment.ChatFragment;
import com.itpro.buildersbackyard.ui.fragment.MessagesListForHire;
import com.itpro.buildersbackyard.ui.fragment.MessagesListProduct;
import com.itpro.buildersbackyard.utils.Constatnts;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by gopal on 27/10/15.
 */
public class ParseReceiver extends WakefulBroadcastReceiver {

    public static final String ACTION = "com.example.package.MESSAGE";
    public static final String PARSE_EXTRA_DATA_KEY = "com.parse.Data";
    public static final String PARSE_JSON_CHANNEL_KEY = "com.parse.Channel";
    private SharedPreferences pref;
    String mUserId;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String channel = intent.getExtras().getString(PARSE_JSON_CHANNEL_KEY);
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString(
                    PARSE_EXTRA_DATA_KEY));
            System.out.println("RECEIVED:>>>" + json.toString());

            String recieverId = json.getString("receiver_id");
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
            mUserId = pref.getString(context.getString(R.string.userId), "0");
           // if (!recieverId.equalsIgnoreCase(mUserId)) {


            int notification_type = json.getInt("notification_type");
            String alert_message = json.getString("alert");
            String sender_id = json.getString("sender_id");
            switch (notification_type) {
                case 1:
                    showNotification(context, alert_message, notification_type);
                    break;
                case 2:
                    showNotification(context, alert_message, notification_type);
                    break;
                case 3:

                    System.out.println("chattt-------" + ChatFragment.is_open);
                    System.out.println("product-------" + MessagesListProduct.is_open);
                    System.out.println("hire-------" + MessagesListForHire.is_open);
                    Intent i = null;
                    if (MessagesListForHire.is_open) {
                        i = new Intent("msg_hire");
                    } else if (MessagesListProduct.is_open) {
                        i = new Intent("msg_product");
                    } else if (ChatFragment.is_open) {
                        i = new Intent("msg_chat");
                    } else {
                        showNotification(context, alert_message, notification_type);
                    }
                    if (i != null) {
                        i.putExtra("sender_id", sender_id);
                        context.sendBroadcast(i);
                    }

                    break;
                case 4:
                    showNotification(context, alert_message, notification_type);
                    break;

            }


           //  }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Context context, String message, int notification_type) {
        Intent ii = new Intent(context, HomeActivity.class);
        ii.putExtra("notification_type", notification_type);

        ii.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                321, ii,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.bb_logo)
                .setContentTitle("Builders Backyard");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setContentText(message);
        /*if (is_alert) {
            mBuilder.setContentText(msg);
        } else {
            mBuilder.setContentText(eventname + "\n" + sendername + " : " + msg);
        }*/
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(321,
                mBuilder.build());
    }
}
