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
package com.itpro.buildersbackyard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ripan on 17/9/15.
 */

/*
* Constatnt Methods And Variables
* */
public class Constatnts {
    public static final int splash_Timeout = 3000;
    public static final int DATE_PICKER = 1;
    public static final int TIME_PICKER = 2;
    public final static String PREFERENCES_FILE = "BuildersBackyard";
    public final static String PREFERENCES_FILE_Lat_Long = "LatLong";
    /*
    * Check Email Validation
    * */
    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /*
    * Return Address Form LAtitude & Longitude
    * @param1 activity
    * @param2 lat
    * @param3 longi
    *
    * */
    public final static ArrayList<String> getAddress(Activity activity, double lat, double longi) {
        /*
        * Returns First Address Associate with LAtitude and Longitude
        * */
        Geocoder geocoder;
        ArrayList<String> location = new ArrayList<>();
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else null
            location.add(address);
            location.add(city);
            location.add(state);
            location.add(country);
            location.add(knownName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Return the string from shared preference for the app.
     *
     * @param context
     */
    public static SharedPreferences getAppPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(PREFERENCES_FILE,
                Context.MODE_PRIVATE);
    }

    /* *
             * Return Address Form Location Name String
     * @param1 locan name
     * @param2 context
     *
     *
             * */
    public static Address getAddressFromLocation(final String locationAddress,
                                                 final Context context) {
         /*
        * Returns First Latitude And Longitude  Associate with Location
        * */
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Address address = null;
        try {
            List
                    addressList = geocoder.getFromLocationName(locationAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                address = (Address) addressList.get(0);
            }
        } catch (Exception e) {
            Log.e("TAG", "Unable to connect to Geocoder", e);
        }

        return address;
    }


    /*
    * Compare Two Dates
    * */
    public static int compareToDay(Date date1, Date date2) {
        /*
        * Return 1 if true
        * */
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }
    /*
     * Convert String  to Date
    * */

    public static Date StringToDate(String Date) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(Date);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

}

