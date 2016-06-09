package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.itpro.buildersbackyard.bean.MyAdds_Data;
import com.itpro.buildersbackyard.bean.ResponseBean;
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

/**
 * Created by root on 24/11/15.
 */
public class MyAds_Adapter extends RecyclerView.Adapter<MyAds_Adapter.ViewHolder> implements AppRequest {
    Activity activity;

    private Map<String, String> mParams;
    private ArrayList<MyAdds_Data> ownerids = new ArrayList<>();
    static  int mPosition;
    private SharedPreferences pref;
    private String mUserId;
    private RecyclerView mRecyclerView;
    public MyAds_Adapter(Activity activity, ArrayList<MyAdds_Data> ownerids, RecyclerView mRecyclerView) {
        super();
try {


    this.activity = activity;
    this.ownerids = ownerids;
    this.mRecyclerView=mRecyclerView;
    pref = activity.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
    mUserId = pref.getString(activity.getString(R.string.userId), "0");
}
catch (Exception e)
{

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.myadsdetails_singleitem, viewGroup, false);

               ViewHolder viewHolder = new ViewHolder(v);
              viewHolder.setIsRecyclable(Boolean.FALSE);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mType.setText(ownerids.get(i).getName());
        viewHolder.mCost.setText(ownerids.get(i).getPrice());
        String createdDate[] =ownerids.get(i).getCreated_at().split(" ");
        Picasso.with(activity).load(UrlConstants.BASE_URL + ownerids.get(i).getProduct_images()).into(viewHolder.imgThumbnail);
// viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        viewHolder.mPostDate.setText(createdDate[0]);

        viewHolder.imgThumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                opendialog(ownerids.get(i).getProduct_id());
                mPosition=i;
                return false;
            }
        });
        //  Log.d("productid----",mProductId+"");
    }

    @Override
    public int getItemCount() {

        return ownerids.size();
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener) {

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener) {
        Log.d("completed", "completed");
        System.out.println("completed");
        // progress.set
        Gson gson = new Gson();
        ResponseBean responseBean = gson.fromJson(listener.getJsonResponse().toString(), ResponseBean.class);
       /* if (is_Scocial.equalsIgnoreCase("0")) {*/
        if (responseBean.getStatus().equals("1")) {
            Toast.makeText(activity, "Deleted", Toast.LENGTH_LONG).show();
           notifyItemRemoved(mPosition);
        } else {
            Toast.makeText(activity, "Status 0", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView mType, mCost, mPostDate;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imageView2);
            mType = (TextView) itemView.findViewById(R.id.txt_name);
            mCost = (TextView) itemView.findViewById(R.id.txt_experience);
            mPostDate = (TextView) itemView.findViewById(R.id.txt_date);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity)activity).addFragmentwithactivity(new JobDetailsFragment());
                }
            });*/
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
        }
    }

    void opendialog(final String position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("Delete");

        // set dialog message
        alertDialogBuilder
                .setMessage(
                        "Are you sure , you want to delete to this add ?")
                .setCancelable(false)
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                Toast.makeText(activity, "Position" + mPosition, Toast.LENGTH_LONG).show();
                                deleteAdd(position);
                                dialog.dismiss();
                                // if this button is clicked, close
                                // current activity
                                // context.finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    class MyclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
}