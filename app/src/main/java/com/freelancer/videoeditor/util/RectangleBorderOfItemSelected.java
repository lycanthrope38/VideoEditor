package com.freelancer.videoeditor.util;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class RectangleBorderOfItemSelected extends Rectangle {
    Rectangle mBottom;
    Rectangle mLeft;
    Rectangle mRight;
    Rectangle mTop;

    public RectangleBorderOfItemSelected(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.mTop = new Rectangle(0.0f, 0.0f, 10.0f, 2.0f, pVertexBufferObjectManager);
        this.mLeft = new Rectangle(0.0f, 0.0f, 2.0f, 10.0f, pVertexBufferObjectManager);
        this.mRight = new Rectangle(0.0f, 0.0f, 2.0f, 10.0f, pVertexBufferObjectManager);
        this.mBottom = new Rectangle(0.0f, 0.0f, 10.0f, 2.0f, pVertexBufferObjectManager);
        this.mTop.setColor(Color.YELLOW);
        this.mLeft.setColor(Color.YELLOW);
        this.mRight.setColor(Color.YELLOW);
        this.mBottom.setColor(Color.YELLOW);
        attachChild(this.mTop);
        attachChild(this.mLeft);
        attachChild(this.mRight);
        attachChild(this.mBottom);
    }

   public void showRectangleSelected(ItemTextSticker mItemTextSticker) {
        detachSelf();
        setWidth(mItemTextSticker.getWidth());
        setHeight(mItemTextSticker.getHeight());
        this.mTop.setWidth(getWidth());
        this.mBottom.setWidth(getWidth());
        this.mLeft.setHeight(getHeight());
        this.mRight.setHeight(getHeight());
        this.mRight.setX(getWidth() - this.mRight.getWidth());
        this.mBottom.setY(getHeight() - this.mBottom.getHeight());
        mItemTextSticker.attachChild(this);
    }
}
