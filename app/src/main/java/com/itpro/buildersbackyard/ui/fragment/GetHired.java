package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.itpro.buildersbackyard.utils.MarshMallowPermission;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.itpro.buildersbackyard.R.id.get_hired_pic;

public class GetHired extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AppRequest, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,  DatePickerDialog.OnDateSetListener {
    private EditText mHiredName, mHiredTrade, mHiredCertifications, mHiredSpecialization, mZipCode;
    private Button mBtnPost;
    private View view;
    private CircleImageView mProfilePic;


    private CircleProgressBar progress;
    private static final String TAG = "google places";
    private int flag, addressSelected = 0;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247,
            151.383362));
    private TextView mAvailability;
    private String mName, mSpecialities, mtrade, mCertifications, mViewHiredStatus = "0", mLatitude = "", mLongitude = "", mUserId, convertedString = "", selectedDate = "", date = "";
    private SharedPreferences pref;
    private Map<String, String> mParams;
    private int myear, mmonth, mday, hour, minute;
    int startDay, startMonth, startYear;
    boolean pastDateSelected;
    private Calendar calendar;
    public static boolean isCamera = Boolean.FALSE;
    MarshMallowPermission marshMallowPermission;
    Double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_get_hired, container, false);
        marshMallowPermission = new MarshMallowPermission(getActivity());

        inflateViews();
        getHiredStatus();
        //autocompleteaddress();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return view;
    }

    private void getHiredStatus() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);

        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().getHiredStatus(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    private void inflateViews() {
        mHiredName = (EditText) view.findViewById(R.id.hired_edt_name);
        mHiredTrade = (EditText) view.findViewById(R.id.hired_edt_trade);
        mHiredCertifications = (EditText) view.findViewById(R.id.hired_edt_certifications);
        mHiredSpecialization = (EditText) view.findViewById(R.id.hired_edt_specialities);
        mZipCode = (EditText) view.findViewById(R.id.add_post_location);
        mProfilePic = (CircleImageView) view.findViewById(get_hired_pic);
        mProfilePic.setOnClickListener(this);
        mBtnPost = (Button) view.findViewById(R.id.hired_post_btn);
        mBtnPost.setOnClickListener(this);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mAvailability = (TextView) view.findViewById(R.id.post_job_startdate_value);
        mAvailability.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        pref.edit().putString(getString(R.string.latitude), "").apply();
        pref.edit().putString(getString(R.string.longitude), "").apply();
        mUserId = pref.getString(getString(R.string.userId), "0");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        calendar = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hired_post_btn:
                post();
                break;
            case R.id.post_job_startdate_value:
                GetHired_DatePicker dialog = new GetHired_DatePicker(this);
                Bundle bundle = new Bundle();
                bundle.putInt("dialog_id", Constatnts.DATE_PICKER);
                bundle.putInt("year", calendar.get(Calendar.YEAR));
                bundle.putInt("month", calendar.get(Calendar.MONTH));
                bundle.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));

                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialogstart");
                break;
            case R.id.get_hired_pic:
                pickimage();
        }
    }

    void pickimage() {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
        alertbox.setMessage("Please Select The Image");
        alertbox.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        pickimage_from_gallery();
                    }
                });
        alertbox.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        pickimage_from_camera();
                    }
                });


        AlertDialog alert_box_show = alertbox.create();

        alert_box_show.show();
    }

    void pickimage_from_camera() {
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                startActivityForResult(intent, 1);

            }
        }
    }

    void pickimage_from_gallery() {

        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select File"),
                    2);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        convertedString = "";
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                Bitmap image = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(getActivity(), image);
                File finalFile = new File(getRealPathFromURI(tempUri));
                String imagePath = finalFile.toString();


                convertedString = convertBase64(imagePath);
                pref.edit().putString(getString(R.string.ConvertedString), convertedString).apply();

                mProfilePic.setImageBitmap(image);
                mProfilePic.setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));


            }

            if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                String imagePath = picturePath;
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(imagePath, options);


                convertedString = convertBase64(imagePath);
                pref.edit().putString(getString(R.string.ConvertedString), convertedString).apply();


                mProfilePic.setImageBitmap(bm);
                mProfilePic.setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));
            }


        } catch (Exception e) {
            Log.d("hello", "" + e.getLocalizedMessage());
        }

    }

    public Uri getImageUri(Activity inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    /* Get current path of captured image */
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    String convertBase64(String fileName) {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;

    }

    String availabilty, location;


    void post() {
        availabilty = mAvailability.getText().toString();
        location = mZipCode.getText().toString();
       /* mLatitude = pref.getString(getString(R.string.latitude), "0");
        mLongitude = pref.getString(getString(R.string.longitude), "0");*/
        mName = mHiredName.getText().toString().trim();
        mtrade = mHiredTrade.getText().toString().trim();
        mCertifications = mHiredCertifications.getText().toString().trim();
        mSpecialities = mHiredSpecialization.getText().toString().trim();
        if (mAvailability.equals("") || mName.equals("") || mtrade.equals("") || mSpecialities.equals("") || location.equals("")) {
            if (mAvailability.equals("")) {
                mAvailability.setError("Enter The Availability");
            }
            if (mName.equals("")) {
                mHiredName.setError("Enter The Name");
            }
            if (mtrade.equals("")) {
                mHiredTrade.setError("Enter The Trade");
            }
            if (mSpecialities.equals("")) {
                mHiredSpecialization.setError("Enter the Specialities");
            }

            if (location.equals("")) {
                mZipCode.setError("Enter the zipcode");
            }
            /*if (mLatitude.equals("") || mLongitude.equals("")) {


                System.out.println("iffff");
                Address address = GetLatLong.getAddressFromLocation(location, getActivity());
                System.out.println("address" + address);
                if(address==null) {
                    mAutocompleteView.setError("Enter The Correct Location");
                    Toast.makeText(getActivity(), "Unable To Fetch The Location Please Enter The Correct Location and Check your Internet Connection", Toast.LENGTH_LONG).show();
                }
                else{
                    mLatitude = String.valueOf(address.getLatitude());
                    mLongitude = String.valueOf(address.getLongitude());

                    pref.edit().putString(getString(R.string.latitude), mLatitude).apply();
                    pref.edit().putString(getString(R.string.longitude), mLongitude).apply();

                 getHired();

                }



            }*/
        } else {
            if (location.length() < 5 || location.length() > 6
                    ) {
                mZipCode
                        .setError("Enter the correct zipcode");

            } else {
                getLatLngfromZip();
            }
        }
    }

    private void getLatLngfromZip() {

        mParams = new HashMap();
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getLatLngFromZip(getActivity(), this, mParams, location);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void getHired() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("name", mName);
        mParams.put("trade", mtrade);
        mParams.put("specialties", mSpecialities);
        mParams.put("certification", mCertifications);
        mParams.put("location", mZipCode.getText().toString());
        mParams.put("latitude", mLatitude);
        mParams.put("longitude", mLongitude);
        mParams.put("availability", availabilty);
        mParams.put("profile_pic", convertedString);
        mParams.put("view_hired", mViewHiredStatus);

        System.out.println("api called");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().addpostgethired(getActivity(), this, mParams);
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
        try {
            progress.setVisibility(View.GONE);
            if (listener.getTag().equalsIgnoreCase("get_hired_status")) {
                Gson gson = new Gson();

                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    if (responseBean.getGetHiredStatus().equals("1")) {
                        mViewHiredStatus = "1";
                        mHiredName.setEnabled(false);
                        mHiredTrade.setEnabled(false);
                        mHiredCertifications.setEnabled(false);
                        mZipCode.setEnabled(false);
                        mHiredSpecialization.setEnabled(false);
                        pref.edit().putString(getString(R.string.latitude), responseBean.getViewHired().getLatitude()).apply();
                        pref.edit().putString(getString(R.string.longitude), responseBean.getViewHired().getLongitude()).apply();
                        mHiredName.setText(responseBean.getViewHired().getName());
                        mHiredTrade.setText(responseBean.getViewHired().getTrade());
                        if (responseBean.getViewHired().getCertification().equals("")) {
                            mHiredCertifications.setText("");
                        } else {
                            mHiredCertifications.setText(responseBean.getViewHired().getCertification());
                        }
                        if (responseBean.getViewHired().getProfilePic() != null) {
                            if (responseBean.getViewHired().getProfilePic().contains("http")) {
                                Picasso.with(getActivity()).load(responseBean.getViewHired().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilePic);
                            } else {
                                Picasso.with(getActivity()).load(UrlConstants.BASE_URL + responseBean.getViewHired().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilePic);
                            }

                        }
                        mZipCode.setText(responseBean.getViewHired().getLocation());
                        mHiredSpecialization.setText(responseBean.getViewHired().getSpecialities());
                        mAvailability.setText(responseBean.getViewHired().getAvailability());
                    } else {
                        mViewHiredStatus = "0";
                        mHiredName.setEnabled(true);
                        mHiredTrade.setEnabled(true);
                        mHiredCertifications.setEnabled(true);
                        mZipCode.setEnabled(true);
                        mHiredSpecialization.setEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if (listener.getTag().equalsIgnoreCase("add_post_get_hired")) {
                Gson gson = new Gson();

                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    pref.edit().putString(getString(R.string.latitude), "").apply();
                    pref.edit().putString(getString(R.string.longitude), "").apply();

                    if (responseBean.getViewHired().getProfilePic() != null) {
                        if (responseBean.getViewHired().getProfilePic().contains("http")) {
                            Picasso.with(getActivity()).load(responseBean.getProfile().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilePic);
                        } else {
                            Picasso.with(getActivity()).load(UrlConstants.BASE_URL + responseBean.getViewHired().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilePic);
                        }

                    }

                    ((BaseActivity) getActivity()).addFragmentWithBackStack(new ForHire(), new GetHired());

                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if (listener.getTag().equals("getLatLngFromZip")) {

                Gson gson = new Gson();
                ResponseBeanZipCode responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBeanZipCode.class);
                if (responseBean.getStatus().equalsIgnoreCase("OK")) {

                    latitude = responseBean.getResults().get(0).getGeometry().getLocation().getLat();
                    longitude = responseBean.getResults().get(0).getGeometry().getLocation().getLng();
                    mLatitude = String.valueOf(latitude);
                    mLongitude = String.valueOf(longitude);
                    getHired();
                    System.out.println(responseBean.getResults().get(0).getFormatted_address() + " <<<<<<<<<<<<<<FO RMATTED ADDRESS");
                } else
                    Toast.makeText(getActivity(), "Wrong ZipCode Entered", Toast.LENGTH_LONG).show();

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;

        int mon = mmonth + 1;
        System.out.println("current---" + myear + mon + mday);
        System.out.println("selected---" + year + "-" + month + "-" + dayOfMonth);
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current = dfDate.parse(String.valueOf(myear) + "-" + String.valueOf(mon) + "-" + String.valueOf(mday));
            Date selected = dfDate.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth));
            if (current.equals(selected)) {
                Toast.makeText(getActivity(),
                        "Previous date cannot be selected!",
                        Toast.LENGTH_LONG).show();
                mAvailability.setText("");
            } else if (selected.before(current)) {
                Toast.makeText(getActivity(),
                        "Previous date cannot be selected!",
                        Toast.LENGTH_LONG).show();
                mAvailability.setText("");
            } else if (selected.after(current)) {
                if (month <= 9) {
                    mAvailability.setText(year + "-" + "0" + month + "-" + dayOfMonth);
                } else {
                    mAvailability.setText(year + "-" + month + "-" + dayOfMonth);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}