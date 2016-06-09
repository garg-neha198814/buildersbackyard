package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.ConversationList;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.fragment.ChatFragment;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 28/10/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> implements AppRequest {

    private Activity context;
    private ArrayList<ConversationList> messagesList = new ArrayList<>();
    private SharedPreferences pref;
    private String type, mUserId,sendType;
    private Fragment fragment;
    private Map<String, String> mParams;

    public MessagesAdapter(Activity context, ArrayList<ConversationList> messagesList, String type, Fragment fragment
    ) {
        try {
            this.fragment = fragment;
            this.context = context;
            this.messagesList = messagesList;
            this.type = type;
            pref = context.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
            mUserId = pref.getString(context.getString(R.string.userId), "0");
        } catch (Exception e) {

        }
    }

    public void remove(int position) {
// current_ads.remove(position);

        deleteMessage(position);
        notifyItemRemoved(position);
        messagesList.remove(position);
    }

    private void deleteMessage(int pos) {

        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("conversation_id", messagesList.get(pos).getConversationId());
      //  Toast.makeText(context,"userid"+mUserId+"conversation_id"+messagesList.get(pos).getConversationId(),Toast.LENGTH_LONG).show();

        if (NetworkUtil.getConnectivityStatusString(context)) {
            ApiRequests.getInstance().delete_conversation(context, this, mParams);
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {

        Gson gson = new Gson();
        ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);

        if (responseBean.getStatus().equals("1")) {
            Toast.makeText(context, responseBean.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMessage,txtGeneralName;
        TextView txtName;
        TextView messageImage;
        ImageView userImage,nextPage;
        LinearLayout chat_linear;

        ViewHolder(View view) {
            super(view);

            txtMessage = (TextView) view.findViewById(R.id.user_message);
            txtName = (TextView) view.findViewById(R.id.user_name);
            messageImage = (TextView) view.findViewById(R.id.image_meessage);
            userImage = (ImageView) view.findViewById(R.id.user_pic);
            nextPage = (ImageView) view.findViewById(R.id.next_btn);
            chat_linear = (LinearLayout) view.findViewById(R.id.chat_linear);
            txtGeneralName = (TextView) view.findViewById(R.id.user_product_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_custom_item, parent, false);
        ViewHolder dataView = new ViewHolder(v);

        return dataView;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//Toast.makeText(context,"name>>>>>"+messagesList.get(position).getProduct_job_hire_name(),Toast.LENGTH_SHORT).show();
        sendType=messagesList.get(position).getType();
        if (messagesList.get(position).getMessageType().equals("1")){

            holder.messageImage.setVisibility(View.VISIBLE);
            holder.txtName.setText(messagesList.get(position).getFriendName());
            holder. txtGeneralName.setText(messagesList.get(position).getProduct_job_hire_name());

            holder.txtMessage.setVisibility(View.GONE);
            if (messagesList.get(position).getProfilePic() == null && messagesList.get(position).getProfilePic().isEmpty()) {
                Picasso.with(context).load("").placeholder(R.mipmap.messageuser).into(holder.userImage);
            } else if (messagesList.get(position).getProfilePic().contains("http")) {
                Picasso.with(context).load(messagesList.get(position).getProfilePic()).error(R.mipmap.messageuser).placeholder(R.mipmap.messageuser).into(holder.userImage);
            } else {
                Picasso.with(context).load(UrlConstants.BASE_URL + messagesList.get(position).getProfilePic()).placeholder(R.mipmap.messageuser).error(R.mipmap.messageuser).into(holder.userImage);
            }
        } else {
            holder.messageImage.setVisibility(View.GONE);
            holder.txtName.setText(messagesList.get(position).getFriendName());
            holder.txtMessage.setText(messagesList.get(position).getLastMessage());
            holder. txtGeneralName.setText(messagesList.get(position).getProduct_job_hire_name());
            if (messagesList.get(position).getProfilePic() == null && messagesList.get(position).getProfilePic().isEmpty()) {
                Picasso.with(context).load("").error(R.mipmap.messageuser).placeholder(R.mipmap.messageuser).into(holder.userImage);

            } else if (messagesList.get(position).getProfilePic().contains("http")) {
                Picasso.with(context).load(messagesList.get(position).getProfilePic()).into(holder.userImage);

            } else {
                Picasso.with(context).load(UrlConstants.BASE_URL + messagesList.get(position).getProfilePic()).error(R.mipmap.messageuser).placeholder(R.mipmap.messageuser).into(holder.userImage);
            }
        }


        holder.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putString(context.getString(R.string.conversationIdProduct), messagesList.get(position).getConversationId()).apply();
                if (mUserId.equals(messagesList.get(position).getSenderId())) {
                    pref.edit().putString(context.getString(R.string.receiverIdProduct), messagesList.get(position).getReceiverId()).apply();
                } else {
                    pref.edit().putString(context.getString(R.string.receiverIdProduct), messagesList.get(position).getSenderId()).apply();
                }
                Toast.makeText(context, "type>>>"+messagesList.get(position).getType(), Toast.LENGTH_SHORT).show();
              //  System.out.println("send type>>>>>>>>>>>>>>>>>>>>>>>"+ messagesList.get(position).getType());
                    ChatFragment fragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type",  messagesList.get(position).getType());
                    fragment.setArguments(bundle);
                    ((BaseActivity) context).replaceFragment(fragment, fragment);



            }
        });


// notifyDataSetChanged();
       /* holder.chat_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putString(context.getString(R.string.conversationIdProduct), messagesList.get(position).getConversationId()).apply();
                if (mUserId.equals(messagesList.get(position).getSenderId())) {
                    pref.edit().putString(context.getString(R.string.receiverIdProduct), messagesList.get(position).getReceiverId()).apply();
                } else {
                    pref.edit().putString(context.getString(R.string.receiverIdProduct), messagesList.get(position).getSenderId()).apply();
                }
                ChatFragment fragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                fragment.setArguments(bundle);
                ((BaseActivity) context).replaceFragment(fragment, fragment);

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

}
