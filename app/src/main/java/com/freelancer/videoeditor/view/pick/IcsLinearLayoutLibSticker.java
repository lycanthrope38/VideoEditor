package com.freelancer.videoeditor.view.pick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

class IcsLinearLayoutLibSticker extends LinearLayout {
    private static final int[] LL = new int[]{16843049, 16843561, 16843562};
    private static final int LL_DIVIDER = 0;
    private static final int LL_DIVIDER_PADDING = 2;
    private static final int LL_SHOW_DIVIDER = 1;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mShowDividers;

    public IcsLinearLayoutLibSticker(Context context, int themeAttr) {
        super(context);
        TypedArray a = context.obtainStyledAttributes(null, LL, themeAttr, LL_DIVIDER);
        setDividerDrawable(a.getDrawable(LL_DIVIDER));
        this.mDividerPadding = a.getDimensionPixelSize(LL_DIVIDER_PADDING, LL_DIVIDER);
        this.mShowDividers = a.getInteger(LL_SHOW_DIVIDER, LL_DIVIDER);
        a.recycle();
    }

    public void setDividerDrawable(Drawable divider) {
        boolean z = false;
        if (divider != this.mDivider) {
            this.mDivider = divider;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = LL_DIVIDER;
                this.mDividerHeight = LL_DIVIDER;
            }
            if (divider == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        int index = indexOfChild(child);
        int orientation = getOrientation();
        LayoutParams params = (LayoutParams) child.getLayoutParams();
        if (hasDividerBeforeChildAt(index)) {
            if (orientation == LL_SHOW_DIVIDER) {
                params.topMargin = this.mDividerHeight;
            } else {
                params.leftMargin = this.mDividerWidth;
            }
        }
        int count = getChildCount();
        if (index == count - 1 && hasDividerBeforeChildAt(count)) {
            if (orientation == LL_SHOW_DIVIDER) {
                params.bottomMargin = this.mDividerHeight;
            } else {
                params.rightMargin = this.mDividerWidth;
            }
        }
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (getOrientation() == LinearLayout.VERTICAL) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
        super.onDraw(canvas);
    }

    private void drawDividersVertical(Canvas canvas) {
        int count = getChildCount();
        int i = LL_DIVIDER;
        while (i < count) {
            View child = getChildAt(i);
            if (!(child == null || child.getVisibility() == GONE || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin);
            }
            i += LL_SHOW_DIVIDER;
        }
        if (hasDividerBeforeChildAt(count)) {
            int bottom;
            View child = getChildAt(count - 1);
            if (child == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child.getBottom();
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    private void drawDividersHorizontal(Canvas canvas) {
        int count = getChildCount();
        int i = LL_DIVIDER;
        while (i < count) {
            View child = getChildAt(i);
            if (!(child == null || child.getVisibility() == GONE || !hasDividerBeforeChildAt(i))) {
                drawVerticalDivider(canvas, child.getLeft() - ((LayoutParams) child.getLayoutParams()).leftMargin);
            }
            i += LL_SHOW_DIVIDER;
        }
        if (hasDividerBeforeChildAt(count)) {
            int right;
            View child = getChildAt(count - 1);
            if (child == null) {
                right = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            } else {
                right = child.getRight();
            }
            drawVerticalDivider(canvas, right);
        }
    }

    private void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(LL_DIVIDER, LL_DIVIDER, LL_DIVIDER, LL_DIVIDER);
        this.mDivider.draw(canvas);
    }

    private void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    private boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0 || childIndex == getChildCount() || (this.mShowDividers & LL_DIVIDER_PADDING) == 0) {
            return false;
        }
        for (int i = childIndex - 1; i >= 0; i--) {
            if (getChildAt(i).getVisibility() != GONE) {
                return true;
            }
        }
        return false;
    }
}
