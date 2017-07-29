package com.freelancer.videoeditor;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.freelancer.videoeditor.di.ApplicationComponent;
import com.freelancer.videoeditor.di.ApplicationModule;
import com.freelancer.videoeditor.di.DaggerApplicationComponent;

import timber.log.Timber;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class VideoEditorApp  extends Application {


    private ApplicationComponent mAppComponent;
    private static VideoEditorApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }


    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    public static synchronized VideoEditorApp getInstance() {
        return sInstance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
