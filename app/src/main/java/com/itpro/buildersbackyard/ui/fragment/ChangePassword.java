package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.FindACandidate;
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
public class ChangePassword extends Fragment implements View.OnClickListener, AppRequest {

    private EditText mCurrentpassword, mNewpassword, mConfirmPassword;
    private Button mSubmit;
    private View view;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private String mUserId, password, confirmPassword;
    InputMethodManager inputManager;
    private String social_status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return view;
    }

    private void inflateViews() {
        mCurrentpassword = (EditText) view.findViewById(R.id.et_currentpassword);
        mNewpassword = (EditText) view.findViewById(R.id.et_newpassword);
        mConfirmPassword = (EditText) view.findViewById(R.id.et_confimrpassword);
        mSubmit = (Button) view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        social_status = pref.getString(getString(R.string.social_login), "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        if (social_status.equalsIgnoreCase("true")) {
            mCurrentpassword.setEnabled(false);
            mNewpassword.setEnabled(false);
            mConfirmPassword.setEnabled(false);
        } else {
            mNewpassword
                    .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                inputManager.hideSoftInputFromWindow(
                                        v.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                password = mNewpassword.getText().toString();

                                if (password.equals("")) {
                                    mNewpassword
                                            .setError("Password can not be empty");
                                } else if (password.contains(" ")) {
                                    mNewpassword.setError("No spaces allowed");
                                } else if (password.length() < 8
                                        || password.length() > 20) {
                                    mNewpassword
                                            .setError("Password must be between 8 to 20");

                                } else {


                                    mNewpassword.setError(null);

                                }

                            }
                        }
                    });

            mConfirmPassword
                    .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                inputManager.hideSoftInputFromWindow(
                                        v.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                confirmPassword = mConfirmPassword
                                        .getText().toString();

                                password = mConfirmPassword.getText().toString();
                                if (!confirmPassword.equals(password)
                                        ) {

                                    mConfirmPassword
                                            .setError("These passwords don't match. Try again");
                                } else if (confirmPassword.equalsIgnoreCase("")) {
                                    mConfirmPassword
                                            .setError("Please enter password.");
                                } else {


                                    mConfirmPassword.setError(null);

                                }
                            }
                        }
                    });
        }

    }


    void validation() {
        String current_pwd;
        current_pwd = mCurrentpassword.getText().toString();
        password = mNewpassword.getText().toString();
        confirmPassword = mConfirmPassword.getText().toString();

        if (current_pwd.equals("") || password.equals("") || confirmPassword.equals("")) {
            if (current_pwd.equals("")) {
                mCurrentpassword.setError("Enter the current password");
            }
            if (password.equals("")) {
                mNewpassword.setError("Enter the New password");
            }
            if (confirmPassword.equals("")) {
                mConfirmPassword.setError("Enter the Confirm Password");
            }

        } else {
            if (!password.equals(confirmPassword)) {
                mNewpassword.setError(" password and confirm password should be same");
            } else {

                mParams = new HashMap();
                mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
                mParams.put("user_id", mUserId);


                mParams.put("old_password", current_pwd);
                mParams.put("new_password", password);

                System.out.println("api called");
                if (NetworkUtil.getConnectivityStatusString(getActivity())) {

                    ApiRequests.getInstance().changePassword(getActivity(), this, mParams);
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
                getActivity().finish();
                Intent gotoHome = new Intent(getActivity(), LoginActivity.class);
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
        try {
            progress.setVisibility(View.GONE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}