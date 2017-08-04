package com.freelancer.videoeditor.util;

import android.graphics.Bitmap;

public interface IOnResourceReady {
    void OnResourceReady(Bitmap bitmap);

    void onLoadFailed();
}
