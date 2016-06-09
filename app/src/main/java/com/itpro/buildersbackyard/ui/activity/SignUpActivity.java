package com.itpro.buildersbackyard.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.MarshMallowPermission;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, AppRequest {
    private EditText mFirstName, mLastName, mContact, mEmail, mPassword, mConfirmPassword;
    private Button mBtnSignUp;
    InputMethodManager inputManager;
    private String firstName, lastName, contactNo, password, confirmPassword, email, convertedString = "";
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private CircleImageView mProfilepic;
    MarshMallowPermission marshMallowPermission;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setCustomFont();
        marshMallowPermission = new MarshMallowPermission(this);
        inflateViews();
        errFunction();
        inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }

    private void setCustomFont() {
        TextView txt = (TextView) findViewById(R.id.splash_label);
        Typeface font = Typeface.createFromAsset(getAssets(), "customfont.ttf");
        txt.setTypeface(font);
    }


    private void inflateViews() {
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mContact = (EditText) findViewById(R.id.contact);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mBtnSignUp = (Button) findViewById(R.id.register);
        mBtnSignUp.setOnClickListener(this);
        progress = (CircleProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mProfilepic = (CircleImageView) findViewById(R.id.profile_pic);
        mProfilepic.setOnClickListener(this);
        pref = this.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
    }


    public void errFunction() {

        mFirstName
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {

                            inputManager.hideSoftInputFromWindow(
                                    v.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            firstName = mFirstName.getText().toString();


                            if (firstName.equals("")) {
                                System.out.println("<<<<<<< EMPTY >>>>>>>>");
                                mFirstName
                                        .setError("Name can not be empty");

                            } else if (firstName.length() < 3 || firstName.length() > 20) {
                                mFirstName
                                        .setError("Name must be between 3 to 20 characters");
                            } else if (!firstName.matches("[a-zA-Z\\s]+")) {
                                mFirstName.setError("Name is invalid");
                            } else {


                                mFirstName.setError(null);

                            }

                        }
                    }
                });

        mLastName
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {

                            inputManager.hideSoftInputFromWindow(
                                    v.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            lastName = mLastName.getText().toString();


                            if (lastName.equals("")) {
                                System.out.println("<<<<<<< EMPTY >>>>>>>>");
                                mLastName
                                        .setError("Name can not be empty");

                            } else if (lastName.length() < 3 || lastName.length() > 20) {
                                mLastName
                                        .setError("Name must be between 3 to 20 characters");
                            } else if (!lastName.matches("[a-zA-Z\\s]+")) {
                                mLastName.setError("Name is invalid");
                            } else {


                                mLastName.setError(null);

                            }

                        }
                    }
                });


        mContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    contactNo = mContact.getText().toString();

                    if (contactNo.equals("")) {
                        mContact.setError("No spaces are allowed");
                    } else if (contactNo.contains(" ")) {
                        mContact.setError("Space not allowed");
                    } else if (contactNo.length() < 10 || contactNo.length() > 14) {
                        mContact.setError("Invalid Number");
                    } else {


                        mContact.setError(null);

                    }

                }
            }
        });

        mEmail
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            inputManager.hideSoftInputFromWindow(
                                    v.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            email = mEmail.getText().toString();

                            if (email.equals("")) {
                                mEmail
                                        .setError("No spaces are allowed");
                            } else if (email.contains(" ")) {
                                mEmail.setError("Space not allowed");
                            } else if (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                                mEmail.setError("Email is invalid");
                            } else {


                                mEmail.setError(null);

                            }

                        }
                    }
                });

        mPassword
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            inputManager.hideSoftInputFromWindow(
                                    v.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            password = mPassword.getText().toString();

                            if (password.equals("")) {
                                mPassword
                                        .setError("Password can not be empty");
                            } else if (password.contains(" ")) {
                                mPassword.setError("No spaces allowed");
                            } else if (password.length() < 8
                                    || password.length() > 20) {
                                mPassword
                                        .setError("Password must be between 8 to 20");

                            } else {


                                mPassword.setError(null);

                            }

                        }
                    }
                });

        mConfirmPassword
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            inputManager.hideSoftInputFromWindow(
                                    v.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            confirmPassword = mConfirmPassword
                                    .getText().toString();

                            password = mConfirmPassword.getText().toString();
                            if (!confirmPassword.equals(password)
                                    ) {

                                mConfirmPassword
                                        .setError("These passwords don't match. Try again");
                            } else if (confirmPassword.equalsIgnoreCase("")) {
                                mConfirmPassword
                                        .setError("Please enter password.");
                            } else {


                                mConfirmPassword.setError(null);

                            }
                        }
                    }
                });


    }

    void validation() {

        firstName = mFirstName.getText().toString().trim();
        lastName = mLastName.getText().toString().trim();
        contactNo = mContact.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        confirmPassword = mConfirmPassword.getText().toString().trim();
        if (firstName.equals("") || lastName.equals("") || contactNo.equals("") || password.equals("") || confirmPassword.equals("")) {
            if (firstName.equals("")) {
                mFirstName.setError(" Enter the name");
            }
            if (lastName.equals("")) {
                mLastName.setError(" Enter the lastname");
            }
            if (contactNo.equals("")) {
                mContact.setError(" Enter the contactNo");
            }
            if (email.equals("")) {
                mEmail.setError(" Enter the email");
            }
            if (password.equals("")) {
                mPassword.setError(" Enter the password");
            }
            if (confirmPassword.equals("")) {
                mConfirmPassword.setError(" Enter the confirm password");
            }

        } else {
            if (!password.equals(confirmPassword)) {
                mPassword.setError(" password and confirm password should be same");
            } else {

                mParams = new HashMap();
                mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
                mParams.put("is_social", "0");
                mParams.put("email", email);
                mParams.put("password", password);
                mParams.put("first_name", firstName);
                mParams.put("last_name", lastName);
                mParams.put("profile_pic", convertedString);
                mParams.put("phone_number", contactNo);
                mParams.put("social_id", "");
                System.out.println("api called");
                if (NetworkUtil.getConnectivityStatusString(this)) {

                    ApiRequests.getInstance().userRegistration(this, this, mParams);
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }


            }
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:

                validation();
                break;
            case R.id.profile_pic:

                pickimage();
                break;
        }
    }

    void pickimage() {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(SignUpActivity.this);
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

                Uri tempUri = getImageUri(this, image);
                File finalFile = new File(getRealPathFromURI(tempUri));
                String imagePath = finalFile.toString();


                convertedString = convertBase64(imagePath);


                mProfilepic.setImageBitmap(image);
                mProfilepic.setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));


            }

            if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
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


                mProfilepic.setImageBitmap(bm);
                mProfilepic.setBackgroundColor(getResources().getColor(
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
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent gotoLogin = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(gotoLogin);
        finish();
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        try {
            if (listener.getTag().equalsIgnoreCase("Use_reg")) {
                progress.setVisibility(View.GONE);
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("1")) {
                    //    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    pref.edit().putString(getString(R.string.userId), responseBean.getUserId()).apply();
                    //   Toast.makeText(this, "userid"+responseBean.getUserId(), Toast.LENGTH_LONG).show();
                    pref.edit().putString(getString(R.string.loginStatus), "login").apply();
                    System.out.println("userrrrr id---" + responseBean.getDetails().getUserId());

                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    this.finish();

                } else {
                    Toast.makeText(this, responseBean.getMessage(), Toast.LENGTH_LONG).show();
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