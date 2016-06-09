package com.itpro.buildersbackyard.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.Map;


public class JobDetailsFragment extends Fragment implements View.OnClickListener,AppRequest {
    private View view;
    private SharedPreferences pref;
    private String jobTitle,mUserId,receiverId,jobId;
    private Map<String, String> mParams;

    private CircleProgressBar progress;
    private TextView mChatBtn ,mPostName,mPostPosition,mPostCertifications,mPostSpecialization,mPostZipcode,mPostConatctNo,mPostStartDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_details, container, false);

        inflateViews();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return view;
    }

    private void inflateViews() {
        mPostName = (TextView) view.findViewById(R.id.post_header);
        mPostPosition = (TextView) view.findViewById(R.id.experience_details);
        mPostCertifications = (TextView) view.findViewById(R.id.certification_details);
        mPostSpecialization = (TextView) view.findViewById(R.id.specialities_details);
        mPostConatctNo = (TextView) view.findViewById(R.id.contact_details);
        mPostZipcode = (TextView) view.findViewById(R.id.zipcode_details);
        mPostStartDate = (TextView) view.findViewById(R.id.startdate_value);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        jobTitle =getArguments().getString("jobTitle", "0");
        pref.edit().putString(getString(R.string.jobTitle),jobTitle).apply();
        mChatBtn = (TextView) view.findViewById(R.id.chatting);
        mPostName.setText(getArguments().getString("jobTitle","0"));
        mPostPosition.setText(getArguments().getString("positionNeeded","0"));
        mPostCertifications.setText(getArguments().getString("certifications", "0"));
        mPostSpecialization.setText(getArguments().getString("specialities","0"));
        mPostConatctNo.setText(getArguments().getString("contactno","0"));
        mPostZipcode.setText(getArguments().getString("zipcode","0"));
        String[] startDate =getArguments().getString("startDate", "0").split(" ");
        mPostStartDate.setText(startDate[0]);
        Toolbar toolbar = (Toolbar) (getActivity()).findViewById(R.id.toolbar);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        jobId =getArguments().getString("jobId","0");
        if (toolbar != null) {
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_search_txt);

            mTitle.setText(jobTitle);


        }



        mPostConatctNo.setText(getArguments().getString("contactno","0"));
        mChatBtn.setOnClickListener(this);
    }
    private void generateConversationId() {
        receiverId = getArguments().getString("userid","0");
        Toast.makeText(getActivity(), "jobid"+jobId, Toast.LENGTH_LONG).show();
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("receiver_id", receiverId);
        mParams.put("type", "job");
        mParams.put("job_id", jobId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().generateConversationId(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chatting:
                generateConversationId();

                break;
        }

    }
    void validation() {
        pref.edit().putString(getString(R.string.receiverIdProduct), receiverId);
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type","job");
        fragment.setArguments(bundle);

        ((BaseActivity) getActivity()).addFragmentWithBackStack(fragment, new JobDetailsFragment());

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
                pref.edit().putString(getString(R.string.conversationIdProduct), responseBean.getConversationObj().getConversationId()).apply();
                validation();
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