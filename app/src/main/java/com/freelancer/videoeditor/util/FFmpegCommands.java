package com.freelancer.videoeditor.util;

import com.freelancer.videoeditor.view.video.VideoEncode;


public class FFmpegCommands {
    private int FPS = 2;
    private String FRAME_RATE_LIMIT = "1";
    private String PREFIX_IMAGE_FILE = "image%d.jpg";
    private int QV = 1;
    private String SCALE_SIZE = "480:480";
    private int START_IMAGE_INDEX = 1;

    public static FFmpegCommands getInstance() {
        return new FFmpegCommands();
    }

    private FFmpegCommands() {
    }

    public FFmpegCommands setScaleSize(String scaleSize) {
        this.SCALE_SIZE = scaleSize;
        return this;
    }

    public FFmpegCommands setFPS(int fps) {
        this.FPS = fps;
        return this;
    }

    public FFmpegCommands setIntervalPicture(String frame_rate) {
        this.FRAME_RATE_LIMIT = frame_rate;
        return this;
    }

    public FFmpegCommands setStartImageIndex(int startIndex) {
        this.START_IMAGE_INDEX = startIndex;
        return this;
    }

    public FFmpegCommands setPrefixImageFile(String fileNameWithSymbol) {
        this.PREFIX_IMAGE_FILE = fileNameWithSymbol;
        return this;
    }

    public String[] genSlideImageCmd(String inputImageFolder, String outputMovieFolder, VideoEncode encode) {
        return ("-framerate 1/" + this.FRAME_RATE_LIMIT + " -start_number " + this.START_IMAGE_INDEX + " -i " + inputImageFolder + "/" + this.PREFIX_IMAGE_FILE + " -q:v " + this.QV + " -vcodec mpeg4 -preset ultrafast" + " -r " + this.FPS + " -pix_fmt yuv420p -vf scale=" + this.SCALE_SIZE + ",setsar=sar=1/1 " + (outputMovieFolder + encode.toString()) + " -y").split(" ");
    }

    public String[] genVideoWithAudio(String urlVideoTemp, String audioPath, String outputMovieFolder, VideoEncode encode, boolean ignoreShortest) {
        return ("-i " + urlVideoTemp + " -i " + audioPath + " -c copy -map 0:v -map 1:a -strict experimental " + (ignoreShortest ? "" : "-shortest ") + (outputMovieFolder + encode.toString()) + " -y").split(" ");
    }

    public String[] genVideoWithBorder(String urlVideo, String overlayImagePath, String outputMovieFolder, VideoEncode encode) {
        return ("-i " + urlVideo + " -i " + overlayImagePath + " -filter_complex [0:v][1:v]overlay=0:0 -vcodec mpeg4 -q:v " + this.QV + " -acodec copy " + (outputMovieFolder + encode.toString()) + " -y").split(" ");
    }

    public String[] genVideoWithFilter(String urlVideo, String overlayImagePath, String outputMovieFolder, VideoEncode encode) {
        return ("-i " + urlVideo + " -i " + overlayImagePath + " -filter_complex [0:v][1:v]overlay=0:0 -vcodec mpeg4 -q:v " + this.QV + " -acodec copy " + (outputMovieFolder + encode.toString()) + " -y").split(" ");
    }

    public String[] genVideoWithEffect(String urlVideo, String urlEffect, String outputMovieFolder, VideoEncode encode, boolean ignoreShortest) {
        return ("-i " + urlVideo + " -i " + urlEffect + " -filter_complex [0:v][1:v]blend=shortest=1:all_mode='normal':all_opacity=0.6" + " -vcodec mpeg4 -q:v " + this.QV + " -strict experimental " + (ignoreShortest ? "" : "-shortest ") + (outputMovieFolder + encode.toString()) + " -y").split(" ");
    }

    public String[] genVideoLoop(String txtPath, String outputAudioPath) {
        return ("-safe 0 -f concat -i " + txtPath + " -c copy " + outputAudioPath + " -y").split(" ");
    }

    public String[] genAudioLoop(String txtPath, String outputAudioPath) {
        return ("-safe 0 -f concat -i " + txtPath + " -c copy " + outputAudioPath + " -y").split(" ");
    }
}
