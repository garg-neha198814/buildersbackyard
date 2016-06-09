package com.itpro.buildersbackyard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.bean.ResponseBeanZipCode;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class  PostJob extends Fragment implements View.OnClickListener, AppRequest, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,  DatePickerDialog.OnDateSetListener {
    private EditText mJobTitle, mJobPosition, mJobCertifications, mJobSpecilaities, mZipcode, mContactNo;
    private TextView mStartDate, mEndDate;
    private Button mBtnPostJob;
    private View view;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private SharedPreferences pref;
    private int myear, mmonth, mday;

    InputMethodManager inputManager;
    private Calendar calendar;
    private int startDay, startMonth, startYear, endDay, endMonth, endYear, flag, addressSelected = 0, hour, minute;
    private String selectedDate = null, date = "", zipCode, contactNo, mUserId, value = "", mLat = "", mLongi = "";
    ;
    boolean pastDateSelected;
    AutoCompleteTextView mLocation;
    private GoogleApiClient mGoogleApiClient;
    PlaceAutocompleteAdapter mAdapter;
    private static final String TAG = "google places";
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247,
            151.383362));
    private Date startDate;
    Double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_post_job, container, false);
        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inflateViews();
        // autocompleteaddress();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void inflateViews() {
        mJobTitle = (EditText) view.findViewById(R.id.post_job_title);
        mJobPosition = (EditText) view.findViewById(R.id.post_job_position);
        mJobCertifications = (EditText) view.findViewById(R.id.post_job_certifications);
        mJobSpecilaities = (EditText) view.findViewById(R.id.post_job_specialities);
        mZipcode = (EditText) view.findViewById(R.id.post_job_zipcode);
        mContactNo = (EditText) view.findViewById(R.id.post_job_contactNo);
        // mLocation = (AutoCompleteTextView) view.findViewById(R.id.post_job_location);
        mStartDate = (TextView) view.findViewById(R.id.post_job_startdate_value);
        mEndDate = (TextView) view.findViewById(R.id.post_job_enddate_value);
        mBtnPostJob = (Button) view.findViewById(R.id.post_job_btn);
        mBtnPostJob.setOnClickListener(this);
        mBtnPostJob.setEnabled(true);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
      /*  pref.edit().putString(getString(R.string.latitude), "").apply();
        pref.edit().putString(getString(R.string.longitude), "").apply();*/
        mUserId = pref.getString(getString(R.string.userId), "0");
        final Calendar c = Calendar.getInstance();
        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);
        calendar = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        mZipcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    zipCode = mZipcode.getText().toString();

                    if (zipCode.equals("")) {
                        mZipcode
                                .setError("zipcode can not be empty");
                    } else if (zipCode.contains(" ")) {
                        mZipcode.setError("No spaces allowed");
                    } else if ((zipCode.length()) < 5 || (zipCode.length() > 6)
                            ) {
                        mZipcode
                                .setError("Enter the correct zipcode");

                    } else {


                        mZipcode.setError(null);

                    }

                }
            }
        });

        mContactNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    contactNo = mContactNo.getText().toString();

                    if (contactNo.equals("")) {
                        mContactNo.setError("Enter the Contact No");
                    } else if (contactNo.contains(" ")) {
                        mContactNo.setError("Space not allowed");
                    } else if (contactNo.length() < 10 || contactNo.length() > 14) {
                        mContactNo.setError("Invalid Number");
                    } else {


                        mContactNo.setError(null);

                    }

                }
            }
        });
    }

    String jobTitle, jobPosition, jobCertification, jobSpecialities, startPostDate, endDate, location;

    void validation() {

        jobTitle = mJobTitle.getText().toString();
        jobPosition = mJobPosition.getText().toString();
        jobCertification = mJobCertifications.getText().toString();
        jobSpecialities = mJobSpecilaities.getText().toString();
        zipCode = mZipcode.getText().toString();
        contactNo = mContactNo.getText().toString();
        startPostDate = mStartDate.getText().toString();
        endDate = mEndDate.getText().toString();
       /* location = mLocation.getText().toString();*/
        if (jobTitle.equals("") || jobPosition.equals("") || jobCertification.equals("") || jobSpecialities.equals("") || zipCode.equals("") || contactNo.equals("") || startPostDate.equals("") || endDate.equals("")) {
            if (jobTitle.equals("")) {
                mJobTitle.setError("Enter The Title");
            }
            if (jobPosition.equals("")) {
                mJobPosition.setError("Enter The Position");
            }
            if (jobCertification.equals("")) {
                mJobCertifications.setError("Enter The Certifications");
            }
            if (jobSpecialities.equals("")) {
                mJobSpecilaities.setError("Enter The Specialities");
            }
            if (zipCode.equals("")) {
                mZipcode.setError("Enter The Zipcode");
            }

            if (startPostDate.isEmpty()) {
                mStartDate.setError("Enter The Start Date");
            }
            if (endDate.equals("")) {
                mEndDate.setError("Enter The End Date");
            }
            if (contactNo.equals("")) {
                mContactNo.setError("Enter The End Date");
            }


           /* if (mLat.equals("") || mLongi.equals("")) {


                System.out.println("iffff");
                Address address = GetLatLong.getAddressFromLocation(location, getActivity());
                System.out.println("address" + address);
                if(address==null) {
                    mLocation.setError("Enter The Correct Location");
                    Toast.makeText(getActivity(), "Unable To Fetch The Location Please Enter The Correct Location and Check your Internet Connection", Toast.LENGTH_LONG).show();
                }
                else{

                    postJob();

                }



            }*/
        } else {
            getLatLngfromZip();


        }

    }

    private void getLatLngfromZip() {

        mParams = new HashMap();
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getLatLngFromZip(getActivity(), this, mParams, zipCode);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void postJob() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("post_title", jobTitle);
        mParams.put("position_needed", jobPosition);

        mParams.put("certification_required", jobCertification);
        mParams.put("specialties", jobSpecialities);
        mParams.put("zipcode", zipCode);
        mParams.put("contact_no", contactNo);
        mParams.put("location", zipCode);
        mParams.put("latitude", mLat);
        mParams.put("longitude", mLongi);
        mParams.put("strt_date", startPostDate);
        mParams.put("end_date", endDate);
        System.out.println("api called");
        Toast.makeText(getActivity(),"latitude>>"+mLat+"longitude"+mLongi,Toast.LENGTH_LONG).show();
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().postNewJob(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_job_btn:
                validation();
                break;

            case R.id.post_job_startdate_value:
                value = "start";
                DateTimeDialogFragmentStart dialog = new DateTimeDialogFragmentStart(this);
                Bundle bundle = new Bundle();
                bundle.putInt("dialog_id", Constatnts.DATE_PICKER);
                bundle.putInt("year", calendar.get(Calendar.YEAR));
                bundle.putInt("month", calendar.get(Calendar.MONTH));
                bundle.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));

                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialogstart");

                /*try {
                    getDatePicker(R.id.post_job_startdate_value);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }*/
                break;
            case R.id.post_job_enddate_value:
                value = "end";
                DateTimeDialogFragmentEnd dialog2 = new DateTimeDialogFragmentEnd(this);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("dialog_id", Constatnts.DATE_PICKER);
                bundle2.putInt("year", calendar.get(Calendar.YEAR));
                bundle2.putInt("month", calendar.get(Calendar.MONTH));
                bundle2.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
                dialog2.setArguments(bundle2);
                dialog2.show(getFragmentManager(), "dialog");
               /* try {
                    getDatePicker(R.id.post_job_enddate_value);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }*/

                break;
        }
    }


    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
        mBtnPostJob.setEnabled(false);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
        if (listener.getTag().equals("getLatLngFromZip")) {
try {
    Gson gson = new Gson();
    ResponseBeanZipCode responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBeanZipCode.class);
    if (responseBean.getStatus().equalsIgnoreCase("OK")) {

        latitude = responseBean.getResults().get(0).getGeometry().getLocation().getLat();
        longitude = responseBean.getResults().get(0).getGeometry().getLocation().getLng();
        mLat = String.valueOf(latitude);
        mLongi = String.valueOf(longitude);
        postJob();
        System.out.println(responseBean.getResults().get(0).getFormatted_address() + " <<<<<<<<<<<<<<FO RMATTED ADDRESS");
    } else
        Toast.makeText(getActivity(), "Wrong ZipCode Entered", Toast.LENGTH_LONG).show();
}catch (Exception e){
    e.printStackTrace();
}

        }

       /* pref.edit().putString(getString(R.string.latitude), "").apply();
        pref.edit().putString(getString(R.string.longitude), "").apply();*/
        if (listener.getTag().equals("post_new_job")) {
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

                    FragmentManager fm = getActivity().getFragmentManager();
                    for (int i = fm.getBackStackEntryCount(); i > fm.getBackStackEntryCount() - 1; i--) {
                        fm.popBackStack();
                    }
                    ((BaseActivity) getActivity()).addFragmentWithBackStack(new FindAJob(), new PostJob());
                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


        int mon = mmonth + 1;
// String tag=view.getTag().toString();
        if (value.equalsIgnoreCase("start")) {
            int month = monthOfYear + 1;
            System.out.println("current---" + myear + mon + mday);
            System.out.println("start---" + year + "-" + month + "-" + dayOfMonth);
            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date current = dfDate.parse(String.valueOf(myear) + "-" + String.valueOf(mon) + "-" + String.valueOf(mday));
                Date startdate = dfDate.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth));

                if (current.equals(startdate)) {
                    Toast.makeText(getActivity(),
                            "Previous date cannot be selected!",
                            Toast.LENGTH_LONG).show();
                    mStartDate.setText("");
                } else if (startdate.before(current)) {
                    Toast.makeText(getActivity(),
                            "Previous date cannot be selected!",
                            Toast.LENGTH_LONG).show();
                    mStartDate.setText("");
                } else if (startdate.after(current)) {
                    if (month <= 9) {
                        mStartDate.setText(year + "-" + "0" + month + "-" + dayOfMonth);
                        startDate = startdate;
                    } else {
                        mStartDate.setText(year + "-" + month + "-" + dayOfMonth);
                        startDate = startdate;
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (value.equalsIgnoreCase("end")) {
            int month = monthOfYear + 1;
            System.out.println("start---" + startDate);
            System.out.println("end---" + year + "-" + month + "-" + dayOfMonth);
            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date current = dfDate.parse(String.valueOf(myear) + "-" + String.valueOf(mon) + "-" + String.valueOf(mday));

                Date endDate = dfDate.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth));
                if (startDate != null && startDate.equals(endDate)) {
                    Toast.makeText(getActivity(),
                            "Previous date cannot be selected!",
                            Toast.LENGTH_LONG).show();
                    mEndDate.setText("");
                } else if (endDate.equals(current)) {
                    Toast.makeText(getActivity(),
                            "Today's date cannot be selected!",
                            Toast.LENGTH_LONG).show();
                    mEndDate.setText("");
                } else if (startDate != null && endDate.before(startDate)) {
                    Toast.makeText(getActivity(),
                            "Previous date cannot be selected!",
                            Toast.LENGTH_LONG).show();
                    mEndDate.setText("");
                } else if (startDate != null && endDate.after(startDate)) {
                    if (month <= 9) {
                        mEndDate.setText(year + "-" + "0" + month + "-" + dayOfMonth);
                    } else {
                        mEndDate.setText(year + "-" + month + "-" + dayOfMonth);
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}
