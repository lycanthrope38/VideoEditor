package com.freelancer.videoeditor.util;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ItemTextSticker extends Sprite {
    RectangleBorderOfItemSelected mRectangleBorder;
    RectangleTextAndSticker mRectangleTextAndSticker;
    OnToolListener.OnSetSpriteForTools onSetSpriteForTools;

    public ItemTextSticker(RectangleTextAndSticker mRectangleTextAndSticker, RectangleBorderOfItemSelected mRectangleBorder, float pX, float pY, float pW, float pH, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pW, pH, pTextureRegion, pVertexBufferObjectManager);
        this.mRectangleTextAndSticker = mRectangleTextAndSticker;
        this.mRectangleBorder = mRectangleBorder;
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown() && RectanglePhoto.isTouch) {
            RectanglePhoto.isTouch = false;
            this.mRectangleTextAndSticker.setItemTextStickerSelected(this);
            if (this.onSetSpriteForTools != null) {
                this.onSetSpriteForTools.onSetSpriteForTools(this, 2);
            }
        }
        return false;
    }

    public OnToolListener.OnSetSpriteForTools getOnSetSpriteForTools() {
        return this.onSetSpriteForTools;
    }

    public void setOnSetSpriteForTools(OnToolListener.OnSetSpriteForTools onSetSpriteForTools) {
        this.onSetSpriteForTools = onSetSpriteForTools;
    }
}
