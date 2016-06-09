package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.MessagesAdapter;
import com.itpro.buildersbackyard.adapter.OnItemClickListener;
import com.itpro.buildersbackyard.adapter.RecyclerViewAdapter;
import com.itpro.buildersbackyard.adapter.SwipeToDismissTouchListener;
import com.itpro.buildersbackyard.adapter.SwipeableItemClickListener;
import com.itpro.buildersbackyard.bean.ConversationList;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesListProduct extends Fragment implements AdapterView.OnItemClickListener, AppRequest {
    private RecyclerView mMessageList;
    private MessagesAdapter adapter;
    private Toolbar mToolbar;
    private View view;
    private SharedPreferences pref;
    private TextView message;
    private Map<String, String> mParams;

    private CircleProgressBar progress;
    private String mUserId;
    public static boolean is_open = Boolean.FALSE;

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
        updateMessage();
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void updateMessage() {
        getMessagesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_messages_list, container, false);
        is_open = Boolean.TRUE;
        getActivity().registerReceiver(reciever, new IntentFilter("msg_product"));
        inflateViews();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return view;
    }

    private void inflateViews() {
        mMessageList = (RecyclerView) view.findViewById(R.id.messages_list);
        message = (TextView) view.findViewById(R.id.message_if_empty);

// GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
// mMessageList.setLayoutManager(mLayoutManager);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageList.setLayoutManager(llm);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mMessageList.setLayoutManager(mLayoutManager);


// mMessageList.setOnItemClickListener(getActivity());


        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
                new RecyclerViewAdapter(mMessageList),
                new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(RecyclerViewAdapter view, int position) {


                    }
                });

        mMessageList.setOnTouchListener(touchListener);
        mMessageList.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        mMessageList.addOnItemTouchListener(new SwipeableItemClickListener(getActivity(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_delete) {
                            adapter.remove(position);

                        } else if (view.getId() == R.id.txt_undo) {
                            touchListener.undoPendingDismiss();
                        }
                    }
                }));

        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
       // getMessagesList();
    }

    private void getMessagesList() {

        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        mParams.put("type", "product");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {

            ApiRequests.getInstance().GetMessagesList(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

            if (responseBean.getStatus().equals("0") && responseBean.getBlockStatus() != null && responseBean.getBlockStatus().equals("1")) {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
                pref.edit().clear();
                getActivity().finish();
                Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(gotoLogin);
            } else if (responseBean.getStatus().equals("1")) {
                ArrayList<ConversationList> messageList = new ArrayList<>();
                for (ConversationList data : responseBean.getConversationsList()) {
                    messageList.add(data);
                }
                if (messageList.size() == 0) {
                    message.setVisibility(view.VISIBLE);
                    message.setText("No Conversation");
                } else {
                    message.setVisibility(view.GONE);
                    adapter = new MessagesAdapter(getActivity(), messageList, "product", MessagesListProduct.this);
                    mMessageList.setAdapter(adapter);
                }

            } else {
                Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
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

    private BroadcastReceiver reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMessagesList();
        }
    };
}
