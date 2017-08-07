package com.freelancer.videoeditor;

import android.app.Application;

import com.bumptech.glide.Glide;

import timber.log.Timber;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class VideoEditorApp  extends Application {


    private static VideoEditorApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

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
