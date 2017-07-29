package com.freelancer.videoeditor.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by thuongle on 07/09/15.
 */
public abstract class BaseFragment extends Fragment {

    protected Unbinder mUnbinder;

    @Nullable
    public BaseFragment create() {
        return create(null);
    }

    @Nullable
    public BaseFragment create(Bundle args) {
        try {
            BaseFragment fragment = this.getClass().newInstance();
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initializeInjector();
        initViews();
        initData();
        return view;
    }

    public BaseActivity activity() {
        return ((BaseActivity) getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }


    protected BasePresenter getPresenter() {
        return null;
    }

    protected abstract void initializeInjector();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract int getLayoutId();
}
