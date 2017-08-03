package com.freelancer.videoeditor.util;

import android.content.Context;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public final class FFmpegUtils {
    private Context mContext;
    private final FFmpeg mFFmpeg;
    private boolean mIsLoadSuccess = true;
    private boolean mLoadDone = false;
    private FFMpegListener mMpegListener;

    public static synchronized FFmpegUtils newInstance(Context context) {
        FFmpegUtils fFmpegUtils;
        synchronized (FFmpegUtils.class) {
            fFmpegUtils = new FFmpegUtils(context);
        }
        return fFmpegUtils;
    }

    private FFmpegUtils(Context context) {
        this.mContext = context;
        this.mFFmpeg = FFmpeg.getInstance(this.mContext);
        this.mLoadDone = false;
    }

    private void showFFMpegRunning() {
        if (this.mMpegListener != null) {
            this.mMpegListener.onFFmpegCommandAlreadyRunning();
        }
    }

    private void showUnsupportedExceptionDialog(Exception ex) {
        if (this.mMpegListener != null) {
            this.mMpegListener.onUnSupportDevice(ex);
        }
    }

    public FFmpeg ffmpeg() {
        return this.mFFmpeg;
    }

    public FFmpegUtils listener(FFMpegListener listener) {
        this.mMpegListener = listener;
        return this;
    }

    public synchronized boolean loadFFMpegBinary() {
        boolean z = true;
        synchronized (this) {
            if (!this.mLoadDone) {
                this.mIsLoadSuccess = true;
                try {
                    ffmpeg().loadBinary(new LoadBinaryResponseHandler() {
                        public void onSuccess() {
                            FFmpegUtils.this.mLoadDone = true;
                        }

                        public void onFailure() {
                            FFmpegUtils.this.showUnsupportedExceptionDialog(new Exception("Your device is not supported"));
                            FFmpegUtils.this.mIsLoadSuccess = false;
                        }
                    });
                } catch (FFmpegNotSupportedException e) {
                    showUnsupportedExceptionDialog(e);
                    this.mIsLoadSuccess = false;
                }
                z = this.mIsLoadSuccess;
            }
        }
        return z;
    }

    public synchronized void executeCommand(String[] cmd) {
        if (!this.mIsLoadSuccess || ffmpeg().isFFmpegCommandRunning()) {
            showFFMpegRunning();
        } else {
            try {
                ffmpeg().execute(cmd, this.mMpegListener);
            } catch (FFmpegCommandAlreadyRunningException e) {
                showFFMpegRunning();
            }
        }
    }

    public boolean cancelAllProcess() {
        return ffmpeg().killRunningProcesses();
    }
}
