package com.freelancer.videoeditor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;

import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;


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

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RectanglePhoto extends RectangleBaseClipping {
    private static final int DRAG = 1;
    private static final int NONE = 0;
    private static final int ZOOM = 2;
    public static boolean isTouch = true;
    public static boolean isTouchRectanglePhoto = false;
    float MAX_SIZE = 0.0f;
    String PATH = "blur_border/";
    float PH_BORDER = AppConst.ZOOM_MAX;
    float PW_BORDER = AppConst.ZOOM_MAX;
    Bitmap bitmapPhoto = null;
    ITextureRegion border_1;
    ITextureRegion border_1_h;
    ITextureRegion border_2;
    ITextureRegion border_2_h;
    ITextureRegion border_goc;
    Sprite bottom;
    Sprite btnAdd;
    int color = -1;
    private float d = 0.0f;
    Sprite goc_bottomLeft;
    Sprite goc_bottomRight;
    Sprite goc_topLeft;
    Sprite goc_topRight;
    boolean isAdd = false;
    boolean isDown = false;
    private float[] lastEvent = null;
    Sprite left;
    ArrayList<Sprite> listBorder = new ArrayList();
    Color mColor;
    float mD = 0.0f;
    ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlas = new ArrayList();
    ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlasNoUnload = new ArrayList();
    ManagerRectanglePhoto mManagerRectanglePhoto;
    float mScaleX;
    float mScaleY;
    private int mode = NONE;
    private float newRot = 0.0f;
    private float oldDist = HandlerTools.ROTATE_R;
    OnToolListener.OnSetSpriteForTools onSetSpriteForTools;
    float onePercent = 0.0f;
    float pTouchAreaLocalX1;
    float pTouchAreaLocalX2;
    float pTouchAreaLocalXOld = 0.0f;
    float pTouchAreaLocalY1;
    float pTouchAreaLocalY2;
    float pTouchAreaLocalYOld = 0.0f;
    Sprite photo;
    Sprite photoBlur;
    ITextureRegion photoBlurITR;
    ITextureRegion photoITR;
    Sprite right;
    Sprite top;
    int type = NONE;
    Uri uriPathFile;

    public RectanglePhoto(PhotoEditorActivity mainActivity, ManagerRectanglePhoto mManagerRectanglePhoto, ITextureRegion btnAddITR, float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(mainActivity, pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.mManagerRectanglePhoto = mManagerRectanglePhoto;
        this.mColor = new Color(((float) getRandomIndex(NONE, 244)) / 255.0f, ((float) getRandomIndex(NONE, 244)) / 255.0f, ((float) getRandomIndex(NONE, 244)) / 255.0f);
        setColor(this.mColor);
        float pWHBtnAdd = (float) (ConfigScreen.SCREENWIDTH / 10);
        this.btnAdd = new Sprite((getWidth() / 2.0f) - (pWHBtnAdd / 2.0f), (getHeight() / 2.0f) - (pWHBtnAdd / 2.0f), pWHBtnAdd, pWHBtnAdd, btnAddITR, this.mVertexBufferObjectManager);
        attachChild(this.btnAdd);
        mainActivity.getMainScene().registerTouchArea(this);
        setOnSetSpriteForTools(mainActivity);
        onLoadResource();
    }

    public int getRandomIndex(int min, int max) {
        return ((int) (Math.random() * ((double) ((max - min) + DRAG)))) + min;
    }

    void onLoadResource() {
        this.border_1 = this.mainActivity.loadTextureRegion(this.PATH, "border_1.png", 500, 92, this.mListBitmapTextureAtlasNoUnload);
        this.border_2 = this.mainActivity.loadTextureRegion(this.PATH, "border_2.png", 500, 500, this.mListBitmapTextureAtlasNoUnload);
        this.border_1_h = this.mainActivity.loadTextureRegion(this.PATH, "border_1_h.png", 92, 500, this.mListBitmapTextureAtlasNoUnload);
        this.border_2_h = this.mainActivity.loadTextureRegion(this.PATH, "border_2_h.png", 500, 500, this.mListBitmapTextureAtlasNoUnload);
        this.border_goc = this.mainActivity.loadTextureRegion(this.PATH, "goc.png", BallSpinFadeLoaderIndicator.ALPHA, BallSpinFadeLoaderIndicator.ALPHA, this.mListBitmapTextureAtlasNoUnload);
        this.border_2.setTextureSize(this.border_2.getWidth() - HandlerTools.ROTATE_R, this.border_2.getHeight() - HandlerTools.ROTATE_R);
        this.border_2_h.setTextureSize(this.border_2_h.getWidth() - HandlerTools.ROTATE_R, this.border_2_h.getHeight() - HandlerTools.ROTATE_R);
        this.border_goc.setTextureSize(this.border_goc.getWidth() - HandlerTools.ROTATE_R, this.border_goc.getHeight() - HandlerTools.ROTATE_R);
    }

    void onAttach() {
    }

    public void resetPositionPhoto(float pW, float pH) {
        if (this.photo != null) {
            this.photo.setPosition((getWidth() / 2.0f) - (this.photo.getWidth() / 2.0f), (getHeight() / 2.0f) - (this.photo.getHeight() / 2.0f));
        }
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (!isTouch) {
            return false;
        }
        if (pSceneTouchEvent.isActionDown()) {
            this.mManagerRectanglePhoto.setmRectanglePhotoSeleted(this);
            isTouchRectanglePhoto = true;
        }
        if (this.photo != null) {
            return touchPhoto(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        }
        if (pSceneTouchEvent.isActionDown()) {
            setColor(Color.RED);
            this.btnAdd.setScale(1.1f);
            this.isDown = true;
        } else if (pSceneTouchEvent.isActionUp() && this.isDown) {
            setColor(this.mColor);
            this.btnAdd.setScale(HandlerTools.ROTATE_R);
            this.isDown = false;
            AppUtil.getInstance().handlerDoWork(new OnThreadListener.IHandler() {
                public void doWork() {
                }
            });
            this.mainActivity.rectangleTextAndSticker.hideRectangBorderAndButtonDeleted();
            isTouchRectanglePhoto = false;
        }
        return true;
    }

    boolean touchPhoto(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {
            if (this.onSetSpriteForTools != null) {
                this.onSetSpriteForTools.onSetSpriteForTools(this.photo, DRAG);
            }
            isTouchRectanglePhoto = true;
            if (pSceneTouchEvent.getPointerID() == 0) {
                this.mode = DRAG;
                this.lastEvent = null;
                this.pTouchAreaLocalX1 = pTouchAreaLocalX;
                this.pTouchAreaLocalY1 = pTouchAreaLocalY;
                this.pTouchAreaLocalXOld = pTouchAreaLocalX;
                this.pTouchAreaLocalYOld = pTouchAreaLocalY;
                this.mScaleX = this.photo.getScaleX();
                this.mScaleY = this.photo.getScaleY();
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
                this.mD = this.photo.getRotation();
            }
        } else if (pSceneTouchEvent.isActionUp()) {
            if (pSceneTouchEvent.getPointerID() == 0) {
                this.mode = NONE;
                this.lastEvent = null;
            } else if (pSceneTouchEvent.getPointerID() == DRAG) {
                this.mode = NONE;
                this.lastEvent = null;
            }
            isTouchRectanglePhoto = false;
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
                this.photo.setPosition(this.photo.getX() + pXMove, this.photo.getY() + pYMove);
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
                        this.photo.setScaleX(pZoomX);
                    }
                    if (pZoomY > AppConst.ZOOM_MIN) {
                        if (pZoomY < AppConst.ZOOM_MIN) {
                            pZoomY = AppConst.ZOOM_MIN;
                        }
                        if (pZoomY > AppConst.ZOOM_MAX) {
                            pZoomY = AppConst.ZOOM_MAX;
                        }
                        this.photo.setScaleY(pZoomY);
                    }
                }
                if (this.lastEvent != null) {
                    this.newRot = rotation();
                    this.photo.setRotation((this.newRot - this.d) + this.mD);
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

    public void reLoad(final Uri mImageCaptureUri) {
        AppUtil.getInstance().showLoading(this.mainActivity);
        Observable.fromCallable(() -> {
            Bitmap bitmap = RectanglePhoto.this.getBimapFromUri(mImageCaptureUri);
            if (bitmap != null) {
                RectanglePhoto.this.uriPathFile = mImageCaptureUri;
                RectanglePhoto.this.reLoad(bitmap);
                RectanglePhoto.this.removeBtnAdd();
                RectanglePhoto.this.setColor(Color.TRANSPARENT);
            }
            return "";
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    AppUtil.getInstance().hideLoading();
                });

    }

    public Bitmap getBimapFromUri(Uri mImageCaptureUri) {
        try {
            Bitmap bf;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(this.mainActivity.getContentResolver().openInputStream(mImageCaptureUri), null, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            options.inJustDecodeBounds = false;
            if (((float) imageWidth) > ((float) ConfigScreen.SCREENWIDTH) * 1.5f || ((float) imageHeight) > ((float) ConfigScreen.SCREENHEIGHT) * 1.5f) {
                options.inSampleSize = ZOOM;
                bf = BitmapFactory.decodeStream(this.mainActivity.getContentResolver().openInputStream(mImageCaptureUri), null, options);
            } else {
                bf = BitmapFactory.decodeStream(this.mainActivity.getContentResolver().openInputStream(mImageCaptureUri));
            }
            return autoRotateBitmap(scaleBitmap(bf), mImageCaptureUri.getEncodedPath());
        } catch (Exception e) {
            Timber.e("getBimapFromUri = " + e.toString());
            return null;
        }
    }

    private Bitmap autoRotateBitmap(Bitmap mBitmap, String pathFile) {
        try {
            int rotation = new ExifInterface(pathFile).getAttributeInt("Orientation", DRAG);
            if (rotation == 6) {
                return AppUtil.getInstance().rotateBitmap(mBitmap, 90.0f);
            }
            if (rotation == 3) {
                return AppUtil.getInstance().rotateBitmap(mBitmap, 180.0f);
            }
            if (rotation == 8) {
                return AppUtil.getInstance().rotateBitmap(mBitmap, 270.0f);
            }
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return mBitmap;
        }
    }

    public void reLoad(Bitmap pBitmap) {
        for (int i = NONE; i < this.mListBitmapTextureAtlas.size(); i += DRAG) {
            ((BitmapTextureAtlas) this.mListBitmapTextureAtlas.get(i)).unload();
        }
        this.mListBitmapTextureAtlas.clear();
        if (this.photo != null) {
            this.mainActivity.removeEntity(this.photo);
            this.photo = null;
        }
        if (this.photoBlur != null) {
            this.mainActivity.removeEntity(this.photoBlur);
            this.photoBlur = null;
        }
        this.photoITR = loadITextureRegion(pBitmap);
        this.bitmapPhoto = pBitmap;
        float pW = getWidth();
        float pH = (this.photoITR.getHeight() * pW) / this.photoITR.getWidth();
        if (pH > getHeight()) {
            pH = getHeight();
            pW = (this.photoITR.getWidth() * pH) / this.photoITR.getHeight();
        }
        this.photo = new Sprite((getWidth() / 2.0f) - (pW / 2.0f), (getHeight() / 2.0f) - (pH / 2.0f), pW, pH, this.photoITR, this.mVertexBufferObjectManager);
        this.photo.setZIndex(ZOOM);
        this.photo.setAlpha(0.0f);
        this.photo.registerEntityModifier(new AlphaModifier(0.3f, 0.0f, HandlerTools.ROTATE_R));
        attachChild(this.photo);
        if (this.onSetSpriteForTools != null) {
            this.onSetSpriteForTools.onSetSpriteForTools(this.photo, DRAG);
        }
    }

    public void addPhotoBlur() {
        if (this.photoBlur == null) {
            AppUtil.getInstance().showLoading(this.mainActivity);
            Observable.fromCallable(() -> {
                RectanglePhoto.this.bitmapPhoto = BlurBitmap.blurBitmap(RectanglePhoto.this.bitmapPhoto, HandlerTools.ROTATE_R, 10);
                RectanglePhoto.this.photoBlurITR = RectanglePhoto.this.loadITextureRegion(RectanglePhoto.this.bitmapPhoto);
                if (RectanglePhoto.this.photoBlur != null) {
                    RectanglePhoto.this.mainActivity.removeEntity(RectanglePhoto.this.photoBlur);
                    RectanglePhoto.this.photoBlur = null;
                }
                float pW = RectanglePhoto.this.getWidth();
                float pH = (RectanglePhoto.this.photoBlurITR.getHeight() * pW) / RectanglePhoto.this.photoBlurITR.getWidth();
                if (pH < RectanglePhoto.this.getHeight()) {
                    pH = RectanglePhoto.this.getHeight();
                    pW = (RectanglePhoto.this.photoBlurITR.getWidth() * pH) / RectanglePhoto.this.photoBlurITR.getHeight();
                }
                float pX = (RectanglePhoto.this.getWidth() / 2.0f) - (pW / 2.0f);
                float pY = (RectanglePhoto.this.getHeight() / 2.0f) - (pH / 2.0f);
                RectanglePhoto.this.photoBlur = new Sprite(pX, pY, pW, pH, RectanglePhoto.this.photoBlurITR, RectanglePhoto.this.mVertexBufferObjectManager);
                RectanglePhoto.this.photoBlur.setZIndex(RectanglePhoto.DRAG);
                RectanglePhoto.this.attachChild(RectanglePhoto.this.photoBlur);
                try {
                    RectanglePhoto.this.sortChildren();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        AppUtil.getInstance().hideLoading();
                        RectanglePhoto.this.mainActivity.isSave = false;
                        RectanglePhoto.this.mainActivity.isSaveChange();
                    });
        }
    }

    ITextureRegion loadITextureRegion(Bitmap pBitmap) {
        BitmapTextureAtlas mBitmapTextureAtlasn = new BitmapTextureAtlas(this.mainActivity.getTextureManager(), pBitmap.getWidth(), pBitmap.getHeight(), BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
        ITextureRegion mITextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlasn, getAtlasBitmap(pBitmap.getWidth(), pBitmap.getHeight(), pBitmap), NONE, NONE);
        mBitmapTextureAtlasn.load();
        this.mListBitmapTextureAtlas.add(mBitmapTextureAtlasn);
        return mITextureRegion;
    }

    public Bitmap scaleBitmap(Bitmap mBitmap) {
        if (((float) mBitmap.getWidth()) <= ((float) ConfigScreen.SCREENWIDTH) * 1.5f) {
            return mBitmap;
        }
        int scaleWidth = ConfigScreen.SCREENWIDTH;
        int scaleHeight = (int) (((float) mBitmap.getHeight()) * (((float) scaleWidth) / ((float) mBitmap.getWidth())));
        if (mBitmap.getWidth() > scaleWidth) {
            return Bitmap.createScaledBitmap(mBitmap, scaleWidth, scaleHeight, true);
        }
        if (mBitmap.getWidth() >= ConfigScreen.SCREENWIDTH / 8) {
            return mBitmap;
        }
        scaleWidth = ConfigScreen.SCREENWIDTH / 8;
        return Bitmap.createScaledBitmap(mBitmap, scaleWidth, (int) (((float) mBitmap.getHeight()) * (((float) scaleWidth) / ((float) mBitmap.getWidth()))), true);
    }

    void removeBtnAdd() {
        if (this.btnAdd != null) {
            this.mainActivity.removeEntity(this.btnAdd);
            this.btnAdd = null;
        }
    }

    public void resetPhoto() {
        if (this.photo != null) {
            this.photo.reset();
            this.photo.setPosition((getWidth() / 2.0f) - (this.photo.getWidth() / 2.0f), (getHeight() / 2.0f) - (this.photo.getHeight() / 2.0f));
        }
    }

    public OnToolListener.OnSetSpriteForTools getOnSetSpriteForTools() {
        return this.onSetSpriteForTools;
    }

    public void setOnSetSpriteForTools(OnToolListener.OnSetSpriteForTools onSetSpriteForTools) {
        this.onSetSpriteForTools = onSetSpriteForTools;
    }

    public Uri getUriPathFile() {
        return this.uriPathFile;
    }

    public void setUriPathFile(Uri uriPathFile) {
        this.uriPathFile = uriPathFile;
    }



    public void OnBorderClick(int type) {
        Timber.e("BLUR", "OnBorderClick type = " + type);
        if (type == 5) {
            if (this.listBorder.size() != 0) {
                AppUtil.getInstance().handlerDoWork(new OnThreadListener.IHandler() {
                    public void doWork() {
//                        RectanglePhoto.this.showDialogSelectColor(RectanglePhoto.this.mainActivity);
                    }
                });
            }
        } else if (type == 0) {
            this.type = type;
            borderNone();
        } else if (this.type != type) {
            this.type = type;
            addBorderForPhoto(type);
        }
    }

    synchronized void addBorderForPhoto(int type) {
        Timber.e("BLUR", "addBorderForPhoto type = " + type);
        if (this.photo != null) {
            if (!this.isAdd) {
                this.isAdd = true;
                this.mainActivity.isSave = false;
                this.mainActivity.isSaveChange();
                this.MAX_SIZE = this.photo.getWidth() / 6.0f;
                this.onePercent = this.MAX_SIZE / 100.0f;
                if (this.PH_BORDER <= 0.0f) {
                    this.PH_BORDER = (float) (ConfigScreen.SCREENWIDTH / 20);
                    this.PW_BORDER = this.PH_BORDER;
                }
                borderNone();
                ITextureRegion mTextureRegion_v = this.border_1;
                ITextureRegion mTextureRegion_h = this.border_1_h;
                if (type == ZOOM || type == 3) {
                    mTextureRegion_v = this.border_2;
                    mTextureRegion_h = this.border_2_h;
                }
                this.top = new Sprite(0.0f, 0.0f, this.photo.getWidth(), this.PH_BORDER, mTextureRegion_v, this.mVertexBufferObjectManager);
                this.bottom = new Sprite(0.0f, this.photo.getHeight() - this.PH_BORDER, this.photo.getWidth(), this.PH_BORDER, mTextureRegion_v, this.mVertexBufferObjectManager);
                this.left = new Sprite(0.0f, 0.0f, this.PW_BORDER, this.photo.getHeight(), mTextureRegion_h, this.mVertexBufferObjectManager);
                this.right = new Sprite(this.photo.getWidth() - this.PW_BORDER, 0.0f, this.PW_BORDER, this.photo.getHeight(), mTextureRegion_h, this.mVertexBufferObjectManager);
                this.photo.attachChild(this.top);
                this.photo.attachChild(this.bottom);
                this.photo.attachChild(this.left);
                this.photo.attachChild(this.right);
                if (type == ZOOM || type == 4) {
                    this.right.setFlippedHorizontal(true);
                    this.right.setFlippedVertical(true);
                    this.bottom.setFlippedHorizontal(true);
                    this.bottom.setFlippedVertical(true);
                    if (type == 4) {
                        float pWH = (this.PH_BORDER / this.border_1.getHeight()) * this.border_goc.getHeight();
                        this.goc_topLeft = new Sprite(0.0f, 0.0f, pWH, pWH, this.border_goc, this.mVertexBufferObjectManager);
                        this.goc_topRight = new Sprite(this.photo.getWidth() - pWH, 0.0f, pWH, pWH, this.border_goc, this.mVertexBufferObjectManager);
                        this.goc_bottomLeft = new Sprite(0.0f, this.photo.getHeight() - pWH, pWH, pWH, this.border_goc, this.mVertexBufferObjectManager);
                        this.goc_bottomRight = new Sprite(this.photo.getWidth() - pWH, this.photo.getHeight() - pWH, pWH, pWH, this.border_goc, this.mVertexBufferObjectManager);
                        this.goc_topRight.setFlippedHorizontal(true);
                        this.goc_bottomLeft.setFlippedVertical(true);
                        this.goc_bottomRight.setFlippedHorizontal(true);
                        this.goc_bottomRight.setFlippedVertical(true);
                        this.photo.attachChild(this.goc_topLeft);
                        this.photo.attachChild(this.goc_topRight);
                        this.photo.attachChild(this.goc_bottomLeft);
                        this.photo.attachChild(this.goc_bottomRight);
                        this.listBorder.add(this.goc_topLeft);
                        this.listBorder.add(this.goc_topRight);
                        this.listBorder.add(this.goc_bottomLeft);
                        this.listBorder.add(this.goc_bottomRight);
                    }
                } else if (type == 3) {
                    this.left.setFlippedVertical(true);
                    this.bottom.setFlippedHorizontal(true);
                    this.bottom.setFlippedVertical(true);
                    this.right.setFlippedHorizontal(true);
                }
                this.listBorder.add(this.top);
                this.listBorder.add(this.bottom);
                this.listBorder.add(this.left);
                this.listBorder.add(this.right);
                for (int i = NONE; i < this.listBorder.size(); i += DRAG) {
                    ((Sprite) this.listBorder.get(i)).setColor(((float) android.graphics.Color.red(this.color)) / 255.0f, ((float) android.graphics.Color.green(this.color)) / 255.0f, ((float) android.graphics.Color.blue(this.color)) / 255.0f, HandlerTools.ROTATE_R);
                }
                this.mainActivity.managerViewCenter.listBlur.updateProgress((int) ((this.PH_BORDER / this.MAX_SIZE) * 100.0f));
                this.isAdd = false;
            }
        }
    }

    public void resizeBorder(int size) {
        this.mainActivity.isSave = false;
        this.mainActivity.isSaveChange();
        this.PW_BORDER = this.onePercent * ((float) size);
        this.PH_BORDER = this.PW_BORDER;
        if (this.top != null) {
            this.top.setHeight(this.PH_BORDER);
        }
        if (this.bottom != null) {
            this.bottom.setHeight(this.PH_BORDER);
            this.bottom.setY(this.photo.getHeight() - this.bottom.getHeight());
        }
        if (this.left != null) {
            this.left.setWidth(this.PW_BORDER);
        }
        if (this.right != null) {
            this.right.setWidth(this.PW_BORDER);
            this.right.setX(this.photo.getWidth() - this.right.getWidth());
        }
        float pWH = (this.PH_BORDER / this.border_1.getHeight()) * this.border_goc.getHeight();
        if (this.goc_topLeft != null) {
            this.goc_topLeft.setWidth(pWH);
            this.goc_topLeft.setHeight(pWH);
        }
        if (this.goc_topRight != null) {
            this.goc_topRight.setWidth(pWH);
            this.goc_topRight.setHeight(pWH);
            this.goc_topRight.setX(this.photo.getWidth() - this.goc_topRight.getWidth());
        }
        if (this.goc_bottomLeft != null) {
            this.goc_bottomLeft.setWidth(pWH);
            this.goc_bottomLeft.setHeight(pWH);
            this.goc_bottomLeft.setY(this.photo.getHeight() - this.goc_bottomLeft.getHeight());
        }
        if (this.goc_bottomRight != null) {
            this.goc_bottomRight.setWidth(pWH);
            this.goc_bottomRight.setHeight(pWH);
            this.goc_bottomRight.setY(this.photo.getHeight() - this.goc_bottomRight.getHeight());
            this.goc_bottomRight.setX(this.photo.getWidth() - this.goc_bottomRight.getWidth());
        }
    }

    synchronized void borderNone() {
        this.mainActivity.isSave = false;
        this.mainActivity.isSaveChange();
        if (this.listBorder != null) {
            while (this.listBorder.size() != 0) {
                Sprite mSprite = (Sprite) this.listBorder.get(NONE);
                mSprite.detachSelf();
                this.listBorder.remove(NONE);
                this.mainActivity.removeEntity(mSprite);
            }
            this.listBorder.clear();
        }
    }
}
