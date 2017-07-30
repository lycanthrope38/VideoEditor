package com.freelancer.videoeditor.util;

public interface IAsyncLoaderCallBack {
    void onCancelled();

    void onCancelled(boolean z);

    void onComplete();

    void workToDo();
}
