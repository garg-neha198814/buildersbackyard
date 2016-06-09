package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by root on 28/10/15.
 */


public class ImagePagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private List<String> imageIdList = new ArrayList<String>();
    Activity activity;
    private int pagerType;
    private String mProductId;

    public ImagePagerAdapter(Activity activity, ArrayList<String> imageIdList, String mProductId) {
        try {
            this.activity = activity;
            this.mProductId = mProductId;
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
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (imageIdList.size()) {
                    case 1:
                        Intent toimage = new Intent(activity, ViewImage.class);
                        toimage.putExtra("image1", imageIdList.get(0));
                        toimage.putExtra("size", imageIdList.size());
                        activity.startActivity(toimage);
                        break;
                    case 2:
                        Intent toimage1 = new Intent(activity, ViewImage.class);
                        toimage1.putExtra("image1", imageIdList.get(0));
                        toimage1.putExtra("image2", imageIdList.get(1));
                        toimage1.putExtra("size", imageIdList.size());
                        activity.startActivity(toimage1);
                        break;
                    case 3:
                        Intent toimage2 = new Intent(activity, ViewImage.class);
                        toimage2.putExtra("image1", imageIdList.get(0));
                        toimage2.putExtra("image2", imageIdList.get(1));
                        toimage2.putExtra("image3", imageIdList.get(2));
                        toimage2.putExtra("size", imageIdList.size());
                        activity.startActivity(toimage2);
                        break;
                }


            }

        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}