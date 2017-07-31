package com.freelancer.videoeditor.config;

import java.util.ArrayList;

public class AppConst {
    public static final int ACTION_SAVE = 0;
    public static final int ALPHA_FILTER = 40;
    public static final String ASSETS = "file:///android_asset";
    public static final String ASSETS_EMOTICON = "file:///android_asset/sticker/emo/";
    public static final String ASSETS_PATH_FOLDER_BACKGROUND = "file:///android_asset/background";
    public static final String ASSETS_PATH_FOLDER_BORDER = "file:///android_asset/border";
    public static final String ASSETS_PATH_FOLDER_FILTER = "file:///android_asset/filter";
    public static final String ASSETS_TICKET = "file:///android_asset/sticker/";
    public static final String ASSET_FOLDER_EMO_NAME = "sticker/emo";
    public static final String ASSET_FOLDER_FILTER_NAME = "filter";
    public static final String BUNDLE_KEY_IS_SORT_TAB = "IS_SORT_TAB";
    public static final int EMOTICON_MAX_COLUMNS = 8;
    public static final int EMOTICON_MAX_ROWS = 2;
    public static final String FOLDER_APP = "/Frames/VideoMaker/";
    public static final String FOLDER_BACKGROUND = "/Background/";
    public static final String FOLDER_FILTER = "/Filter/";
    public static final String FORMAT_BACKGROUND = ".jpg";
    public static final String FORMAT_FILLTER = ".jpg";
    public static final String FORMAT_FRAME = ".png";
    public static final int HEIGHT_FRAME = 800;
    public static final String KEY_ADMOB_BANNER = "KEY_ADMOB_BANNER";
    public static final String KEY_ADMOB_FULL_BANNER = "KEY_ADMOB_FULL_BANNER";
    public static final String LINK_ROOT = "http://139.59.241.64/App";
    public static final String NAME_PHOTO_SHARE = "share.png";
    public static String PATH_FILE_SAVE_PHOTO = "mnt/sdcard/Pictures_VideoMaker/";
    public static String PATH_FILE_SAVE_SHARE_PHOTO = "mnt/sdcard/Share/";
    public static final String PREFIX_BACKGROUND = "background";
    public static final String PREFIX_BORDER = "border";
    public static final String PREFIX_FILTER = "filter";
    public static final String PREFIX_THUMB_BACKGROUND = "background";
    public static final String PREFIX_THUMB_FRAME = "frame";
    public static final int REQUEST_CODE_CHOOSE_PHOTO_VIA_CAMERA = 0;
    public static final int REQUEST_CODE_CHOOSE_PHOTO_VIA_FILE = 1;
    public static final int REQUEST_CODE_STICKER_SCREEN = 109;
    public static String[] STICKER_COLOR_DEFAULT = null;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_STICKER = 2;
    public static final int TYPE_TEXT = 3;
    public static final int WIDTH_FRAME = 800;
    public static final float ZOOM_MAX = 5.0f;
    public static final float ZOOM_MIN = 0.1f;
    public static final int WIDTH_VIDEO = 720;
    public static final String OUT_VIDEO_NAME_FORMAT = "yyyy_MM_dd_hh_mm_ss_a";

    static {
        String[] strArr = new String[TYPE_STICKER];
        strArr[REQUEST_CODE_CHOOSE_PHOTO_VIA_CAMERA] = "#f5f5f5";
        strArr[TYPE_PHOTO] = "#e7e7e7";
        STICKER_COLOR_DEFAULT = strArr;
    }

    public static ArrayList<String> FORMAT_IMAGE = new ArrayList<String>() {
        {
            add(".PNG");
            add(".JPEG");
            add(AppConst.FORMAT_FILLTER);
            add(AppConst.FORMAT_FRAME);
            add(".jpeg");
            add(".JPG");
            add(".GIF");
            add(".gif");
        }
    };
}
