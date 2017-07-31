package com.freelancer.videoeditor.vo;

import java.io.Serializable;

public class PhotoFrames implements Serializable {
    private String cover;
    private String folder;
    private String icon;
    private String name;
    private int totalImage;
    private int typeFrame;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getTotalImage() {
        return this.totalImage;
    }

    public void setTotalImage(int totalImage) {
        this.totalImage = totalImage;
    }

    public int getTypeFrame() {
        return this.typeFrame;
    }

    public void setTypeFrame(int typeFrame) {
        this.typeFrame = typeFrame;
    }
}
