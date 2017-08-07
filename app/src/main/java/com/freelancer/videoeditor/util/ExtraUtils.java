package com.freelancer.videoeditor.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.freelancer.videoeditor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ExtraUtils {
    public static final String BASE_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 192;

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static float pixelsToSp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float spToPixels(Context context, float sp) {
        return sp * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getCurrentSdkVersion() {
        return VERSION.SDK_INT;
    }

    @TargetApi(16)
    public static void removeGlobalOnLayoutListener(View target, OnGlobalLayoutListener listener) {
        if (getCurrentSdkVersion() < 16) {
            target.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            target.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isWhitespace(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void makeDeviceVibrate(Context context, long vibrateTime) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(vibrateTime);
    }

    public static void clearNotification(Context context, int id) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
    }

    public static List<ResolveInfo> getListAppInstalled(Context context) {
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public static boolean checkAppIsInstalled(Context context, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            for (ResolveInfo ri : getListAppInstalled(context)) {
                if (packageName.equals(ri.activityInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void clipBoardText(Context context, String label, String text) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(label, text));
    }

    public static void openMarket(Context context, String packageName) {
        Intent i = new Intent("android.intent.action.VIEW");
        try {
            i.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            openBrowser(context, BASE_GOOGLE_PLAY + packageName);
        }
    }

    public static void openBrowser(Context context, String url) {
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            url = "http://" + url;
        }
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openApp(Context context, String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            Toast.makeText(context, context.getString(R.string.message_package_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    public static void displayImage(Context context, ImageView image, String url) {
        displayImage(context, image, url, R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null);
    }

    public static void displayImage(Context context, ImageView image, String url, IOnResourceReady iOnResourceReady) {
        displayImage(context, image, url, R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null, iOnResourceReady);
    }

    public static void displayImage(Context context, ImageView image, int resourceId) {
        displayImage(context, image, Integer.valueOf(resourceId), R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null);
    }

    public static void displayImage(Context context, ImageView image, Object imageSource, int fallBackDrawable, int errorDrawable) {
        Glide.with(context).load(imageSource).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(errorDrawable).fallback(fallBackDrawable).error(errorDrawable).animate(R.anim.anim_fade_in).into(image);
    }

    public static void displayImage(Context context, ImageView image, Object imageSource, int fallBackDrawable, int errorDrawable, final IOnResourceReady iOnResourceReady) {
        Glide.with(context).load(imageSource).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(errorDrawable).fallback(fallBackDrawable).error(errorDrawable).animate(R.anim.anim_fade_in).into(new BitmapImageViewTarget(image) {
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
            }

            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (iOnResourceReady != null) {
                    iOnResourceReady.onLoadFailed();
                }
            }

            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                if (iOnResourceReady != null) {
                    iOnResourceReady.OnResourceReady(drawable);
                }
            }
        });
    }

    public static void displayImageWithTransform(Context context, ImageView imageView, String url, Transformation transformation) {
        Glide.with(context).load(url).fitCenter().bitmapTransform(new Transformation[]{transformation}).diskCacheStrategy(DiskCacheStrategy.ALL).fallback(R.drawable.mylibsutil_bg_null).error(R.drawable.mylibsutil_bg_null).into(imageView);
    }

    public static void displayImageNoCache(Context context, ImageView image, Object url) {
        displayImageNoCache(context, image, url, R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null);
    }

    public static void displayImageNoCache(Context context, ImageView image, Object url, int fallBackDrawable, int errorDrawable) {
        Glide.with(context).load(url).asBitmap().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().fallback(fallBackDrawable).error(errorDrawable).into(image);
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static void setBackgroundFullScreen(Activity activity, View targetView, int drawableId) {
        DisplayMetrics dm = getDisplayInfo(activity);
        setImageBackground(activity, targetView, drawableId, dm.widthPixels, dm.heightPixels);
    }

    public static void setImageBackground(Activity activity, final View view, int drawableId, int targetWidth, int targetHeight) {
        Glide.with(activity).load(Integer.valueOf(drawableId)).asBitmap().into(new SimpleTarget<Bitmap>(targetWidth, targetHeight) {
            @TargetApi(16)
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (!resource.isRecycled()) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                    if (ExtraUtils.getCurrentSdkVersion() >= 16) {
                        view.setBackground(bitmapDrawable);
                    } else {
                        view.setBackgroundDrawable(bitmapDrawable);
                    }
                }
            }
        });
    }

    public static File getExternalPath() {
        return Environment.getExternalStorageDirectory();
    }

    public static void copyTextToClipBoard(Context context, String text, String message) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(text, text));
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void shareImageViaIntent(Activity activity, String pathImage, boolean isChooser) {
        Intent shareIntent;
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(FileUtils.MIME_TYPE_IMAGE);
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(pathImage)));
        if (isChooser) {
            shareIntent = Intent.createChooser(intent, activity.getString(R.string.text_share_via));
        } else {
            shareIntent = intent;
        }
        activity.startActivity(shareIntent);
    }

    public static void shareTextViaIntent(Activity activity, String content, boolean isChooser) {
        Intent shareIntent;
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", content);
        if (isChooser) {
            shareIntent = Intent.createChooser(intent, activity.getString(R.string.text_share_via));
        } else {
            shareIntent = intent;
        }
        activity.startActivity(shareIntent);
    }

    public static void shareVideoViaIntent(Context context, String pathVideo, boolean isChooser) {
        Intent shareIntent;
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(FileUtils.MIME_TYPE_VIDEO);
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(pathVideo)));
        if (isChooser) {
            shareIntent = Intent.createChooser(intent, context.getString(R.string.text_share_via));
        } else {
            shareIntent = intent;
        }
        context.startActivity(shareIntent);
    }

    public static void playVideoViaIntent(Context context, String pathVideo) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(pathVideo));
        intent.setDataAndType(Uri.parse(pathVideo), "video/mp4");
        context.startActivity(intent);
    }

    public static void setLocale(Context context, int countryId) {
        Locale locale = Locale.US;
        Configuration newConfig = new Configuration();
        newConfig.locale = locale;
        Resources resources = context.getResources();
        resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
    }

    public static String[] listAssetFiles(Context context, String path) {
        try {
            return context.getAssets().list(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Options getOriginSizeOfImage(String pathName) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        return options;
    }

    public static Options getOriginSizeOfImage(Context context, int resourceId) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId);
        return options;
    }

    public static Options getOriginSizeOfImage(Context context, String assetsPath) throws IOException {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getAssets().open(assetsPath), null, options);
        return options;
    }

    public static void setLayoutParams(View view, int width, int height) {
        LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
    }

    public static void openAppSettings(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, null));
        context.startActivity(intent);
    }

    public static void scanFile(Context contexts, String pathFile) {
        scanFile(contexts, new String[]{pathFile});
    }

    public static void scanFile(Context contexts, String[] paths) {
        MediaScannerConnection.scanFile(contexts, paths, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
            }
        });
    }

    public static void hideKeyboard(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideDefaultKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }

    public static int getColor(Context context, int id) {
        if (VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(id, context.getTheme());
        }
        return context.getResources().getColor(id);
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (getCurrentSdkVersion() >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void createFolder(String dir) {
        File folder = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static boolean saveBitmapToPNG(Bitmap bitmap, String targetFile) {
        return saveBitmapToFile(bitmap, targetFile, CompressFormat.PNG, 100);
    }

    public static boolean saveBitmapToJPG(Bitmap bitmap, String targetFile) {
        return saveBitmapToFile(bitmap, targetFile, CompressFormat.JPEG, 100);
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String targetFile, CompressFormat compress, int quality) {
        Exception e;
        Throwable th;
        createFolder(targetFile.substring(0, targetFile.lastIndexOf("/")));
        FileOutputStream out = null;
        boolean isSave = false;
        try {
            FileOutputStream out2 = new FileOutputStream(targetFile);
            try {
                bitmap.compress(compress, quality, out2);
                isSave = true;
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        out = out2;
                    }
                }
                out = out2;
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return isSave;
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (out != null) {
            }
            return isSave;
        }
        return isSave;
    }

    public static void setSquareSize(View view, int size) {
        view.getLayoutParams().width = size;
        view.getLayoutParams().height = size;
    }

    public static void hideAndShowKeyBoard(Context context, View target) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInputFromInputMethod(target.getWindowToken(), 0);
    }

    public static Drawable bitmapToDrawable(Resources res, Bitmap source) {
        return new BitmapDrawable(res, source);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
