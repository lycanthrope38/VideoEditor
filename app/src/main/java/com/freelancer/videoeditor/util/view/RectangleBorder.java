package com.freelancer.videoeditor.util.view;

import android.graphics.Bitmap;
import android.view.View;

import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import libs.photoeditor.ui.dialog.CommonDialog;
import libs.photoeditor.ui.interfaces.OnLoadImageFromURL;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class RectangleBorder extends RectangleBaseClipping {
    private static final String TAG = "RectangleBorder";
    BitmapTextureAtlas mBitmapTextureAtlasn;
    ITextureRegion mITextureRegion;
    Sprite mSpritePhoto;

    public RectangleBorder(PhotoEditorActivity mainActivity, float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(mainActivity, pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        setColor(Color.TRANSPARENT);
    }

    void onLoadResource() {
    }

    void onAttach() {
    }

    public void removePhoto() {
        if (this.mBitmapTextureAtlasn != null) {
            this.mBitmapTextureAtlasn.unload();
        }
        if (this.mSpritePhoto != null) {
            this.mainActivity.getMainScene().unregisterTouchArea(this.mSpritePhoto);
            this.mainActivity.removeEntity(this.mSpritePhoto);
            this.mSpritePhoto = null;
        }
    }

    void reLoad(Bitmap pBitmap) {
        removePhoto();
        this.mBitmapTextureAtlasn = new BitmapTextureAtlas(this.mainActivity.getTextureManager(), pBitmap.getWidth(), pBitmap.getHeight(), BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
        this.mITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlasn, getAtlasBitmap(pBitmap.getWidth(), pBitmap.getHeight(), pBitmap), 0, 0);
        this.mBitmapTextureAtlasn.load();
        float pH = (getWidth() * this.mITextureRegion.getHeight()) / this.mITextureRegion.getWidth();
        this.mSpritePhoto = new Sprite(0.0f, 0.0f, getWidth(), pH, this.mITextureRegion, this.mainActivity.mVertexBufferObjectManager);
        this.mSpritePhoto.setAlpha(0.0f);
        attachChild(this.mSpritePhoto);
        animationShow(this.mSpritePhoto);
        this.mainActivity.isSave = false;
        this.mainActivity.isSaveChange();
    }

    void downloadFrame(String url, final OnLoadImageFromURL onLoad) {
        MyImageLoader.download(this.mainActivity, url, new FakeImageSimpleImageLoadingListener(MyImageLoader.getSingleFakeImage(this.mainActivity, url)) {
            public void onLoadingStarted(String imageUri, View view) {
            }

            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                onLoad.onFail();
            }

            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                onLoad.onCompleted(loadedImage);
            }

            public void onLoadingCancelled(String imageUri, View view) {
                RectangleBorder.this.load(imageUri, onLoad);
            }
        }, new ImageLoadingProgressListener() {
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                UtilLib.getInstance().updateDialogProgress((int) ((((float) current) / ((float) total)) * 100.0f));
            }
        });
    }

    public void load(String url, final OnLoadImageFromURL onLoadSuccessFail) {
        OnLoadImageFromURL mOnLoadImageFromURL = new OnLoadImageFromURL() {
            public void onCompleted(Bitmap mBitmap) {
                RectangleBorder.this.reLoad(mBitmap);
                if (onLoadSuccessFail != null) {
                    onLoadSuccessFail.onCompleted(mBitmap);
                }
                UtilLib.getInstance().hideLoadingDownload();
            }

            public void onFail() {
                if (onLoadSuccessFail != null) {
                    onLoadSuccessFail.onFail();
                }
                UtilLib.getInstance().hideLoadingDownload();
                UtilLib.getInstance().handlerDoWork(new IHandler() {
                    public void doWork() {
                        CommonDialog.netWorkNotConnect(RectangleBorder.this.mainActivity);
                    }
                });
            }
        };
        UtilLib.getInstance().showLoadingProgress(this.mainActivity);
        downloadFrame(url, mOnLoadImageFromURL);
    }

    void animationShow(Sprite mSpritePhoto) {
        mSpritePhoto.registerEntityModifier(new AlphaModifier(0.3f, 0.0f, HandlerTools.ROTATE_R));
    }
}
