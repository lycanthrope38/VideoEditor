package com.freelancer.videoeditor.view.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.view.base.BaseActivity;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import java.io.File;
import net.margaritov.preference.colorpicker.BuildConfig;

public class VideoSavedActivity extends BaseActivity {
    private static final int ORIGIN_HEIGHT_SCREEN = 1280;
    private static final int ORIGIN_WDITH_SCREEN = 720;
    private static final String TAG = "VideoSavedActivity";
    @BindView(R.id.button_back_home)
    ImageView buttonBackHome;
    @BindView(R.id.button_more)
    ImageView buttonMore;
    @BindView(R.id.button_rate)
    ImageView buttonRate;
    @BindView(R.id.button_share_video)
    ImageView buttonShare;
    @BindView(R.id.media_controller_saved)
    UniversalMediaController mMediaController;
    private String mVideoUrl;
    @BindView(R.id.root_header_video_saved)
    RelativeLayout rootHeader;
    @BindView(R.id.video_layout_saved)
    FrameLayout rootVideo;
    @BindView(R.id.text_button_video_saved)
    TextView textButtonSave;
    @BindView(R.id.text_view_path)
    TextView textFilePath;
    @BindView(R.id.video_view_saved)
    UniversalVideoView videoViewEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_saved);
        ButterKnife.bind(this);
        init();
        showVideo();
    }

    private void init() {
        this.mVideoUrl = getIntent().getStringExtra(AppConst.BUNDLE_KEY_VIDEO_URL);
        this.textFilePath.setText(getString(R.string.text_file_path, new Object[]{BuildConfig.FLAVOR + this.mVideoUrl}));
        this.videoViewEditor.setMediaController(this.mMediaController);
        if (getIntent().getBooleanExtra(AppConst.BUNDLE_KEY_VIDEO_OPEN_FROM_MY_VIDEO, false)) {
            String fileName = new File(this.mVideoUrl).getName();
            this.textButtonSave.setText(fileName.substring(0, fileName.lastIndexOf(FileUtils.HIDDEN_PREFIX)));
        }
        if (getIntent().getBooleanExtra(AppConst.BUNDLE_KEY_HOME, false)) {
            this.buttonBackHome.setImageResource(R.drawable.btn_home_selector);
        } else {
            this.buttonBackHome.setImageResource(com.piclistphotofromgallery.R.drawable.piclist_selector_button_back);
        }
        resizeLayout();
    }

    private void resizeLayout() {
        DisplayMetrics metrics = ExtraUtils.getDisplayInfo(this);
        int SCREEN_WIDTH = metrics.widthPixels;
        int heightTop = ((metrics.heightPixels - AppUtils.getNavigateBarHeight(this)) * 80) / ORIGIN_HEIGHT_SCREEN;
        this.rootHeader.getLayoutParams().height = heightTop;
        setSquareSize(this.buttonBackHome, (heightTop / 10) * 8);
        this.textButtonSave.getLayoutParams().height = heightTop;
        setSquareSize(this.rootVideo, SCREEN_WIDTH);
        setSquareSize(this.buttonShare, SCREEN_WIDTH / 5);
        setSquareSize(this.buttonRate, SCREEN_WIDTH / 5);
        setSquareSize(this.buttonMore, SCREEN_WIDTH / 5);
    }

    private void setSquareSize(View view, int size) {
        view.getLayoutParams().width = size;
        view.getLayoutParams().height = size;
    }

    private void stopVideo() {
        if (this.videoViewEditor.isPlaying()) {
            this.videoViewEditor.stopPlayback();
        }
    }

    private void showVideo() {
        if (TextUtils.isEmpty(this.mVideoUrl)) {
            T.show((int) R.string.message_no_video);
            return;
        }
        L.d(TAG, "VIDEO PLAY: " + this.mVideoUrl);
        stopVideo();
        this.mMediaController.setTitle(getVideoName());
        this.mMediaController.setFullscreenEnabled(false);
        this.videoViewEditor.setVideoPath(this.mVideoUrl);
        this.videoViewEditor.requestFocus();
        this.videoViewEditor.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                VideoSavedActivity.this.videoViewEditor.start();
            }
        });
        this.videoViewEditor.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                VideoSavedActivity.this.mMediaController.reset();
                VideoSavedActivity.this.videoViewEditor.requestFocus();
                VideoSavedActivity.this.videoViewEditor.start();
            }
        });
    }

    private String getVideoName() {
        return this.mVideoUrl.substring(this.mVideoUrl.lastIndexOf(File.separator) + 1, this.mVideoUrl.length());
    }

    @OnClick({2131689643, 2131689644, 2131689645, 2131689637})
    public void onClickShare(View view) {
        switch (view.getId()) {
            case R.id.button_back_home /*2131689637*/:
                finish();
                return;
            case R.id.button_share_video /*2131689643*/:
                ExtraUtils.shareVideoViaIntent(this, this.mVideoUrl, true);
                return;
            case com.libmoreutil.R.id.button_rate /*2131689644*/:
                ExtraUtils.openMarket(this, getPackageName());
                return;
            case R.id.button_more /*2131689645*/:
                UtilLib.getInstance().nextMyListAppOnGooglePlay(this, AppConst.NAME_STORE);
                return;
            default:
                return;
        }
    }

    protected void onPause() {
        super.onPause();
        this.videoViewEditor.pause();
    }

    protected void onResume() {
        super.onResume();
        this.videoViewEditor.resume();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
    }
}
