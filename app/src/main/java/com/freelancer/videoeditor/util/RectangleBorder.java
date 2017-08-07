package com.freelancer.videoeditor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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


    public void load(Context context, String folder, String url) {
        Observable.fromCallable(() -> {
            Bitmap bm = getBitmapFromAssets(context, folder, url.substring(url.lastIndexOf(File.separator) + 1, url.length()));
            RectangleBorder.this.reLoad(bm);
            return "";
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public Bitmap getBitmapFromAssets(Context context, String folder, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(folder + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    void animationShow(Sprite mSpritePhoto) {
        mSpritePhoto.registerEntityModifier(new AlphaModifier(0.3f, 0.0f, HandlerTools.ROTATE_R));
    }
}
