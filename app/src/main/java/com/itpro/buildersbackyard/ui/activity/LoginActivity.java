package com.itpro.buildersbackyard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.fb_package.FbLogin;
import com.itpro.buildersbackyard.fb_package.FbResult;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.fragment.ProductList;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Query;


public class LoginActivity extends BaseActivity implements View.OnClickListener, AppRequest, FbResult,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private EditText mUsername, mPassword;
    private Button mBtnLogin, mBtnFacebook, mBtnSignUp;
    private TextView mForgetPassword;
    private FbLogin FbLogin1;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private String is_Scocial, socialemail, Social_ID;
    private TwitterLoginButton loginButton;
    private int flag;
    private boolean loginRegistration = Boolean.FALSE;
    public GoogleApiClient mGoogleApiClient;
    private final String TAG = "BuildersBackyard";
    public LocationRequest mLocationRequest;
    public Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setCustomFont();
        inflateViews();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 1000) // 10 seconds, in milliseconds
                .setFastestInterval(2000);
    }

    private void setCustomFont() {
        TextView txt = (TextView) findViewById(R.id.splash_label);
        Typeface font = Typeface.createFromAsset(getAssets(), "customfont.ttf");
        txt.setTypeface(font);
    }

    private void inflateViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mBtnFacebook = (Button) findViewById(R.id.fb_login);
        mBtnSignUp = (Button) findViewById(R.id.register);
        mBtnLogin = (Button) findViewById(R.id.login);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        progress = (CircleProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mBtnFacebook.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setText("Connect with Twitter");
        loginButton.setTextSize(14);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                flag = 1;
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                System.out.println("result in case of twitter>>>>>"+result.data);
                //User user=result.data;
                getTwitterData(session);
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
               /* String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";

                LoginWithTwitter(session.getUserId() + "", session.getUserName() + "", "", "", "");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
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
        buildGoogleApiClient();
        System.out.println("in start-----------------");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(LoginActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        System.out.println("in on stop-------------");
        super.onStop();
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
                            Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
            getvalues();
          /*  new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {

                }
            }.start();*/


        }


    }

    void getvalues() {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        if (currentLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            pref.edit().putString(getString(R.string.current_latitude), String.valueOf(currentLocation.getLatitude())).apply();
            pref.edit().putString(getString(R.string.current_longitude), String.valueOf(currentLocation.getLongitude())).apply();
            Log.d("latnogjgfjgfusagf", "dfgdsfgsdgdshgdfshf" + currentLocation.getLatitude());
          //  ((BaseActivity) LoginActivity.this).addFragment(new ProductList());
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
        // ((BaseActivity) HomeActivity.this).addFragment(new ProductList());

    }

    void validation() {
        String email, password;

        email = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        if (email.equals("") || password.equals("")) {
            if (email.equals("")) {
                mUsername.setError("Enter The Email");
            }
            if (password.equals("")) {
                mPassword.setError("Enter The Password");
            }
        } else {
            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("is_social", "0");
            mParams.put("email", email);
            mParams.put("password", password);

            mParams.put("social_id", "");
            System.out.println("api called");
            if (NetworkUtil.getConnectivityStatusString(this)) {
                ApiRequests.getInstance().userLogin(this, this, mParams);
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fb_login:
                if (NetworkUtil.getConnectivityStatusString(this)) {
                    FbLogin1 = new FbLogin(LoginActivity.this);
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.register:
                Intent gotoSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(gotoSignUp);
                finish();
                break;

            case R.id.login:
                validation();
                break;
            case R.id.forget_password:

                Intent gotoForgetPwd = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(gotoForgetPwd);
                break;
        }
    }

    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        System.out.println("current time " + System.currentTimeMillis());
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(),
                    "Press Back Button Again In Order To Exit",
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
            // progress.set
            Gson gson = new Gson();
            ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
            if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                finish();
                Intent gotoLogin = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                if (flag == 1) {
                    pref.edit().putString(getString(R.string.social_login), "true").apply();
                }
                pref.edit().putString(getString(R.string.userId), responseBean.getDetails().getUserId()).apply();
                pref.edit().putString(getString(R.string.loginStatus), "login").apply();
                System.out.println("userrrrr id---" + responseBean.getDetails().getUserId());
                //Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                this.finish();

            } else {
                System.out.println("else login called");
                Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FbLogin1 != null)
            FbLogin1.OnActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        try {
            Log.d("failed", "failed");
            progress.setVisibility(View.GONE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onloginerror(FacebookException e) {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onloginsuccess(LoginResult result) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
//                Log.d("responce", jsonObject.toString());
                try {
                    flag = 1;
                    System.out.println("email>>>"+jsonObject.toString());
                    socialemail = jsonObject.optString("email");
                    Profile profile = Profile.getCurrentProfile();
                    Social_ID = profile.getId();
                    LoginWithFacebook(profile.getId(), profile.getFirstName(), profile.getLastName(), socialemail, profile.getProfilePictureUri(100, 100).toString());
                    //System.out.println("picture"+profile.getProfilePictureUri(100, 100).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        graphRequest.executeAsync();
    }

    @Override
    public void onlogincancel() {
        Toast.makeText(this, "error in login with fb", Toast.LENGTH_SHORT).show();
    }

    public void LoginWithTwitter(String id, String firstName, String lastName, String email, String profile_pic) {
        is_Scocial = "1";
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("is_social", is_Scocial);
        mParams.put("email", email);
        mParams.put("first_name", firstName);
        mParams.put("last_name", lastName);
        mParams.put("social_id", id);
        mParams.put("profile_pic", profile_pic);
        ApiRequests.getInstance().userLogin(this, this, mParams);
        loginRegistration = Boolean.TRUE;
    }

    public void LoginWithFacebook(String id, String firstName, String lastName, String email, String profile_pic) {
        is_Scocial = "1";
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("is_social", is_Scocial);
        mParams.put("email", email);
        mParams.put("first_name", firstName);
        mParams.put("last_name", lastName);
        mParams.put("social_id", id);
        mParams.put("profile_pic", profile_pic);
        ApiRequests.getInstance().userLogin(this, this, mParams);
        loginRegistration = Boolean.TRUE;
    }


    public void getTwitterData(final TwitterSession session) {
        MyTwitterApiClient tapiclient = new MyTwitterApiClient(session);
        tapiclient.getCustomService().show(session.getUserId(),
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

//                        TwitterAuthToken authToken = session.getAuthToken();
//                        String token = authToken.token;
//                        String secret = authToken.secret;
//                        name.setText(result.data.name);
//                        location.setText(result.data.location);
//                        new ImageDownloader(profileImageView)
//                                .execute(result.data.profileImageUrl);
//
//                        Log.d("Name", name);
//                        Log.d("city", location);


                        String msg = "@" + result.data.name + " logged in! (#" + session.getUserId() + ")";

                        LoginWithTwitter(result.data.idStr + "", result.data.name + "","","", result.data.profileImageUrl);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

                    public void failure(TwitterException exception) {
                        // Do something on failure
                        exception.printStackTrace();
                    }
                });


    }


    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public CustomService getCustomService() {
            return getService(CustomService.class);
        }


    }

    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") long id, Callback<User> cb);
    }
}