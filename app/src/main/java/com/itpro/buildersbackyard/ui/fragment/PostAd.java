package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.bean.ResponseBeanZipCode;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.HomeActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class PostAd extends Fragment implements View.OnClickListener, AppRequest, OnItemSelectedListener {
    private EditText mProductPrice, mProductDesc, mProductTitle, mZipcode, mBrand;
    private ImageView mPostPic1, mPostPic2, mPostPic3, mPostPic1Success, mPostPic2Success, mPostPic3Success;
    private Spinner mPostCategory;
    private View view;
    private String randomNumber, combinedImagesPath = "", productZipcode;
    private Map<String, String> mParams;
    private CircleProgressBar progressPic1, progressPic2, progressPic3;
    private SharedPreferences pref;
    Double latitude, longitude;
    private int count = 0;
    InputMethodManager inputManager;
    private GoogleApiClient mGoogleApiClient;
    private String mCategory;
    private Button mAddPostBtn;
    private CircleProgressBar progress;
    private String mLatitude, mLongitude, mUserId,mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_post_ad, container, false);
        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inflateViews();
        setSpinnerElements();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return view;
    }

    private void setSpinnerElements() {
        // Category Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Category");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        // Creating adapter for category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPostCategory.setAdapter(categoryAdapter);
    }

    private void inflateViews() {
        mPostCategory = (Spinner) view.findViewById(R.id.spinner_add_post_category);
        //  mBtnNext = (Button) view.findViewById(R.id.next);
        mPostCategory.setOnItemSelectedListener(this);
        mAddPostBtn = (Button) view.findViewById(R.id.post_add_btn);
        mAddPostBtn.setOnClickListener(this);
        mAddPostBtn.setEnabled(true);
        mPostPic1 = (ImageView) view.findViewById(R.id.post_pic_1);
        mPostPic2 = (ImageView) view.findViewById(R.id.post_pic_2);
        mPostPic3 = (ImageView) view.findViewById(R.id.post_pic_3);
        mPostPic1Success = (ImageView) view.findViewById(R.id.post_pic_1_success);
        mPostPic2Success = (ImageView) view.findViewById(R.id.post_pic_2_success);
        mPostPic3Success = (ImageView) view.findViewById(R.id.post_pic_3_success);
        mProductTitle = (EditText) view.findViewById(R.id.p_title);
        mProductDesc = (EditText) view.findViewById(R.id.p_description);
        mProductPrice = (EditText) view.findViewById(R.id.p_price);
        mZipcode = (EditText) view.findViewById(R.id.p_zipcode);
        mBrand = (EditText) view.findViewById(R.id.p_brand);
        // mBtnNext.setEnabled(false);
        // mBtnNext.setOnClickListener(this);
        mPostPic1.setOnClickListener(this);
        mPostPic2.setOnClickListener(this);
        mPostPic3.setOnClickListener(this);
        mPostPic2.setEnabled(false);
        mPostPic3.setEnabled(false);
        // mBtnNext.setEnabled(false);
        progressPic1 = (CircleProgressBar) view.findViewById(R.id.progressBarpic1);
        progressPic2 = (CircleProgressBar) view.findViewById(R.id.progressBarpic2);
        progressPic3 = (CircleProgressBar) view.findViewById(R.id.progressBarpic3);
        progressPic1.setVisibility(View.INVISIBLE);
        progressPic2.setVisibility(View.INVISIBLE);
        progressPic3.setVisibility(View.INVISIBLE);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        randomNumber = String.valueOf(Math.random() * 100);
        pref.edit().putString(getString(R.string.randomNumber), randomNumber).apply();
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mUserId = pref.getString(getString(R.string.userId), "0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_add_btn:
                validation();
                break;
            case R.id.post_pic_1:
                pickimage(1);
                break;
            case R.id.post_pic_2:
                pickimage(2);
                break;
            case R.id.post_pic_3:
                pickimage(3);
                break;
        }
    }


    void pickimage(final int position) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
        alertbox.setMessage("Please Select The Image");
        alertbox.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        pickimage_from_gallery(position);
                    }
                });
        alertbox.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        pickimage_from_camera(position);
                    }
                });


        AlertDialog alert_box_show = alertbox.create();

        alert_box_show.show();
    }

    String sentTo = "";

    void pickimage_from_camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        sentTo = "camera";

        startActivityForResult(intent, position);

    }

    void pickimage_from_gallery(int position) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        sentTo = "gallery";
        startActivityForResult(Intent.createChooser(intent, "Select File"),
                position);

    }


    void validation() {
        String productTitle, productDescription, productPrice, productImages, brand;
        productTitle = mProductTitle.getText().toString();
        productDescription = mProductDesc.getText().toString();
        productPrice = mProductPrice.getText().toString();
        productImages = combinedImagesPath;
        brand = mBrand.getText().toString();
        productZipcode = mZipcode.getText().toString();

        if (productTitle.equals("") || productDescription.equals("") || productPrice.equals("") || productImages.equals("") || productZipcode.equals("")) {
            if (productTitle.equals("")) {
                mProductTitle.setError("Enter the product title");
            }
            if (productDescription.equals("")) {
                mProductDesc.setError("Enter the product description");
            }
            if (productPrice.equals("")) {
                mProductPrice.setError("Enter the product price");
            }
            if (productImages.equals("")) {
                Toast.makeText(getActivity(), "please add atleast one image", Toast.LENGTH_SHORT).show();
            }
            if (productZipcode.equals("")) {
                mZipcode
                        .setError("zipcode can not be empty");
            }
          /*  if (brand.equals("")) {
                mBrand
                        .setError("brand can not be empty");
            }*/
            if (mCategory.equals("")) {
                Toast.makeText(getActivity(), "Please select a  category", Toast.LENGTH_LONG).show();
            }

        } else {
            if (productZipcode.length() < 5 || productZipcode.length() > 6
                    ) {
                mZipcode
                        .setError("Enter the correct zipcode");

            }else if (mCategory.equals("Select Category")) {
                Toast.makeText(getActivity(), "Please select a  category", Toast.LENGTH_LONG).show();
            } else {
                getLatLngfromZip(productZipcode);


                // mBtnNext.setEnabled(true);
                /*PostAd2 fragment = new PostAd2(); //  object of next fragment
                Bundle bundle = new Bundle();
                bundle.putString("adTitle", productTitle);
                bundle.putString("adDescription", productDescription);
                bundle.putString("adPrice", productPrice);
                bundle.putString("adZipcode", productZipcode);


                fragment.setArguments(bundle);
                ((BaseActivity) getActivity()).addFragmentWithBackStack(fragment, new PostAd());
            }*/
            }
        }
    }

    private void addpost() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        try {
            if (sentTo.equals("camera") && resultCode == Activity.RESULT_OK) {

                Bitmap image = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(getActivity(), image);
                File finalFile = new File(getRealPathFromURI(tempUri));
                String imagePath = finalFile.toString();
                combinedImagesPath = combinedImagesPath + imagePath + ",";

                if (requestCode == 1) {


                    final String convertedString = convertBase64(imagePath);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadImage(convertedString, 1);

                        }
                    }, 1000);

                    mPostPic1.setImageBitmap(image);
                    mPostPic1.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }
                if (requestCode == 2) {


                    String convertedString = convertBase64(imagePath);
                    uploadImage(convertedString, 2);
                    mPostPic2.setImageBitmap(image);
                    mPostPic2.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }
                if (requestCode == 3) {


                    String convertedString = convertBase64(imagePath);
                    uploadImage(convertedString, 3);
                    mPostPic3.setImageBitmap(image);
                    mPostPic3.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }


            }

            if (sentTo.equals("gallery") && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                String imagePath = picturePath;
                combinedImagesPath = combinedImagesPath + imagePath + ",";
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


                if (requestCode == 1) {


                    String convertedString = convertBase64(imagePath);

                    uploadImage(convertedString, 1);
                    mPostPic1.setImageBitmap(bm);
                    mPostPic1.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }
                if (requestCode == 2) {


                    String convertedString = convertBase64(imagePath);
                    uploadImage(convertedString, 2);
                    mPostPic2.setImageBitmap(bm);
                    mPostPic2.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }
                if (requestCode == 3) {


                    String convertedString = convertBase64(imagePath);
                    uploadImage(convertedString, 3);
                    mPostPic3.setImageBitmap(bm);
                    mPostPic3.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                }

            }
        } catch (Exception e) {
            Log.d("hello", "" + e.getLocalizedMessage());
        }

    }

    private void uploadImage(String imageSelectedPath, int countvalue) {
        try {
            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("random_string", randomNumber);

            mParams.put("type", "product");
            mParams.put("product_image", imageSelectedPath);
            System.out.println("api called");
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {
                count = countvalue;
                ApiRequests.getInstance().uploadImage(getActivity(), this, mParams);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }

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


    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        if (listener.getTag().equalsIgnoreCase("upload_image")) {

            try {
                mAddPostBtn.setEnabled(false);
                mPostPic1.setEnabled(false);
                mPostPic2.setEnabled(false);
                mPostPic3.setEnabled(false);
                if (count == 0) {
                    progressPic1.setVisibility(view.INVISIBLE);
                    progressPic2.setVisibility(view.INVISIBLE);
                    progressPic3.setVisibility(view.INVISIBLE);
                }
                if (count == 1) {
                    progressPic1.setVisibility(view.VISIBLE);
                }
                if (count == 2) {
                    progressPic2.setVisibility(view.VISIBLE);
                }
                if (count == 3) {
                    progressPic3.setVisibility(view.VISIBLE);
                }

                Log.d("started", "started");
            }  catch (Exception e) {

            }
        } else {
            progress.setVisibility(view.VISIBLE);
        }
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        if (listener.getTag().equalsIgnoreCase("upload_image")) {
            try {
                progressPic1.setVisibility(view.GONE);
                progressPic2.setVisibility(view.GONE);
                progressPic3.setVisibility(view.GONE);
                // mBtnNext.setEnabled(true);
                // progress.set
                Gson gson = new Gson();

                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    mAddPostBtn.setEnabled(true);
                    if (count == 0) {
                        mPostPic1Success.setVisibility(view.INVISIBLE);
                        mPostPic2Success.setVisibility(view.INVISIBLE);
                        mPostPic3Success.setVisibility(view.INVISIBLE);
                    }
                    if (count == 1) {


                        mPostPic1Success.setVisibility(view.VISIBLE);
                        mPostPic1Success.setBackgroundResource(R.mipmap.green_click);
                        mPostPic2.setEnabled(true);
                    }
                    if (count == 2) {
                        mPostPic2Success.setVisibility(view.VISIBLE);
                        mPostPic2Success.setBackgroundResource(R.mipmap.green_click);
                        mPostPic3.setEnabled(true);
                    }
                    if (count == 3) {
                        mPostPic3Success.setVisibility(view.VISIBLE);
                        mPostPic3Success.setBackgroundResource(R.mipmap.green_click);
                    }


                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
        if (listener.getTag().equalsIgnoreCase("add_post")) {
            try {
                progress.setVisibility(view.INVISIBLE);
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
                    for (int i = fm.getBackStackEntryCount(); i >= fm.getBackStackEntryCount() - 1; i--) {
                        fm.popBackStack();
                    }
                    ((BaseActivity) getActivity()).addFragmentWithBackStack(new ProductList(), new PostAd());
                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (listener.getTag().equals("getLatLngFromZip")) {
            try {
                Gson gson = new Gson();
                ResponseBeanZipCode responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBeanZipCode.class);
                if (responseBean.getStatus().equalsIgnoreCase("OK")) {

                    latitude = responseBean.getResults().get(0).getGeometry().getLocation().getLat();
                    longitude = responseBean.getResults().get(0).getGeometry().getLocation().getLng();
                    mLocation = responseBean.getResults().get(0).getFormatted_address();
                    System.out.println("loaction name>>>>>>>>>>>>>>>." + mLocation);
                    mLatitude = String.valueOf(latitude);
                    mLongitude = String.valueOf(longitude);
                    mParams = new HashMap();
                    mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
                    mParams.put("user_id", mUserId);
                    mParams.put("name", mProductTitle.getText().toString());
                    mParams.put("price", mProductPrice.getText().toString());
                    mParams.put("decription", mProductDesc.getText().toString());
                    mParams.put("zipcode", productZipcode);
                    mParams.put("category", mCategory);
                    mParams.put("brand", mBrand.getText().toString());
                    mParams.put("random_string", pref.getString(getString(R.string.randomNumber), "0"));
                    mParams.put("location", mLocation);
                    mParams.put("latitude", mLatitude);
                    mParams.put("longitude", mLongitude);
                    System.out.println("api called");
                    if (NetworkUtil.getConnectivityStatusString(getActivity())) {

                        ApiRequests.getInstance().addPost(getActivity(), this, mParams);

                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                    System.out.println(responseBean.getResults().get(0).getFormatted_address() + " <<<<<<<<<<<<<<FO RMATTED ADDRESS");
                } else {
                    progress.setVisibility(view.GONE);
                    Toast.makeText(getActivity(), "Wrong ZipCode Entered", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        if (listener.getTag().equalsIgnoreCase("upload_image")) {
            try {
                Log.d("failed", "failed");
                progressPic1.setVisibility(view.GONE);
                progressPic2.setVisibility(view.GONE);
                progressPic3.setVisibility(view.GONE);
                if (count == 0) {
                    mPostPic1Success.setVisibility(view.INVISIBLE);
                    mPostPic2Success.setVisibility(view.INVISIBLE);
                    mPostPic3Success.setVisibility(view.INVISIBLE);
                }
                if (count == 1) {
                    mPostPic1Success.setVisibility(view.VISIBLE);
                    mPostPic1Success.setBackgroundResource(R.mipmap.unloaded);
                }
                if (count == 2) {
                    mPostPic2Success.setVisibility(view.VISIBLE);
                    mPostPic2Success.setBackgroundResource(R.mipmap.unloaded);
                }
                if (count == 3) {
                    mPostPic3Success.setVisibility(view.VISIBLE);
                    mPostPic3Success.setBackgroundResource(R.mipmap.unloaded);
                }


            } catch (Exception e) {


            }
        } else {
            progress.setVisibility(view.INVISIBLE);
        }
    }

    private void getLatLngfromZip(String mAdZipcode) {

        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getLatLngFromZip(getActivity(), this, mParams, mAdZipcode);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_add_post_category:
                mCategory = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}