package com.freelancer.videoeditor.util;

import android.graphics.Bitmap;

import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.view.base.BaseSprite;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.HashMap;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import timber.log.Timber;

public class RectangleTextAndSticker extends RectangleBaseClipping implements IButtonSprite {
    private static final int DRAG = 1;
    private static final int NONE = 0;
    private static final int ZOOM = 2;
    BaseSprite btnDeleted;
    ITextureRegion btnDeletedITR;
    private float d = 0.0f;
    HashMap<String, BitmapTextureAtlas> hashMapBitmapTextureAtlas = new HashMap();
    ItemTextSticker itemTextStickerForTool;
    ItemTextSticker itemTextStickerSelected;
    String key = "";
    private float[] lastEvent = null;
    float mD = 0.0f;
    float mScaleX;
    float mScaleY;
    private int mode = NONE;
    private float newRot = 0.0f;
    private float oldDist = HandlerTools.ROTATE_R;
    OnSetSpriteForTools onSetSpriteForTools;
    float pTouchAreaLocalX1;
    float pTouchAreaLocalX2;
    float pTouchAreaLocalXOld = 0.0f;
    float pTouchAreaLocalY1;
    float pTouchAreaLocalY2;
    float pTouchAreaLocalYOld = 0.0f;
    RectangleBorderOfItemSelected rectangleBorder;

