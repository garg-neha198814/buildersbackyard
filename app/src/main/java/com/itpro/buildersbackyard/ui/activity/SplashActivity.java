package com.itpro.buildersbackyard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.utils.Constatnts;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3sNUNhMQm1T0JRBierlUBXXGQ";
    private static final String TWITTER_SECRET = " qZZCNHGHj4HWqUxw7m9IjB3KLsBJ4d6RBJ8g6hwiXp0wQIiG99";
    private double mLat, mLong;
    private View view;
    private String latitude, longitude, mUserId, mMilesSelected = "";
    boolean backpress = false;
    private SharedPreferences pref;
    private SharedPreferences latlong;
    private String status;
    private GoogleApiClient mGoogleApiClient;
    private final String TAG = "BuildersBackyard";
    private LocationRequest mLocationRequest;
    Location mLastLocation;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 1000) // 10 seconds, in milliseconds
                .setFastestInterval(1000);

        setCustomFont();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.itpro.buildersbackyard",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        status = pref.getString(getString(R.string.loginStatus), "0");


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        //  mGoogleApiClient.connect();
        buildGoogleApiClient();
        System.out.println("in start-----------------");
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        System.out.println("in on stop-------------");
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    private void setCustomFont() {
        TextView txt = (TextView) findViewById(R.id.splash_label);
        Typeface font = Typeface.createFromAsset(getAssets(), "customfont.ttf");
        txt.setTypeface(font);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        backpress = true;
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            System.exit(0);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        } else {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);


            if (currentLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                 pref.edit().putString(getString(R.string.current_latitude), String.valueOf(currentLocation.getLatitude())).apply();
                  pref.edit().putString(getString(R.string.current_longitude), String.valueOf(currentLocation.getLongitude())).apply();
                Log.d("latnogjgfjgfusagf", "dfgdsfgsdgdshgdfshf" + currentLocation.getLatitude());
                Thread th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {

                            Thread.sleep(Constatnts.splash_Timeout);
                            if (!backpress) {

                                if (status.equalsIgnoreCase("login")) {
                                    Intent gotoAdds = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(gotoAdds);
                                    finish();
                                } else {
                                    Intent gotoLogin = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(gotoLogin);
                                    finish();
                                }
                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend-------------------------");
        System.out.println("in on connection suspend" + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
        System.out.println("in on connection failed ---------------------------------" + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Location received: " + location.toString());
         pref.edit().putString(getString(R.string.current_latitude), String.valueOf(location.getLatitude())).apply();
         pref.edit().putString(getString(R.string.current_longitude), String.valueOf(location.getLongitude())).apply();
        System.out.println("in location changed------------------");
        System.out.println("latttttttttttttttttttttttttttt-" + String.valueOf(location.getLatitude()));
        System.out.println("longggggggggggggggggggggggggggggg-" + String.valueOf(location.getLongitude()));
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    Thread.sleep(Constatnts.splash_Timeout);
                    if (!backpress) {

                        if (status.equalsIgnoreCase("login")) {
                            Intent gotoAdds = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(gotoAdds);
                            finish();
                        } else {
                            Intent gotoLogin = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(gotoLogin);
                            finish();
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        th.start();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(SplashActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}