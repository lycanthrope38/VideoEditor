package com.freelancer.videoeditor.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.ArrayList;

public class ViewBottom {
    public static int PHEIGHT_BOTTOM = 0;
    int PERCENT_BOTTOM = 30;
    LinearLayout btnBackground;
    LinearLayout btnBorder;
    LinearLayout btnBottomSelected;
    LinearLayout btnFilter;
    LinearLayout btnSticker;
    LinearLayout btnText;
    ImageView iconBackground;
    ImageView iconBorder;
    ImageView iconFilter;
    ImageView iconSticker;
    ImageView iconText;
    RelativeLayout layoutAllTools;
    RelativeLayout layoutBottom;
    LinearLayout layoutBottomTools;
    ArrayList<String> listPathPhoto;
    public ListPhoto listPhoto;
    PhotoEditorActivity mainActivity;
    OnViewListener.OnViewBottom onViewBottom;
    public ViewTools viewTools;

    public ViewBottom(PhotoEditorActivity mainActivity, ArrayList<String> listPathPhoto) {
        this.mainActivity = mainActivity;
        this.listPathPhoto = listPathPhoto;
        this.viewTools = new ViewTools(mainActivity);
    }

    public void findID(final View mView) {
        this.layoutBottom = (RelativeLayout) mView.findViewById(R.id.layoutBottom);
        this.layoutAllTools = (RelativeLayout) mView.findViewById(R.id.layoutAllTools);
        this.layoutBottomTools = (LinearLayout) this.layoutBottom.findViewById(R.id.layoutBottomTools);
        this.btnBorder = (LinearLayout) this.layoutBottom.findViewById(R.id.btnBorder);
        this.btnSticker = (LinearLayout) this.layoutBottom.findViewById(R.id.btnSticker);
        this.btnFilter = (LinearLayout) this.layoutBottom.findViewById(R.id.btnFilter);
        this.btnText = (LinearLayout) this.layoutBottom.findViewById(R.id.btnText);
        this.btnBackground = (LinearLayout) this.layoutBottom.findViewById(R.id.btnBackground);
        this.iconBorder = (ImageView) this.layoutBottom.findViewById(R.id.iconBorder);
        this.iconSticker = (ImageView) this.layoutBottom.findViewById(R.id.iconSticker);
        this.iconFilter = (ImageView) this.layoutBottom.findViewById(R.id.iconFilter);
        this.iconText = (ImageView) this.layoutBottom.findViewById(R.id.iconText);
        this.iconBackground = (ImageView) this.layoutBottom.findViewById(R.id.iconBackground);
        setOnClickListenerForButton();
        this.layoutBottom.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ExtraUtils.removeGlobalOnLayoutListener(ViewBottom.this.layoutBottom, this);
                ViewBottom.PHEIGHT_BOTTOM = (int) ((((float) ViewBottom.this.layoutBottom.getHeight()) / 100.0f) * ((float) ViewBottom.this.PERCENT_BOTTOM));
                int pHLayoutBottom = ViewBottom.this.layoutBottom.getHeight() - ViewBottom.PHEIGHT_BOTTOM;
                ViewBottom.this.iniSizeLayout();
                ViewBottom.this.viewTools.findID(ViewBottom.this.layoutBottom, ViewBottom.PHEIGHT_BOTTOM);
                ViewBottom.this.listPhoto = new ListPhoto(ViewBottom.this.mainActivity, mView, ViewBottom.this.listPathPhoto, pHLayoutBottom);
            }
        });
    }

    public void iniSizeLayout() {
        this.layoutAllTools.getLayoutParams().height = PHEIGHT_BOTTOM;
        int pWicon = (int) ((((float) PHEIGHT_BOTTOM) / 100.0f) * 40.0f);
        int pHicon = pWicon;
        setWHButton(this.iconBorder, pWicon, pHicon);
        setWHButton(this.iconSticker, pWicon, pHicon);
        setWHButton(this.iconText, pWicon, pHicon);
        setWHButton(this.iconBackground, pWicon, pHicon);
        setWHButton(this.iconFilter, pWicon, pHicon);
    }

    void setOnClickListenerForButton() {
        handlerButtonBottom(this.btnBorder);
        handlerButtonBottom(this.btnSticker);
        handlerButtonBottom(this.btnText);
        handlerButtonBottom(this.btnBackground);
        handlerButtonBottom(this.btnFilter);
    }

    public void handlerButtonBottom(final LinearLayout linearLayout) {
        linearLayout.setOnTouchListener(new OnTouchListener() {
            private Rect rect;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    linearLayout.setBackgroundColor(ViewBottom.this.mainActivity.getResources().getColor(R.color.bg_button_bottom_selected));
                    this.rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    return true;
                } else if (!this.rect.contains(v.getLeft() + ((int) event.getX()), v.getTop() + ((int) event.getY()))) {
                    linearLayout.setBackgroundColor(ViewBottom.this.mainActivity.getResources().getColor(R.color.bg_tran));
                    return false;
                } else if (event.getAction() != 1) {
                    return true;
                } else {
                    ViewBottom.this.onClick(linearLayout);
                    return true;
                }
            }
        });
    }

    public void onClick(LinearLayout linearLayout) {
        if (this.btnBottomSelected != null) {
            this.btnBottomSelected.setBackgroundColor(this.mainActivity.getResources().getColor(R.color.bg_tran));
            if (this.btnBottomSelected.getId() == linearLayout.getId()) {
                this.btnBottomSelected = null;
                if (this.onViewBottom != null) {
                    this.onViewBottom.UnCheck();
                    return;
                }
                return;
            }
        }
        this.btnBottomSelected = linearLayout;
        this.btnBottomSelected.setBackgroundColor(this.mainActivity.getResources().getColor(R.color.bg_button_bottom_selected));
        if (this.onViewBottom != null) {
            int id = this.btnBottomSelected.getId();
            if (id == R.id.btnBorder) {
                this.onViewBottom.OnBorder();
            } else if (id == R.id.btnBackground) {
                this.onViewBottom.OnBackground();
            } else if (id == R.id.btnFilter) {
                this.onViewBottom.OnFilter();
            } else if (id == R.id.btnText) {
                this.onViewBottom.OnText();
            } else if (id == R.id.btnSticker) {
                this.onViewBottom.OnSticker();
                this.btnBottomSelected.setBackgroundColor(this.mainActivity.getResources().getColor(R.color.bg_tran));
                this.onViewBottom.UnCheck();
                this.btnBottomSelected = null;
            }
        }
    }

    public void setUnCheckButtonBottom() {
        if (this.btnBottomSelected != null) {
            this.btnBottomSelected.setBackgroundColor(this.mainActivity.getResources().getColor(R.color.bg_tran));
            this.btnBottomSelected = null;
        }
    }

    void setWHButton(ImageView mButton, int pW, int pH) {
        mButton.getLayoutParams().height = pH;
        mButton.getLayoutParams().width = pW;
    }

    public void setVisibleLayoutBottom(final int visible, final boolean isAnimation) {
        if (visible != ViewBottom.this.layoutBottomTools.getVisibility()) {
            ViewBottom.this.layoutBottomTools.setVisibility(visible);
            if (!isAnimation) {
                return;
            }
            if (visible == 0) {
                ViewBottom.this.layoutBottomTools.startAnimation(AnimationUtils.loadAnimation(ViewBottom.this.mainActivity, R.anim.libphotoeditor_slide_in_bottom));
            } else if (visible == 8) {
                ViewBottom.this.layoutBottomTools.startAnimation(AnimationUtils.loadAnimation(ViewBottom.this.mainActivity, R.anim.libphotoeditor_slide_out_bottom));
            }
        }
    }

    public OnViewListener.OnViewBottom getOnViewBottom() {
        return this.onViewBottom;
    }

    public void setOnViewBottom(OnViewListener.OnViewBottom onViewBottom) {
        this.onViewBottom = onViewBottom;
    }
}
