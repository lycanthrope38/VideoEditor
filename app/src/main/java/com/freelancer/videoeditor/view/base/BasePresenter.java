package com.freelancer.videoeditor.view.base;

/**
 * Created by thuongle on 07/09/15.
 *
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public interface BasePresenter {

    void subscribe();

    void unsubscribe();

    void onDestroy();
}

