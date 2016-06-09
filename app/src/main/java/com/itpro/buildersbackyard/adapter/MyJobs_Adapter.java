package com.itpro.buildersbackyard.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Find_Job_Details;
import com.itpro.buildersbackyard.utils.Constatnts;

import java.util.ArrayList;

/**
 * Created by root on 24/11/15.
 */
public class MyJobs_Adapter extends RecyclerView.Adapter<MyJobs_Adapter.ViewHolder> {
    Context activity;
    private ArrayList<Find_Job_Details> jobs = new ArrayList<>();
    static  int mPosition;
    private SharedPreferences pref;
    private String mUserId;
    RecyclerView mRecyclerView;
    public MyJobs_Adapter(Context activity, ArrayList<Find_Job_Details> jobs, RecyclerView mRecyclerView) {
        super();
        this.activity = activity;
        this.jobs = jobs;
        this.mRecyclerView=mRecyclerView;
        pref = activity.getSharedPreferences(Constatnts.PREFERENCES_FILE, 0);
        mUserId = pref.getString(activity.getString(R.string.userId), "0");
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.myjobs_singleitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.mType.setText(jobs.get(i).getPostTitle());
        viewHolder.mCost.setText(jobs.get(i).getPositionNeeded());
        String createdDate[] = jobs.get(i).getStartDate().split(" ");
        viewHolder.mPostDate.setText(createdDate[0]);
    }

    @Override
    public int getItemCount() {

        return jobs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mType, mCost, mPostDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mType = (TextView) itemView.findViewById(R.id.txt_name);
            mCost = (TextView) itemView.findViewById(R.id.txt_experience);
            mPostDate = (TextView) itemView.findViewById(R.id.txt_date);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity)activity).addFragmentwithactivity(new JobDetailsFragment());
                }
            });*/
        }
    }
}