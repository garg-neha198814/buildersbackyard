package com.itpro.buildersbackyard.ui.fragment;

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

import com.facebook.login.LoginManager;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.HomeActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.ui.activity.MessagesHome;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class MyHome extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView search, chat;
    private SharedPreferences pref;
    private String titleToolbar,mUserId;
    private TextView title;
    private Button mBtnMyAds, mBtnMyJobs;

    private LinearLayout mMyHomeTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myhome);
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

        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        mMyHomeTabs = (LinearLayout) findViewById(R.id.myhome_tabs);
        mMyHomeTabs.setVisibility(View.VISIBLE);
        mBtnMyAds = (Button) findViewById(R.id.myads);
        mBtnMyJobs = (Button) findViewById(R.id.myjobs);
        mBtnMyAds.setOnClickListener(this);
        mBtnMyJobs.setOnClickListener(this);
        mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
        mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
        ((BaseActivity) MyHome.this).addFragment(new MyAdsSwipe());

    }

    public void inflateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        search = (ImageView) mToolbar.findViewById(R.id.search);
        chat = (ImageView) mToolbar.findViewById(R.id.message);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MyHome.this).addFragmentwithactivity(new Search());
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomessage = new Intent(MyHome.this, MessagesHome.class);
                startActivity(gotomessage);

            }
        });
        title = (TextView) mToolbar.findViewById(R.id.toolbar_search_txt);
        title.setText("MY HOME");
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.my_account:
                ((BaseActivity) MyHome.this).addFragmentwithactivity(new MyAccount());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
            case R.id.messages:
                Intent got = new Intent(this, MessagesHome.class);
                startActivity(got);
                mMyHomeTabs.setVisibility(View.GONE);
            case R.id.notification:
                ((BaseActivity) MyHome.this).addFragmentwithactivity(new Notification());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
            case R.id.settings:
                ((BaseActivity) MyHome.this).addFragmentwithactivity(new Settings());
                mMyHomeTabs.setVisibility(View.GONE);
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
                        finish();
                        Intent gotoLogin = new Intent(MyHome.this, LoginActivity.class);
                        gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

                Intent gotohome = new Intent(MyHome.this, HomeActivity.class);
                startActivity(gotohome);
                finish();
                break;
            case R.id.edit_profile:
                ((BaseActivity) MyHome.this).addFragmentwithactivity(new EditProfile());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.myads:
                ((BaseActivity) MyHome.this).addFragment(new MyAdsSwipe());
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
                break;
            case R.id.myjobs:
                ((BaseActivity) MyHome.this).addFragment(new MyJobs());
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                break;
        }

    }



    @Override
    public void onBackPressed() {
        System.out.println("back pressed() called of productlist-----------------------------------");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();


        } else {
            super.onBackPressed();
        }

    }

    private android.app.FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();

                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {

                        title.setText("MY HOME");

                        search.setVisibility(View.VISIBLE);
                        chat.setVisibility(View.VISIBLE);
                        mMyHomeTabs.setVisibility(View.VISIBLE);

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
                        } else if (titleToolbar.equalsIgnoreCase("ProductActivity")) {
                            title.setText("ALBERT");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("PostAd")) {
                            title.setText("POST YOUR AD");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                          /*  mTabsLayout.setVisibility(View.GONE);*/
                            chat.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("PostAd2")) {
                            title.setText("POST YOUR AD");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("ChatFragment")) {
                            title.setText("ALBERT");
                            chat.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            search.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("MessagesListProduct")) {
                            title.setText("MY MESSAGES");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("Notification")) {
                            title.setText("NOTIFICATIONS");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("Search")) {
                            title.setText("SEARCH");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("MyAccount")) {
                            title.setText("MYACCOUNT");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        }  else if (titleToolbar.equalsIgnoreCase("FindAjob")) {
                            title.setText("Find A Job");
                            search.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("GetHired")) {
                            title.setText("Get Hired");
                            search.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("Postjob")) {
                            title.setText("Post A Job");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("ForHire")) {
                            title.setText("For Hire");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("GetHired_Detail")) {
                            title.setText("Post a Job");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("Settings")) {
                            title.setText("SETTINGS");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("ChangePassword")) {
                            title.setText("CHANGE PASSWORD");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("ContactUs")) {
                            title.setText("CONTACT US");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("DeleteAccount")) {
                            title.setText("DELETE ACCOUNT");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE
                            );
                        } else if (titleToolbar.equalsIgnoreCase("MyAds")) {
                            title.setText("MY ADS");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
                            mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
                        } else if (titleToolbar.equalsIgnoreCase("MyJobs")) {
                            title.setText("MY JOBS");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);
                            mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                            mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                        }


                        currentFragment.onResume();
                    }
                }
            }
        };

        return result;
    }
}
