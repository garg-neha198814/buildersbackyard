package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
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
import com.itpro.buildersbackyard.ui.activity.ViewImage;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ProductActivity extends Fragment implements View.OnClickListener, AppRequest {
    private AutoScrollViewPager viewPager;
    private TextView mUsername, mCost, mDescription, mZipCode, mCity, mDIstance;
    private Button mMakeOffer;
    private CircleIndicator defaultIndicator;

    private int flag;

    private View view;
    private Map<String, String> mParams;

    private String mCurrentLatitude, mCurrentLongitude, mProductId, mUserId, ownerid, distanceFar1;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private CircleImageView candidatepic;
    private TextView ownerName;
    private Toolbar toolbar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product, container, false);

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
       /* viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ToImage = new Intent(getActivity(), ViewImage.class);
                ToImage.putExtra("image", getArguments().getString("productid", "0"));
                getActivity().startActivity(ToImage);
            }
        });*/

    }

    private void inflateViews() {
        candidatepic = (CircleImageView) view.findViewById(R.id.candidate_pic);
        ownerName = (TextView) view.findViewById(R.id.ownername);
        mUsername = (TextView) view.findViewById(R.id.textView_username);
        mCost = (TextView) view.findViewById(R.id.textView_money);
        mDescription = (TextView) view.findViewById(R.id.textView_description);
        mZipCode = (TextView) view.findViewById(R.id.editText_zipcode);
        mCity = (TextView) view.findViewById(R.id.editText_city);
        mDIstance = (TextView) view.findViewById(R.id.editText_distance);
        mMakeOffer = (Button) view.findViewById(R.id.button_makeoffer);
        mMakeOffer.setEnabled(false);
        mMakeOffer.setOnClickListener(this);

        mProductId = getArguments().getString("productid", "0");
        distanceFar1 = getArguments().getString("distance", "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);

        mUserId = pref.getString(getString(R.string.userId), "0");
        mCurrentLatitude = pref.getString(getString(R.string.current_latitude), "0");
        mCurrentLongitude = pref.getString(getString(R.string.current_longitude), "0");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_makeoffer:
                generateConversationId();

        }

    }

    private void generateConversationId() {
        String receiverId = pref.getString(getString(R.string.receiverIdProduct), "0");
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("receiver_id", receiverId);
        mParams.put("type", "product");
        mParams.put("product_id", mProductId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            flag = 2;
            ApiRequests.getInstance().generateConversationId(getActivity(), this, mParams);
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
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
        if (flag == 1) {
            try {
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


                    Float distanceFar = distance(Double.parseDouble(mCurrentLatitude), Double.parseDouble(mCurrentLongitude), Double.parseDouble(latitude), Double.parseDouble(longitude));
                    ArrayList<String> s = new ArrayList<>();
                    for (int i = 0; i < responseBean.getProduct_details().getProduct_images().size(); i++) {
                        s.add(responseBean.getProduct_details().getProduct_images().get(i).getImage());
                    }
                    pref.edit().putString(getString(R.string.productTitle), responseBean.getProduct_details().getName()).apply();
                    pref.edit().putString(getString(R.string.receiverIdProduct), ownerid).apply();

                    toolbar = (Toolbar) (getActivity()).findViewById(R.id.toolbar);
                    if (toolbar != null) {
                        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_search_txt);

                        mTitle.setText(responseBean.getProduct_details().getName());


                    }
                    if (!responseBean.getProduct_details().getOwner_pic().contains("http"))
                        Picasso.with(getActivity()).load(UrlConstants.BASE_URL + responseBean.getProduct_details().getOwner_pic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(candidatepic);
                    else
                        Picasso.with(getActivity()).load(responseBean.getProduct_details().getOwner_pic()).into(candidatepic);
                    Double dist = Double.parseDouble(distanceFar1);
                    ownerName.setText(responseBean.getProduct_details().getOwner_name());
                    mCost.setText("$" + responseBean.getProduct_details().getPrice());
                    mUsername.setText(responseBean.getProduct_details().getName());
                    mDescription.setText(responseBean.getProduct_details().getDecription());
                    mZipCode.setText(responseBean.getProduct_details().getZipcode());
                    mCity.setText(responseBean.getProduct_details().getLocation());
                    mDIstance.setText( String.format( " %.2f", dist ) + " Miles");
                    viewPager.setAdapter(new ImagePagerAdapter(getActivity(), s, mProductId));
                    defaultIndicator.setViewPager(viewPager);
                    mMakeOffer.setEnabled(true);


                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (flag == 2) {
            try {
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    //  Toast.makeText(getActivity(), "successfully generated", Toast.LENGTH_LONG).show();
                    pref.edit().putString(getString(R.string.conversationIdProduct), responseBean.getConversationObj().getConversationId()).apply();
                    pref.edit().putString(getString(R.string.ownerId), ownerid).apply();
                    // pref.edit().putString(getString(R.string.receiverIncaseOfSameOwnerId),responseBean.getConversationObj().getSenderId()).apply();
                    validation();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
    }
}