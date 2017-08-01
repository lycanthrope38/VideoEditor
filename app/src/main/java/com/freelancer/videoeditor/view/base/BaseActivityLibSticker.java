package com.freelancer.videoeditor.view.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.Locale;

public class BaseActivityLibSticker extends FragmentActivity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
    }

    public String getLocale() {
        return Locale.getDefault().getLanguage();
    }


    public void popFragment(Fragment fragment) {
        operatorTransaction(0, fragment, Transaction.NONE, BackStack.POP_BACK_STACK);
    }

    public void removeFragment(Fragment fragment) {
        operatorTransaction(0, fragment, Transaction.REMOVE, BackStack.NONE);
    }

    public void addFragmentToRootContent(Fragment fragment, BackStack backStack) {
        addFragment(16908290, fragment, backStack);
    }

    public void addFragment(int containerId, Fragment fragment, BackStack backStack) {
        operatorTransaction(containerId, fragment, Transaction.REPLACE, backStack);
    }

    public void showDialog(DialogFragment dialogFragment) {
        if (dialogFragment != null) {
            dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getCanonicalName());
        }
    }

    public Fragment getFragment(String tagName) {
        return getSupportFragmentManager().findFragmentByTag(tagName);
    }

    public String generateTag(Class<? extends Fragment> cls) {
        return cls.getSimpleName();
    }

    public void operatorTransaction(int containerId, Fragment fragment, Transaction transaction, BackStack backStack) {
        FragmentManager fm = getSupportFragmentManager();
        String tagName = generateTag(fragment.getClass());
        if (backStack == BackStack.POP_BACK_STACK) {
            fm.popBackStack(tagName, 0);
            fm.executePendingTransactions();
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
//        switch (AnonymousClass1.$SwitchMap$com$libsticker$BaseActivityLibSticker$Transaction[transaction.ordinal()]) {
//            case UtilLib.FLIP_VERTICAL /*1*/:
//                ft.add(containerId, fragment, tagName);
//                break;
//            case UtilLib.FLIP_HORIZONTAL /*2*/:
//                ft.replace(containerId, fragment, tagName);
//                break;
//            case R.styleable.View_paddingEnd /*3*/:
//                ft.remove(fragment);
//                break;
//        }
        if (backStack == BackStack.ADD_TO_BACK_STACK) {
            ft.addToBackStack(tagName);
        }
        ft.commitAllowingStateLoss();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
