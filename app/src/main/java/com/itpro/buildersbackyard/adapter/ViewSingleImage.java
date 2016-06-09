package com.itpro.buildersbackyard.adapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.itpro.buildersbackyard.R;
import com.squareup.picasso.Picasso;

/**
 * Created by root on 22/12/15.
 */
public class ViewSingleImage extends ActionBarActivity {


    ImageView mFullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_image);

        String image = getIntent().getStringExtra("image");
        mFullImage = (ImageView) findViewById(R.id.fullimage);

        if (image == null) {
            Toast.makeText(this, "Device Error", Toast.LENGTH_LONG).show();
            finish();
        } else
            Picasso.with(this).load(image).into(mFullImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}