package com.freelancer.videoeditor.view.base;

import android.support.v4.app.Fragment;

import org.andengine.util.ViewUtils;

import java.util.Locale;

public class BaseFragment extends Fragment {


    public String getLocale() {
        return Locale.getDefault().getLanguage();
    }

    public void onDestroyView() {
//        ViewUtils.clearRootView(getView());
        super.onDestroyView();
    }
}
