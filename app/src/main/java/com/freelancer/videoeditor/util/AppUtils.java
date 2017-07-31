package com.freelancer.videoeditor.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.view.video.VideoEncode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class AppUtils {
    static final String GA_CATEGORY = "ACTION";


    public static String getOutputVideoPath(String targetPath, String videoName) {
        return targetPath + File.separator + videoName;
    }

    public static String getFullOutputVideoPath(String targetPath, String videoName, VideoEncode encode) {
        return targetPath + File.separator + videoName + encode.toString();
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        InputStream istr = null;
        try {
            istr = context.getAssets().open(strName);
        } catch (IOException e) {
            Log.e("TAG", "getBitmapFromAsset E = " + e.toString());
        }
        return BitmapFactory.decodeStream(istr);
    }

    public static void deleteFolder(File fileOrDirectory) {
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                for (File child : fileOrDirectory.listFiles()) {
                    deleteFolder(child);
                }
            }
            fileOrDirectory.delete();
        }
    }

    public static boolean containsWhiteSpace(String testCode) {
        if (testCode != null) {
            for (int i = 0; i < testCode.length(); i++) {
                if (Character.isWhitespace(testCode.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void saveBitmap(Bitmap mBitmap, String path) throws Exception {
        FileOutputStream out = new FileOutputStream(path);
        mBitmap.compress(CompressFormat.JPEG, 100, out);
        out.close();
    }

    public static void saveBitmap(Bitmap mBitmap, String path, int q) throws Exception {
        FileOutputStream out = new FileOutputStream(path);
        mBitmap.compress(CompressFormat.JPEG, q, out);
        out.close();
    }

    public static void copyAssetsToSd(Context context, String assetFolder, String outDifFolder, String fileName, String outName) {
        IOException e;
        Throwable th;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(assetFolder + File.separator + fileName);
            OutputStream out2 = new FileOutputStream(new File(outDifFolder, outName));
            try {
                copyFile(in, out2);
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                    }
                }
                if (out2 != null) {
                    try {
                        out2.close();
                        out = out2;
                        return;
                    } catch (IOException e3) {
                        out = out2;
                        return;
                    }
                }
            } catch (IOException e4) {
                e = e4;
                out = out2;
                try {
                    Log.e("tag", "Failed to copy asset file: " + fileName, e);
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e6) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e8) {
                        }
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e9) {
            e = e9;
            Log.e("tag", "Failed to copy asset file: " + fileName, e);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void copyAssets(Context context, String outDirFolder) {
        IOException e;
        OutputStream out;
        Throwable th;
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e2) {
            Log.e("tag", "Failed to get asset file list.", e2);
        }
        InputStream inputStream;
        if (files != null) {
            for (String filename : files) {
                inputStream = null;
                out = null;
                try {
                    inputStream = assetManager.open(filename);
                    OutputStream out2 = new FileOutputStream(new File(outDirFolder, filename));
                    try {
                        copyFile(inputStream, out2);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e3) {
                            }
                        }
                        if (out2 != null) {
                            try {
                                out2.close();
                                out = out2;
                            } catch (IOException e4) {
                                out = out2;
                            }
                        }
                    } catch (IOException e5) {
                        out = out2;
                        try {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e6) {
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e7) {
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        out = out2;
                    }
                } catch (IOException e8) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            return;
        }
        if (out != null) {
            out.close();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e10) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public static int getNavigateBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static String writeFileWithContent(String content, String outDir, String outName) {
        String outFilePath = outDir + File.separator + outName;
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath), "UTF-8"));
            bw.write(content);
            bw.close();
            return outFilePath;
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e("WRITE_FILE", "ERROR: " + e.getMessage());
            return null;
        }
    }

    public static String createVideoNameByTime() {
        return new SimpleDateFormat(AppConst.OUT_VIDEO_NAME_FORMAT, Locale.getDefault()).format(new Date());
    }

    public static void displayImage(Context context, ImageView image, String url) {
        displayImage(context, image, url, R.drawable.icon_default, R.drawable.icon_default);
    }

    public static void displayImage(Context context, ImageView image, int resourceId) {
        displayImage(context, image, Integer.valueOf(resourceId), R.drawable.icon_default, R.drawable.icon_default);
    }

    public static void displayImage(Context context, ImageView image, File imageFile) {
        displayImage(context, image, imageFile, R.drawable.icon_default, R.drawable.icon_default);
    }

    public static void displayImage(Context context, ImageView image, Object imageSource, int fallBackDrawable, int errorDrawable) {
        Glide.with(context).load(imageSource).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(errorDrawable).fallback(fallBackDrawable).error(errorDrawable).animate(R.anim.fade_in).into(image);
    }

    public static void displayImageWithTransform(Context context, ImageView imageView, String url, Transformation transformation) {
        Glide.with(context).load(url).fitCenter().bitmapTransform(new Transformation[]{transformation}).diskCacheStrategy(DiskCacheStrategy.ALL).fallback(R.drawable.icon_default).error(R.drawable.icon_default).into(imageView);
    }

    public static void displayImageNoCache(Context context, ImageView image, Object url) {
        displayImageNoCache(context, image, url, R.drawable.icon_default, R.drawable.icon_default);
    }

    public static void displayImageNoCache(Context context, ImageView image, Object url, int fallBackDrawable, int errorDrawable) {
        Glide.with(context).load(url).asBitmap().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().fallback(fallBackDrawable).error(errorDrawable).into(image);
    }

    public static int getVersionCode() {
        return 2;
    }


    public static void startTaskService(Context context, long intervalMillis) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(0, System.currentTimeMillis(), intervalMillis, PendingIntent.getBroadcast(context, 0, new Intent(context, CheckUseAppReceiver.class), 0));
    }
}