    public RectangleTextAndSticker(PhotoEditorActivity mainActivity, float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(mainActivity, pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        setColor(Color.TRANSPARENT);
        this.rectangleBorder = new RectangleBorderOfItemSelected(0.0f, 0.0f, 10.0f, 10.0f, pVertexBufferObjectManager);
        this.rectangleBorder.setAlpha(0.0f);
        mainActivity.getMainScene().registerTouchArea(this);
    }

    public void onLoadResource() {
        this.btnDeletedITR = this.mainActivity.loadTextureRegion("button/", "btnDelete.png", 128, 128, this.mListBitmapTextureAtlas);
    }

    public void onAttach() {
        float pW = (float) (ConfigScreen.SCREENWIDTH / 15);
        this.btnDeleted = new BaseSprite(0.0f, 0.0f, pW, (this.btnDeletedITR.getHeight() * pW) / this.btnDeletedITR.getWidth(), this.btnDeletedITR, this.mVertexBufferObjectManager);
        this.btnDeleted.setmIButtonSprite(this);
        this.btnDeleted.setUserData(Integer.valueOf(-1000));
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        return touchPhoto(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    boolean touchPhoto(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (this.itemTextStickerSelected == null || RectanglePhoto.isTouchRectanglePhoto) {
            RectanglePhoto.isTouch = true;
            return false;
        }
        if (pSceneTouchEvent.isActionDown()) {
            if (pSceneTouchEvent.getPointerID() == 0) {
                this.mode = DRAG;
                this.lastEvent = null;
                this.pTouchAreaLocalX1 = pTouchAreaLocalX;
                this.pTouchAreaLocalY1 = pTouchAreaLocalY;
                this.pTouchAreaLocalXOld = pTouchAreaLocalX;
                this.pTouchAreaLocalYOld = pTouchAreaLocalY;
                this.mScaleX = this.itemTextStickerSelected.getScaleX();
                this.mScaleY = this.itemTextStickerSelected.getScaleY();
            } else if (pSceneTouchEvent.getPointerID() == DRAG) {
                this.pTouchAreaLocalX2 = pTouchAreaLocalX;
                this.pTouchAreaLocalY2 = pTouchAreaLocalY;
                this.oldDist = spacing();
                if (this.oldDist > 10.0f) {
                    this.mode = ZOOM;
                }
                this.lastEvent = new float[4];
                this.lastEvent[NONE] = this.pTouchAreaLocalX1;
                this.lastEvent[DRAG] = this.pTouchAreaLocalX2;
                this.lastEvent[ZOOM] = this.pTouchAreaLocalY1;
                this.lastEvent[3] = this.pTouchAreaLocalY2;
                this.d = rotation();
                this.mD = this.itemTextStickerSelected.getRotation();
            }
        } else if (pSceneTouchEvent.isActionUp()) {
            if (pSceneTouchEvent.getPointerID() == 0) {
                this.mode = NONE;
                this.lastEvent = null;
            } else if (pSceneTouchEvent.getPointerID() == DRAG) {
                this.mode = NONE;
                this.lastEvent = null;
            }
            this.itemTextStickerSelected = null;
            RectanglePhoto.isTouch = true;
            return false;
        } else if (pSceneTouchEvent.isActionMove()) {
            this.mainActivity.isSave = false;
            this.mainActivity.isSaveChange();
            if (pSceneTouchEvent.getPointerID() == 0) {
                this.pTouchAreaLocalX1 = pTouchAreaLocalX;
                this.pTouchAreaLocalY1 = pTouchAreaLocalY;
            } else if (pSceneTouchEvent.getPointerID() == DRAG) {
                this.pTouchAreaLocalX2 = pTouchAreaLocalX;
                this.pTouchAreaLocalY2 = pTouchAreaLocalY;
            }
            if (this.mode == DRAG) {
                float pXMove = pTouchAreaLocalX - this.pTouchAreaLocalXOld;
                float pYMove = pTouchAreaLocalY - this.pTouchAreaLocalYOld;
                this.pTouchAreaLocalXOld = pTouchAreaLocalX;
                this.pTouchAreaLocalYOld = pTouchAreaLocalY;
                this.itemTextStickerSelected.setPosition(this.itemTextStickerSelected.getX() + pXMove, this.itemTextStickerSelected.getY() + pYMove);
            } else if (this.mode == ZOOM) {
                float newDist = spacing();
                if (newDist > 10.0f) {
                    float scale = newDist / this.oldDist;
                    float pZoomX = 0.0f;
                    float pZoomY = 0.0f;
                    float tmp;
                    if (scale > HandlerTools.ROTATE_R) {
                        tmp = scale - HandlerTools.ROTATE_R;
                        if (this.mScaleX > 15.0f || this.mScaleY > 15.0f) {
                            tmp *= 50.0f;
                        } else if (this.mScaleX > 8.0f || this.mScaleY > 8.0f) {
                            tmp *= AppConst.ZOOM_MAX;
                        } else if (this.mScaleX > AppConst.ZOOM_MAX || this.mScaleY > AppConst.ZOOM_MAX) {
                            tmp *= 3.0f;
                        }
                        pZoomX = this.mScaleX + tmp;
                        pZoomY = this.mScaleY + tmp;
                    } else if (scale < HandlerTools.ROTATE_R) {
                        tmp = HandlerTools.ROTATE_R - scale;
                        if (this.mScaleX > 8.0f || this.mScaleY > 8.0f) {
                            tmp *= AppConst.ZOOM_MAX;
                        } else if (this.mScaleX > AppConst.ZOOM_MAX || this.mScaleY > AppConst.ZOOM_MAX) {
                            tmp *= 3.0f;
                        }
                        pZoomX = this.mScaleX - tmp;
                        pZoomY = this.mScaleY - tmp;
                    }
                    if (pZoomX > AppConst.ZOOM_MIN) {
                        if (pZoomX < AppConst.ZOOM_MIN) {
                            pZoomX = AppConst.ZOOM_MIN;
                        }
                        if (pZoomX > AppConst.ZOOM_MAX) {
                            pZoomX = AppConst.ZOOM_MAX;
                        }
                        this.itemTextStickerSelected.setScaleX(pZoomX);
                        updateScaleBtnDelete();
                    }
                    if (pZoomY > AppConst.ZOOM_MIN) {
                        if (pZoomY < AppConst.ZOOM_MIN) {
                            pZoomY = AppConst.ZOOM_MIN;
                        }
                        if (pZoomY > AppConst.ZOOM_MAX) {
                            pZoomY = AppConst.ZOOM_MAX;
                        }
                        this.itemTextStickerSelected.setScaleY(pZoomY);
                        updateScaleBtnDelete();
                    }
                }
                if (this.lastEvent != null) {
                    this.newRot = rotation();
                    this.itemTextStickerSelected.setRotation((this.newRot - this.d) + this.mD);
                }
            }
        }
        return true;
    }

    private float spacing() {
        float x = this.pTouchAreaLocalX1 - this.pTouchAreaLocalX2;
        float y = this.pTouchAreaLocalY1 - this.pTouchAreaLocalY2;
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private float rotation() {
        return (float) Math.toDegrees(Math.atan2((double) (this.pTouchAreaLocalY1 - this.pTouchAreaLocalY2), (double) (this.pTouchAreaLocalX1 - this.pTouchAreaLocalX2)));
    }

    public synchronized void addItem(Bitmap bitmap, int type) {
        this.mainActivity.isSave = false;
        this.mainActivity.isSaveChange();
        this.key = String.valueOf(System.currentTimeMillis());
        ITextureRegion mITextureRegion = loadITextureRegion(bitmap);
        float pW = (float) ((ConfigScreen.SCREENWIDTH / 5) * 3);
        float pH = (mITextureRegion.getHeight() * pW) / mITextureRegion.getWidth();
        ItemTextSticker mItemTextSticker = new ItemTextSticker(this, this.rectangleBorder, (getWidth() / 2.0f) - (pW / 2.0f), (getHeight() / 2.0f) - (pH / 2.0f), pW, pH, mITextureRegion, this.mVertexBufferObjectManager);
        mItemTextSticker.setOnSetSpriteForTools(this.mainActivity);
        if (this.onSetSpriteForTools != null) {
            this.onSetSpriteForTools.onSetSpriteForTools(mItemTextSticker, ZOOM);
        }
        mItemTextSticker.setUserData(this.key);
        if (type == ZOOM) {
            mItemTextSticker.setScale(0.5f);
        } else {
            mItemTextSticker.setScale(0.8f);
        }
        mItemTextSticker.setAlpha(0.0f);
        this.mainActivity.getMainScene().registerTouchArea(mItemTextSticker);
        final ItemTextSticker itemTextSticker = mItemTextSticker;
        mItemTextSticker.registerEntityModifier(new AlphaModifier(0.3f, 0.0f, HandlerTools.ROTATE_R) {
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                RectangleTextAndSticker.this.setItemTextStickerSelected(itemTextSticker);
            }
        });
        attachChild(mItemTextSticker);
    }

    synchronized ITextureRegion loadITextureRegion(Bitmap pBitmap) {
        ITextureRegion mITextureRegion;
        BitmapTextureAtlas mBitmapTextureAtlasn = new BitmapTextureAtlas(this.mainActivity.getTextureManager(), pBitmap.getWidth(), pBitmap.getHeight(), BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
        mITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlasn, getAtlasBitmap(pBitmap.getWidth(), pBitmap.getHeight(), pBitmap), NONE, NONE);
        mBitmapTextureAtlasn.load();
        this.hashMapBitmapTextureAtlas.put(this.key, mBitmapTextureAtlasn);
        return mITextureRegion;
    }

    public ItemTextSticker getItemTextStickerSelected() {
        return this.itemTextStickerSelected;
    }

    public void setItemTextStickerSelected(ItemTextSticker itemTextStickerSelected) {
        itemTextStickerSelected.detachSelf();
        sortChildren();
        attachChild(itemTextStickerSelected);
        this.mainActivity.getMainScene().unregisterTouchArea(itemTextStickerSelected);
        this.mainActivity.getMainScene().registerTouchArea(itemTextStickerSelected);
        this.rectangleBorder.detachSelf();
        this.rectangleBorder.showRectangleSelected(itemTextStickerSelected);
        this.btnDeleted.detachSelf();
        this.btnDeleted.setPosition(0.0f, 0.0f);
        itemTextStickerSelected.attachChild(this.btnDeleted);
        this.mainActivity.getMainScene().unregisterTouchArea(this.btnDeleted);
        this.mainActivity.getMainScene().registerTouchArea(this.btnDeleted);
        this.itemTextStickerSelected = itemTextStickerSelected;
        this.itemTextStickerForTool = itemTextStickerSelected;
        updateScaleBtnDelete();
    }

    public void updateScaleBtnDelete() {
        if (this.btnDeleted != null) {
            this.btnDeleted.setScale(this.itemTextStickerForTool.getWidth() / this.itemTextStickerForTool.getWidthScaled());
        }
    }

    synchronized void deleteItemTextSticker() {
        if (this.itemTextStickerForTool != null) {
            this.mainActivity.isSave = false;
            this.mainActivity.isSaveChange();
            String key = (String) this.itemTextStickerForTool.getUserData();
            this.itemTextStickerForTool.detachSelf();
            this.mainActivity.getMainScene().unregisterTouchArea(this.itemTextStickerForTool);
            this.mainActivity.removeEntity(this.itemTextStickerForTool);
            ((BitmapTextureAtlas) this.hashMapBitmapTextureAtlas.get(key)).unload();
            this.hashMapBitmapTextureAtlas.remove(key);
            hideRectangBorderAndButtonDeleted();
        }
    }

    public void hideRectangBorderAndButtonDeleted() {
        this.btnDeleted.detachSelf();
        this.mainActivity.getMainScene().unregisterTouchArea(this.btnDeleted);
        this.rectangleBorder.detachSelf();
        this.itemTextStickerSelected = null;
        this.itemTextStickerForTool = null;
    }

    public void removeAllSticker() {
        while (getChildCount() != 0) {
            ItemTextSticker mItemTextSticker = (ItemTextSticker) getChildByIndex(NONE);
            mItemTextSticker.detachSelf();
            this.mainActivity.mainScene.unregisterTouchArea(mItemTextSticker);
            this.mainActivity.removeEntity(mItemTextSticker);
        }
        this.itemTextStickerSelected = null;
        this.itemTextStickerForTool = null;
    }

    public Sprite onDown(Sprite mSprite) {
        return null;
    }

    public Sprite onUp(Sprite mSprite) {
        return null;
    }

    public Sprite onMove(Sprite mSprite) {
        return null;
    }

    public Sprite onClick(Sprite mSprite) {
        Timber.e("deleted item");
        deleteItemTextSticker();
        if (this.onSetSpriteForTools != null) {
            this.onSetSpriteForTools.onDeleteSprite();
        }
        return null;
    }

    public Sprite onMoveOut(Sprite mSprite) {
        return null;
    }

    public OnSetSpriteForTools getOnSetSpriteForTools() {
        return this.onSetSpriteForTools;
    }

    public void setOnSetSpriteForTools(OnSetSpriteForTools onSetSpriteForTools) {
        this.onSetSpriteForTools = onSetSpriteForTools;
    }
}
