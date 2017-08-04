package com.freelancer.videoeditor.util;

import android.graphics.Bitmap;

public interface OnLoadImageFromURL {
    void onCompleted(Bitmap bitmap);

    void onFail();
}
