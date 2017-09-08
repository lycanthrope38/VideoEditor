package com.freelancer.videoeditor.util;

public class AsyncCallBack implements OnThreadListener.IAsyncLoaderCallBack {
    Object object;

    public AsyncCallBack(Object object) {
        this.object = object;
    }

    public void workToDo() {
    }

    public void onComplete() {
    }

    public void onCancelled() {
    }

    public void onCancelled(boolean result) {
    }
}
