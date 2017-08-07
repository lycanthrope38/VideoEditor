package com.freelancer.videoeditor.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.freelancer.videoeditor.BuildConfig;
import com.freelancer.videoeditor.config.AppConst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.andengine.util.FileUtils;
import org.andengine.util.StreamUtils;

public class MyFile {
    public static void ini(Activity mActivity) {
        File mFile = new File(AppConst.PATH_FILE_SAVE_PHOTO);
        if (!mFile.isDirectory()) {
            mFile.mkdirs();
        }
        if (new File(AppConst.PATH_FILE_SAVE_PHOTO).exists()) {
            mFile = new File(AppConst.PATH_FILE_SAVE_SHARE_PHOTO);
            if (!mFile.isDirectory()) {
                mFile.mkdirs();
            }
        } else {
            String path = FileUtils.getAbsolutePathOnExternalStorage(mActivity,"");
            AppConst.PATH_FILE_SAVE_PHOTO = path;
            AppConst.PATH_FILE_SAVE_SHARE_PHOTO = path;
            File fp = new File(path);
            if (!fp.isDirectory()) {
                fp.mkdirs();
            }
        }
        File f = new File(AppConst.PATH_FILE_SAVE_SHARE_PHOTO + AppConst.NAME_PHOTO_SHARE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(Bitmap mBitmap, String path) {
        FileOutputStream out = null;
        try {
            FileOutputStream out2 = new FileOutputStream(path);
            mBitmap.compress(CompressFormat.PNG, 100, out2);
            out = out2;
        } catch (FileNotFoundException e2) {
            StreamUtils.flushCloseStream(out);
        }
    }
}
