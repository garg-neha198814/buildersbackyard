package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;

/**
 * Created by root on 24/11/15.
 */
public class Settings extends Fragment implements View.OnClickListener {

    private View view;
    private LinearLayout lin1,lin2,lin3,lin4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        inflateViews();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void inflateViews() {
        lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        lin2 = (LinearLayout) view.findViewById(R.id.lin2);
        lin3 = (LinearLayout) view.findViewById(R.id.lin3);
        lin4 = (LinearLayout) view.findViewById(R.id.lin4);
        lin1.setOnClickListener(this);
        lin2.setOnClickListener(this);
        lin3.setOnClickListener(this);
        lin4.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin1:
                ((BaseActivity) getActivity()).addFragmentWithBackStack( new TermsOfService(),new Settings());
                break;
            case R.id.lin2:
                ((BaseActivity) getActivity()).addFragmentWithBackStack( new ContactUs(),new Settings());
                break;
            case R.id.lin3:
                ((BaseActivity) getActivity()).addFragmentWithBackStack( new ChangePassword(),new Settings());
                break;
            case R.id.lin4:
                ((BaseActivity) getActivity()).addFragmentWithBackStack( new DeleteAccount(),new Settings());
                break;
        }

    }
}