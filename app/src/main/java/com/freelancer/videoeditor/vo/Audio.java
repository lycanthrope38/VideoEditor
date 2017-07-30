package com.freelancer.videoeditor.vo;

import java.io.Serializable;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class Audio implements Serializable {
    private String artist;
    private String duration;
    private boolean isChecked = false;
    private String name;
    private String pathFile;
    private String pathFolder;
    private int seconds;

    public Audio(String name, String pathFile, String pathFolder, String duration, String artist) {
        this.name = name;
        this.pathFile = pathFile;
        this.pathFolder = pathFolder;
        this.duration = duration;
        this.artist = artist;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathFile() {
        return this.pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathFolder() {
        return this.pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}

