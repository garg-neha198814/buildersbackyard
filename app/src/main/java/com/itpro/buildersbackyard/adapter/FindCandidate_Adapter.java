package com.itpro.buildersbackyard.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itpro.buildersbackyard.R;
import com.itpro.buildersbackyard.bean.Find_Candidate;
import com.itpro.buildersbackyard.ui.activity.BaseActivity;
import com.itpro.buildersbackyard.ui.fragment.GetHired_Detail;
import com.itpro.buildersbackyard.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 16/11/15.
 */
public class FindCandidate_Adapter extends RecyclerView.Adapter<FindCandidate_Adapter.ViewHolder> {
    Context activity;
    private ArrayList<Find_Candidate> getHiredCandidatesList = new ArrayList<>();

    public FindCandidate_Adapter(Activity activity, ArrayList<Find_Candidate> getHiredCandidatesList) {
        super();
        try {
            this.activity = activity;
            this.getHiredCandidatesList = getHiredCandidatesList;
        } catch (Exception e) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.findacandidate_singleitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mCandidateName.setText(getHiredCandidatesList.get(i).getName());
        viewHolder.mCandidateTrade.setText(getHiredCandidatesList.get(i).getTrade());

        if (!getHiredCandidatesList.get(i).getProfile_pic().isEmpty()) {
            Picasso.with(activity).load(UrlConstants.BASE_URL + getHiredCandidatesList.get(i).getProfile_pic()).into(viewHolder.mCandidatePic);
        } else if (getHiredCandidatesList.get(i).getProfile_pic().contains("http")) {
            Picasso.with(activity).load(getHiredCandidatesList.get(i).getProfile_pic()).into(viewHolder.mCandidatePic);

        }


        viewHolder.mCandidateAvailability.setText(getHiredCandidatesList.get(i).getAvailability());
        viewHolder.mCandidateLocation.setText(getHiredCandidatesList.get(i).getLocation());
        viewHolder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetHired_Detail fragment = new GetHired_Detail(); //  object of next fragment
                Bundle bundle = new Bundle();
                bundle.putString("candidateId", getHiredCandidatesList.get(i).getId());
                bundle.putString("name", getHiredCandidatesList.get(i).getName());
                bundle.putString("trade", getHiredCandidatesList.get(i).getTrade());
                bundle.putString("certification", getHiredCandidatesList.get(i).getCertification());
                bundle.putString("specialties", getHiredCandidatesList.get(i).getSpecialties());
                bundle.putString("availability", getHiredCandidatesList.get(i).getAvailability());
                bundle.putString("location", getHiredCandidatesList.get(i).getLocation());
                bundle.putString("userid", getHiredCandidatesList.get(i).getUser_id());
                bundle.putString("profile_pic", getHiredCandidatesList.get(i).getProfile_pic());
                fragment.setArguments(bundle);
                ((BaseActivity) activity).addFragmentwithactivity(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {

        return getHiredCandidatesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView mCandidatePic;
        public TextView mCandidateName, mCandidateTrade, mCandidateAvailability, mCandidateLocation;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            mCandidatePic = (CircleImageView) itemView.findViewById(R.id.candidate_pic);
            linear = (LinearLayout) itemView.findViewById(R.id.container_inner_item);
            mCandidateName = (TextView) itemView.findViewById(R.id.candidate_name);
            mCandidateTrade = (TextView) itemView.findViewById(R.id.candidate_trade);
            mCandidateAvailability = (TextView) itemView.findViewById(R.id.candidate_availability);
            mCandidateLocation = (TextView) itemView.findViewById(R.id.candidate_location);

        }
    }
}