package com.freelancer.videoeditor.di;

import android.app.Application;
import android.content.Context;


import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by thuongle on 07/09/15.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Application application();

    @ApplicationScope
    Context context();

    FragmentComponent plus();
}
