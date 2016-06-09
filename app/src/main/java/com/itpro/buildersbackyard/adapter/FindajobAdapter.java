package com.itpro.buildersbackyard.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Find_Job_Details;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.fragment.FindAJob;
import com.itpro.buildersbackyard.ui.fragment.JobDetailsFragment;

import java.util.ArrayList;

/**
 * Created by root on 16/11/15.
 */
public class FindajobAdapter  extends RecyclerView.Adapter
        <FindajobAdapter.ListItemViewHolder> {
   private ArrayList<Find_Job_Details> Jobs = new ArrayList<>();
    static  Context activity;


    public FindajobAdapter(Context activity ,ArrayList<Find_Job_Details> Jobs) {
        super();
        this.activity=activity;
        if (Jobs == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.Jobs = Jobs;

    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.findajob_singleitem, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, final int position) {


        String[] startDate =Jobs.get(position).getStartDate().split(" ");
        String[] endDate =Jobs.get(position).getEndDate().split(" ");
        viewHolder.mJobTitle.setText(String.valueOf(Jobs.get(position).getPostTitle()));
        viewHolder.mJobSpecialities.setText(String.valueOf(Jobs.get(position).getSpecialities()));
        viewHolder.mJobStartDate.setText(String.valueOf(startDate[0]));
        viewHolder.mJobEnddate.setText(String.valueOf(endDate[0]));

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailsFragment fragment = new JobDetailsFragment(); //  object of next fragment
                Bundle bundle = new Bundle();
                bundle.putString("jobId", Jobs.get(position).getId());
                bundle.putString("jobTitle", Jobs.get(position).getPostTitle());
                bundle.putString("positionNeeded", Jobs.get(position).getPositionNeeded());
                bundle.putString("zipcode", Jobs.get(position).getZipcode());
                bundle.putString("certifications", Jobs.get(position).getCertificationRequired());
                bundle.putString("specialities", Jobs.get(position).getSpecialities());
                bundle.putString("contactno", Jobs.get(position).getContactNo());
                bundle.putString("startDate", Jobs.get(position).getStartDate());
                bundle.putString("endDate", Jobs.get(position).getEndDate());
                bundle.putString("location", Jobs.get(position).getLocation());
                bundle.putString("lat", Jobs.get(position).getLatitude());
                bundle.putString("longitude", Jobs.get(position).getLongitude());
                bundle.putString("userid", Jobs.get(position).getUserId());
                bundle.putString("created_at", Jobs.get(position).getCreatedAt());
                fragment.setArguments(bundle);
                ((BaseActivity)activity).addFragmentWithBackStack(fragment,new FindAJob());
            }
        });

    }

    @Override
    public int getItemCount() {
        return Jobs.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView mJobTitle,mJobSpecialities,mJobStartDate,mJobEnddate;
        LinearLayout mainLayout;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mJobTitle = (TextView) itemView.findViewById(R.id.job_title);
            mJobSpecialities = (TextView) itemView.findViewById(R.id.job_specialities);
            mJobStartDate = (TextView) itemView.findViewById(R.id.job_startdate);
            mJobEnddate=(TextView) itemView.findViewById(R.id.job_enddate);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.container_inner_item);

        }
    }
}
