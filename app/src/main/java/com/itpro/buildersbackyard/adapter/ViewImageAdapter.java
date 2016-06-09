package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.activity.ViewImage;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by root on 22/12/15.
 */
public class ViewImageAdapter  extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private List<String> imageIdList = new ArrayList<String>();
    Activity activity;
    private int pagerType;
    private String mProductId;

    public ViewImageAdapter(Activity activity, ArrayList<String> imageIdList) {
        try {
            this.activity = activity;
            this.imageIdList = imageIdList;
            mLayoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }

    }

    @Override
    public int getCount() {
        return imageIdList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.cusomlistview_items, container, false);
        ImageView im = (ImageView) itemView.findViewById(R.id.imageView1);
        //im.setImageResource(imageIdList.get(position));
        Picasso.with(activity).load(UrlConstants.BASE_URL + imageIdList.get(position)).into(im);
        ((AutoScrollViewPager) container).addView(itemView, 0);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}