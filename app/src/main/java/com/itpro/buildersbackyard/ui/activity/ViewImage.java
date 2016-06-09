package com.itpro.buildersbackyard.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.ImagePagerAdapter;
import com.itpro.buildersbackyard.adapter.ViewImageAdapter;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by root on 22/12/15.
 */
public class ViewImage extends ActionBarActivity {
    private AutoScrollViewPager viewPager;
    private SharedPreferences pref;
    private Map<String, String> mParams;
    private int size;
    private ArrayList<String> images;
    private ViewImageAdapter viewAdapter;

    private void addViewPager() {
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        // defaultIndicator = (CircleIndicator) findViewById(R.id.circle);
        viewPager.setInterval(2000);
        viewPager.setScrollDurationFactor(7.0f);
        viewPager.startAutoScroll();
        viewPager.setCycle(true);
        viewAdapter = new ViewImageAdapter(this, images);
        viewPager.setAdapter(viewAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        images = new ArrayList<>();
        size = getIntent().getIntExtra("size", 0);
        switch (size) {
            case 1:
                images.add(getIntent().getStringExtra("image1"));
                break;
            case 2:
                images.add(getIntent().getStringExtra("image1"));
                images.add(getIntent().getStringExtra("image2"));
                break;
            case 3:
                images.add(getIntent().getStringExtra("image1"));
                images.add(getIntent().getStringExtra("image2"));
                images.add(getIntent().getStringExtra("image3"));
                break;
        }
        addViewPager();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
