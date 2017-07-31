package com.freelancer.videoeditor.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

public class ListBlur implements OnClickListener, OnSeekBarChangeListener {
    public static final int BORDER_1 = 1;
    public static final int BORDER_2 = 2;
    public static final int BORDER_3 = 3;
    public static final int BORDER_4 = 4;
    public static final int COLOR = 5;
    public static final int NONE = 0;
    private static final String TAG = "ListBlur";
    ImageView btnBorder_1;
    ImageView btnBorder_2;
    ImageView btnBorder_3;
    ImageView btnBorder_4;
    ImageView btnBorder_none;
    ImageView btnColor;
    RelativeLayout layoutParentBlur;
    PhotoEditorActivity mainActivity;
    OnToolsBlur onToolsBlur;
    SeekBar seekBarToolsPhoto;

    public ListBlur(PhotoEditorActivity mainActivity, RelativeLayout layoutCenter) {
        this.mainActivity = mainActivity;
        findID(layoutCenter);
    }

    public void findID(View mView) {
        this.layoutParentBlur = (RelativeLayout) mView.findViewById(R.id.layoutParentBlur);
        this.layoutParentBlur.setOnClickListener(this);
        this.seekBarToolsPhoto = (SeekBar) this.layoutParentBlur.findViewById(R.id.seekBarToolsPhoto);
        this.seekBarToolsPhoto.setOnSeekBarChangeListener(this);
        this.btnColor = (ImageView) this.layoutParentBlur.findViewById(R.id.btnColor);
        this.btnBorder_none = (ImageView) this.layoutParentBlur.findViewById(R.id.btnBorder_none);
        this.btnBorder_1 = (ImageView) this.layoutParentBlur.findViewById(R.id.btnBorder_1);
        this.btnBorder_2 = (ImageView) this.layoutParentBlur.findViewById(R.id.btnBorder_2);
        this.btnBorder_3 = (ImageView) this.layoutParentBlur.findViewById(R.id.btnBorder_3);
        this.btnBorder_4 = (ImageView) this.layoutParentBlur.findViewById(R.id.btnBorder_4);
        handlerButtonTools(this.btnColor);
        handlerButtonTools(this.btnBorder_none);
        handlerButtonTools(this.btnBorder_1);
        handlerButtonTools(this.btnBorder_2);
        handlerButtonTools(this.btnBorder_3);
        handlerButtonTools(this.btnBorder_4);
    }

    public void setVisibleLayoutBorder(final int visible, final boolean isAnimation) {
        UtilLib.getInstance().handlerDoWork(new IHandler() {
            public void doWork() {
                ListBlur.this.layoutParentBlur.setVisibility(visible);
                if (isAnimation) {
                    ListBlur.this.layoutParentBlur.startAnimation(AnimationUtils.loadAnimation(ListBlur.this.mainActivity, R.anim.libphotoeditor_fade_in));
                }
            }
        });
    }

    public void handlerButtonTools(View view) {
        view.setOnTouchListener(new OnTouchListener() {
            private Rect rect;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    v.setAlpha(0.7f);
                } else if (event.getAction() == ListBlur.BORDER_1) {
                    ListBlur.this.onClick(v);
                    v.setAlpha(HandlerTools.ROTATE_R);
                }
                return true;
            }
        });
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.seekBarToolsPhoto != null) {
            this.onToolsBlur.OnSeekBarChange(progress);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void updateProgress(final int progress) {
        UtilLib.getInstance().handlerDoWork(new IHandler() {
            public void doWork() {
                if (ListBlur.this.seekBarToolsPhoto != null) {
                    ListBlur.this.seekBarToolsPhoto.setProgress(progress);
                }
            }
        });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnColor) {
            this.onToolsBlur.OnBorderClick(COLOR);
        } else if (i == R.id.btnBorder_none) {
            this.onToolsBlur.OnBorderClick(NONE);
            if (this.seekBarToolsPhoto != null) {
                this.seekBarToolsPhoto.setProgress(NONE);
            }
        } else if (i == R.id.btnBorder_1) {
            this.onToolsBlur.OnBorderClick(BORDER_1);
        } else if (i == R.id.btnBorder_2) {
            this.onToolsBlur.OnBorderClick(BORDER_2);
        } else if (i == R.id.btnBorder_3) {
            this.onToolsBlur.OnBorderClick(BORDER_3);
        } else if (i == R.id.btnBorder_4) {
            this.onToolsBlur.OnBorderClick(BORDER_4);
        }
    }

    public OnToolsBlur getOnToolsBlur() {
        return this.onToolsBlur;
    }

    public void setOnToolsBlur(OnToolsBlur onToolsBlur) {
        this.onToolsBlur = onToolsBlur;
    }
}
