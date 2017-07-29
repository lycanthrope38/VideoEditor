package com.freelancer.videoeditor.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;


import com.freelancer.videoeditor.VideoEditorApp;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by thuongle on 07/09/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable mSubscription = new CompositeDisposable();
    protected Unbinder mUnbinder;


    // used for vector drawable
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(this.getLayoutId(), null, false);
        setContentView(rootView);
        mUnbinder = ButterKnife.bind(this);

        initializeInjector();
        initViews();
        initData();

    }

    public VideoEditorApp getApp() {
        return (VideoEditorApp) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
        mUnbinder.unbind();
    }

    protected abstract BasePresenter getPresenter();

    protected void reloadActivity() {
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    protected abstract void initializeInjector();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract int getLayoutId();

}
