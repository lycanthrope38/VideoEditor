package com.freelancer.videoeditor.view.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyVideoActivity extends AppCompatActivity {
    private MyVideoAdapter mSampleAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

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
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle("My Videos");

        File mFile = new File(AppConst.OUT_VIDEO_FOLDER);
        if (mFile.exists()) {
            this.mSampleAdapter = new MyVideoAdapter(this, AppConst.OUT_VIDEO_FOLDER);
            this.mRecyclerView.setAdapter(this.mSampleAdapter);
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            return;
        }
        mFile.mkdirs();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
