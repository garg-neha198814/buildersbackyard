package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.FindajobAdapter;
import com.itpro.buildersbackyard.bean.Find_Job_Details;
import com.itpro.buildersbackyard.bean.JobDetails;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;

import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16/11/15.
 */
public class FindAJob extends Fragment implements View.OnClickListener ,AppRequest {
    private FindajobAdapter adapter;
    private RecyclerView recyclerView;

    private Map<String, String> mParams ;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private View view;
    private String  latitude, longitude,userId;
    private double mLat, mLong;
    public FindAJob() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_findajob, container, false);


        inflateViews();
      //  getLatLongitude();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;

    }

    private void getJobs() {
        userId = pref.getString(getString(R.string.userId),"0");
        mParams = new HashMap();
        mParams.put("token","a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id",userId);
        mParams.put("latitude",latitude);
        mParams.put("longitude",longitude);

        mParams.put("social_id", "");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getJobs(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void inflateViews() {
        progress=(CircleProgressBar)view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        latitude = pref.getString(getString(R.string.current_latitude),"");
        longitude =pref.getString(getString(R.string.current_longitude), "");
        getJobs();
    }
   /* void getLatLongitude(){
        GPSTracker gps = new GPSTracker(getActivity());
        mLat = gps.getLatitude();
        mLong = gps.getLongitude();

        // Converting double to string
        latitude = String.valueOf(mLat);
        longitude = String.valueOf(mLong);

    }*/
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.myList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);



    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
        Log.d("started", "started");
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        try {
            Log.d("completed", "completed");
            System.out.println("completed");
            progress.setVisibility(View.GONE);
            // progress.set
            Gson gson = new Gson();
            ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
       /* if (is_Scocial.equalsIgnoreCase("0")) {*/
            if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                ArrayList<Find_Job_Details> Jobs = new ArrayList<>();
                for (Find_Job_Details data : responseBean.getFindJob()) {
                    if (!data.getUserId().equals(userId)) {
                        Jobs.add(data);
                    }

                    adapter = new FindajobAdapter(getActivity(), Jobs);
                    recyclerView.setAdapter(adapter);
                }


            } else {
                System.out.println("else login called");
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
    public void onClick(View v) {

    }
}


