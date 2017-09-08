package com.freelancer.videoeditor.util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ViewTop implements OnClickListener {
    public static int PHEIGHT_TOP = 0;
    int PERCENT_TOP = 5;
    ImageView btnBack;
    Button btnDone;
    Button btnSave;
    boolean isSaveOld = true;
    RelativeLayout layoutTop;
    PhotoEditorActivity mainActivity;
    OnViewListener.OnViewTop onViewTop;

    public ViewTop(PhotoEditorActivity mainActivity, View mainView) {
        this.mainActivity = mainActivity;
        this.layoutTop = (RelativeLayout) mainView.findViewById(R.id.layoutTop);
        this.btnBack = (ImageView) this.layoutTop.findViewById(R.id.btnBack);
        this.btnSave = (Button) mainView.findViewById(R.id.btnSave);
        this.btnDone = (Button) mainView.findViewById(R.id.btnDone);
        this.btnSave.setOnClickListener(this);
        this.btnDone.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);
        PHEIGHT_TOP = (ConfigScreen.SCREENHEIGHT / 100) * this.PERCENT_TOP;
        this.layoutTop.getLayoutParams().height = PHEIGHT_TOP;
        this.btnBack.getLayoutParams().height = PHEIGHT_TOP;
        this.btnBack.getLayoutParams().width = PHEIGHT_TOP;
    }

    public void onClick(View v) {
        if (this.onViewTop != null) {
            int i = v.getId();
            if (i == R.id.btnBack) {
                this.onViewTop.OnBack();
            } else if (i == R.id.btnSave) {
                this.onViewTop.OnSave();
            } else if (i == R.id.btnDone) {
                this.onViewTop.OnDone();
            }
        }
    }

    public void isSaveChange(final boolean isSave) {
        Observable.fromCallable(() -> {
            if (ViewTop.this.isSaveOld != isSave) {
                ViewTop.this.isSaveOld = isSave;
                if (isSave) {
                    ViewTop.this.btnSave.setVisibility(View.GONE);
                    ViewTop.this.btnDone.setVisibility(View.VISIBLE);
                } else {
                    ViewTop.this.btnSave.setVisibility(View.VISIBLE);
                    ViewTop.this.btnDone.setVisibility(View.GONE);
                }
                ViewTop.this.startAnimation(ViewTop.this.btnSave);
                ViewTop.this.startAnimation(ViewTop.this.btnDone);
            }
            return "";
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void startAnimation(View mView) {
        if (mView.getVisibility() == View.VISIBLE) {
            mView.startAnimation(AnimationUtils.loadAnimation(this.mainActivity, R.anim.libphotoeditor_slide_out_bottom_500ms));
        } else if (mView.getVisibility() == View.GONE) {
            mView.startAnimation(AnimationUtils.loadAnimation(this.mainActivity, R.anim.libphotoeditor_slide_in_bottom_500ms));
        }
    }

    public OnViewListener.OnViewTop getOnViewTop() {
        return this.onViewTop;
    }

    public void setOnViewTop(OnViewListener.OnViewTop onViewTop) {
        this.onViewTop = onViewTop;
    }
}
