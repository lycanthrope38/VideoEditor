package com.freelancer.videoeditor.view.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;

import java.io.File;

public class MyVideoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyVideoAdapter mSampleAdapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MyVideoActivity.class));
    }

    @RequiresApi(api = 21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (ExtraUtils.getCurrentSdkVersion() >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color_navigation));
        }
        setContentView(R.layout.activity_my_video);
        File mFile = new File(AppConst.OUT_VIDEO_FOLDER);
        if (mFile.exists()) {
            this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            this.mSampleAdapter = new MyVideoAdapter(this, AppConst.OUT_VIDEO_FOLDER);
            this.mRecyclerView.setAdapter(this.mSampleAdapter);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MyVideoActivity.this.finish();
                }
            });
            return;
        }
        mFile.mkdirs();
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
