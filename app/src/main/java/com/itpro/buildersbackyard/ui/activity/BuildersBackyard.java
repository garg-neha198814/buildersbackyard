package com.itpro.buildersbackyard.ui.activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import static com.parse.ParseUser.*;

/**
 * Created by root on 8/12/15.
 */
public class BuildersBackyard extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "JOivJkdVuQ61hk0elp9M0F9V1r8ak5NZVF2Edj8k", "Yf8qZZvKMG2BKl9J8JIV8zmc7P4HXPR11uYJVcd0");

    }
}
