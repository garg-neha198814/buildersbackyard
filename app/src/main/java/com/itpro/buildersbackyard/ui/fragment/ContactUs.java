package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.HomeActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 24/11/15.
 */
public class ContactUs extends Fragment implements View.OnClickListener,AppRequest {

    private EditText mSubject, mMessage, mEmail;
    private Button mSubmit;
    InputMethodManager inputManager;
    private View view;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private String mUserId,email;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contactus, container, false);

        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void inflateViews() {
        mSubject = (EditText) view.findViewById(R.id.et_subject);
        mMessage = (EditText) view.findViewById(R.id.et_message);
        mEmail = (EditText) view.findViewById(R.id.et_email);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        mSubmit = (Button) view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);

    }
    void validation() {
        String subject, message;
        subject = mSubject.getText().toString();
        message = mMessage.getText().toString();
        email=mEmail.getText().toString();

        if (subject.equals("") || message.equals("") || email.equals("")) {
            if (subject.equals("")) {
                mSubject.setError("Enter the subject");
            }
            if (message.equals("")) {
                Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_LONG).show();
            }
            if (email.equals("")) {
                mEmail.setError("Enter the Email");
            }

        } else {
            if (email.contains(" ")) {
                mEmail.setError("Space not allowed");
            } else if (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                mEmail.setError("Email is invalid");
            } else {


                mEmail.setError(null);


                mParams = new HashMap();
                mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
                mParams.put("user_id", mUserId);


                mParams.put("subject", subject);
                mParams.put("message", message);
                mParams.put("email", email);
                System.out.println("api called");
                if (NetworkUtil.getConnectivityStatusString(getActivity())) {

                    ApiRequests.getInstance().contactUs(getActivity(), this, mParams);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                validation();
                break;
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
            System.out.println("response from json >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + listener.getJsonResponse());

            ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

            if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                Intent gotoHome = new Intent(getActivity(), HomeActivity.class);
                startActivity(gotoHome);

            } else {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
    }
}