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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.MyAds_Adapter;
import com.itpro.buildersbackyard.adapter.MyJobs_Adapter;
import com.itpro.buildersbackyard.bean.Find_Job_Details;
import com.itpro.buildersbackyard.bean.MyAdds_Data;
import com.itpro.buildersbackyard.bean.ResponseBean;
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

/**
 * Created by root on 25/11/15.
 */
public class MyJobs extends Fragment implements AppRequest {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private View view;
    private Map<String, String> mParams;
    private SharedPreferences pref;
    private String mUserId;
    private CircleProgressBar progress;
    TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myjobs, container, false);

        inflateViews();
        getMyJobs();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void getMyJobs() {

        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        System.out.println("api called");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getMyJobs(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void inflateViews() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.myList);
        mRecyclerView.setHasFixedSize(true);
        message = (TextView) view.findViewById(R.id.message_if_empty);
        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
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
                ArrayList<Find_Job_Details> jobs = new ArrayList<>();
                for (Find_Job_Details data : responseBean.getMy_job()) {

                    jobs.add(data);
                }

                    message.setVisibility(view.GONE);
                    mAdapter = new MyJobs_Adapter(getActivity(), jobs, mRecyclerView);
                    mRecyclerView.setAdapter(mAdapter);



            } else {
                message.setVisibility(view.VISIBLE);
                message.setText("No Jobs");
                //Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}