package com.itpro.buildersbackyard.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends BaseActivity implements View.OnClickListener, AppRequest {
    private EditText mUserEmail;
    private Button mBtnRequestPwd;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        inflateViews();
    }

    private void inflateViews() {
        mUserEmail = (EditText) findViewById(R.id.user_email);
        mBtnRequestPwd = (Button) findViewById(R.id.request_password);
        progress = (CircleProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mBtnRequestPwd.setOnClickListener(this);
        pref = getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_password:
                validation();
        }

    }

    private void validation() {
        String email;
        email = mUserEmail.getText().toString();
        if (email.equals("")) {
            mUserEmail.setError("Please enter the email id to recover the password");
        } else {
            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");

            mParams.put("email", email);

            System.out.println("api called");
            ApiRequests.getInstance().forgotPassword(this, this, mParams);
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
                Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                finish();
                Intent gotoLogin = new Intent(ForgetPassword.this, LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                this.finish();
            } else {
                Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
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
