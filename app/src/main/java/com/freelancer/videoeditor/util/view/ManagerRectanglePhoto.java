package com.freelancer.videoeditor.util.view;

import android.net.Uri;

import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.ArrayList;
import org.andengine.opengl.texture.region.ITextureRegion;

public class ManagerRectanglePhoto extends BaseObjectMainActivity {
    ITextureRegion btnAddITR;
    RectanglePhoto mRectanglePhotoSeleted = null;

    public ManagerRectanglePhoto(PhotoEditorActivity mainActivity) {
        super(mainActivity);
    }

    public void onLoadResource() {
        this.btnAddITR = this.mainActivity.loadTextureRegion("button/", "btnAdd.png", 80, 80, this.mListBitmapTextureAtlas);
    }

    public void onAttach() {
    }

    public void addRectanglePhoto(PhotoEditorActivity mainActivity, RectangleBaseClipping mRectangleMain, int type) {
        ArrayList<ItemFrame> mListItemFrames = new DefineFrame().getFrame(type, mRectangleMain.getWidth(), mRectangleMain.getHeight());
        for (int index = 0; index < mListItemFrames.size(); index++) {
            ItemFrame mItemFrame = (ItemFrame) mListItemFrames.get(index);
            RectanglePhoto mRectanglePhoto = new RectanglePhoto(mainActivity, this, this.btnAddITR, mItemFrame.getpXStart(), mItemFrame.getpYStart(), mItemFrame.getpWidth(), mItemFrame.getpHeight(), mainActivity.getVertexBufferObjectManager());
            mRectangleMain.attachChild(mRectanglePhoto);
            if (this.mRectanglePhotoSeleted == null) {
                this.mRectanglePhotoSeleted = mRectanglePhoto;
            }
        }
    }

    public void loadPhotoFromURI(Uri uri) {
        this.mRectanglePhotoSeleted.reLoad(uri);
    }

    public RectanglePhoto getmRectanglePhotoSeleted() {
        return this.mRectanglePhotoSeleted;
    }

    public void setmRectanglePhotoSeleted(RectanglePhoto mRectanglePhotoSeleted) {
        this.mRectanglePhotoSeleted = mRectanglePhotoSeleted;
    }
}
