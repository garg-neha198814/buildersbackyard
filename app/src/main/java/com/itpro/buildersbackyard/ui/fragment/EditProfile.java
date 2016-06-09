package com.itpro.buildersbackyard.ui.fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.HomeActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.ui.activity.MessagesHome;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
public class EditProfile extends Fragment implements AppRequest, View.OnClickListener {
    private EditText mFirstName, mLastName, mContact, mEmail;
    private Button mBtnEdit;
    InputMethodManager inputManager;
    private String firstName, lastName, contactNo, password, confirmPassword, email, convertedString = "", mUserId;
    private Map<String, String> mParams;
    private CircleProgressBar progress;
    private CircleImageView mProfilepic;
    MarshMallowPermission marshMallowPermission;
    private View view;
    private SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(Boolean.TRUE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        marshMallowPermission = new MarshMallowPermission(getActivity());
        inflateViews();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return view;
    }

    private void inflateViews() {
        mFirstName = (EditText) view.findViewById(R.id.first_name);
        mLastName = (EditText) view.findViewById(R.id.last_name);
        mContact = (EditText) view.findViewById(R.id.contact);
        mEmail = (EditText) view.findViewById(R.id.email);
        mBtnEdit = (Button) view.findViewById(R.id.register);
        mBtnEdit.setOnClickListener(this);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        mProfilepic = (CircleImageView) view.findViewById(R.id.profile_pic);
        mEmail.setEnabled(false);
        mProfilepic.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        getUserProfile();
    }

    private void getUserProfile() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
      //  Toast.makeText(getActivity(), "userId" + mUserId, Toast.LENGTH_LONG).show();
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().ViewProfile(getActivity(), this, mParams);
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
            Gson gson = new Gson();
            ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
            if (listener.getTag().equalsIgnoreCase("view_profile")) {
                if (responseBean.getStatus().equals("1")) {

                    mFirstName.setText(responseBean.getProfile().getFirstName());
                    mLastName.setText(responseBean.getProfile().getLastName());
                    mContact.setText(responseBean.getProfile().getPhoneNumber());
                    mEmail.setText(responseBean.getProfile().getEmail());
                    System.out.println("profile pi---" + responseBean.getProfile().getProfilePic());
                    if (responseBean.getProfile().getProfilePic() != null && responseBean.getProfile().getProfilePic().isEmpty()) {
                        mProfilepic.setBackgroundResource(R.mipmap.user);
                    } else if (responseBean.getProfile().getProfilePic().contains("http")) {
                        Picasso.with(getActivity()).load(responseBean.getProfile().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilepic);

                    } else {
                        Picasso.with(getActivity()).load(UrlConstants.BASE_URL + responseBean.getProfile().getProfilePic()).error(R.mipmap.user).placeholder(R.mipmap.user).into(mProfilepic);
                    }


                } else {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if (listener.getTag().equalsIgnoreCase("edit_profile")) {
                if (responseBean.getStatus().equals("1")) {
                    FragmentManager fm = getActivity().getFragmentManager();
                    for (int i = fm.getBackStackEntryCount(); i > fm.getBackStackEntryCount() - 1; i--) {
                        fm.popBackStack();
                    }
                    ((BaseActivity) getActivity()).addFragmentWithBackStack(new ProductList(), new EditProfile());
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

    private void validation() {
        firstName = mFirstName.getText().toString().trim();
        lastName = mLastName.getText().toString().trim();
        contactNo = mContact.getText().toString().trim();
        email = mEmail.getText().toString().trim();

        if (firstName.equals("") || lastName.equals("") || contactNo.equals("")) {
            if (firstName.equals("")) {
                mFirstName.setError(" Enter the name");
            }
            if (lastName.equals("")) {
                mLastName.setError(" Enter the lastname");
            }
            if (contactNo.equals("")) {
                mContact.setError(" Enter the contactNo");
            }


        } else {


            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("user_id", mUserId);
            mParams.put("email", email);

            mParams.put("first_name", firstName);
            mParams.put("last_name", lastName);
            mParams.put("profile_pic", convertedString);
            mParams.put("phone_number", contactNo);
            mParams.put("social_id", "");
            System.out.println("api called");
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {

                ApiRequests.getInstance().EditProfile(getActivity(), this, mParams);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }


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


                mProfilepic.setImageBitmap(image);
                mProfilepic.setBackgroundColor(getResources().getColor(
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

}
