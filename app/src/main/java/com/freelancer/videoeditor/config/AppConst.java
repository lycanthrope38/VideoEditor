package com.freelancer.videoeditor.config;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class AppConst {
    public static final String ASSETS_PATH_FOLDER_BACKGROUND = "file:///android_asset/background";
    public static final String ASSETS_PATH_FOLDER_BORDER = "file:///android_asset/border";
    public static final String ASSETS_PATH_FOLDER_FILTER = "file:///android_asset/filter";
    public static final String BUNDLE_KEY_STICKER_PATH = "BUNDLE_KEY_STICKER_PATH";
    public static final String FOLDER_FILTER = "filter_video";
    public static final String FORMAT_FILLTER = ".jpg";
    public static final String FORMAT_FRAME = ".png";
    public static final String NAME_PHOTO_SHARE = "share.png";
    public static String PATH_FILE_SAVE_PHOTO = "mnt/sdcard/Pictures_VideoMaker/";
    public static String PATH_FILE_SAVE_SHARE_PHOTO = "mnt/sdcard/Share/";
    public static final String PREFIX_BORDER = "border";
    public static final String PREFIX_FILTER = "filter";
    public static final String PREFIX_THUMB_BACKGROUND = "background";
    public static final int REQUEST_CODE_CHOOSE_PHOTO_VIA_CAMERA = 0;
    public static String[] STICKER_COLOR_DEFAULT = null;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_STICKER = 2;
    public static final float ZOOM_MAX = 5.0f;
    public static final float ZOOM_MIN = 0.1f;
    public static final int WIDTH_VIDEO = 720;
    public static final String OUT_VIDEO_NAME_FORMAT = "yyyy_MM_dd_hh_mm_ss_a";

    public static final String ACTION_INTENT_FILTER_PHOTO_CROP = "com.BestPhotoEditor.CropPhoto.COMPLETED";
    public static final String ASSET_FILTER_NAME = "filter_video";
    public static final String ASSET_FOLDER_EFFECT_VIDEO_NAME = "effect/video";
    public static final String AUDIO_COPY_TEMP = ".TempAudioCopy";
    public static final String AUDIO_LOOP_TEMP = ".AudioLoop";
    public static final String BUNDLE_KEY_HOME = "BACK_HOME";
    public static final String BUNDLE_KEY_LIST_IMG_PICK = "LIST_IMG_PICK";
    public static final String BUNDLE_KEY_VIDEO_OPEN_FROM_MY_VIDEO = "VIDEO_OPEN_FROM_MY_VIDEO";
    public static final String BUNDLE_KEY_VIDEO_URL = "VIDEO_URL";
    public static final String DEFAULT_INTERVAL_IMAGE = "2";
    public static final String EFFECT_DEFAULT_NAME = "effect0.mp4";
    public static final String EFFECT_LOOP_TEMP = ".EffectLoop";
    public static final String EFFECT_NONE_NAME = "NONE";
    public static final String FILTER_TEMP_NAME = "Filter.png";
    public static final String FOLDER_THEME = "overlay_border";
    public static final String FOLDER_STICKER = "sticker";
    public static final String FOLDER_BORDER = "border";
    public static final String FOLDER_FILTER_IMAGE = "filter";
    public static final String FORMAT_DATE_DEFAULT = "yyyy/MM/dd";
    public static final String FORMAT_DATE_VN = "dd/MM/yyyy";
    public static final String FULL_ASSET_FOLDER_BORDER_NAME = "file:///android_asset/overlay_border";
    public static final String FULL_ASSET_FOLDER_STICKER = "file:///android_asset/sticker";
    public static final String FULL_ASSET_FOLDER_FILTER_NAME = "file:///android_asset/filter_video";
    public static int HEIGHT_IMAGE = WIDTH_VIDEO;
    public static final List<String> LIST_PERMISSION_REQUEST = new ArrayList<>();
    public static final String MEDIA_TEXT_TEMP = "MY_LIST.txt";
    public static final String MP3_ZING_FOLDER = "ZingMp3";
    public static final String MP3_ZING_FOLDER1 = "Zing MP3";
    public static final String NO_MEDIA_NAME = ".nomedia";
    public static final String OUR_NO_MEDIA = "mnt/sdcard/VideoEditor/Temp/.nomedia";
    public static final String RELATIVE_TEMP_FOLDER = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoEditor/Temp/");
    public static final String OUT_AUDIO_TEMP_FOLDER = (RELATIVE_TEMP_FOLDER + "Audio");
    public static final String OUT_BORDER_TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/Border";
    public static final String OUT_EFFECT_TEMP_FOLDER = (RELATIVE_TEMP_FOLDER + "Effect");
    public static final String OUT_FILTER_TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/Filter";
    public static final String OUT_IMAGE_TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/Image";
    public static final String OUT_LOOP_MEDIA_TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/Loop";
    public static final String OUT_VIDEO_FOLDER = "mnt/sdcard/VideoEditor/VideoEditor";
    public static final String OUT_VIDEO_PREFIX = "Video_";
    public static final String OUT_VIDEO_TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/Video";
    public static final String PREFIX_OUT_IMAGE = ".Image";
    public static final String TEMP_FOLDER = "mnt/sdcard/VideoEditor/Temp/";
    public static final String VIDEO_TEMP_NAME = ".VideoTemp";
    public static final String VIDEO_WITH_AUDIO_TEMP_NAME = ".VideoTempAudio";
    public static final String VIDEO_WITH_EFFECT_TEMP = ".VideoEffectTemp";
    public static final String VIDEO_WITH_FILTER_TEMP = ".VideoFilterTemp";
    public static int WIDTH_IMAGE = WIDTH_VIDEO;
    public static final String WEB_DIALOG_PARAM_MEDIA = "media";
    public static final String WEB_DIALOG_PARAM_TITLE = "title";

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
