package com.freelancer.videoeditor.util;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;

public abstract class FFMpegListener extends ExecuteBinaryResponseHandler {
    public abstract void onFFmpegCommandAlreadyRunning();

    public abstract void onUnSupportDevice(Exception exception);
}
