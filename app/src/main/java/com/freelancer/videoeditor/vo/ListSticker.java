package com.freelancer.videoeditor.vo;

import java.io.Serializable;

public class ListSticker implements Serializable {
    private String folder;
    private String icon;
    private String name;
    private int totalImage;

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

    public int getTotalImage() {
        return this.totalImage;
    }

    public void setTotalImage(int totalImage) {
        this.totalImage = totalImage;
    }
}
