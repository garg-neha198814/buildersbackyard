package com.itpro.buildersbackyard.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.fragment.EditProfile;
import com.itpro.buildersbackyard.ui.fragment.MessagesListForHire;
import com.itpro.buildersbackyard.ui.fragment.MessagesListProduct;
import com.itpro.buildersbackyard.ui.fragment.MyAccount;
import com.itpro.buildersbackyard.ui.fragment.Notification;
import com.itpro.buildersbackyard.ui.fragment.Search;
import com.itpro.buildersbackyard.ui.fragment.Settings;
import com.itpro.buildersbackyard.utils.Constatnts;

import java.util.ArrayList;
import java.util.List;

public class MessagesHome extends BaseActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private ImageView search, chat;
    private SharedPreferences pref;
    private String titleToolbar;
    private TextView title;
    private Button mBtnMyAds, mBtnMyJobs;
    private LinearLayout mMyHomeTabs;
    private MessagesListProduct fragment;
    private MessagesListForHire fragment1;
    public static boolean is_open = Boolean.FALSE;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_home);
        is_open = Boolean.TRUE;
        value = "true";
        inflateToolbar();
        inflateViews();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getFragmentManager().addOnBackStackChangedListener(getListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        is_open = Boolean.TRUE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_open = Boolean.FALSE;
        value = "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        is_open = Boolean.FALSE;
        value = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_open = Boolean.TRUE;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        is_open = Boolean.TRUE;
        mMyHomeTabs.setVisibility(View.VISIBLE);

    }

    private void inflateViews() {
        // Calling the RecyclerView
        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mMyHomeTabs = (LinearLayout) findViewById(R.id.myhome_tabs);
        mMyHomeTabs.setVisibility(View.VISIBLE);
        mBtnMyAds = (Button) findViewById(R.id.myads);
        mBtnMyJobs = (Button) findViewById(R.id.myjobs);
        mBtnMyAds.setOnClickListener(this);
        mBtnMyJobs.setOnClickListener(this);
        mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
        mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
        MessagesListProduct fragment = new MessagesListProduct();
        Bundle bundle = new Bundle();
        bundle.putString("type", "product");
        fragment.setArguments(bundle);
        ((BaseActivity) MessagesHome.this).addFragment(fragment);
    }

    public void inflateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        search = (ImageView) mToolbar.findViewById(R.id.search);
        chat = (ImageView) mToolbar.findViewById(R.id.message);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MessagesHome.this).addFragmentwithactivity(new Search());
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomessage = new Intent(MessagesHome.this, MessagesHome.class);
                startActivity(gotomessage);
            }
        });
        if (value.equalsIgnoreCase("true")) {
            chat.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
        }
        title = (TextView) mToolbar.findViewById(R.id.toolbar_search_txt);
        title.setText("MY MESSAGES");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void checkFragmetVisibility(){
      String name = title.getText().toString();

        if(name.equalsIgnoreCase("MY MESSAGES")){

        }
        else{

            Intent got = new Intent(this, MessagesHome.class);
            startActivity(got);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        value = "";
        is_open = Boolean.FALSE;
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
                ((BaseActivity) MessagesHome.this).addFragmentwithactivity(new MyAccount());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
            case R.id.messages:
                checkFragmetVisibility();
                mMyHomeTabs.setVisibility(View.VISIBLE);
                break;

            case R.id.notification:
                ((BaseActivity) MessagesHome.this).addFragmentwithactivity(new Notification());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
            case R.id.settings:
                ((BaseActivity) MessagesHome.this).addFragmentwithactivity(new Settings());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
            case R.id.logout:


                final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("Are you sure you want to Logout ?");
                alertbox.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FragmentManager fm = getFragmentManager();
                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        pref.edit().clear().apply();
                        String social_login = pref.getString(getString(R.string.social_login), "0");
                        if (social_login.equalsIgnoreCase("true")) {
                            LoginManager.getInstance().logOut();
                        }
                        finish();

                        Intent gotoLogin = new Intent(MessagesHome.this, LoginActivity.class);
                        gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(gotoLogin);
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
                Intent gotohome = new Intent(MessagesHome.this, HomeActivity.class);
                gotohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotohome);
                finish();
                break;
            case R.id.edit_profile:
                ((BaseActivity) MessagesHome.this).addFragmentwithactivity(new EditProfile());
                mMyHomeTabs.setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myads:
                fragment = new MessagesListProduct();
                Bundle bundle = new Bundle();
                bundle.putString("type", "product");
                fragment.setArguments(bundle);
                ((BaseActivity) MessagesHome.this).replaceFragmentHire(new MessagesListProduct());
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_selected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_unselected);
                fragment1 = null;
                break;
            case R.id.myjobs:
                fragment1 = new MessagesListForHire();
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "hire");
                fragment1.setArguments(bundle1);
                ((BaseActivity) MessagesHome.this).replaceFragmentHire(new MessagesListForHire());
                mMyHomeTabs.setVisibility(View.VISIBLE);
                mBtnMyAds.setBackgroundResource(R.drawable.btn_unselected);
                mBtnMyJobs.setBackgroundResource(R.drawable.btn_selected);
                fragment = null;
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


        } else {
            super.onBackPressed();
            this.finish();
        }

    }

    private android.app.FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();

                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {

                        title.setText("MY MESSAGES");

                        search.setVisibility(View.GONE);
                        chat.setVisibility(View.GONE);
                        mMyHomeTabs.setVisibility(View.VISIBLE);

                    } else {
                        String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        Fragment currentFragment = getFragmentManager()
                                .findFragmentByTag(fragmentTag);

                        if (currentFragment == null) {
                            title.setText("Chat");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.GONE);
                            return;
                        }
                        titleToolbar = currentFragment.getTag();
                        if (titleToolbar.equalsIgnoreCase("ProductList")) {
                            title.setText("BUILDERS BACKYARD");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("ProductActivity")) {
                            title.setText(pref.getString(getString(R.string.productTitle), "0"));
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
                            title.setText("Chat");
                            chat.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);
                        } else if (titleToolbar.equalsIgnoreCase("MessagesListProduct")) {
                            title.setText("MY MESSAGES");
                            search.setVisibility(View.GONE);
                            chat.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.VISIBLE);
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
                        } else if (titleToolbar.equalsIgnoreCase("FindAjob")) {
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
                        } else if (titleToolbar.equalsIgnoreCase("TermsOfService")) {
                            title.setText("Terms Of Service");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                        } else if (titleToolbar.equalsIgnoreCase("SearchedList")) {
                            title.setText("SearchedList");
                            search.setVisibility(View.VISIBLE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);
                        }
                        else if (titleToolbar.equalsIgnoreCase("EditProfile")) {
                            title.setText("Profile");
                            search.setVisibility(View.GONE);
                            mMyHomeTabs.setVisibility(View.GONE);
                            chat.setVisibility(View.VISIBLE);


                        }

                        currentFragment.onResume();
                    }
                }
            }
        };

        return result;
    }


}