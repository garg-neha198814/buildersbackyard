package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Notification_List;
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
import java.util.Map;

/**
 * Created by root on 28/10/15.
 */
public class NotificationAdapter extends BaseAdapter implements AppRequest {
    private Activity context;
    int flag;
    static int mPosition;
    private Map<String, String> mParams;
    public String mUserId, convertedString, imagePath, ownerid;
    public static boolean is_open = Boolean.FALSE;
    public SharedPreferences pref;
    int[] notificationListBackground = new int[]{R.drawable.notificationlistbackground1,
            R.drawable.notificationlistbackground2};
    ArrayList<Notification_List> notificationList = new ArrayList<>();

    public NotificationAdapter(Activity context, ArrayList<Notification_List> notificationList
    ) {
        try {
            this.notificationList = notificationList;
            this.context = context;
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
            mUserId = pref.getString(context.getString(R.string.userId), "0");
        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // System.out.println("position Adapter" + position);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = view;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.notification_custom_item, null, true);
        }
        int listItemBackgroundPosition = position % notificationListBackground.length;
        rowView.setBackgroundResource(notificationListBackground[listItemBackgroundPosition]);
        TextView txtMessage = (TextView) rowView
                .findViewById(R.id.notification_message);
        TextView txtTime = (TextView) rowView
                .findViewById(R.id.notification_time);
        ImageView btnDelete = (ImageView) rowView
                .findViewById(R.id.notification_delete);
        String list[] = notificationList.get(position).getCreated_at().split(" ");
        txtMessage.setText(notificationList.get(position).getAlert());
        txtTime.setText(list[0]);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification(notificationList.get(position).getNotification_id());
                mPosition = position;
            }
        });

        notifyDataSetChanged();

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }

    public void deleteNotification(String id) {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("notification_id", id);
        if (NetworkUtil.getConnectivityStatusString(context)) {
            ApiRequests.getInstance().DeleteNotification(context, this, mParams);
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        System.out.println("in started");
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        Gson gson = new Gson();
        ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

        if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
            Toast.makeText(context, responseBean.getMessage(), Toast.LENGTH_LONG).show();
            pref.edit().clear();
            context.finish();
            Intent gotoLogin = new Intent(context, LoginActivity.class);
            context.startActivity(gotoLogin);
        } else if (responseBean.getStatus().equals("1")) {
          //  Toast.makeText(context, "" + responseBean.getStatus(), Toast.LENGTH_LONG).show();
            notificationList.remove(mPosition);
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "" + responseBean.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        System.out.println("in started");
    }
}