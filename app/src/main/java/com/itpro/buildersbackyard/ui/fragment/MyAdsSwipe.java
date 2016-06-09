package com.itpro.buildersbackyard.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.adapter.MyAds_Adapter;
import com.itpro.buildersbackyard.adapter.OnItemClickListener;
import com.itpro.buildersbackyard.adapter.RecyclerViewAdapter;
import com.itpro.buildersbackyard.adapter.SwipeToDismissTouchListener;
import com.itpro.buildersbackyard.adapter.SwipeableItemClickListener;
import com.itpro.buildersbackyard.bean.MyAdds_Data;
import com.itpro.buildersbackyard.bean.ResponseBean;
import com.itpro.buildersbackyard.io.http.ApiRequests;
import com.itpro.buildersbackyard.io.http.BaseTask;
import com.itpro.buildersbackyard.io.listener.AppRequest;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.activity.LoginActivity;
import com.itpro.buildersbackyard.utils.Constatnts;
import com.itpro.buildersbackyard.utils.NetworkUtil;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class MyAdsSwipe extends Fragment implements AppRequest {
    private View view;
    private MyBaseAdapter adapter;
    private RecyclerView recyclerView;
    private Map<String, String> mParams;
    private SharedPreferences pref;
    public String mUserId;
    private CircleProgressBar progress;
   private ArrayList<MyAdds_Data> current_ads;
    TextView message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view_activity, container, false);
        inFlateView();
        getMyAdds();
        return view;
    }

    public void getMyAdds() {
        mParams = new HashMap();
        mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
        mParams.put("user_id", mUserId);
        System.out.println("api called");
        if (NetworkUtil.getConnectivityStatusString(getActivity())) {
            ApiRequests.getInstance().getMyAdds(getActivity(), this, mParams);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    void inFlateView()

    {
        pref = getActivity().getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(getString(R.string.userId), "0");
        message = (TextView) view.findViewById(R.id.message_if_empty);
        progress = (CircleProgressBar) view.findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);


        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(recyclerView),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {


                            }
                        });

        recyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        recyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(getActivity(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_delete) {
                            adapter.remove(position, current_ads.get(position).getProduct_id());

                        } else if (view.getId() == R.id.txt_undo) {
                            touchListener.undoPendingDismiss();
                        }
                        else if (view.getId() == R.id.txt_edit) {
                            adapter.ediy(position);
                            //touchListener.undoPendingDismiss();
                        }

                        else { // R.id.txt_data

                        }
                    }
                }));
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);

        Gson gson = new Gson();
        ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
        if(responseBean.getStatus().equals("0") && responseBean.getBlockStatus()!=null&&responseBean.getBlockStatus().equals("1")){
            Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
            pref.edit().clear();
            getActivity().finish();
            Intent gotoLogin = new Intent(getActivity(),LoginActivity.class);
            startActivity(gotoLogin);
        }
        else if (responseBean.getStatus().equals("1")) {
            current_ads = new ArrayList<>();
            for (MyAdds_Data data : responseBean.getCurrent_add()) {
                current_ads.add(data);
            }

                message.setVisibility(view.GONE);
                adapter = new MyBaseAdapter(getActivity(), current_ads);
                recyclerView.setAdapter(adapter);


        } else {
            message.setVisibility(view.VISIBLE);
            message.setText("No Products");
           // Toast.makeText(getActivity(), responseBean.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {
        progress.setVisibility(View.GONE);
    }

    static class MyBaseAdapter extends RecyclerView.Adapter<MyBaseAdapter.MyViewHolder> implements AppRequest {
        private Map<String, String> mParams;
        private static final int SIZE = 100;
        Activity activity;
        // private final List<String> mDataSet = new ArrayList<>();
        private SharedPreferences pref;
        String mUserId;
        ArrayList<MyAdds_Data> current_ads;

        MyBaseAdapter(Activity activity, ArrayList<MyAdds_Data> current_ads) {
            this.current_ads = current_ads;
            this.activity = activity;
            pref = activity.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
            mUserId = pref.getString(activity.getString(R.string.userId), "0");
           /* for (int i = 0; i < SIZE; i++)
                mDataSet.add(i, "This is row number " + i);*/
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myadsdetails_singleitem, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(v);
            viewHolder.setIsRecyclable(Boolean.FALSE);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int i) {
            // holder.dataTextView.setText(current_ads.get(position));
            holder.mType.setText(current_ads.get(i).getName());
            holder.mCost.setText("$"+current_ads.get(i).getPrice());
            Picasso.with(activity).load(UrlConstants.BASE_URL + current_ads.get(i).getProduct_images()).into(holder.imgThumbnail);
            String[] createdDate= current_ads.get(i).getCreated_at().split(" ");
            holder.mPostDate.setText(createdDate[0]);
        }

        @Override
        public int getItemCount() {
            return current_ads.size();
        }

        public void remove(int position, String productid) {
            // current_ads.remove(position);
            deleteAdd(productid);
            notifyItemRemoved(position);
            current_ads.remove(position);
        }

        public void ediy(int position) {


            EditProduct fragment = new EditProduct(); //  object of next fragment
            Bundle bundle = new Bundle();
            bundle.putString("userid", mUserId);
            bundle.putString("productid", current_ads.get(position).getProduct_id());
            fragment.setArguments(bundle);
            ((BaseActivity)activity).addFragmentWithBackStack(fragment,new MyAdsSwipe());
        }

        @Override
        public <T> void onRequestStarted(BaseTask<T> listener) {

        }

        @Override
        public <T> void onRequestCompleted(BaseTask<T> listener) {
            try {
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
                if (responseBean.getStatus().equals("1")) {

                } else {
                    Toast.makeText(activity, responseBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public <T> void onRequestFailed(BaseTask<T> listener) {

        }

        static class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgThumbnail;
            public TextView mType, mCost, mPostDate;

            MyViewHolder(View view) {
                super(view);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.imageView2);
                mType = (TextView) itemView.findViewById(R.id.txt_name);
                mCost = (TextView) itemView.findViewById(R.id.txt_experience);
                mPostDate = (TextView) itemView.findViewById(R.id.txt_date);
            }
        }

        public void deleteAdd(String productid) {
            mParams = new HashMap();
            Log.d("productid----", productid + "");
            mParams.put("token", "a152e84173914146e4bc4f391sd0f686ebc4f31");
            mParams.put("product_id", productid);
            mParams.put("user_id", mUserId);
            System.out.println("api called");
            if (NetworkUtil.getConnectivityStatusString(activity)) {
                ApiRequests.getInstance().deleteAdd(activity, this, mParams);
            } else {
                Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

}
