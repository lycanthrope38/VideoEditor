package com.freelancer.videoeditor.util;

/**
 * Created by ThongLe on 9/8/2017.
 */

public interface OnThreadListener {
    interface IAsyncLoaderCallBack {
        void onCancelled();

        void onCancelled(boolean z);

        void onComplete();

        void workToDo();
    }
    interface IClose {
        void onClose();
    }

    interface IHandler {
        void doWork();
    }
    interface IOnBackLoading {
        void onBack();
    }
}
