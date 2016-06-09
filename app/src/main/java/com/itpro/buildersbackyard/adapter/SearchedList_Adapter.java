package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Searched_Data;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.fragment.ProductActivity;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 16/11/15.
 */
public class SearchedList_Adapter extends RecyclerView.Adapter<SearchedList_Adapter.ViewHolder> {
    Context activity;
    private ArrayList<Searched_Data> searchedList = new ArrayList<>();

    public SearchedList_Adapter(Activity activity, ArrayList<Searched_Data> searchedList) {
        super();
        try{
            this.activity = activity;
            this.searchedList = searchedList;
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        System.out.println("json data>>>>" +searchedList.get(i).getDistanceproduct());
        viewHolder.postProductName.setText(searchedList.get(i).getName());
        viewHolder.postProductPrice.setText("$"+searchedList.get(i).getPrice());
        Picasso.with(activity).load(UrlConstants.BASE_URL + searchedList.get(i).getProduct_images()).into(viewHolder.postProductImage);
   viewHolder.postProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity fragment = new ProductActivity(); //  object of next fragment
                Bundle bundle = new Bundle();
                bundle.putString("productid", searchedList.get(i).getProduct_id());
                bundle.putString("distance", searchedList.get(i).getDistanceproduct());
                fragment.setArguments(bundle);
                ((BaseActivity) activity).addFragmentwithactivity(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {

        return searchedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postProductImage;
        public TextView postProductName,postProductPrice;

        public ViewHolder(final View itemView) {
            super(itemView);
            postProductImage = (ImageView) itemView.findViewById(R.id.post_product_image);
            postProductName = (TextView) itemView.findViewById(R.id.post_product_name);
            postProductPrice = (TextView) itemView.findViewById(R.id.post_product_price);
        }
    }
}