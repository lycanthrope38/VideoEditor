package com.freelancer.videoeditor.view.photo;

import java.io.Serializable;
import java.util.ArrayList;

public class PhotoEditorData implements Serializable {
    private String actionIntentFilterPhotoCrop = "";
    private int currentCropVersion;
    private String formatOutImage;
    private int heightImage;
    private ArrayList<String> listPathPhoto;
    private int numerStartImage;
    private String packageNameCrop;
    private String pathFolderSaveTemp;
    private String prefixOutImage;
    private String urlApiSticker = "";
    private int widthImage;



    public String getUrlApiSticker() {
        return this.urlApiSticker;
    }

    public void setUrlApiSticker(String urlApiSticker) {
        this.urlApiSticker = urlApiSticker;
    }


    public int getHeightImage() {
        return this.heightImage;
    }

    public void setHeightImage(int heightImage) {
        this.heightImage = heightImage;
    }

    public ArrayList<String> getListPathPhoto() {
        return this.listPathPhoto;
    }

    public void setListPathPhoto(ArrayList<String> listPathPhoto) {
        this.listPathPhoto = listPathPhoto;
    }

    public String getPathFolderSaveTemp() {
        return this.pathFolderSaveTemp;
    }

    public void setPathFolderSaveTemp(String pathFolderSaveTemp) {
        this.pathFolderSaveTemp = pathFolderSaveTemp;
    }

    public String getPrefixOutImage() {
        return this.prefixOutImage;
    }

    public void setPrefixOutImage(String prefixOutImage) {
        this.prefixOutImage = prefixOutImage;
    }

    public int getWidthImage() {
        return this.widthImage;
    }

    public void setWidthImage(int widthImage) {
        this.widthImage = widthImage;
    }

    public int getNumerStartImage() {
        return this.numerStartImage;
    }

    public void setNumerStartImage(int numerStartImage) {
        this.numerStartImage = numerStartImage;
    }

    public String getFormatOutImage() {
        return this.formatOutImage;
    }

    public void setFormatOutImage(String formatOutImage) {
        this.formatOutImage = formatOutImage;
    }

    public String getPackageNameCrop() {
        return this.packageNameCrop;
    }

    public void setPackageNameCrop(String packageNameCrop) {
        this.packageNameCrop = packageNameCrop;
    }

    public int getCurrentCropVersion() {
        return this.currentCropVersion;
    }

    public void setCurrentCropVersion(int currentCropVersion) {
        this.currentCropVersion = currentCropVersion;
    }

    public String getActionIntentFilterPhotoCrop() {
        return this.actionIntentFilterPhotoCrop;
    }

    public void setActionIntentFilterPhotoCrop(String actionIntentFilterPhotoCrop) {
        this.actionIntentFilterPhotoCrop = actionIntentFilterPhotoCrop;
    }
}
