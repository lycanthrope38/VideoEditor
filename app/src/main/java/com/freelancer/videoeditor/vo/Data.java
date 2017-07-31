package com.freelancer.videoeditor.vo;

import java.util.List;

public class Data {
    private List<PhotoFrames> listPhotoFrames;
    private List<ListSticker> listStickers;

    public List<ListSticker> getListCateSticker() {
        return this.listStickers;
    }

    public void setListCateSticker(List<ListSticker> listCateStatus) {
        this.listStickers = listCateStatus;
    }

    public List<PhotoFrames> getListPhotoFrames() {
        return this.listPhotoFrames;
    }

    public void setListPhotoFrames(List<PhotoFrames> listPhotoFrames) {
        this.listPhotoFrames = listPhotoFrames;
    }
}
