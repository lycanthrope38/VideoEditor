package com.freelancer.videoeditor.view.video;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.AppUtils;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.FFMpegListener;
import com.freelancer.videoeditor.util.FFmpegCommands;
import com.freelancer.videoeditor.util.FFmpegUtils;
import com.freelancer.videoeditor.util.FileUtils;
import com.freelancer.videoeditor.util.FilenameUtils;
import com.freelancer.videoeditor.util.IHandler;
import com.freelancer.videoeditor.util.OnToolBoxListener;
import com.freelancer.videoeditor.view.base.BaseActivity;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;
import com.freelancer.videoeditor.view.photo.PhotoEditorData;
import com.freelancer.videoeditor.vo.Audio;
import com.freelancer.videoeditor.vo.ListVideoEffect;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import net.margaritov.preference.colorpicker.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class VideoEditorActivity extends BaseActivity implements OnToolBoxListener {
    private static final int ORIGIN_HEIGHT_SCREEN = 1280;
    private static final int ORIGIN_WDITH_SCREEN = 720;
    public static final int REQUEST_EDITOR_PHOTO = 2003;
    public static final int REQUEST_PICK_AUDIO = 2002;
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String TAG = "VideoEditorActivity";
    private int SCREEN_HEIGHT = ORIGIN_HEIGHT_SCREEN;
    private int SCREEN_WIDTH = ORIGIN_WDITH_SCREEN;
    private List<String> arrDuration;
    @BindView(R.id.image_btn_duration)
    ImageView btnDuration;
    @BindView(R.id.image_btn_editor)
    ImageView btnEditor;
    @BindView(R.id.image_btn_effect)
    ImageView btnEffect;
    @BindView(R.id.image_btn_music)
    ImageView btnMusic;
    @BindView(R.id.image_btn_theme)
    ImageView btnTheme;
    @BindView(R.id.button_back)
    ImageView buttonBack;
    @BindView(R.id.button_tool_duration)
    LinearLayout buttonDuration;
    @BindView(R.id.button_tool_editor)
    LinearLayout buttonEditor;
    @BindView(R.id.button_tool_effect)
    LinearLayout buttonEffect;
    @BindView(R.id.button_tool_music)
    LinearLayout buttonMusic;
    @BindView(R.id.button_tool_theme)
    LinearLayout buttonTheme;
    @BindView(R.id.toolbox_container_fragment)
    FrameLayout containerFragment;
    @BindView(R.id.container_tool_bar)
    LinearLayout containerToolBar;
    @BindView(R.id.view_divide_duration)
    View divideDuration;
    @BindView(R.id.view_divide_editor)
    View divideEditor;
    @BindView(R.id.view_divide_effect)
    View divideEffect;
    @BindView(R.id.view_divide_music)
    View divideMusic;
    @BindView(R.id.view_divide_theme)
    View divideTheme;
    private long durationExec = 0;
    @BindView(R.id.fragment_effect)
    LinearLayout effectFragment;
    private FFmpegUtils fmpegUtils;
    private IHandler iHandler;
    @BindView(R.id.image_filter)
    ImageView imageFilter;
    @BindView(R.id.image_overlay_border)
    ImageView imageOverlayBorder;
    private boolean isGenSuccess = false;
    private boolean isIgnoreShortestAudio = false;
    private boolean isIgnoreShortestEffect = false;
    private boolean isSaveVideo = false;
    private boolean isVideoWithAudio = false;
    private boolean isVideoWithEffect = false;
    @BindView(R.id.linear_button_save)
    LinearLayout layoutButtonSave;
    @BindView(R.id.layoutSeekBar)
    RelativeLayout layoutSeekBar;
    private String mAudioPickedPath;
    private Bitmap mBitmapFilter;
    private String mBorderPath;
    private String mCommands;
    private Audio mCurrentAudioSelected;
    private ListVideoEffect mCurrentEffectSelected;
    private Process mCurrentProcess = Process.GEN_VIDEO;
    private String mCurrentVideoUrl;
    private String mEffectPathSelected;
    private float mFilterOpacity = 0.4f;
    private String mFilterPath;
    private String mFullVideoAudioPath;
    private String mFullVideoBorderPath;
    private String mFullVideoEffectPath;
    private String mFullVideoFilterPath;
    private String mFullVideoPath;
    private int mIndexDurationSelected = 3;
    private String mIntervalImage = AppConst.DEFAULT_INTERVAL_IMAGE;
    MusicFragment mItemMusicFragment;
    private ArrayList<String> mListImage;
    @BindView(R.id.media_controller)
    UniversalMediaController mMediaController;
    private ProgressDialog mProgressDialog;
    private FFMpegListener mVideoEncodeListener = new FFMpegListener() {
        public void onUnSupportDevice(Exception ex) {
            Toast.makeText(VideoEditorActivity.this,  R.string.message_device_not_support, Toast.LENGTH_SHORT).show();
            VideoEditorActivity.this.isGenSuccess = false;
            VideoEditorActivity.this.dismissDialog();
            VideoEditorActivity.this.finish();
        }

        public void onFFmpegCommandAlreadyRunning() {
            Timber.d(VideoEditorActivity.TAG, "FFMPEG BUSY");
            VideoEditorActivity.this.isGenSuccess = false;
        }

        public void onFailure(String message) {
            super.onFailure(message);
            VideoEditorActivity.this.isGenSuccess = false;
            VideoEditorActivity.this.dismissDialog();
            Timber.d(VideoEditorActivity.TAG, "VIDEO FAILED: " + message);
            Toast.makeText(VideoEditorActivity.this, "Failed message " + VideoEditorActivity.this.mCommands, Toast.LENGTH_SHORT).show();
        }

        public void onStart() {
            super.onStart();
            Timber.d(VideoEditorActivity.TAG, "Started command : ffmpeg " + VideoEditorActivity.this.mCommands);
            VideoEditorActivity.this.stopVideo();
            VideoEditorActivity.this.isGenSuccess = false;
            VideoEditorActivity.this.durationExec = System.currentTimeMillis();
            VideoEditorActivity.this.showDialog();
        }

        public void onProgress(String s) {
            VideoEditorActivity.this.mProgressDialog.setMessage("Processing\n" + s);
            VideoEditorActivity.this.isGenSuccess = false;
        }

        public void onSuccess(String message) {
            super.onSuccess(message);
            VideoEditorActivity.this.isGenSuccess = true;
            VideoEditorActivity.this.dismissDialog();
            Timber.d(VideoEditorActivity.TAG, "Process: " + VideoEditorActivity.this.mCurrentProcess + " >>> OUT SUCCESS: " + message);
            switch (VideoEditorActivity.this.mCurrentProcess.ordinal()) {
                case 0:
                    VideoEditorActivity.this.isVideoWithAudio = false;
                    VideoEditorActivity.this.showVideo(VideoEditorActivity.this.mFullVideoPath);
                    return;
                case 1:
                    VideoEditorActivity.this.isVideoWithAudio = true;
                    VideoEditorActivity.this.showVideo(VideoEditorActivity.this.mFullVideoAudioPath);
                    return;
                case 2:
                    VideoEditorActivity.this.showVideoSavedSuccessfully(VideoEditorActivity.this.mFullVideoBorderPath);
                    VideoEditorActivity.this.scanVideoFile(VideoEditorActivity.this.mFullVideoBorderPath);
                    VideoEditorActivity.this.finish();
                    return;
                case 3:
                    if (VideoEditorActivity.this.isBorderSelected()) {
                        VideoEditorActivity.this.mCurrentVideoUrl = VideoEditorActivity.this.mFullVideoFilterPath;
                        VideoEditorActivity.this.generateVideoWithBorder();
                        return;
                    }
                    VideoEditorActivity.this.showVideoSavedSuccessfully(VideoEditorActivity.this.mFullVideoFilterPath);
                    VideoEditorActivity.this.scanVideoFile(VideoEditorActivity.this.mFullVideoFilterPath);
                    VideoEditorActivity.this.finish();
                    return;
                case 4:
                    VideoEditorActivity.this.isVideoWithEffect = true;
                    VideoEditorActivity.this.showVideo(VideoEditorActivity.this.mFullVideoEffectPath);
                    return;
                case 5:
                    VideoEditorActivity.this.generateVideoWithAudio();
                    return;
                case 6:
                    VideoEditorActivity.this.generateVideoWithEffect();
                    return;
                default:
                    return;
            }
        }

        public void onFinish() {
            super.onFinish();
            VideoEditorActivity.this.durationExec = System.currentTimeMillis() - VideoEditorActivity.this.durationExec;
            Timber.d(VideoEditorActivity.TAG, ">>> TIME FINISHED: " + VideoEditorActivity.this.durationExec + " ms");
            if (VideoEditorActivity.this.isGenSuccess && VideoEditorActivity.this.mCurrentProcess == Process.GEN_VIDEO && VideoEditorActivity.this.iHandler != null) {
                VideoEditorActivity.this.iHandler.doWork();
            }
        }
    };
    @BindView(R.id.fragment_music_container)
    LinearLayout musicFragment;
    private OnSeekBarChangeListener onSeekBarChange = new OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            VideoEditorActivity.this.updateProgress(progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
    @BindView(R.id.root_header_video_editor)
    RelativeLayout rootHeader;
    @BindView(R.id.root_tool_bar)
    LinearLayout rootToolBar;
    @BindView(R.id.video_layout)
    FrameLayout rootVideo;
    @BindView(R.id.seekBarAlpha)
    SeekBar seekBarAlpha;
    @BindView(R.id.image_button_save)
    ImageView textButtonSave;
    @BindView(R.id.text_title_duration)
    TextView textDuration;
    @BindView(R.id.text_title_editor)
    TextView textEditor;
    @BindView(R.id.text_title_effect)
    TextView textEffect;
    @BindView(R.id.text_title_music)
    TextView textMusic;
    @BindView(R.id.text_title_theme)
    TextView textTheme;
    @BindView(R.id.fragment_theme)
    LinearLayout themeFragment;
    @BindView(R.id.txtAlpha)
    TextView txtAlpha;
    @BindView(R.id.video_view_editor)
    UniversalVideoView videoViewEditor;

    private enum Process {
        GEN_VIDEO,
        GEN_VIDEO_WITH_AUDIO,
        GEN_VIDEO_WITH_BORDER,
        GEN_VIDEO_WITH_EFFECT,
        GEN_VIDEO_WITH_FILTER,
        GEN_AUDIO_LOOP,
        GEN_VIDEO_LOOP
    }

    private enum Reset {
        BOTH,
        AUDIO,
        EFFECT
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editor);
        ButterKnife.bind(this);
        init();
        resizeLayout();
        deleteAllMediaTemp();
        createHideFile();
        this.fmpegUtils = FFmpegUtils.newInstance(this);
        this.fmpegUtils.listener(this.mVideoEncodeListener).loadFFMpegBinary();
        this.mListImage = getIntent().getStringArrayListExtra(AppConst.BUNDLE_KEY_LIST_IMG_PICK);
        generateVideoFromImagePicked();
        this.buttonTheme.performClick();
    }

    private void scanAudio(String path) {
        int i = 0;
        File[] file = new File(path.substring(0, path.lastIndexOf("/"))).listFiles((dir, name) -> name.endsWith(".mp3"));
        String[] paths = new String[file.length];
        int i2 = 0;
        int length = file.length;
        while (i < length) {
            paths[i2] = file[i].getAbsolutePath();
            i2++;
            i++;
        }
        ExtraUtils.scanFile(getApplicationContext(), paths);
    }

    private void createHideFile() {
        createFolder(AppConst.TEMP_FOLDER);
        File f = new File(AppConst.OUR_NO_MEDIA);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFile() {
        boolean isDelete = false;
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        String zingMp3Folder = AppConst.MP3_ZING_FOLDER;
        if (!new File(rootPath + zingMp3Folder).exists()) {
            zingMp3Folder = AppConst.MP3_ZING_FOLDER1;
            if (!new File(rootPath + zingMp3Folder).exists()) {
                return;
            }
        }
        String NO_MEDIA = zingMp3Folder + File.separator + AppConst.NO_MEDIA_NAME;
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + NO_MEDIA;
        File f = new File(fullPath);
        if (f.exists()) {
            isDelete = f.delete();
            scanAudio(fullPath);
        } else {
            fullPath = "mnt/sdcard/" + NO_MEDIA;
            f = new File(fullPath);
            if (f.exists()) {
                isDelete = f.delete();
                scanAudio(fullPath);
            }
        }
        Timber.d(TAG, "Mp3Zing NOMEDIA is delete: " + isDelete);
    }

    private void deleteAllMediaTemp() {
        deleteFile();
        deleteFolder(AppConst.OUT_VIDEO_TEMP_FOLDER);
        deleteFolder(AppConst.OUT_AUDIO_TEMP_FOLDER);
        deleteFolder(AppConst.OUT_BORDER_TEMP_FOLDER);
    }

    private void deleteFolder(String pathFolder) {
        AppUtils.deleteFolder(new File(pathFolder));
    }

    private void init() {
        this.mItemMusicFragment = (MusicFragment) getFragmentById(R.id.music_fragment);
        this.arrDuration = Arrays.asList(getResources().getStringArray(R.array.array_duration_value));
        this.videoViewEditor.setMediaController(this.mMediaController);
        this.imageFilter.setVisibility(View.GONE);
        this.seekBarAlpha.setOnSeekBarChangeListener(this.onSeekBarChange);
        updateProgress(100);
    }

    private void resizeLayout() {
        DisplayMetrics metrics = ExtraUtils.getDisplayInfo(this);
        this.SCREEN_WIDTH = metrics.widthPixels;
        this.SCREEN_HEIGHT = metrics.heightPixels - AppUtils.getNavigateBarHeight(this);
        int heightTop = (this.SCREEN_HEIGHT * 80) / ORIGIN_HEIGHT_SCREEN;
        this.rootHeader.getLayoutParams().height = heightTop;
        setSquareSize(this.buttonBack, heightTop);
//        this.layoutButtonSave.getLayoutParams().height = heightTop;
        int width = (this.SCREEN_WIDTH * 2) / ORIGIN_WDITH_SCREEN;
        int height = (width * 24) /2;
        this.textButtonSave.getLayoutParams().width = (int) (((float) width) * 1.3f);
        this.textButtonSave.getLayoutParams().height = (int) (((float) height) * 1.3f);
        setSquareSize(this.rootVideo, this.SCREEN_WIDTH);
        this.containerToolBar.getLayoutParams().height = (this.SCREEN_HEIGHT * 480) / ORIGIN_HEIGHT_SCREEN;
        this.rootToolBar.getLayoutParams().height = (this.SCREEN_HEIGHT * 175) / ORIGIN_HEIGHT_SCREEN;
        this.containerFragment.getLayoutParams().height = (this.SCREEN_HEIGHT * 305) / ORIGIN_HEIGHT_SCREEN;
        int iconWidth = (this.SCREEN_WIDTH * 75) / ORIGIN_WDITH_SCREEN;
        setSquareSize(this.btnTheme, iconWidth);
        setSquareSize(this.btnDuration, iconWidth);
        setSquareSize(this.btnEffect, iconWidth);
        setSquareSize(this.btnMusic, iconWidth);
        setSquareSize(this.btnEditor, iconWidth);
    }

    private void setSquareSize(View view, int size) {
        view.getLayoutParams().width = size;
        view.getLayoutParams().height = size;
    }

    @OnClick({R.id.button_back, R.id.linear_button_save, R.id.button_tool_duration, R.id.button_tool_effect, R.id.button_tool_music, R.id.button_tool_theme, R.id.button_tool_editor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back :
                onBackPressed();
                return;
            case R.id.linear_button_save :
                if (isFilterSelected()) {
                    saveVideoWithFilter();
                    return;
                } else {
                    saveVideoWithBorder();
                    return;
                }
            default:
                clickToolBox(view);
                return;
        }
    }

    private void scanVideoFile(String pathFile) {
        ExtraUtils.scanFile(getApplicationContext(), pathFile);
    }

    private void outVideo() {
        String outVideoPath = AppConst.OUT_VIDEO_FOLDER;
        createFolder(outVideoPath);
        String saveFile = AppUtils.getFullOutputVideoPath(outVideoPath, AppConst.OUT_VIDEO_PREFIX + AppUtils.createVideoNameByTime(), VideoEncode.MP4);
        try {
            Timber.d(TAG, "CURRENT VIDEO URL: " + this.mCurrentVideoUrl);
            new File(this.mCurrentVideoUrl).renameTo(new File(saveFile));
            showVideoSavedSuccessfully(saveFile);
            scanVideoFile(saveFile);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Save: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap alphaBitmap(Bitmap src, float opacity) {
        Bitmap transBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAlpha((int) (255.0f * opacity));
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        return transBitmap;
    }

    private void bitmapToFile(Bitmap bitmap) {
        String outFolder = AppConst.OUT_FILTER_TEMP_FOLDER;
        createFolder(outFolder);
        try {
            Bitmap bm = alphaBitmap(bitmap, this.mFilterOpacity);
            bm.setHasAlpha(true);
            String outPath = outFolder + File.separator + AppConst.FILTER_TEMP_NAME;
            OutputStream stream = new FileOutputStream(outPath);
            bm.compress(CompressFormat.PNG, 100, stream);
            stream.close();
            this.mFilterPath = outPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFilterSelected() {
        return this.imageFilter.getVisibility() == View.VISIBLE;
    }

    private boolean isBorderSelected() {
        return !TextUtils.isEmpty(this.mBorderPath);
    }

    private void saveVideoWithFilter() {
        bitmapToFile(this.mBitmapFilter);
        if (isFilterSelected() && !TextUtils.isEmpty(this.mFilterPath)) {
            generateVideoWithFilter();
        } else if (isBorderSelected()) {
            saveVideoWithBorder();
        } else {
            outVideo();
        }
    }

    private void saveVideoWithBorder() {
        if (!isBorderSelected() || TextUtils.isEmpty(this.mBorderPath)) {
            outVideo();
        } else {
            generateVideoWithBorder();
        }
    }

    private void showVideoSavedSuccessfully(final String videoUrl) {
        nextVideoSavedActivity(videoUrl);
    }

    private void nextVideoSavedActivity(String videoUrl) {
        this.isSaveVideo = true;
        Intent intent = new Intent(this, VideoSavedActivity.class);
        intent.putExtra(AppConst.BUNDLE_KEY_VIDEO_URL, videoUrl);
        intent.putExtra(AppConst.BUNDLE_KEY_HOME, true);
        Toast.makeText(this, videoUrl, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void stopVideo() {
        if (this.videoViewEditor.isPlaying()) {
            this.videoViewEditor.stopPlayback();
        }
    }

    private void showVideo(String urlVideo) {
        if (!TextUtils.isEmpty(urlVideo)) {
            Timber.d(TAG, "VIDEO PLAY: " + urlVideo);
            stopVideo();
            this.mCurrentVideoUrl = urlVideo;
//            this.mMediaController.setFullscreenEnabled(false);
            this.videoViewEditor.setVideoPath(urlVideo);
            this.videoViewEditor.requestFocus();
            this.videoViewEditor.setOnPreparedListener(mp -> VideoEditorActivity.this.videoViewEditor.start());
            this.videoViewEditor.setOnCompletionListener(mp -> {
                VideoEditorActivity.this.mMediaController.reset();
                VideoEditorActivity.this.videoViewEditor.requestFocus();
                VideoEditorActivity.this.videoViewEditor.start();
            });
        }
    }

    private void createFolder(String pathFolder) {
        File rootFolder = new File(pathFolder);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
    }

    private void generateVideoFromImagePicked() {
        if (this.mListImage != null && !this.mListImage.isEmpty()) {
            this.mCurrentProcess = Process.GEN_VIDEO;
            Timber.d(TAG, "LIST IMG: " + this.mListImage.toString());
            String outFolder = AppConst.OUT_IMAGE_TEMP_FOLDER;
            String outVideo = AppConst.OUT_VIDEO_TEMP_FOLDER;
            String videoName = AppConst.VIDEO_TEMP_NAME;
            VideoEncode outEncode = VideoEncode.MP4;
            this.mFullVideoPath = AppUtils.getFullOutputVideoPath(outVideo, videoName, outEncode);
            createFolder(outFolder);
            createFolder(outVideo);
            String[] cmd = FFmpegCommands.getInstance().setPrefixImageFile(".Image%d.jpg").setScaleSize("720:720").setStartImageIndex(1).setIntervalPicture(this.mIntervalImage).genSlideImageCmd(outFolder, AppUtils.getOutputVideoPath(outVideo, videoName), outEncode);
            this.mCommands = Arrays.toString(cmd);
            this.fmpegUtils.executeCommand(cmd);
        }
    }

    private void generateAudioLoop(String extension) {
        this.mCurrentProcess = Process.GEN_AUDIO_LOOP;
        String outMediaTempFolder = AppConst.OUT_LOOP_MEDIA_TEMP_FOLDER;
        createFolder(outMediaTempFolder);
        this.mAudioPickedPath = outMediaTempFolder + File.separator + AppConst.AUDIO_LOOP_TEMP + extension;
        String[] cmd = FFmpegCommands.getInstance().genAudioLoop(outMediaTempFolder + File.separator + AppConst.MEDIA_TEXT_TEMP, this.mAudioPickedPath);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
        Timber.d(TAG, "AUDIO LOOP PATH OUT:" + this.mAudioPickedPath);
    }

    private void generateVideoWithAudio() {
        if (isResetMedia()) {
            showResetMediaToast(Reset.AUDIO);
            this.isVideoWithEffect = false;
        }
        this.mCurrentProcess = Process.GEN_VIDEO_WITH_AUDIO;
        String outVideo = AppConst.OUT_VIDEO_TEMP_FOLDER;
        String videoAudioTempName = AppConst.VIDEO_WITH_AUDIO_TEMP_NAME;
        VideoEncode outEncode = VideoEncode.MP4;
        this.mFullVideoAudioPath = AppUtils.getFullOutputVideoPath(outVideo, videoAudioTempName, outEncode);
        createFolder(outVideo);
        String[] cmd = FFmpegCommands.getInstance().genVideoWithAudio(this.isVideoWithEffect ? this.mFullVideoEffectPath : this.mFullVideoPath, this.mAudioPickedPath, AppUtils.getOutputVideoPath(outVideo, videoAudioTempName), outEncode, this.isIgnoreShortestAudio);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
        Timber.d(TAG, "AUDIO PATH: " + this.mAudioPickedPath);
    }

    private void generateVideoWithFilter() {
        this.mCurrentProcess = Process.GEN_VIDEO_WITH_FILTER;
        String outVideo = AppConst.OUT_VIDEO_FOLDER;
        String videoTempName = AppConst.OUT_VIDEO_PREFIX + AppUtils.createVideoNameByTime();
        if (isBorderSelected()) {
            outVideo = AppConst.OUT_VIDEO_TEMP_FOLDER;
            videoTempName = AppConst.VIDEO_WITH_FILTER_TEMP;
        }
        createFolder(outVideo);
        String outVideoPath = AppUtils.getOutputVideoPath(outVideo, videoTempName);
        VideoEncode outEncode = VideoEncode.MP4;
        this.mFullVideoFilterPath = AppUtils.getFullOutputVideoPath(outVideo, videoTempName, outEncode);
        String[] cmd = FFmpegCommands.getInstance().genVideoWithFilter(this.mCurrentVideoUrl, this.mFilterPath, outVideoPath, outEncode);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
    }

    private void generateVideoWithBorder() {
        this.mCurrentProcess = Process.GEN_VIDEO_WITH_BORDER;
        String outVideo = AppConst.OUT_VIDEO_FOLDER;
        createFolder(outVideo);
        String videoTempName = AppConst.OUT_VIDEO_PREFIX + AppUtils.createVideoNameByTime();
        String outVideoPath = AppUtils.getOutputVideoPath(outVideo, videoTempName);
        VideoEncode outEncode = VideoEncode.MP4;
        this.mFullVideoBorderPath = AppUtils.getFullOutputVideoPath(outVideo, videoTempName, outEncode);
        String[] cmd = FFmpegCommands.getInstance().genVideoWithBorder(this.mCurrentVideoUrl, this.mBorderPath, outVideoPath, outEncode);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
    }

    private void generateEffectLoop(String extension) {
        this.mCurrentProcess = Process.GEN_VIDEO_LOOP;
        String outMediaTempFolder = AppConst.OUT_LOOP_MEDIA_TEMP_FOLDER;
        createFolder(outMediaTempFolder);
        this.mEffectPathSelected = outMediaTempFolder + File.separator + AppConst.EFFECT_LOOP_TEMP + extension;
        String[] cmd = FFmpegCommands.getInstance().genVideoLoop(outMediaTempFolder + File.separator + AppConst.MEDIA_TEXT_TEMP, this.mEffectPathSelected);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
        Timber.d(TAG, "EFFECT LOOP PATH OUT:" + this.mEffectPathSelected);
    }

    private void generateVideoWithEffect() {
        if (isResetMedia()) {
            showResetMediaToast(Reset.EFFECT);
            this.isVideoWithAudio = false;
            this.mItemMusicFragment.removeAudioText();
        }
        this.mCurrentProcess = Process.GEN_VIDEO_WITH_EFFECT;
        String outVideo = AppConst.OUT_VIDEO_TEMP_FOLDER;
        String videoTempName = AppConst.VIDEO_WITH_EFFECT_TEMP;
        String outVideoPath = AppUtils.getOutputVideoPath(outVideo, videoTempName);
        VideoEncode outEncode = VideoEncode.MP4;
        this.mFullVideoEffectPath = AppUtils.getFullOutputVideoPath(outVideo, videoTempName, outEncode);
        String[] cmd = FFmpegCommands.getInstance().genVideoWithEffect(this.isVideoWithAudio ? this.mFullVideoAudioPath : this.mFullVideoPath, this.mEffectPathSelected, outVideoPath, outEncode, this.isIgnoreShortestEffect);
        this.mCommands = Arrays.toString(cmd);
        this.fmpegUtils.executeCommand(cmd);
    }

    private void showDialog() {
        this.mProgressDialog = new ProgressDialog(this);
        int androidVersion = ExtraUtils.getCurrentSdkVersion();
        if (androidVersion >= 11 && androidVersion < 21) {
            this.mProgressDialog = new ProgressDialog(this, 2);
        } else if (androidVersion >= 21) {
            this.mProgressDialog = new ProgressDialog(this, 16974374);
        }
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setTitle(R.string.message_create_video);
        this.mProgressDialog.setMessage("Processing...");
        this.mProgressDialog.show();
    }

    private void dismissDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

    private void clickToolBox(View view) {
        switch (view.getId()) {
            case R.id.button_tool_theme :
                showTheme();
                this.btnTheme.setImageResource(R.drawable.ic_theme_press);
                this.btnEffect.setImageResource(R.drawable.ic_filter);
                this.btnMusic.setImageResource(R.drawable.ic_music);
                setTextColor(this.textTheme, R.color.color_title_toolbox_press);
                setTextColor(this.textEffect, R.color.color_title_toolbox);
                setTextColor(this.textMusic, R.color.color_title_toolbox);
                this.divideTheme.setVisibility(View.VISIBLE);
                this.divideEffect.setVisibility(View.INVISIBLE);
                this.divideMusic.setVisibility(View.INVISIBLE);
                return;
            case R.id.button_tool_duration:
                showChangeDuration();
                return;
            case R.id.button_tool_music:
                showMusic();
                this.btnMusic.setImageResource(R.drawable.ic_music_press);
                this.btnEffect.setImageResource(R.drawable.ic_filter);
                this.btnTheme.setImageResource(R.drawable.ic_theme);
                setTextColor(this.textMusic, R.color.color_title_toolbox_press);
                setTextColor(this.textEffect, R.color.color_title_toolbox);
                setTextColor(this.textTheme, R.color.color_title_toolbox);
                this.divideMusic.setVisibility(View.VISIBLE);
                this.divideEffect.setVisibility(View.INVISIBLE);
                this.divideTheme.setVisibility(View.INVISIBLE);
                return;
            case R.id.button_tool_effect :
                showFilter();
                this.btnEffect.setImageResource(R.drawable.ic_filter_press);
                this.btnMusic.setImageResource(R.drawable.ic_music);
                this.btnTheme.setImageResource(R.drawable.ic_theme);
                setTextColor(this.textEffect, R.color.color_title_toolbox_press);
                setTextColor(this.textMusic, R.color.color_title_toolbox);
                setTextColor(this.textTheme, R.color.color_title_toolbox);
                this.divideEffect.setVisibility(View.VISIBLE);
                this.divideMusic.setVisibility(View.INVISIBLE);
                this.divideTheme.setVisibility(View.INVISIBLE);
                return;
            case R.id.button_tool_editor :
                if (isResetMedia()) {
                    showResetMediaDialog(R.string.message_warning_change_image, (dialog, which) -> VideoEditorActivity.this.showEditorPhoto(), null);
                    return;
                } else {
                    showEditorPhoto();
                    return;
                }
            default:
                return;
        }
    }

    private void setTextColor(TextView tv, int resId) {
        tv.setTextColor(getResources().getColor(resId));
    }

    public void onBackPressed() {
        if (this.isSaveVideo) {
            super.onBackPressed();
        } else {
            showDialogSaveVideo();
        }
    }

    private void showDialogSaveVideo() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.title_dialog_save_video);
        builder.setCancelable(true);
        builder.setMessage(R.string.message_save_video);
        builder.setPositiveButton(R.string.text_button_yes, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VideoEditorActivity.this.saveVideoWithBorder();
            }
        });
        builder.setNegativeButton(R.string.text_button_no, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VideoEditorActivity.this.finish();
            }
        });
        builder.setNeutralButton(R.string.text_cancel, null);
        builder.create().show();
    }

    private void showChangeDuration() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arrays.asList(getResources().getStringArray(R.array.array_duration)));
        Builder builder = new Builder(this);
        builder.setTitle(R.string.title_dialog_duration);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(adapter, this.mIndexDurationSelected, null);
        builder.setPositiveButton(R.string.text_apply, (dialog, which) -> {
            final int itemClick = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            final String value = VideoEditorActivity.this.arrDuration.get(itemClick);
            if (VideoEditorActivity.this.isResetMedia()) {
                VideoEditorActivity.this.showResetMediaDialog(R.string.message_warning_change_duration, (dialog1, which1) -> {
                    VideoEditorActivity.this.mItemMusicFragment.removeAudioText();
                    VideoEditorActivity.this.isVideoWithEffect = false;
                    VideoEditorActivity.this.isVideoWithAudio = false;
                    VideoEditorActivity.this.mIndexDurationSelected = itemClick;
                    VideoEditorActivity.this.mIntervalImage = value;
                    VideoEditorActivity.this.createVideoFromImages();
                }, (dialog12, which12) -> VideoEditorActivity.this.showChangeDuration());
                return;
            }
            VideoEditorActivity.this.mIndexDurationSelected = itemClick;
            VideoEditorActivity.this.clickApplyDuration(value);
        });
        builder.setNegativeButton(R.string.text_cancel, null);
        AlertDialog dialog = builder.create();
        int height = (int) (((float) ExtraUtils.getDisplayInfo(this).heightPixels) * 0.7f);
        LayoutParams lp = new LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = height;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        if (ExtraUtils.getCurrentSdkVersion() < 21) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    private void clickApplyDuration(String value) {
        Timber.d(TAG, "INTERVAL PHOTO: " + value + " seconds");
        this.mIntervalImage = value;
        if (this.isVideoWithAudio) {
            this.iHandler = new IHandler() {
                public void doWork() {
                    VideoEditorActivity.this.addMusicToVideo(VideoEditorActivity.this.mCurrentAudioSelected);
                    VideoEditorActivity.this.iHandler = null;
                }
            };
            generateVideoFromImagePicked();
        } else if (this.isVideoWithEffect) {
            this.iHandler = new IHandler() {
                public void doWork() {
                    VideoEditorActivity.this.addEffectToVideo(VideoEditorActivity.this.mCurrentEffectSelected);
                    VideoEditorActivity.this.iHandler = null;
                }
            };
            generateVideoFromImagePicked();
        } else {
            createVideoFromImages();
        }
    }

    private void createVideoFromImages() {
        this.iHandler = null;
        generateVideoFromImagePicked();
    }

    private void showTheme() {
        hideSeekBar();
        showNormalVideoFrame(true);
        this.themeFragment.setVisibility(View.VISIBLE);
        this.musicFragment.setVisibility(View.GONE);
        this.effectFragment.setVisibility(View.GONE);
    }

    private void showMusic() {
        hideSeekBar();
        showNormalVideoFrame(true);
        this.themeFragment.setVisibility(View.GONE);
        this.musicFragment.setVisibility(View.VISIBLE);
        this.effectFragment.setVisibility(View.GONE);
    }

    private void showFilter() {
        showNormalVideoFrame(false);
        this.themeFragment.setVisibility(View.GONE);
        this.musicFragment.setVisibility(View.GONE);
        this.effectFragment.setVisibility(View.VISIBLE);
    }

    private void showEffect() {
        this.themeFragment.setVisibility(View.GONE);
        this.musicFragment.setVisibility(View.GONE);
        this.effectFragment.setVisibility(View.VISIBLE);
    }

    public Bitmap getBitmapFromAssets(String fileName) {
        InputStream is = null;
        try {
            is = getAssets().open(AppConst.FOLDER_THEME + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    private void showLayoutSeekBar() {
        this.layoutSeekBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        this.layoutSeekBar.setVisibility(View.VISIBLE);
    }

    private void hideSeekBar() {
        this.layoutSeekBar.setVisibility(View.INVISIBLE);
    }

    private void updateProgress(int progress) {
        this.seekBarAlpha.setProgress(progress);
        this.txtAlpha.setText(progress + " %");
        this.mFilterOpacity = ((float) progress) / 100.0f;
        this.imageFilter.setAlpha(this.mFilterOpacity);
    }

    private void showNormalVideoFrame(boolean isShow) {
        if (isShow) {
            this.mMediaController.show();
            this.mMediaController.setVisibility(View.VISIBLE);
            this.videoViewEditor.setMediaController(this.mMediaController);
            this.imageOverlayBorder.setVisibility(View.VISIBLE);
            return;
        }
        this.mMediaController.hide();
        this.mMediaController.setVisibility(View.GONE);
        this.videoViewEditor.setMediaController(null);
        this.imageOverlayBorder.setVisibility(View.GONE);
        if (isFilterSelected()) {
            showLayoutSeekBar();
        }
    }

    @Override
    public void onPassData(Action action, Object object) {
        switch (action.ordinal()) {
            case 3:
                if (object instanceof String) {
                    String data = (String) object;
                    if (data.contains("none")) {
                        this.mBorderPath = null;
                        this.imageOverlayBorder.setImageBitmap(null);
                        this.imageOverlayBorder.setVisibility(View.INVISIBLE);
                        return;
                    }
                    Bitmap bm = getBitmapFromAssets(data.substring(data.lastIndexOf(File.separator) + 1, data.length()));
                    if (bm != null) {
                        this.imageOverlayBorder.setImageBitmap(bm);
                    } else {
                        ExtraUtils.displayImage((Context) this, this.imageOverlayBorder, data);
                    }
                    this.imageOverlayBorder.setVisibility(View.VISIBLE);
                    assetToFile(data);
                    return;
                }
                return;
            case 5:
                if (object instanceof String) {
                    String data = (String) object;
                    if (data.contains("none")) {
                        this.mFilterPath = null;
                        this.imageFilter.setVisibility(View.GONE);
                        hideSeekBar();
                        return;
                    }

                }

                showLayoutSeekBar();
                this.mBitmapFilter = (Bitmap) object;
                this.imageFilter.setImageBitmap(this.mBitmapFilter);
                this.imageFilter.setVisibility(View.VISIBLE);
                return;

//                else if (object instanceof Bitmap) {
//
//                } else {
//                    return;
//                }
//            case 3:
//                if (object instanceof Audio) {
//                    this.mCurrentAudioSelected = (Audio) object;
//                    addMusicToVideo(this.mCurrentAudioSelected);
//                    return;
//                }
//                return;
            case 4:
                removeAudio();
                return;
//            case 5:
//                if (object instanceof ListVideoEffect) {
//                    this.mCurrentEffectSelected = (ListVideoEffect) object;
//                    addEffectToVideo(this.mCurrentEffectSelected);
//                    return;
//                }
//                return;
            default:
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d(TAG, "RESULT CODE: " + resultCode);
        if (resultCode == -1 && requestCode == REQUEST_EDITOR_PHOTO) {
            Timber.d(TAG, "REQUEST_EDITOR_PHOTO");
            if (isResetMedia()) {
                this.isVideoWithEffect = false;
                this.isVideoWithAudio = false;
                this.mItemMusicFragment.removeAudioText();
                Timber.d(TAG, "CLEAR ALL EFFECT");
            }
            this.mListImage = data.getExtras().getStringArrayList(PhotoEditorActivity.KEY_LIST_PATH_PHOTO);
            clickApplyDuration(this.mIntervalImage);
        }
    }

    private void removeAudio() {
        if (isResetMedia()) {
            this.isVideoWithAudio = false;
            generateVideoWithEffect();
            return;
        }
        this.mCurrentAudioSelected = null;
        this.mAudioPickedPath = BuildConfig.FLAVOR;
        this.isVideoWithAudio = false;
        showVideo(this.isVideoWithEffect ? this.mFullVideoEffectPath : this.mFullVideoPath);
    }

    private void assetToFile(String assetPath) {
        String borderFileName = assetPath.substring((AppConst.FULL_ASSET_FOLDER_BORDER_NAME + File.separator).length(), assetPath.length());
        String outBorderFileName = FileUtils.HIDDEN_PREFIX + borderFileName;
        Timber.d(TAG, "BORDER FILE NAME: " + outBorderFileName);
        String outFolder = AppConst.OUT_BORDER_TEMP_FOLDER;
        createFolder(outFolder);
        AppUtils.copyAssetsToSd(this, AppConst.FOLDER_THEME, outFolder, borderFileName, outBorderFileName);
        this.mBorderPath = outFolder + File.separator + outBorderFileName;
    }

    private void addMusicToVideo(Audio audio) {
        String audioFile = audio.getPathFile();
        if (AppUtils.containsWhiteSpace(audioFile)) {
            File tempAudio = new File(AppConst.OUT_AUDIO_TEMP_FOLDER);
            AppUtils.deleteFolder(tempAudio);
            if (!tempAudio.exists()) {
                tempAudio.mkdirs();
            }
            String saveAudioFile = (AppConst.OUT_AUDIO_TEMP_FOLDER + File.separator + AppConst.AUDIO_COPY_TEMP) + (FileUtils.HIDDEN_PREFIX + FilenameUtils.getExtension(audio.getName()));
            File saveFile = new File(saveAudioFile);
            if (saveFile.exists()) {
                Timber.d(TAG, "FILE OLD DELETED: " + saveFile.delete());
            }
            Timber.d(TAG, "FILE COPIED: " + saveAudioFile);
            try {
                org.andengine.util.FileUtils.copyFile(new File(audioFile), saveFile);
                this.mAudioPickedPath = saveAudioFile;
                audio.setPathFile(saveAudioFile);
                checkDuration(audio);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Timber.e(TAG, "Error Add music: " + e.getMessage());
                return;
            }
        }
        this.mAudioPickedPath = audioFile;
        checkDuration(audio);
    }

    private void checkDuration(Audio audio) {
        int audioDuration = audio.getSeconds();
        if (audioDuration <= 0) {
            Toast.makeText(this,  R.string.message_audio_length_too_short, Toast.LENGTH_SHORT).show();
            return;
        }
        int videoDuration = getCurrentVideoDuration();
        Timber.d(TAG, "VIDEO DURATION: " + videoDuration);
        if (videoDuration > audioDuration) {
            Timber.d(TAG, "AUDIO DURATION: " + audioDuration);
            loopAudio(audio, videoDuration);
            return;
        }
        this.isIgnoreShortestAudio = false;
        generateVideoWithAudio();
    }

    private void loopAudio(Audio audio, int videoLength) {
        int audioLength = audio.getSeconds();
        String audioPath = audio.getPathFile();
        String extension = FileUtils.HIDDEN_PREFIX + FilenameUtils.getExtension(audio.getName());
        Timber.d(TAG, "AUDIO EXTENSION CHOOSE: " + extension);
        int countLoop = Math.round((float) (videoLength / audioLength));
        float surplus = (float) (videoLength % audioLength);
        if (surplus > 0.0f && surplus > ((float) Math.round(((float) videoLength) * 0.05f))) {
            countLoop++;
        }
        Timber.d(TAG, ">>> COUNT LOOP: " + countLoop);
        String outDir = AppConst.OUT_LOOP_MEDIA_TEMP_FOLDER;
        createFolder(outDir);
        String fullTextPath = writeLoopMedia(audioPath, countLoop, outDir, AppConst.MEDIA_TEXT_TEMP);
        Timber.d(TAG, ">>>> TEXT PATH: " + fullTextPath);
        if (TextUtils.isEmpty(fullTextPath)) {
            this.isIgnoreShortestAudio = true;
            generateVideoWithAudio();
            return;
        }
        this.isIgnoreShortestAudio = countLoop * audioLength < videoLength;
        generateAudioLoop(extension);
    }

    private void effect0AssetsToFile() {
        String effectFileName = AppConst.EFFECT_DEFAULT_NAME;
        String outEffectFileName = FileUtils.HIDDEN_PREFIX + effectFileName;
        Timber.d(TAG, "EFFECT FILE NAME: " + outEffectFileName);
        String outFolder = AppConst.OUT_EFFECT_TEMP_FOLDER;
        createFolder(outFolder);
        AppUtils.copyAssetsToSd(this, AppConst.ASSET_FOLDER_EFFECT_VIDEO_NAME, outFolder, effectFileName, outEffectFileName);
        this.mEffectPathSelected = outFolder + File.separator + outEffectFileName;
    }

    private String getEffectPath(String effectName) {
        return AppConst.OUT_EFFECT_TEMP_FOLDER + File.separator + FileUtils.HIDDEN_PREFIX + effectName;
    }

    private void addEffectToVideo(ListVideoEffect effect) {
        String effectName = effect.getFileName();
        if (effectName.equals(AppConst.EFFECT_NONE_NAME)) {
            removeEffect();
            return;
        }
        String fullEffectPath = getEffectPath(effectName);
        if (effectName.equals(AppConst.EFFECT_DEFAULT_NAME) && !new File(fullEffectPath).exists()) {
            effect0AssetsToFile();
        }
        checkDuration(effect);
    }

    private void removeEffect() {
        if (isResetMedia()) {
            this.isVideoWithEffect = false;
            generateVideoWithAudio();
            return;
        }
        this.mFullVideoEffectPath = BuildConfig.FLAVOR;
        this.isVideoWithEffect = false;
        showVideo(this.isVideoWithAudio ? this.mFullVideoAudioPath : this.mFullVideoPath);
    }

    private void checkDuration(ListVideoEffect effect) {
        int videoDuration = getCurrentVideoDuration();
        int effectDuration = effect.getDuration();
        Timber.d(TAG, "VIDEO DURATION: " + videoDuration);
        this.mEffectPathSelected = getEffectPath(effect.getFileName());
        if (videoDuration > effectDuration) {
            Timber.d(TAG, "EFFECT DURATION: " + effectDuration);
            loopEffect(effect, videoDuration);
            return;
        }
        this.isIgnoreShortestEffect = false;
        generateVideoWithEffect();
    }

    private void loopEffect(ListVideoEffect effect, int videoLength) {
        int effectLength = effect.getDuration();
        String effectName = effect.getFileName();
        String effectPath = getEffectPath(effectName);
        String extension = FileUtils.HIDDEN_PREFIX + FilenameUtils.getExtension(effectName);
        Timber.d(TAG, "Effect EXTENSION CHOOSE: " + extension);
        int countLoop = Math.round((float) (videoLength / effectLength));
        float surplus = (float) (videoLength % effectLength);
        if (surplus > 0.0f && surplus > ((float) Math.round(((float) videoLength) * 0.05f))) {
            countLoop++;
        }
        Timber.d(TAG, ">>> COUNT LOOP: " + countLoop);
        String outDir = AppConst.OUT_LOOP_MEDIA_TEMP_FOLDER;
        createFolder(outDir);
        String fullTextPath = writeLoopMedia(effectPath, countLoop, outDir, AppConst.MEDIA_TEXT_TEMP);
        Timber.d(TAG, ">>>> TEXT PATH: " + fullTextPath);
        if (TextUtils.isEmpty(fullTextPath)) {
            this.isIgnoreShortestEffect = true;
            generateVideoWithEffect();
            return;
        }
        this.isIgnoreShortestEffect = countLoop * effectLength < videoLength;
        generateEffectLoop(extension);
    }

    private boolean isResetMedia() {
        return this.isVideoWithAudio && this.isVideoWithEffect;
    }

    private void showResetMediaToast(Reset reset) {
        switch (reset.ordinal()) {
            case 1:
                Toast.makeText(this, R.string.message_reset_audio, Toast.LENGTH_SHORT).show();
                return;
            case 2:
                Toast.makeText(this, R.string.message_reset_effect, Toast.LENGTH_SHORT).show();
                return;
            default:
                return;
        }
    }

    private void showResetMediaDialog(int message, OnClickListener onClick, OnClickListener onCancel) {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.title_reset_media);
        builder.setMessage(message);
//        builder.setIcon(17301543);
        builder.setPositiveButton(R.string.text_continue, onClick);
        builder.setNegativeButton(R.string.text_cancel, onCancel);
        builder.create().show();
    }

    private int getCurrentVideoDuration() {
        return Math.round(Float.valueOf(this.mIntervalImage).floatValue() * ((float) this.mListImage.size()));
    }

    private String writeLoopMedia(String inputFile, int loop, String outputDir, String fileName) {
        String line = "file '" + inputFile + "'";
        String content = BuildConfig.FLAVOR;
        if (loop <= 1) {
            return null;
        }
        for (int i = 0; i < loop; i++) {
            content = content + line + "\n";
        }
        Timber.d(TAG, ">>>> TEXT CONTENT: " + content);
        return AppUtils.writeFileWithContent(content, outputDir, fileName);
    }

    private void showEditorPhoto() {
        PhotoEditorData photoEditorData = new PhotoEditorData();
        photoEditorData.setListPathPhoto(this.mListImage);
        photoEditorData.setPathFolderSaveTemp(AppConst.OUT_IMAGE_TEMP_FOLDER);
        photoEditorData.setPrefixOutImage(AppConst.PREFIX_OUT_IMAGE);
        photoEditorData.setNumerStartImage(1);
        photoEditorData.setWidthImage(AppConst.WIDTH_IMAGE);
        photoEditorData.setHeightImage(AppConst.HEIGHT_IMAGE);
        photoEditorData.setUrlApiSticker(AppConst.URL_STICKER);
        photoEditorData.setFormatOutImage(AppConst.FORMAT_FILLTER);
        photoEditorData.setPackageNameCrop(AppConst.PACKAGE_NAME_CROP);
        photoEditorData.setCurrentCropVersion(2);
        photoEditorData.setActionIntentFilterPhotoCrop(AppConst.ACTION_INTENT_FILTER_PHOTO_CROP);
        Intent mIntent = new Intent(this, PhotoEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PhotoEditorActivity.KEY_PHOTO_EDITOR_DATA, photoEditorData);
        mIntent.putExtras(bundle);
        startActivityForResult(mIntent, REQUEST_EDITOR_PHOTO);
    }
}
