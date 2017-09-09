package com.freelancer.videoeditor.view.photo;

import android.content.Intent;
import android.net.Uri;

import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.OnClickListener;
import com.freelancer.videoeditor.util.AppUtil;

import java.io.File;
import java.io.IOException;
import org.andengine.entity.util.ScreenCapture;

import timber.log.Timber;

public class MyScreenCapture extends ScreenCapture {
    PhotoEditorActivity mainActivity;
    int pHeightCapture = 0;
    int pWidthCapture = 0;
    int pXstartCapture = 0;
    int pYstartCapture = 0;

    public void capture(PhotoEditorActivity mainActivity, String name, int action, OnClickListener.OnCapture mOnCapture) {
        Timber.e("start capture");
        this.mainActivity = mainActivity;
        AppUtil.getInstance().showLoading(mainActivity);
        capture(name, action, mOnCapture);
    }

    public void set(int pXstartCapture, int pYstartCapture, int pWidthCapture, int pHeightCapture) {
        this.pXstartCapture = pXstartCapture;
        this.pYstartCapture = pYstartCapture;
        this.pWidthCapture = pWidthCapture;
        this.pHeightCapture = pHeightCapture;
    }

    private void capture(String name, final int action, final OnClickListener.OnCapture mOnCapture) {
        this.mainActivity.getMainScene().attachChild(this);
        final String pathNewFile = name;
        Timber.e("capture pathNewFile = " + pathNewFile);
        capture(this.pXstartCapture, this.pYstartCapture, this.pWidthCapture, this.pHeightCapture, pathNewFile, new IScreenCaptureCallback() {
            public void onScreenCaptured(String pFilePath) {
                MyScreenCapture.this.detachSelf();
                if (action == 0) {
                    MyScreenCapture.this.galleryAddPic(pFilePath);
                }
                if (mOnCapture != null) {
                    mOnCapture.onSuccess(pathNewFile);
                }
                AppUtil.getInstance().hideLoading();
            }

            public void onScreenCaptureFailed(String pFilePath, Exception pException) {
                MyScreenCapture.this.detachSelf();
                if (mOnCapture != null) {
                    mOnCapture.onFail();
                }
                AppUtil.getInstance().hideLoading();
            }
        });
    }

    String getPathNewFile(int action, String name) {
        String pathNewFile = "";
        if (action == 0) {
            pathNewFile = AppConst.PATH_FILE_SAVE_PHOTO + name;
        } else {
            pathNewFile = AppConst.PATH_FILE_SAVE_SHARE_PHOTO + AppConst.NAME_PHOTO_SHARE;
        }
        File f = new File(pathNewFile);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathNewFile;
    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
        this.mainActivity.sendBroadcast(mediaScanIntent);
    }
}
