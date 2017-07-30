package com.freelancer.videoeditor.util.view;

import android.graphics.Bitmap;

import com.freelancer.videoeditor.util.BaseList;
import com.freelancer.videoeditor.util.IHandler;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RectangleFilter extends RectangleBorder {
    public static int publicAlpha = 40;
    BaseList listFilter;

    public RectangleFilter(PhotoEditorActivity mainActivity, BaseList listFilter, float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(mainActivity, pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.listFilter = listFilter;
    }

    void reLoad(Bitmap pBitmap) {
        if (this.mBitmapTextureAtlasn != null) {
            this.mBitmapTextureAtlasn.unload();
        }
        if (this.mSpritePhoto != null) {
            this.mainActivity.getMainScene().unregisterTouchArea(this.mSpritePhoto);
            this.mainActivity.removeEntity(this.mSpritePhoto);
        }
        this.mBitmapTextureAtlasn = new BitmapTextureAtlas(this.mainActivity.getTextureManager(), pBitmap.getWidth(), pBitmap.getHeight(), BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
        this.mITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(this.mBitmapTextureAtlasn, getAtlasBitmap(pBitmap.getWidth(), pBitmap.getHeight(), pBitmap), 0, 0);
        this.mBitmapTextureAtlasn.load();
        float pH = (getWidth() * this.mITextureRegion.getHeight()) / this.mITextureRegion.getWidth();
        this.mSpritePhoto = new Sprite(0.0f, 0.0f, getWidth(), pH, this.mITextureRegion, this.mainActivity.mVertexBufferObjectManager);
        this.mSpritePhoto.setAlpha(0.0f);
        updatePositionAndWidthHeight();
        attachChild(this.mSpritePhoto);
        animationShow(this.mSpritePhoto);
        this.mainActivity.isSave = false;
        this.mainActivity.isSaveChange();
    }

    void animationShow(Sprite mSpritePhoto) {
        mSpritePhoto.registerEntityModifier(new AlphaModifier(0.3f, 0.0f, ((float) publicAlpha) / 100.0f) {
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                UtilLib.getInstance().handlerDoWork(new IHandler() {
                    public void doWork() {
                        RectangleFilter.this.listFilter.updateProgess();
                    }
                });
            }
        });
    }

    void updatePositionAndWidthHeight() {
        float pWidth = getWidth();
        float pHeight = (this.mSpritePhoto.getHeight() * pWidth) / this.mSpritePhoto.getWidth();
        if (pHeight < getHeight()) {
            pHeight = getHeight();
            pWidth = (this.mSpritePhoto.getWidth() * pHeight) / this.mSpritePhoto.getHeight();
        }
        this.mSpritePhoto.setWidth(pWidth);
        this.mSpritePhoto.setHeight(pHeight);
        this.mSpritePhoto.setPosition((getWidth() / 2.0f) - (this.mSpritePhoto.getWidth() / 2.0f), (getHeight() / 2.0f) - (this.mSpritePhoto.getHeight() / 2.0f));
    }

    public void updateAlpha(int alpha) {
        if (this.mSpritePhoto != null) {
            publicAlpha = alpha;
            this.mSpritePhoto.setAlpha(((float) publicAlpha) / 100.0f);
        }
    }
}
