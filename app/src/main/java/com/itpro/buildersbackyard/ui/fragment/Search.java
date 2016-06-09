package com.itpro.buildersbackyard.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.PlaceAutocompleteAdapter;
import com.itpro.buildersbackyard.bean.ResponseBeanZipCode;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import com.itpro.buildersbackyard.utils.SeekbarWithIntervals;
public class Search extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,  AppRequest {
    private EditText mKeyword, mZipcode, mBrand;
    private Spinner mLocality, mCategory;
    private SeekBar mPriceBar;

   // private SeekBar  mMilesBar;
    private Button mSearchBtn, mCancelBtn;
    private TextView mPrice, mMiles, mPriceDisplay, mMilesDisplay,txtVal;
    private Map<String, String> mParams;
    private View view;
    private CircleProgressBar progress;
    private String mSearchLocality, mSearchCategory, mLatitude, mLongitude, mUserId, mSearchBrand,mMilesValue="0";
    private GoogleApiClient mGoogleApiClient;
    PlaceAutocompleteAdapter mAdapter;
    AutoCompleteTextView mAutocompleteView;
    private int milesProgessValue = 0, priceProgressValue = 0;
    private static final String TAG = "google places";
    int flag, addressSelected = 0;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247,
            151.383362));

    private SharedPreferences pref;
    Double latitude, longitude;
    private SeekbarWithIntervals SeekbarWithIntervals = null;
    private List<String> getIntervals() {
        return new ArrayList<String>() {{
            add("0");
            add("20");
            add("50");
            add("100");
            add("2000");
        }};
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private SeekbarWithIntervals getSeekbarWithIntervals(View view) {
        if (SeekbarWithIntervals == null) {
            SeekbarWithIntervals = (SeekbarWithIntervals) view.findViewById(R.id.seekbar1);
        }

        return SeekbarWithIntervals;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);

        inflateViews();
        setSpinnerElements();

        //autocompleteaddress();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return view;
    }





    private void setSpinnerElements() {

        // locality Spinner Drop down elements
        List<String> locality = new ArrayList<String>();
        locality.add("Search by Locality");
        locality.add("locality2");
        locality.add("locality3");
        locality.add("locality4");
        locality.add("locality5");
        locality.add("locality6");

        // Category Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Search by Category");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // brand Spinner Drop down elements
        List<String> brand = new ArrayList<String>();
        brand.add("Search by Brand");
        brand.add("brand2");
        brand.add("brand3");
        brand.add("brand4");
        brand.add("brand5");
        brand.add("brand6");

        // Creating adapter for locality spinner
        ArrayAdapter<String> localityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locality);


        // Creating adapter for category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);


        // Creating adapter for brand spinner
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brand);

        // Drop down layout style - list view with radio button

        localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching  adapter to spinner
