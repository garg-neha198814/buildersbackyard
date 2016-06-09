package com.itpro.buildersbackyard.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.FindCandidate_Adapter;
import com.itpro.buildersbackyard.adapter.GridAdapter;
import com.itpro.buildersbackyard.bean.Find_Candidate;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.fragment.MessagesListProduct;
import com.itpro.buildersbackyard.ui.fragment.MyAccount;
import com.itpro.buildersbackyard.ui.fragment.Notification;
import com.itpro.buildersbackyard.ui.fragment.Settings;
import com.itpro.buildersbackyard.utils.Constatnts;

import com.itpro.buildersbackyard.utils.NetworkUtil;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 16/11/15.
 */
public class FindACandidate extends Fragment implements AppRequest {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Toolbar mToolbar;
    private ImageView search, chat;
    private TextView title;
    private View view;
    private String titleToolbar, mLatitude, mLongitude, mUserId;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private Map<String, String> mParams;
    private Double mLat, mLong;
    InputMethodManager inputManager;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_findcandidate, container, false);
        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return view;
    }

    private void inflateViews() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);

        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        mLatitude = pref.getString(getString(R.string.current_latitude), "");
        mLongitude = pref.getString(getString(R.string.current_longitude), "");
    }

    @Override
    public void onResume() {
        super.onResume();
        inflateViews();

        getCandidates();
    }


    private void getCandidates() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("latitude", mLatitude);
        mParams.put("longitude", mLongitude);
        System.out.println("api called");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().getCandidateList(getActivity(), this, mParams);
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
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                ArrayList<Find_Candidate> candidatesList = new ArrayList<>();
                for (Find_Candidate data : responseBean.getFind_candidate()) {
                    if (!data.getUser_id().equals(mUserId)) {
                        candidatesList.add(data);
                    }
                }
                mAdapter = new FindCandidate_Adapter(getActivity(), candidatesList);
                mRecyclerView.setAdapter(mAdapter);


            } else {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
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