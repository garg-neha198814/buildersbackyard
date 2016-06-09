package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.ChatArrayAdapter;
import com.itpro.buildersbackyard.bean.MessagesData;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatFragment extends Fragment implements AppRequest, View.OnClickListener {
    private static final String TAG = "ChatFragment";

    private ChatArrayAdapter mAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageButton buttonSend, buttonCamera;
    private Toolbar mToolbar;
    private Bitmap image;
    private SharedPreferences pref;
private  int mWidth,mHeight;
    int flag;
    private View view;
    private Map<String, String> mParams;

    String mUserId, convertedString, imagePath, ownerid;
    private CircleProgressBar progress;
    public static boolean is_open = Boolean.FALSE;
    MarshMallowPermission marshMallowPermission;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat, container, false);

        is_open = Boolean.TRUE;
        marshMallowPermission = new MarshMallowPermission(getActivity());
        inflateViews();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().registerReceiver(reciever, new IntentFilter("msg_chat"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
// getActivity().unregisterReceiver(reciever);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        is_open = Boolean.TRUE;
    }

    @Override
    public void onStart() {
        super.onStart();
        is_open = Boolean.TRUE;
    }

    @Override
    public void onResume() {
        super.onResume();
        is_open = Boolean.TRUE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(reciever);
        is_open = Boolean.FALSE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        is_open = Boolean.FALSE;
    }

    @Override
    public void onPause() {
        super.onPause();
        is_open = Boolean.FALSE;
    }


    void inflateViews() {
        Intent i = getActivity().getIntent();
        buttonSend = (ImageButton) view.findViewById(R.id.imageButton_send);
        listView = (ListView) view.findViewById(R.id.listView1);
        chatText = (EditText) view.findViewById(R.id.chatText);
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        buttonCamera = (ImageButton) view.findViewById(R.id.imageButton_camera);
        buttonCamera.setOnClickListener(this);
        buttonSend.setOnClickListener(this);

        getMessages(getArguments().getString("type", "0"));


    }


    private void sendMessage() {

        String message;
        message = chatText.getText().toString();
        if (message.equals("")) {
            Toast.makeText(getActivity(), "Please Enter the mesaage", Toast.LENGTH_LONG).show();

        } else {
            String type = getArguments().getString("type");

            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("sender_id", mUserId);
            mParams.put("receiver_id", pref.getString(getString(R.string.receiverIdProduct), "0"));
            mParams.put("conversation_id", pref.getString(getString(R.string.conversationIdProduct), "0"));
            mParams.put("message", chatText.getText().toString());
            mParams.put("message_type", "0");

            mParams.put("type", getArguments().getString("type"));
            Toast.makeText(getActivity(),"type>>send message"+getArguments().getString("type"),Toast.LENGTH_LONG).show();
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {
                flag = 1;
                ApiRequests.getInstance().SendMessage(getActivity(), this, mParams);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void sendPic() {

        String message;
        message = convertedString;
        if (message.equals("")) {
            Toast.makeText(getActivity(), "Please Select a pic", Toast.LENGTH_LONG).show();

        } else {

            mParams = new HashMap();
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("sender_id", mUserId);
            mParams.put("receiver_id", pref.getString(getString(R.string.receiverIdProduct), "0"));
            mParams.put("conversation_id", pref.getString(getString(R.string.conversationIdProduct), "0"));
            mParams.put("message", message);
            mParams.put("message_type", "1");
            mParams.put("type", getArguments().getString("type"));
            if (NetworkUtil.getConnectivityStatusString(getActivity())) {

                ApiRequests.getInstance().SendMessage(getActivity(), this, mParams);
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


            if (listener.getTag().equalsIgnoreCase("send_message")) {

                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {
                    getMessages(getArguments().getString("type"));
                } else {
                    Toast.makeText(getActivity(), "" + responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                    System.out.println(mUserId + "<<<<<<<<<<<<<<<USERID");
                    Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                    pref.edit().clear();
                    getActivity().finish();
                    Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                } else if (responseBean.getStatus().equals("1")) {


                    ArrayList<MessagesData> messages = new ArrayList<>();
                    for (MessagesData data : responseBean.getMessages()) {
                        messages.add(data);
                    }
                    mAdapter = new ChatArrayAdapter(getActivity(), messages, mWidth, mHeight);
                    listView.setAdapter(mAdapter);
                    listView.invalidateViews();
                    listView.setSelection(mAdapter.getCount() - 1);
                    chatText.setText("");


                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getMessages(String type) {
        String conversationId = pref.getString(getString(R.string.conversationIdProduct), "0");
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("conversation_id", conversationId);
        mParams.put("type", type);
        mParams.put("user_id", mUserId);
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().GetMessages(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {

        try{
            progress.setVisibility(View.GONE);
        }catch(Exception e){
            e.printStackTrace();
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


                sendPic();


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
                sendPic();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_send:
                sendMessage();

                break;
            case R.id.imageButton_camera:
               pickimage();

        }

    }

    private BroadcastReceiver reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMessages(getArguments().getString("type"));
        }
    };
}