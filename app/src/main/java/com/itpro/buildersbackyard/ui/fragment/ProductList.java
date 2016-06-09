package com.itpro.buildersbackyard.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.GridAdapter;
import com.itpro.buildersbackyard.bean.Product_Data;
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

public class ProductList extends Fragment implements View.OnClickListener, AppRequest {
     RecyclerView mRecyclerView;
    private Spinner mMiles,mDisplayMilesOnScroll;
     RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ImageView mBtnCamera;
    private Map<String, String> mParams;
    private SharedPreferences pref;
    private double mLat, mLong;
    private View view;
    private String latitude, longitude, mUserId, mMilesSelected = "With in 10 Miles";
    private CircleProgressBar progress;
    private boolean flag = false;
    ArrayList<Product_Data> postProductList;
TextView message;
    public ProductList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_list, container, false);


        inflateViews();
        setSpinnerElements();
      //  getproductList();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }




    private void inflateViews() {
        // Calling the RecyclerView
        mBtnCamera = (ImageView) view.findViewById(R.id.camera_btn);
        postProductList = new ArrayList<>();
        message = (TextView) view.findViewById(R.id.message_if_empty);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mBtnCamera.setOnClickListener(this);
        mMiles = (Spinner) view.findViewById(R.id.spinner_miles);

        //mDisplayMilesOnScroll =(Spinner) view.findViewById(R.id.spinner_miles_scroll);
        mMiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // mMiles.setVisibility(View.VISIBLE);
              //  mDisplayMilesOnScroll.setVisibility(View.GONE);
                mMilesSelected = parent.getItemAtPosition(position).toString();

                if(!postProductList.isEmpty()){
                    postProductList.clear();
                }
                getproductList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* mDisplayMilesOnScroll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMiles.setVisibility(View.VISIBLE);
               // mDisplayMilesOnScroll.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"display selected spinner",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

      /*  mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int positionView = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                System.out.println("position>>>>>>>>>>>" + positionView);
                Toast.makeText(getActivity(),"scroll called",Toast.LENGTH_SHORT).show();
                int len =postProductList.size();
                //mMiles.setVisibility(View.GONE);
                // mDisplayMilesOnScroll.setVisibility(View.VISIBLE);
                if(positionView<=len){
                    String distance = postProductList.get(positionView).getDistance();
                    Double dist = Double.parseDouble(distance);
                    if(dist>=0 && dist<=2){
                        mMiles.setSelection(3);
                    }else if(dist>2 && dist<=4){
                        mMiles.setSelection(2);
                    }else if(dist>4 && dist<=6){
                        mMiles.setSelection(1);
                    }else if(dist>6 && dist<=8){
                        mMiles.setSelection(0);
                    }else {
                        mMiles.setSelection(5);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });*/
    }

    private void setSpinnerElements() {

        // locality Spinner Drop down elements
        List<String> miles = new ArrayList<String>();
        miles.add("With in 10 Miles");

        miles.add("With in 20 Miles");
        miles.add("With in 40 Miles");
        miles.add("Over 40 Miles");


        // Creating adapter for locality spinner
        ArrayAdapter<String> milesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.search_miles_spinner, miles) {
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(14);
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }
        };


        // Drop down layout style - list view with radio button

        milesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching  adapter to spinner
        mMiles.setAdapter(milesAdapter);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn:
                ((BaseActivity) getActivity()).addFragmentWithBackStack(new PostAd(), new ProductList());


                break;

        }

    }

   /* public void showSettingsAlert() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting DialogHelp Title
        alertDialog.setTitle("GPS is settings");

        // Setting DialogHelp Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu? Click cancel if alredy on");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!flag){
                            flag=true;
                            displayToast("Setting on");
                            Intent intent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,1);
                            dialog.dismiss();
                        }else{
                            flag =false;
                            displayToast("latlong 1 more check");
                            onResume();

                        }



                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag=true;
                        displayToast("latlong cancel");
                        dialog.cancel();
                        onResume();
                    }
                });
        alertDialog.setNeutralButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        onResume();
                    }
                });
        if(!flag){
            displayToast("alert show");
            alertDialog.show();

        }else{
            displayToast("one more check before alert show");
            flag =false;
            onResume();

        }
        // Showing Alert Message

    }*/

    void getproductList() {

        String[] miles = mMilesSelected.split(" ");
        System.out.println("service for list lattttttttttttttttttttttt---------"+pref.getString(getString(R.string.current_latitude),"0"));
        System.out.println("service for list longgggggggggggggggggggggggg---------"+pref.getString(getString(R.string.current_longitude),"0"));

        mParams = new HashMap();
        if (miles[0].equalsIgnoreCase("Over") || mMilesSelected.equals("")) {
            mParams.put("miles", "");
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("user_id", mUserId);
            mParams.put("latitude", pref.getString(getString(R.string.current_latitude),"0"));
            mParams.put("longitude", pref.getString(getString(R.string.current_longitude), "0"));
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {
                ApiRequests.getInstance().getproductList(getActivity(), this, mParams);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {

            mParams.put("miles", miles[2]);
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("user_id", mUserId);
            mParams.put("latitude", pref.getString(getString(R.string.current_latitude),"0"));
            mParams.put("longitude", pref.getString(getString(R.string.current_longitude), "0"));
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {
                ApiRequests.getInstance().getproductList(getActivity(), this, mParams);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
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
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                System.out.println("called >>>>>>>>>>");

                for (Product_Data data : responseBean.getProduct_data()) {
                    postProductList.add(data);
                }

                    message.setVisibility(view.GONE);
                    mAdapter = new GridAdapter(getActivity(), postProductList);
                    mRecyclerView.setAdapter(mAdapter);



            } else {
                message.setVisibility(view.VISIBLE);
                message.setText("No Products");
               // Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
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


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}

