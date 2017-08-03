package com.freelancer.videoeditor.view.base;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.util.ExtraUtils;

import org.andengine.util.ViewUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public enum BackStack {
        NONE,
        ADD_TO_BACK_STACK,
        POP_BACK_STACK
    }

    public enum Transaction {
        NONE,
        ADD,
        REPLACE,
        REMOVE
    }

    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (ExtraUtils.getCurrentSdkVersion() >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color_navigation));
        }
    }

    public String getLocale() {
        return Locale.getDefault().getLanguage();
    }


    public Fragment getFragmentInBackStack(Class<? extends Fragment> frg) {
        return getSupportFragmentManager().findFragmentByTag(generateTag(frg));
    }


    public void showDialog(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getCanonicalName());
    }

    public Fragment getFragment(String tagName) {
        return getSupportFragmentManager().findFragmentByTag(tagName);
    }

    public Fragment getFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    public String generateTag(Class<? extends Fragment> cls) {
        return cls.getSimpleName();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
//        ViewUtils.clearActivity(this);
        super.onDestroy();
    }
}
