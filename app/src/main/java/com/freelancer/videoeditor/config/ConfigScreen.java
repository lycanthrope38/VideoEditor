package com.freelancer.videoeditor.config;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;

public class ConfigScreen {
    public static int SCREENHEIGHT = 0;
    public static int SCREENHEIGHT_REAL = 0;
    public static int SCREENWIDTH = 0;
    public static int SCREENWIDTH_REAL = 0;
    public static RatioResolutionPolicy mRatioResolutionPolicy;
    public static ScreenOrientation mScreenOrientation = ScreenOrientation.LANDSCAPE_FIXED;

    public static void ini(Activity mActivity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        SCREENHEIGHT = displaymetrics.heightPixels;
        SCREENWIDTH = displaymetrics.widthPixels;
        mRatioResolutionPolicy = new RatioResolutionPolicy((float) SCREENWIDTH, (float) SCREENHEIGHT);
        SCREENWIDTH_REAL = displaymetrics.widthPixels;
        SCREENHEIGHT_REAL = displaymetrics.heightPixels;
    }

    public static Point getScreenWidthHeight(Activity mActivity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return new Point(displaymetrics.widthPixels, displaymetrics.heightPixels);
    }

    public static float getCenterX() {
        return (float) (SCREENWIDTH / 2);
    }

    public static float getCenterY() {
        return (float) (SCREENHEIGHT / 2);
    }
}
