package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.ImagePagerAdapter;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.FindACandidate;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;


public class ForHire extends Fragment implements View.OnClickListener {

    private TextView mPostJob, mFindJob, mFindCandidate, mGetHired;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_for_hire, container, false);

        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void inflateViews() {
        mPostJob = (TextView) view.findViewById(R.id.post_job);
        mFindJob = (TextView) view.findViewById(R.id.find_job);
        mFindCandidate = (TextView) view.findViewById(R.id.find_candidate);
        mGetHired = (TextView) view.findViewById(R.id.get_hired);
        mPostJob.setOnClickListener(this);
        mFindJob.setOnClickListener(this);
        mFindCandidate.setOnClickListener(this);
        mGetHired.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_job:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new PostJob(), new ForHire());
                break;
            case R.id.find_job:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new FindAJob(), new ForHire());
                break;
            case R.id.find_candidate:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new FindACandidate(), new ForHire());

                break;
            case R.id.get_hired:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new GetHired(), new ForHire());
                break;
        }

    }
}
