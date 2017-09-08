package com.freelancer.videoeditor.view.base;

import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.OnThreadListener;
import com.freelancer.videoeditor.util.OnViewListener;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BaseSprite extends Sprite {
    boolean isDown = false;
    boolean isMoveOut = true;
    boolean isScale;
    boolean isUp = true;
    OnViewListener.IButtonSprite mIButtonSprite;

    public BaseSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }

    public BaseSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, boolean isScale) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.isScale = isScale;
    }

    public BaseSprite(float pX, float pY, float width, float heigh, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, width, heigh, pTextureRegion, pVertexBufferObjectManager);
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (!isVisible()) {
            return false;
        }
        if (pSceneTouchEvent.isActionDown()) {
            if (this.isScale) {
                animation(HandlerTools.ROTATE_R, 1.2f, null);
            }
            this.isUp = true;
            this.isDown = true;
            if (this.mIButtonSprite != null) {
                this.mIButtonSprite.onDown(this);
            }
        } else if (pSceneTouchEvent.isActionUp()) {
            if (this.isUp && this.isDown) {
                registerEntityModifier(new DelayModifier(0.05f) {
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        if (BaseSprite.this.isScale) {
                            BaseSprite.this.animation(1.2f, HandlerTools.ROTATE_R, new OnThreadListener.IClose() {
                                public void onClose() {
                                    BaseSprite.this.registerEntityModifier(new DelayModifier(AppConst.ZOOM_MIN) {
                                        protected void onModifierFinished(IEntity pItem) {
                                            super.onModifierFinished(pItem);
                                            if (BaseSprite.this.mIButtonSprite != null && BaseSprite.this.getAlpha() == HandlerTools.ROTATE_R) {
                                                BaseSprite.this.mIButtonSprite.onClick(BaseSprite.this);
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            BaseSprite.this.mIButtonSprite.onClick(BaseSprite.this);
                        }
                    }
                });
            }
            if (this.mIButtonSprite != null) {
                this.mIButtonSprite.onUp(this);
            }
            this.isDown = false;
        } else if (pSceneTouchEvent.isActionMove()) {
            if ((pTouchAreaLocalX < HandlerTools.ROTATE_R || pTouchAreaLocalX > getWidth() - HandlerTools.ROTATE_R || pTouchAreaLocalY < HandlerTools.ROTATE_R || pTouchAreaLocalY > getHeight() - HandlerTools.ROTATE_R) && this.isMoveOut) {
                if (this.mIButtonSprite != null) {
                    this.mIButtonSprite.onMoveOut(this);
                }
                if (this.isUp && this.isScale) {
                    animation(1.2f, HandlerTools.ROTATE_R, null);
                }
                this.isUp = false;
                this.isDown = false;
                return false;
            } else if (this.mIButtonSprite != null) {
                this.mIButtonSprite.onMove(this);
            }
        } else if (pSceneTouchEvent.isActionOutside()) {
        }
        return true;
    }

    public OnViewListener.IButtonSprite getmIButtonSprite() {
        return this.mIButtonSprite;
    }

    public void setmIButtonSprite(OnViewListener.IButtonSprite mIButtonSprite) {
        this.mIButtonSprite = mIButtonSprite;
    }

    public void animation(float start, float end, OnThreadListener.IClose mIClose) {
        final OnThreadListener.IClose iClose = mIClose;
        registerEntityModifier(new ScaleModifier(0.05f, start, end) {
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                if (iClose != null) {
                    iClose.onClose();
                }
            }
        });
    }

    public void autoClick() {
        animation(HandlerTools.ROTATE_R, 1.2f, null);
        registerEntityModifier(new DelayModifier(AppConst.ZOOM_MIN) {
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                BaseSprite.this.animation(1.2f, HandlerTools.ROTATE_R, new OnThreadListener.IClose() {
                    public void onClose() {
                        if (BaseSprite.this.mIButtonSprite != null) {
                            BaseSprite.this.mIButtonSprite.onClick(BaseSprite.this);
                        }
                    }
                });
            }
        });
    }

    public boolean isMoveOut() {
        return this.isMoveOut;
    }

    public void setMoveOut(boolean isMoveOut) {
        this.isMoveOut = isMoveOut;
    }
}
