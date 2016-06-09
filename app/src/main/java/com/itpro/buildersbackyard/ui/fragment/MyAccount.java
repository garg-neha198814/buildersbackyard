package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.MessagesHome;


public class MyAccount extends Fragment implements View.OnClickListener {

   private TextView mTxtPostAd, mTxtCurrentAd,  mTxtMessage, mTxtSetting;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_my_account, container, false);

        inflateViews();


        return view;
    }

    private void inflateViews() {

        mTxtPostAd = (TextView) view.findViewById(R.id.post_new_adv);
        mTxtCurrentAd = (TextView) view.findViewById(R.id.current_adv);

        mTxtMessage = (TextView) view.findViewById(R.id.message);
        mTxtSetting = (TextView) view.findViewById(R.id.setting);
        mTxtPostAd.setOnClickListener(this);
        mTxtCurrentAd.setOnClickListener(this);

        mTxtMessage.setOnClickListener(this);
        mTxtSetting.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_new_adv:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new PostAd(), new MyAccount());
                break;
            case R.id.current_adv:
                Intent it=new Intent(getActivity(),MyHome.class);
                startActivity(it);
                break;

            case R.id.message:
                    Intent gotoMessagesHome = new Intent(getActivity(), MessagesHome.class);
                    startActivity(gotoMessagesHome);



                break;
            case R.id.setting:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new Settings(), new MyAccount());
                break;
        }
    }


}