package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 20/11/15.
 */
public class GetHired_Detail extends Fragment implements View.OnClickListener, AppRequest {

    private TextView mHiredTrade, mHiredName, mHiredAvailabilty, mHiredContact, mChatting, mHiredLocation, mHiredCertifications, mHiredSpecialities;

    private View view;
    private SharedPreferences pref;
    private String mName, mTrade, mCertificate, mSpeciality, mLocation, mProfilepic, mUserId, receiverId, mCandidateId;
    private CircleImageView profile_pic;
    private Map<String, String> mParams;

    private CircleProgressBar progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gethired_detail, container, false);


        inflateViews();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        // inflateViews();

        return view;
    }

    private void inflateViews() {
        mHiredName = (TextView) view.findViewById(R.id.hired_candidate_name);
        mHiredAvailabilty = (TextView) view.findViewById(R.id.hired_candidate_availablity);
        mHiredTrade = (TextView) view.findViewById(R.id.hired_candidate_trade);
        mHiredSpecialities = (TextView) view.findViewById(R.id.hired_candidate_specialities);
        mHiredCertifications = (TextView) view.findViewById(R.id.hired_candidate_certification);
        mHiredLocation = (TextView) view.findViewById(R.id.hired_candidate_location);
        mHiredContact = (TextView) view.findViewById(R.id.hired_candidate_contact);

        profile_pic = (CircleImageView) view.findViewById(R.id.hired_candidate_pic);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mTrade = getArguments().getString("trade", "0");
        pref.edit().putString(getString(R.string.tradeTitle), mTrade).apply();
        mName = getArguments().getString("name", "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mUserId = pref.getString(getString(R.string.userId), "0");
        mCertificate = getArguments().getString("certification", "0");
        mSpeciality = getArguments().getString("specialties", "0");
        mCandidateId = getArguments().getString("candidateId", "0");
        mLocation = getArguments().getString("location", "0");
        mProfilepic = getArguments().getString("profile_pic", "0");
        String[] startDate = getArguments().getString("availability", "0").split(" ");
        mHiredAvailabilty.setText(startDate[0]);
        Picasso.with(getActivity()).load(UrlConstants.BASE_URL + mProfilepic).into(profile_pic);

        mHiredTrade.setText(mTrade);
        mHiredName.setText(mName);


        mHiredCertifications.setText(mCertificate);
        mHiredSpecialities.setText(mSpeciality);

        mHiredLocation.setText(mLocation);
        if (!mProfilepic.equals("")) {
            Picasso.with(getActivity()).load(UrlConstants.BASE_URL + mProfilepic).into(profile_pic);
        }
        Toolbar toolbar = (Toolbar) (getActivity()).findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_search_txt);

            mTitle.setText(mTrade);


        }
        mChatting = (TextView) view.findViewById(R.id.chatting_btn);
        mChatting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatting_btn:
                generateConversationId();
                break;
        }
    }

    private void generateConversationId() {
        Toast.makeText(getActivity(), "candidate id>>>."+mCandidateId, Toast.LENGTH_LONG).show();
        receiverId = getArguments().getString("userid", "0");
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("receiver_id", receiverId);
        mParams.put("type", "hire");
        mParams.put("hire_id", mCandidateId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().generateConversationId(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void validation() {
        pref.edit().putString(getString(R.string.receiverIdProduct), receiverId).apply();
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "hire");
        fragment.setArguments(bundle);
        progress.setVisibility(View.GONE);
        ((BaseActivity) getActivity()).addFragmentWithBackStack(fragment, new GetHired_Detail());

    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
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
            pref.edit().putString(getString(R.string.conversationIdProduct), responseBean.getConversationObj().getConversationId()).apply();
            validation();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
    }
}