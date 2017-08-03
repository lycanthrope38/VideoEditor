package com.freelancer.videoeditor.vo;

import java.util.List;

public class ListVideoEffect {
    private int duration;
    private String fileName;
    private int id;
    private List<ListVideoEffect> listVideoEffect;
    private String thumb;
    private int thumblocalResId;
    private String videoUrl;

    public List<ListVideoEffect> getListVideoEffect() {
        return this.listVideoEffect;
    }

    public void setListVideoEffect(List<ListVideoEffect> listVideoEffect) {
        this.listVideoEffect = listVideoEffect;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return this.thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getThumblocalResId() {
        return this.thumblocalResId;
    }

    public void setThumblocalResId(int thumblocalResId) {
        this.thumblocalResId = thumblocalResId;
    }
}
