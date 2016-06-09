package com.itpro.buildersbackyard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.MessagesData;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatArrayAdapter extends BaseAdapter implements AppRequest {

    private TextView chatText;
    private ArrayList<MessagesData> chatMessageList = new ArrayList<>();
    private LinearLayout singleMessageContainer;
    private SharedPreferences pref;
    static Context context;
    private String mUserId, SenderID;
    private Map<String, String> mParams;
    static int mPosition;
    private int deviceWidth,deviceHeight;

    public ChatArrayAdapter(Context context, ArrayList<MessagesData> chatMessageList, int deviceWidth,int deviceHeight ) {
        super();
        try {
            this.context = context;
            this.chatMessageList = chatMessageList;
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
            mUserId = pref.getString(context.getString(R.string.userId), "0");
            this.deviceWidth = deviceWidth;
            this.deviceHeight = deviceHeight;
        } catch (Exception e) {
        }
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }

        RelativeLayout Chatting = (RelativeLayout) row
                .findViewById(R.id.Chat);
        RelativeLayout ChattingData = (RelativeLayout) row
                .findViewById(R.id.singleMessageContainer);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
        ImageView image = (ImageView) row.findViewById(R.id.sendPic);
        SenderID = chatMessageList.get(position).getSenderId();
        String messageType = chatMessageList.get(position).getMessageType();
        System.out.println("device width>>>>" + deviceWidth);
        image.setMaxWidth((deviceWidth*3)/4);
        image.setMaxHeight(deviceHeight/3);

        if (messageType.equals("1")) {

            chatText.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);

            ChattingData.setBackgroundResource(checkuser() ? 0 : 0);
           /* ChattingData.setBackgroundResource(checkuser() ? R.drawable.chat_right
                    : R.drawable.chat_left);*/
            Chatting.setGravity(checkuser() ? Gravity.RIGHT : Gravity.LEFT);


            final String message_id = chatMessageList.get(position).getMessageId();
            final String conversation_id = chatMessageList.get(position).getConversationId();

            Picasso.with(context).load(UrlConstants.BASE_URL + chatMessageList.get(position).getMessage()).into(image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ToImage = new Intent(context, ViewSingleImage.class);
                    ToImage.putExtra("image", UrlConstants.BASE_URL + chatMessageList.get(position).getMessage());
                    context.startActivity(ToImage);
                }
            });

            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                    alertbox.setMessage("Are you sure you want to delete the image ?");
                    alertbox.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteMessage(message_id, conversation_id);
                            mPosition = position;
                        }
                    });
                    alertbox.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert_box_show = alertbox.create();
                    alert_box_show.show();
                    return true;
                }
            });
        }
        if (messageType.equals("0")) {
            ChattingData.setBackgroundResource(checkuser() ? R.drawable.chat_right
                    : R.drawable.chat_left);
            Chatting.setGravity(checkuser() ? Gravity.RIGHT : Gravity.LEFT);
            chatText.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            chatText.setText(chatMessageList.get(position).getMessage());

            final String message_id = chatMessageList.get(position).getMessageId();
            final String conversation_id = chatMessageList.get(position).getConversationId();
            Chatting.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                    alertbox.setMessage("Are you sure you want to delete the message ?");
                    alertbox.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMessage(message_id, conversation_id);
                            mPosition = position;
                        }
                    });
                    alertbox.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert_box_show = alertbox.create();
                    alert_box_show.show();
                    return true;
                }

            });
        }
        return row;
    }

    private void deleteMessage(String message_id, String conversation_id) {

        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("conversation_id", conversation_id);
        mParams.put("message_id", message_id);
        if (NetworkUtil.getConnectivityStatusString(context)) {
            ApiRequests.getInstance().delete_message(context, this, mParams);
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkuser() {
        if (mUserId.equals(SenderID)) {
            return true;
        }
        return false;
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
// chatMessageList.notify();
        chatMessageList.remove(mPosition);
        notifyDataSetChanged();
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