//        mLocality.setAdapter(localityAdapter);
        mCategory.setAdapter(categoryAdapter);
        //  mBrand.setAdapter(brandAdapter);
    }


    private void inflateViews() {
        mKeyword = (EditText) view.findViewById(R.id.search_by_keyword);
        mZipcode = (EditText) view.findViewById(R.id.search_by_zipcode);
        mBrand = (EditText) view.findViewById(R.id.search_by_brand);
       // mLocality = (Spinner) view.findViewById(R.id.spinner_locality);
        //mBrand = (Spinner) view.findViewById(R.id.spinner_brand);
        mCategory = (Spinner) view.findViewById(R.id.spinner_category);
        mPriceBar = (SeekBar) view.findViewById(R.id.price_seekbar);
     // mMilesBar = (SeekBar) view.findViewById(R.id.miles_seekbar);
        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mMiles = (TextView) view.findViewById(R.id.miles_selected_value);
        mPrice = (TextView) view.findViewById(R.id.price_selected_value);
        mPriceDisplay = (TextView) view.findViewById(R.id.price_value);
        mMilesDisplay = (TextView) view.findViewById(R.id.miles_value);
        mPriceDisplay.setVisibility(view.VISIBLE);
        mMilesDisplay.setVisibility(view.VISIBLE);
        mSearchBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
     //   mLocality.setOnItemSelectedListener(this);
        // mBrand.setOnItemSelectedListener(this);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mCategory.setOnItemSelectedListener(this);

        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        /*pref.edit().putString(getString(R.string.latitude), "").apply();
        pref.edit().putString(getString(R.string.longitude), "").apply();*/
        mUserId = pref.getString(getString(R.string.userId), "0");

        mPriceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceProgressValue = progress;
                //   Toast.makeText(getActivity(),"price>>>selected progress"+priceProgressValue,Toast.LENGTH_LONG).show();
                String price = (progress / 1000) + " ";
                mPriceDisplay.setVisibility(view.INVISIBLE);
                mPrice.setVisibility(view.VISIBLE);
                if (progress == 0) {
                    mPrice.setText("0 K");
                } else if (progress > 0 && progress <= 1000) {
                    mPrice.setText("0 to 1K");
                } else {
                    mPrice.setText("0 to " +price+"K");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

      /*  mMilesBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                milesProgessValue = progress;
                //   Toast.makeText(getActivity(),"miles>>>selected progress"+milesProgessValue,Toast.LENGTH_LONG).show();
                String miles = progress + " ";
                mMilesDisplay.setVisibility(view.GONE);
                mMiles.setVisibility(view.VISIBLE);
                mMiles.setText(miles + "Miles");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        final List<String> seekbarIntervals = getIntervals();
        getSeekbarWithIntervals(view).setIntervals(seekbarIntervals);

        SeekbarWithIntervals.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMilesDisplay.setVisibility(View.INVISIBLE);
                mMiles.setVisibility(View.VISIBLE);
                //txtVal.setText(String.valueOf(progress + 1));
               // txtVal.setText(seekbarIntervals.get(progress));
                mMilesValue = seekbarIntervals.get(progress);

                if(mMilesValue.equals("0")) {
                    mMiles.setText(seekbarIntervals.get(progress) + "Miles");
                }else{
                    mMiles.setText("0 to"+seekbarIntervals.get(progress) + "Miles");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                searchCandidate();

                break;
            case R.id.cancel_btn:
                getFragmentManager().popBackStack();
                break;
        }

    }


    String miles, price, location, zipcode, brand, keyword, category;

    void searchCandidate() {
        keyword = mKeyword.getText().toString();
        miles = mMiles.getText().toString();
        price = mPrice.getText().toString();
        zipcode = mZipcode.getText().toString();

        brand = mBrand.getText().toString();

        if (keyword.equals("") && zipcode.equals("") && brand.equals("") && mSearchCategory.equalsIgnoreCase("Search by Category") && mMilesValue.equals("0")&& priceProgressValue == 0) {

            Toast.makeText(getActivity(), "Please fill atleast one field", Toast.LENGTH_LONG).show();


        } else {
            if(!mMilesValue.equals("0")){
                if(zipcode.equals("")){
                    mZipcode
                            .setError("Enter the correct zipcode");
                }
                else{
                    if (zipcode.length() < 5 || zipcode.length() > 6
                            ) {
                        mZipcode
                                .setError("Enter the correct zipcode");

                    } else {
                        getLatLngfromZip();
                    }
                }
            }
            else if (!zipcode.equals("")) {
                if (zipcode.length() < 5 || zipcode.length() > 6
                        ) {
                    mZipcode
                            .setError("Enter the correct zipcode");

                } else {
                    getLatLngfromZip();
                }
            } else {

                mLatitude = pref.getString(getString(R.string.current_latitude),"0");
                mLongitude = pref.getString(getString(R.string.current_longitude),"0");
                searchProducts();
            }
        }
       /* mLatitude = pref.getString(getString(R.string.latitude), "0");
        mLongitude = pref.getString(getString(R.string.longitude), "0");*/
        // location = mAutocompleteView.getText().toString();
       /* if (location.equals("") || mLatitude.equals("") || mLongitude.equals("")) {

            mAutocompleteView.setError("Please Select The Location");
            if (mLatitude.equals("") || mLongitude.equals("")) {


                System.out.println("iffff");
                Address address = GetLatLong.getAddressFromLocation(location, getActivity());
                System.out.println("address" + address);
                if (address == null) {
                    mAutocompleteView.setError("Enter The Correct Location");
                    Toast.makeText(getActivity(), "Unable To Fetch The Location Please Enter The Correct Location and Check your Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    mLatitude = String.valueOf(address.getLatitude());
                    mLongitude = String.valueOf(address.getLongitude());

                    pref.edit().putString(getString(R.string.latitude), mLatitude).apply();
                    pref.edit().putString(getString(R.string.longitude), mLongitude).apply();
                    searchProducts();

                }

            }
        } else {

            searchProducts();

        }*/
    }

    private void getLatLngfromZip() {

        mParams = new HashMap();
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            System.out.println("zipcode>>>>>>" + zipcode);
            ApiRequests.getInstance().getLatLngFromZip(getActivity(), this, mParams, zipcode);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void searchProducts() {
        if (mSearchCategory.equalsIgnoreCase("Search by Category")) {
            mSearchCategory = "";
        }





       /* String milesSelected =miles.substring(0,miles.length()-5);
        String priceSelected =price.substring(0,price.length()-1);
        Toast.makeText(getActivity(),"miles>>>>"+milesSelected+"price>>>>"+priceSelected,Toast.LENGTH_LONG).show();*/
       // Toast.makeText(getActivity(),"latitude>>>>"+mLatitude+"mLongitude>>>>"+mLongitude,Toast.LENGTH_LONG).show();
        SearchedList fragment = new SearchedList(); //  object of next fragment
        Bundle bundle = new Bundle();
        bundle.putString("category", mSearchCategory);
        bundle.putString("price", priceProgressValue + "");
        bundle.putString("miles", mMilesValue);
        bundle.putString("brand", brand);
        bundle.putString("keyword", keyword);

        bundle.putString("location", zipcode);
        bundle.putString("latitude", mLatitude);
        bundle.putString("longitude", mLongitude);

        fragment.setArguments(bundle);
        FragmentManager fm = getActivity().getFragmentManager();
        for (int i = fm.getBackStackEntryCount(); i > fm.getBackStackEntryCount() - 1; i--) {
            fm.popBackStack();
        }
        ((BaseActivity) getActivity()).addFragmentWithBackStack(fragment, new Search());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            /*case R.id.spinner_locality:
                mSearchLocality = parent.getItemAtPosition(position).toString();
                break;*/
            case R.id.spinner_category:
                mSearchCategory = parent.getItemAtPosition(position).toString();
                break;
            /*case R.id.spinner_brand:
                mSearchBrand = parent.getItemAtPosition(position).toString();
                break;*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        System.out.println("started");
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        progress.setVisibility(View.INVISIBLE);

        try {
            Gson gson = new Gson();
            ResponseBeanZipCode responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBeanZipCode.class);
            if (responseBean.getStatus().equalsIgnoreCase("OK")) {

                latitude = responseBean.getResults().get(0).getGeometry().getLocation().getLat();
                longitude = responseBean.getResults().get(0).getGeometry().getLocation().getLng();
                mLatitude = String.valueOf(latitude);
                mLongitude = String.valueOf(longitude);
                //    Toast.makeText(getActivity(), "latitude search" + mLatitude + "longitude" + mLongitude, Toast.LENGTH_LONG).show();
                if (mLatitude.equals("") || mLongitude.equals("")) {

                } else {
                    searchProducts();
                }
                System.out.println(responseBean.getResults().get(0).getFormatted_address() + " <<<<<<<<<<<<<<FO RMATTED ADDRESS");
            } else {
                Toast.makeText(getActivity(), "Wrong Zipcode", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

        }
    }


    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        System.out.println("failed");
        progress.setVisibility(View.INVISIBLE);
    }
}