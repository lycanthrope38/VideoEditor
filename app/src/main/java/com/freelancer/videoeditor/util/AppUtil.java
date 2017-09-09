package com.freelancer.videoeditor.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class AppUtil {
    public static final int FLIP_HORIZONTAL = 2;
    public static final int FLIP_VERTICAL = 1;
    private static AppUtil utilLib;
    public final int REQUEST_CODE_ASK_ALL_PERMISSIONS = PhotoEditorActivity.REQUEST_CODE_CROP;
    final Handler mHandler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
//            msg.obj.doWork();
            return true;
        }
    });
    public ProgressDialog mProgressDialog;
    public ProgressDialog mProgressDialogDownload;

    public static AppUtil getInstance() {
        if (utilLib == null) {
            utilLib = new AppUtil();
        }
        return utilLib;
    }

    private AppUtil() {
    }


    public void handlerDoWork(OnThreadListener.IHandler mIHandler) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(0, mIHandler));
    }


    public int getRandomIndex(int min, int max) {
        return ((int) (Math.random() * ((double) ((max - min) + FLIP_VERTICAL)))) + min;
    }

    public boolean appInstalledOrNot(String uri, Context mContext) {
        if (uri.length() == 0 || mContext == null) {
            return false;
        }
        try {
            mContext.getPackageManager().getPackageInfo(uri, FLIP_VERTICAL);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {
            NetworkInfo[] netInfo = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
            int length = netInfo.length;
            for (int i = 0; i < length; i += FLIP_VERTICAL) {
                NetworkInfo ni = netInfo[i];
                if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                    haveConnectedWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
            if (haveConnectedWifi || haveConnectedMobile) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }

    public void nextChoseLiveWallpaper(Activity mActivity, Class<?> mC) {
        Intent intent;
        try {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(mActivity, mC));
            mActivity.startActivity(intent);
        } catch (Exception e) {
            try {
                intent = new Intent();
                intent.setAction("android.service.wallpaper.LIVE_WALLPAPER_CHOOSER");
                mActivity.startActivityForResult(intent, FLIP_VERTICAL);
            } catch (Exception e2) {
            }
        }
    }

    public Drawable getDrawableFromAssets(Context mActivity, String path) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(mActivity.getAssets().open(path), null);
        } catch (Exception e) {
        }
        return drawable;
    }

    public void showDialogShare(Context mContext, String textShare) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction("android.intent.action.SEND");
            sendIntent.putExtra("android.intent.extra.TEXT", textShare);
            sendIntent.setType("text/plain");
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
        }
    }

    public int getVersionCode(Context mContext, String packageName) {
        try {
            return mContext.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    public String getDeviceId(Context mContext) {
        return Secure.getString(mContext.getContentResolver(), "android_id");
    }

    public void addShortcut(Context mContext, String name, String url, int idResource) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction("android.intent.action.VIEW");
        shortcutIntent.setData(Uri.parse(url));
        Intent addIntent = new Intent();
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        addIntent.putExtra("android.intent.extra.shortcut.NAME", name);
        addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(mContext, idResource));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        mContext.getApplicationContext().sendBroadcast(addIntent);
    }

    public void addShortcut(Context mContext, String name, String url, Bitmap bitmap, String name_package) {
        if (name_package.length() == 0 || !appInstalledOrNot(name_package, mContext)) {
            Intent shortcutIntent = new Intent();
            shortcutIntent.setAction("android.intent.action.VIEW");
            shortcutIntent.setData(Uri.parse(url));
            Intent addIntent = new Intent();
            addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
            addIntent.putExtra("android.intent.extra.shortcut.NAME", name);
            addIntent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
            addIntent.putExtra("duplicate", false);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            mContext.getApplicationContext().sendBroadcast(addIntent);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        if (bm == null) {
            return bm;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i += FLIP_VERTICAL) {
                hexString.append(Integer.toHexString(messageDigest[i] & BallSpinFadeLoaderIndicator.ALPHA));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

//    public void shareImageAndText(Context mContext, String pathFile, String SUBJECT, String TEXT) {
//        try {
//            File myFile = new File(pathFile);
//            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(myFile.getName().substring(myFile.getName().lastIndexOf(FileUtils.HIDDEN_PREFIX) + FLIP_VERTICAL));
//            Intent sharingIntent = new Intent();
//            sharingIntent.setAction("android.intent.action.SEND");
//            sharingIntent.setType(type);
//            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
//            sharingIntent.putExtra("android.intent.extra.SUBJECT", SUBJECT);
//            sharingIntent.putExtra("android.intent.extra.TEXT", TEXT);
//            if (mContext.getPackageManager().queryIntentActivities(sharingIntent, NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REQUEST).size() > 0) {
//                mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.title_share_file)));
//            } else {
//                showDetailApp(mContext, mContext.getPackageName());
//            }
//        } catch (Exception e) {
//            Log.e(BuildConfig.FLAVOR, "shareImageAndText error = " + e.toString());
//        }
//    }

    public Bitmap flip(Bitmap src, int type) {
        Matrix matrix = new Matrix();
        if (type == FLIP_VERTICAL) {
            matrix.preScale(HandlerTools.ROTATE_R, HandlerTools.ROTATE_L);
        } else if (type != FLIP_HORIZONTAL) {
            return null;
        } else {
            matrix.preScale(HandlerTools.ROTATE_L, HandlerTools.ROTATE_R);
        }
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        if (source == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void showLoading(Activity mActivity) {
        showLoading(mActivity, null);
    }

    public void showLoading(final Activity mActivity, final OnThreadListener.IOnBackLoading mOnBackLoading) {
        if (mActivity != null) {
            if (AppUtil.this.mProgressDialog == null) {
                AppUtil.this.mProgressDialog = new ProgressDialog(mActivity) {
                    public void onBackPressed() {
                        super.onBackPressed();
                        if (mOnBackLoading != null) {
                            mOnBackLoading.onBack();
                        }
                    }
                };
                AppUtil.this.mProgressDialog.setMessage("Loading");
                AppUtil.this.mProgressDialog.setCancelable(false);
                AppUtil.this.mProgressDialog.show();
            }
        }
    }

    public void hideLoading() {
        if (AppUtil.this.mProgressDialog != null && AppUtil.this.mProgressDialog.isShowing()) {
            AppUtil.this.mProgressDialog.dismiss();
            AppUtil.this.mProgressDialog = null;
        }
    }

    public void showLoadingProgress(final Activity activity, final String message) {
        if (activity != null) {
            if (AppUtil.this.mProgressDialogDownload == null) {
                AppUtil.this.mProgressDialogDownload = new ProgressDialog(activity);
                AppUtil.this.mProgressDialogDownload.setProgressStyle(AppUtil.FLIP_VERTICAL);
                AppUtil.this.mProgressDialogDownload.setMessage(message);
                AppUtil.this.mProgressDialogDownload.setCancelable(false);
                AppUtil.this.mProgressDialogDownload.show();
            }
        }
    }

    public void updateDialogProgress(final int progress) {
        if (AppUtil.this.mProgressDialogDownload != null && AppUtil.this.mProgressDialogDownload.isShowing()) {
            AppUtil.this.mProgressDialogDownload.setIndeterminate(false);
            AppUtil.this.mProgressDialogDownload.setProgress(progress);
        }
    }

    public void hideLoadingDownload() {
        if (AppUtil.this.mProgressDialogDownload != null && AppUtil.this.mProgressDialogDownload.isShowing()) {
            AppUtil.this.mProgressDialogDownload.dismiss();
            AppUtil.this.mProgressDialogDownload = null;
        }
    }



    public void setOnCustomTouchView(View view, final com.freelancer.videoeditor.util.OnClickListener.OnCustomTouchListener onCustomTouchListener) {
        view.setOnTouchListener(new OnTouchListener() {
            private Rect rect;

            public boolean onTouch(View v, MotionEvent event) {
                if (onCustomTouchListener == null) {
                    return false;
                }
                if (event.getAction() == 0) {
                    this.rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    onCustomTouchListener.OnCustomTouchDown(v, event);
                } else if (this.rect != null && !this.rect.contains(v.getLeft() + ((int) event.getX()), v.getTop() + ((int) event.getY()))) {
                    onCustomTouchListener.OnCustomTouchMoveOut(v, event);
                    return false;
                } else if (event.getAction() == AppUtil.FLIP_VERTICAL) {
                    onCustomTouchListener.OnCustomTouchUp(v, event);
                } else {
                    onCustomTouchListener.OnCustomTouchOther(v, event);
                }
                return true;
            }
        });
    }

    public void setOnCustomTouchViewScale(final View view, final com.freelancer.videoeditor.util.OnClickListener.OnCustomClickListener customClickListener) {
        setOnCustomTouchView(view, new com.freelancer.videoeditor.util.OnClickListener.OnCustomTouchListener() {
            private void setScale(float scale) {
                view.setScaleX(scale);
                view.setScaleY(scale);
            }

            public void OnCustomTouchDown(View v, MotionEvent event) {
                setScale(0.9f);
            }

            public void OnCustomTouchMoveOut(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
            }

            public void OnCustomTouchUp(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
                if (customClickListener != null) {
                    customClickListener.OnCustomClick(v, event);
                }
            }

            public void OnCustomTouchOther(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
            }
        });
    }

    // scale view
    public void setOnCustomTouchViewScaleNotOther(final View view, final com.freelancer.videoeditor.util.OnClickListener.OnCustomClickListener customClickListener) {
        setOnCustomTouchView(view, new com.freelancer.videoeditor.util.OnClickListener.OnCustomTouchListener() {
            private void setScale(float scale) {
                view.setScaleX(scale);
                view.setScaleY(scale);
            }

            public void OnCustomTouchDown(View v, MotionEvent event) {
                setScale(0.9f);
            }

            public void OnCustomTouchMoveOut(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
            }

            public void OnCustomTouchUp(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
                if (customClickListener != null) {
                    customClickListener.OnCustomClick(v, event);
                }
            }

            public void OnCustomTouchOther(View v, MotionEvent event) {
            }
        });
    }

    public void setOnCustomTouchViewAlphaNotOther(final View view, final com.freelancer.videoeditor.util.OnClickListener.OnCustomClickListener customClickListener) {
        setOnCustomTouchView(view, new com.freelancer.videoeditor.util.OnClickListener.OnCustomTouchListener() {
            private void setAlpha(float alpha) {
                view.setAlpha(alpha);
            }

            public void OnCustomTouchDown(View v, MotionEvent event) {
                setAlpha(0.7f);
            }

            public void OnCustomTouchMoveOut(View v, MotionEvent event) {
                setAlpha(HandlerTools.ROTATE_R);
            }

            public void OnCustomTouchUp(View v, MotionEvent event) {
                setAlpha(HandlerTools.ROTATE_R);
                if (customClickListener != null) {
                    customClickListener.OnCustomClick(v, event);
                }
            }

            public void OnCustomTouchOther(View v, MotionEvent event) {
            }
        });
    }

    public static boolean isLocaleVn() {
        return Locale.getDefault().getLanguage().equalsIgnoreCase("vi");
    }


}
