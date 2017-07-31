package com.freelancer.videoeditor.vo;

public class ItemFrame {
    int order = 0;
    float pHeight = 0.0f;
    float pWidth = 0.0f;
    float pXStart = 0.0f;
    float pYStart = 0.0f;

    public ItemFrame(int order, float pXStart, float pYStart, float pWidth, float pHeight) {
        this.order = order;
        this.pXStart = pXStart;
        this.pYStart = pYStart;
        this.pWidth = pWidth;
        this.pHeight = pHeight;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getpXStart() {
        return this.pXStart;
    }

    public void setpXStart(float pXStart) {
        this.pXStart = pXStart;
    }

    public float getpYStart() {
        return this.pYStart;
    }

    public void setpYStart(float pYStart) {
        this.pYStart = pYStart;
    }

    public float getpWidth() {
        return this.pWidth;
    }

    public void setpWidth(float pWidth) {
        this.pWidth = pWidth;
    }

    public float getpHeight() {
        return this.pHeight;
    }

    public void setpHeight(float pHeight) {
        this.pHeight = pHeight;
    }
}
