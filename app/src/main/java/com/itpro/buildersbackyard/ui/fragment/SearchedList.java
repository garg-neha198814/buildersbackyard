package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.GridAdapter;
import com.itpro.buildersbackyard.adapter.SearchedList_Adapter;
import com.itpro.buildersbackyard.bean.Product_Data;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.bean.Searched_Data;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchedList extends Fragment implements AppRequest {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Map<String, String> mParams;
    private SharedPreferences pref;
    private View view;
    private CircleProgressBar progress;
    private String mcategory, mPrice, mMiles, mlocation, mLatitude, mLongitude, mUserId, mBrand, mKeyword;
    private TextView message;

    public SearchedList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searched_list, container, false);


        inflateViews();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }


    private void inflateViews() {
        // Calling the RecyclerView

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        message = (TextView) view.findViewById(R.id.product_if_empty);
        message.setVisibility(view.GONE);
        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);

        mUserId = pref.getString(getString(R.string.userId), "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mcategory = getArguments().getString("category", "0");
        mPrice = getArguments().getString("price", "0");
        mMiles = getArguments().getString("miles", "0");
        mlocation = getArguments().getString("location", "0");
        mLatitude = getArguments().getString("latitude", "0");
        mLongitude = getArguments().getString("longitude", "0");
        mBrand = getArguments().getString("brand", "0");
        mKeyword = getArguments().getString("keyword", "0");
       /* if(!searchedList.isEmpty()){
            searchedList.clear();
        }*/

        searchCandidate();
    }


    void searchCandidate() {

        // Toast.makeText(getActivity(),"keyword in selected list"+mKeyword,Toast.LENGTH_LONG).show();


        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("category", mcategory);
       mParams.put("max_price", mPrice);
       mParams.put("nearby_distance", mMiles);
        mParams.put("location",mlocation);
        mParams.put("latitude",mLatitude);
        mParams.put("longitude",mLongitude);
        mParams.put("keywords", mKeyword);
        mParams.put("brand", mBrand);
        mParams.put("user_id",mUserId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().searchCandidate(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
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
            if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                message.setVisibility(view.GONE);
                ArrayList<Searched_Data> searchedList = new ArrayList<>();
                for (Searched_Data data : responseBean.getSearch_product()) {
                    System.out.println("data>>>>>>>>>>>>>>>>>>>>>>searched" + data.getLocation());
                  // Toast.makeText(getActivity(),"data>>>>>>>>>>>>>>>>>>>>>>searched" + data.getLocation(),Toast.LENGTH_LONG).show();
                    searchedList.add(data);
                }

                mAdapter = new SearchedList_Adapter(getActivity(), searchedList);
                mRecyclerView.setAdapter(mAdapter);

            } else {
                message.setVisibility(view.VISIBLE);
                message.setText("No Products");
                //  Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


