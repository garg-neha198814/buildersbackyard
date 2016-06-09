package com.itpro.buildersbackyard.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Product_Data;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.fragment.ProductActivity;
import com.itpro.buildersbackyard.ui.fragment.ProductList;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Edwin on 28/02/2015.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    Context activity;
    private ArrayList<Product_Data> productDataList = new ArrayList<>();
    int position;
    private SharedPreferences pref;
    public GridAdapter(Context activity, ArrayList<Product_Data> productDataList) {
        super();
        try {
            this.activity = activity;
            this.productDataList = productDataList;
            pref = activity.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        } catch (Exception e) {

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
        viewHolder.postProductName.setText(productDataList.get(i).getName());
        viewHolder.postProductPrice.setText("$"+productDataList.get(i).getPrice());
        Picasso.with(activity).load(UrlConstants.BASE_URL + productDataList.get(i).getProduct_images()).into(viewHolder.postProductImage);
        position = i;

        viewHolder.postProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putString(activity.getString(R.string.productTitle), "").apply();
                ProductActivity fragment = new ProductActivity(); //  object of next fragment
                Bundle bundle = new Bundle();
                bundle.putString("productid", productDataList.get(i).getProduct_id());
                bundle.putString("distance", productDataList.get(i).getDistance());

                fragment.setArguments(bundle);
                ((BaseActivity) activity).addFragmentWithBackStack(fragment, new ProductList());
            }
        });

    }

    @Override
    public int getItemCount() {

        return productDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postProductImage;
        public TextView postProductName, postProductPrice;

        public ViewHolder(final View itemView) {
            super(itemView);
            postProductImage = (ImageView) itemView.findViewById(R.id.post_product_image);
            postProductName = (TextView) itemView.findViewById(R.id.post_product_name);
            postProductPrice = (TextView) itemView.findViewById(R.id.post_product_price);

        }
    }
}