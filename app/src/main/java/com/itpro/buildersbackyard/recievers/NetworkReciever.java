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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itpro.buildersbackyard.utils.NetworkUtil;


/**
 * Created by ripan on 18/9/15.
 */
public class NetworkReciever extends BroadcastReceiver {
    /*
    * Reciever For Network Status
    * */
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean status = NetworkUtil.getConnectivityStatusString(context);
        IsConnected(status);
    }
    public static boolean IsConnected(boolean status) {

        return status;
    }
}
