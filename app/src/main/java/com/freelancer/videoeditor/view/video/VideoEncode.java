package com.freelancer.videoeditor.view.video;

public enum VideoEncode {
    MP4(".mp4"),
    AVI(".avi");
    
    private String data;

    private VideoEncode(String data) {
        this.data = data;
    }

    public String toString() {
        return this.data;
    }
}
