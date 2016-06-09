package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.itpro.buildersbackyard.adapter.ImagePagerAdapter;
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
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

public class EditProduct extends Fragment implements View.OnClickListener, AppRequest {
    private AutoScrollViewPager viewPager;
    private TextView mUsername, mDescription, mZipCode, mCategory, mLocation;
    private Button mMakeOffer;
    private CircleIndicator defaultIndicator;
    private EditText mPrice;
    private int flag;

    private View view;
    private Map<String, String> mParams;

    private String mCurrentLatitude, mCurrentLongitude, mProductId, mUserId, ownerid;
    private CircleProgressBar progress;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editproduct, container, false);

        addViewPager();
        inflateViews();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        getproductlist_item();

        return view;
    }


    private float distance(double lat1, double lon1, double lat2, double lon2) {
        Location mylocation = new Location("");
        Location dest_location = new Location("");
        dest_location.setLatitude(lat2);
        dest_location.setLongitude(lon2);
        mylocation.setLatitude(lat1);
        mylocation.setLongitude(lon1);
        float distance1 = mylocation.distanceTo(dest_location);//in meters

        return distance1;
    }


    private void addViewPager() {
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.view_pager);
        defaultIndicator = (CircleIndicator) view.findViewById(R.id.circle);
        viewPager.setInterval(2000);
        viewPager.setScrollDurationFactor(7.0f);
        viewPager.startAutoScroll();
        viewPager.setCycle(true);


    }

    private void inflateViews() {
        mUsername = (TextView) view.findViewById(R.id.textView_username);
        // mCost = (TextView) view.findViewById(R.id.textView_money);
        mDescription = (TextView) view.findViewById(R.id.textView_description);
        mPrice = (EditText) view.findViewById(R.id.editprice);
        mZipCode = (TextView) view.findViewById(R.id.editText_zipcode);
        mCategory = (TextView) view.findViewById(R.id.editText_category);
        mLocation = (TextView) view.findViewById(R.id.editText_location);
        mMakeOffer = (Button) view.findViewById(R.id.button_makeoffer);
        mMakeOffer.setEnabled(false);
        mMakeOffer.setOnClickListener(this);
        mProductId = getArguments().getString("productid", "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        mCurrentLatitude = pref.getString(getString(R.string.current_latitude), "0");
        mCurrentLongitude = pref.getString(getString(R.string.current_latitude), "0");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_makeoffer:
                editProduct();

        }

    }

    private void editProduct() {
        String receiverId = pref.getString(getString(R.string.receiverIdProduct), "0");
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("product_id", mProductId);
        mParams.put("user_id", mUserId);
        mParams.put("price", mPrice.getText().toString().trim());
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            flag = 2;
            ApiRequests.getInstance().EditProduct(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void validation() {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "product");
        fragment.setArguments(bundle);

        ((BaseActivity) getActivity()).addFragmentWithBackStack(fragment, new ProductActivity());

    }

    void getproductlist_item() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("product_id", mProductId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            flag = 1;
            ApiRequests.getInstance().getproductlist_item(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
        if(flag==2){
            mMakeOffer.setEnabled(false);
        }
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        try {
            progress.setVisibility(View.GONE);
            if (flag == 1) {
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {

                    ownerid = responseBean.getProduct_details().getOwner_id();

                    String latitude = responseBean.getProduct_details().getLatitude();
                    String longitude = responseBean.getProduct_details().getLongitude();


                    //   Float distanceFar = distance(Double.parseDouble(mCurrentLatitude), Double.parseDouble(mCurrentLongitude), Double.parseDouble(latitude), Double.parseDouble(longitude));
                    ArrayList<String> s = new ArrayList<>();
                    for (int i = 0; i < responseBean.getProduct_details().getProduct_images().size(); i++) {
                        s.add(responseBean.getProduct_details().getProduct_images().get(i).getImage());
                    }
                    pref.edit().putString(getString(R.string.productTitle), responseBean.getProduct_details().getName()).apply();
                    pref.edit().putString(getString(R.string.receiverIdProduct), ownerid).apply();

                    Toolbar toolbar = (Toolbar) (getActivity()).findViewById(R.id.toolbar);
                    if (toolbar != null) {
                        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_search_txt);

                        mTitle.setText(responseBean.getProduct_details().getName());


                    }

                    mCategory.setText(responseBean.getProduct_details().getCategory());
                    mUsername.setText(responseBean.getProduct_details().getName());
                    mDescription.setText(responseBean.getProduct_details().getDecription());
                    mZipCode.setText(responseBean.getProduct_details().getZipcode());
                    mLocation.setText(responseBean.getProduct_details().getLocation());
                    mPrice.setText(responseBean.getProduct_details().getPrice());
                    //   mCost.setText(responseBean.getProduct_details().getPrice());
                /*mUsername.setText(responseBean.getProduct_details().getName());
                mDescription.setText(responseBean.getProduct_details().getDecription());
                mZipCode.setText(responseBean.getProduct_details().getZipcode());
                //  mCategory.setText(responseBean.getProduct_details().getCategory());
                mLocation.setText(responseBean.getProduct_details().getLocation());*/
                    viewPager.setAdapter(new ImagePagerAdapter(getActivity(), s, ""));
                    defaultIndicator.setViewPager(viewPager);

                    mMakeOffer.setEnabled(true);
                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (flag == 2) {
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    FragmentManager fm = getActivity().getFragmentManager();
                    for (int i = fm.getBackStackEntryCount(); i > fm.getBackStackEntryCount() - 1; i--) {
                        fm.popBackStack();
                    }
                    ((BaseActivity) getActivity()).addFragment(new MyAdsSwipe());
                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
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