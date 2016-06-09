package com.itpro.buildersbackyard.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.fragment.EditProfile;
import com.itpro.buildersbackyard.ui.fragment.FindAJob;
import com.itpro.buildersbackyard.ui.fragment.ForHire;
import com.itpro.buildersbackyard.ui.fragment.MyAccount;
import com.itpro.buildersbackyard.ui.fragment.MyAdsSwipe;
import com.itpro.buildersbackyard.ui.fragment.MyJobs;
import com.itpro.buildersbackyard.ui.fragment.Notification;
import com.itpro.buildersbackyard.ui.fragment.PostAd;
import com.itpro.buildersbackyard.ui.fragment.ProductList;
import com.itpro.buildersbackyard.ui.fragment.Search;
import com.itpro.buildersbackyard.ui.fragment.Settings;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements View.OnClickListener, AppRequest {

    private Toolbar mToolbar;
    private ImageView search, chat;
    private SharedPreferences pref;
    private String titleToolbar, mLatitude, mLongitude, mUserId;
    private TextView title;
    private Button mBtnProducts, mBtnForHire, mBtnMyAds, mBtnMyJobs;
    private Map<String, String> mParams;
    private LinearLayout mTabsLayout, mMyHomeTabs;
    private CircleProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inflateToolbar();
        inflateViews();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getFragmentManager().addOnBackStackChangedListener(getListener());
    }

    private void inflateViews() {
        // Calling the RecyclerView



        mBtnForHire = (Button) findViewById(R.id.forhire_btn);
        mBtnProducts = (Button) findViewById(R.id.products_btn);
        mTabsLayout = (LinearLayout) findViewById(R.id.tabs);
        mTabsLayout.setVisibility(View.VISIBLE);
        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        ParsePush.subscribeInBackground("BuildersBackyard" + mUserId, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("subcribed");
                } else {
                    e.printStackTrace();
                }
            }
        });

        Intent i = getIntent();
        if (i != null) {
            int notification_type = i.getIntExtra("notification_type", 0);
            if (notification_type == 0) {
                ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
            } else if (notification_type == 1) {
                ((BaseActivity) HomeActivity.this).addFragment(new FindAJob());
            } else if (notification_type == 2) {
                ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
            } else if (notification_type == 3) {
                Intent gotomessage = new Intent(HomeActivity.this, MessagesHome.class);
                startActivity(gotomessage);
            }
        }
        mBtnForHire.setOnClickListener(this);
        mBtnProducts.setOnClickListener(this);
        mBtnProducts.setBackgroundResource(R.drawable.btn_selected);
        mBtnForHire.setBackgroundResource(R.drawable.btn_unselected);
        mMyHomeTabs = (LinearLayout) findViewById(R.id.myhome_tabs);
        mMyHomeTabs.setVisibility(View.GONE);
        mBtnMyAds = (Button) findViewById(R.id.myads);
        mBtnMyJobs = (Button) findViewById(R.id.myjobs);
        mBtnMyAds.setOnClickListener(this);
        mBtnMyJobs.setOnClickListener(this);
        mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
        mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
        mBtnProducts.setTag("1");
        mBtnForHire.setTag("0");
        progress = (CircleProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
    }

    /* @Override
     protected void onResume() {
         super.onResume();
         Intent i=getIntent();
         if(i!=null) {
             int notification_type = i.getIntExtra("notification_type", 0);
             if (notification_type == 0) {
                 ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
             } else if (notification_type == 1) {
                 ((BaseActivity) HomeActivity.this).addFragment(new FindAJob());
             } else if (notification_type == 2) {
                 ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
             } else if (notification_type == 3) {
                 Intent gotomessage = new Intent(HomeActivity.this, MessagesHome.class);
                 startActivity(gotomessage);
             }
         }
     }*/


    public void inflateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        search = (ImageView) mToolbar.findViewById(R.id.search);
        chat = (ImageView) mToolbar.findViewById(R.id.message);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new Search());
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new ChatFragment());
                Intent gotomessage = new Intent(HomeActivity.this, MessagesHome.class);
                startActivity(gotomessage);

            }
        });
        title = (TextView) mToolbar.findViewById(R.id.toolbar_search_txt);
        title.setText("BUILDERS BACKYARD");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.my_account:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new MyAccount());
                mTabsLayout.setVisibility(View.GONE);
                break;
            case R.id.messages:

                Intent got = new Intent(HomeActivity.this, MessagesHome.class);
                startActivity(got);
                break;
            case R.id.notification:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new Notification());
                mTabsLayout.setVisibility(View.GONE);
                break;
            case R.id.settings:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new Settings());
                mTabsLayout.setVisibility(View.GONE);
                break;
            case R.id.logout:

                final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("Are you sure you want to Logout ?");
                alertbox.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ParsePush.unsubscribeInBackground("BuildersBackyard" + mUserId, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    System.out.println("subcribed");
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                        pref.edit().clear().apply();
                        FragmentManager fm = getFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        String social_login = pref.getString(getString(R.string.social_login), "0");
                        if (social_login.equalsIgnoreCase("true")) {
                            LoginManager.getInstance().logOut();
                        }
                        Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
                        gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(gotoLogin);
                        finish();
                    }
                });
                alertbox.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alert_box_show = alertbox.create();
                alert_box_show.show();


                break;
            case R.id.home:
                finish();
                Intent gothome = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(gothome);
                break;
            case R.id.edit_profile:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new EditProfile());
                mTabsLayout.setVisibility(View.GONE);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.products_btn:
                ((BaseActivity) HomeActivity.this).addFragment(new ProductList());
                mTabsLayout.setVisibility(View.VISIBLE);
                mBtnProducts.setBackgroundResource(R.drawable.btn_selected);
                mBtnForHire.setBackgroundResource(R.drawable.btn_unselected);
                title.setText("Products");
                search.setVisibility(View.VISIBLE);
                mBtnProducts.setTag("1");
                mBtnForHire.setTag("0");
                break;
            case R.id.forhire_btn:
                ((BaseActivity) HomeActivity.this).addFragment(new ForHire());
                search.setVisibility(View.GONE);
                mTabsLayout.setVisibility(View.VISIBLE);
                mBtnProducts.setBackgroundResource(R.drawable.btn_unselected);
                mBtnForHire.setBackgroundResource(R.drawable.btn_selected);
                title.setText("For Hire");
                mBtnProducts.setTag("0");
                mBtnForHire.setTag("1");
                break;
            case R.id.myads:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new MyAdsSwipe());
                mTabsLayout.setVisibility(View.GONE);
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
                break;
            case R.id.myjobs:
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new MyJobs());
                mTabsLayout.setVisibility(View.GONE);
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                break;
        }

    }

    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;


    @Override
    public void onBackPressed() {
        System.out.println("back pressed() called of productlist-----------------------------------");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            String fragmentTag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
            Fragment currentFragment = getFragmentManager()
                    .findFragmentByTag(fragmentTag);
            titleToolbar = currentFragment.getTag();

            if (titleToolbar.equals("PostAd2")) {

                deleteProductImages();
            }
            if (titleToolbar.equals("PostAd")) {

                deleteProductImages();
            }

        } else {

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

    }

    private android.app.FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();

                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {

                        title.setText("BUILDERS BACKYARD");
                        String tag = (String) mBtnForHire.getTag();

                        if (tag.equals("1")) {
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.VISIBLE);
                        } else {
                            search.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.VISIBLE);
                        }

                    } else {
                        String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        Fragment currentFragment = getFragmentManager()
                                .findFragmentByTag(fragmentTag);
                        titleToolbar = currentFragment.getTag();
                        if (titleToolbar.equalsIgnoreCase("ProductList")) {
                            title.setText("BUILDERS BACKYARD");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("ProductActivity")) {
                            title.setText(pref.getString(getString(R.string.productTitle), "0"));
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("PostAd")) {
                            title.setText("POST YOUR AD");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);

                        } else if (titleToolbar.equalsIgnoreCase("PostAd2")) {
                            title.setText("POST YOUR AD");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("ChatFragment")) {
                            title.setText("CHAT");
                            chat.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("MessagesListProduct")) {
                            title.setText("MESSAGES");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("Notification")) {
                            title.setText("NOTIFICATIONS");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("Search")) {
                            title.setText("SEARCH");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("MyAccount")) {
                            title.setText("MYACCOUNT");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("FindAjob")) {
                            title.setText("Find A Job");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("GetHired")) {
                            title.setText("Get Hired");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("Postjob")) {
                            title.setText("Post A Job");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("ForHire")) {
                            title.setText("For Hire");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("GetHired_Detail")) {
                            title.setText(pref.getString(getString(R.string.tradeTitle), "0"));
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("Settings")) {
                            title.setText("SETTINGS");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("ChangePassword")) {
                            title.setText("CHANGE PASSWORD");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("ContactUs")) {
                            title.setText("CONTACT US");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("DeleteAccount")) {
                            title.setText("DELETE ACCOUNT");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("MyAds")) {
                            title.setText("MY ADS");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("MyJobs")) {
                            title.setText("MY JOBS");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                            mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                            mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                        } else if (titleToolbar.equalsIgnoreCase("MyHome")) {
                            title.setText("MY HOME");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                            mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                            mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                        } else if (titleToolbar.equalsIgnoreCase("SearchedList")) {
                            title.setText("PRODUCTS");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("JobDetailsFragment")) {
                            title.setText(pref.getString(getString(R.string.jobTitle), "0"));
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("TermsOfService")) {
                            title.setText("Terms Of Service");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("SearchedList")) {
                            title.setText("SearchedList");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("FindACandidate")) {
                            title.setText("Find A Candidate");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("EditProfile")) {
                            title.setText("Profile");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mTabsLayout.setVisibility(View.GONE
                            );
                        }
                        currentFragment.onResume();
                    }
                }
            }
        };

        return result;
    }


    void deleteProductImages() {
        String randomNumber = pref.getString(getString(R.string.randomNumber), "0");
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("random_string", randomNumber);
        if (NetworkUtil.getConnectivityStatusString(this)) {
            ApiRequests.getInstance().deleteUploadImages(this, this, mParams);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
            Gson gson = new Gson();
            ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
            if (responseBean.getStatus().equals("1")) {
                ((BaseActivity) HomeActivity.this).addFragmentwithactivity(new PostAd());

            } else {

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
            pref.edit().putString(getString(R.string.randomNumber), "0").apply();
        }catch(Exception e){
            e.printStackTrace();
        }
    }






}