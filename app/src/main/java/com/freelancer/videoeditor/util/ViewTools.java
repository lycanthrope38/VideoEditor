package com.freelancer.videoeditor.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

public class ViewTools {
    LinearLayout btnChangePhoto;
    LinearLayout btnClose;
    LinearLayout btnFlipH;
    LinearLayout btnFlipV;
    LinearLayout btnRotateL;
    LinearLayout btnRotateR;
    LinearLayout btnZoomIn;
    LinearLayout btnZoomOut;
    ImageView iconChangePhoto;
    ImageView iconClose;
    ImageView iconFlipH;
    ImageView iconFlipV;
    ImageView iconRotateL;
    ImageView iconRotateR;
    ImageView iconZoomIn;
    ImageView iconZoomOut;
    boolean isFistShowToolsBottom = false;
    LinearLayout layoutTools;
    PhotoEditorActivity mainActivity;
    OnViewTools onViewTools;

    public ViewTools(PhotoEditorActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void findID(RelativeLayout layoutBottom, int pWHButton) {
        this.layoutTools = (LinearLayout) layoutBottom.findViewById(R.id.layoutTools);
        this.btnChangePhoto = (LinearLayout) layoutBottom.findViewById(R.id.btnCrop);
        this.btnFlipV = (LinearLayout) layoutBottom.findViewById(R.id.btnFlipV);
        this.btnFlipH = (LinearLayout) layoutBottom.findViewById(R.id.btnFlipH);
        this.btnZoomIn = (LinearLayout) layoutBottom.findViewById(R.id.btnZoomIn);
        this.btnZoomOut = (LinearLayout) layoutBottom.findViewById(R.id.btnZoomOut);
        this.btnRotateL = (LinearLayout) layoutBottom.findViewById(R.id.btnRotateL);
        this.btnRotateR = (LinearLayout) layoutBottom.findViewById(R.id.btnRotateR);
        this.btnClose = (LinearLayout) layoutBottom.findViewById(R.id.btnClose);
        this.iconChangePhoto = (ImageView) layoutBottom.findViewById(R.id.iconCrop);
        this.iconFlipV = (ImageView) layoutBottom.findViewById(R.id.iconFlipV);
        this.iconFlipH = (ImageView) layoutBottom.findViewById(R.id.iconFlipH);
        this.iconZoomIn = (ImageView) layoutBottom.findViewById(R.id.iconZoomIn);
        this.iconZoomOut = (ImageView) layoutBottom.findViewById(R.id.iconZoomOut);
        this.iconRotateL = (ImageView) layoutBottom.findViewById(R.id.iconRotateL);
        this.iconRotateR = (ImageView) layoutBottom.findViewById(R.id.iconRotateR);
        this.iconClose = (ImageView) layoutBottom.findViewById(R.id.iconClose);
        setOnClickListenerForButton();
        iniSizeLayout(pWHButton);
        setOnViewTools(this.mainActivity);
    }

    public void iniSizeLayout(int pWHButton) {
        int pWicon = (int) ((((float) pWHButton) / 100.0f) * 50.0f);
        int pHicon = pWicon;
        setWHButton(this.iconChangePhoto, pWicon, pHicon);
        setWHButton(this.iconFlipV, pWicon, pHicon);
        setWHButton(this.iconZoomIn, pWicon, pHicon);
        setWHButton(this.iconZoomOut, pWicon, pHicon);
        setWHButton(this.iconRotateL, pWicon, pHicon);
        setWHButton(this.iconFlipH, pWicon, pHicon);
        setWHButton(this.iconRotateL, pWicon, pHicon);
        setWHButton(this.iconRotateR, pWicon, pHicon);
        setWHButton(this.iconClose, pWicon, pHicon);
    }

    void setOnClickListenerForButton() {
        handlerButtonTools(this.btnChangePhoto);
        handlerButtonTools(this.btnFlipV);
        handlerButtonTools(this.btnZoomIn);
        handlerButtonTools(this.btnZoomOut);
        handlerButtonTools(this.btnRotateL);
        handlerButtonTools(this.btnFlipH);
        handlerButtonTools(this.btnRotateL);
        handlerButtonTools(this.btnRotateR);
        handlerButtonTools(this.btnClose);
    }

    public void handlerButtonTools(final View view) {
        view.setOnTouchListener(new OnTouchListener() {
            private Rect rect;

            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if (event.getAction() == 0) {
                    view.setBackgroundColor(ViewTools.this.mainActivity.getResources().getColor(R.color.bg_button_bottom_selected));
                    this.rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    ViewTools.this.onClick(id, 0);
                } else if (!this.rect.contains(v.getLeft() + ((int) event.getX()), v.getTop() + ((int) event.getY()))) {
                    view.setBackgroundColor(ViewTools.this.mainActivity.getResources().getColor(R.color.bg_tran));
                    ViewTools.this.onClick(id, 1);
                    return false;
                } else if (event.getAction() == 1) {
                    view.setBackgroundColor(ViewTools.this.mainActivity.getResources().getColor(R.color.bg_tran));
                    ViewTools.this.onClick(id, 1);
                }
                return true;
            }
        });
    }

    public void onClick(int id, int action) {
        if (id == R.id.btnCrop) {
            if (action == 1) {
                this.onViewTools.onCrop();
            }
        } else if (id == R.id.btnFlipH) {
            if (action == 1) {
                this.onViewTools.onFlipH();
            }
        } else if (id == R.id.btnFlipV) {
            if (action == 1) {
                this.onViewTools.onFlipV();
            }
        } else if (id == R.id.btnZoomIn) {
            this.onViewTools.onZoomIn(action);
        } else if (id == R.id.btnZoomOut) {
            this.onViewTools.onZoomOut(action);
        } else if (id == R.id.btnRotateL) {
            this.onViewTools.onRotateL(action);
        } else if (id == R.id.btnRotateR) {
            this.onViewTools.onRotateR(action);
        } else if (id == R.id.btnClose && action == 1) {
            setVisibleLayoutTools(8, true, 1);
        }
    }

    public void setVisibleLayoutTools(final int visible, final boolean isAnimation, final int type) {
        UtilLib.getInstance().handlerDoWork(new IHandler() {
            public void doWork() {
                if (ViewTools.this.isFistShowToolsBottom) {
                    if (visible == 0) {
                        if (type == 1) {
                            ViewTools.this.btnChangePhoto.setVisibility(0);
                        } else {
                            ViewTools.this.btnChangePhoto.setVisibility(8);
                        }
                    }
                    if (visible != ViewTools.this.layoutTools.getVisibility()) {
                        ViewTools.this.layoutTools.setVisibility(visible);
                        ViewTools.this.onViewTools.onShowHide(visible);
                        if (!isAnimation) {
                            return;
                        }
                        if (visible == 0) {
                            ViewTools.this.layoutTools.startAnimation(AnimationUtils.loadAnimation(ViewTools.this.mainActivity, R.anim.libphotoeditor_slide_in_bottom));
                            return;
                        } else if (visible == 8) {
                            ViewTools.this.layoutTools.startAnimation(AnimationUtils.loadAnimation(ViewTools.this.mainActivity, R.anim.libphotoeditor_slide_out_bottom));
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                ViewTools.this.isFistShowToolsBottom = true;
            }
        });
    }

    void setWHButton(ImageView mButton, int pW, int pH) {
        mButton.getLayoutParams().height = pH;
        mButton.getLayoutParams().width = pW;
    }

    public OnViewTools getOnViewTools() {
        return this.onViewTools;
    }

    public void setOnViewTools(OnViewTools onViewTools) {
        this.onViewTools = onViewTools;
    }
}
