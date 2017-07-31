package com.freelancer.videoeditor.util;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.ArrayList;

public class ListPhoto {
    public int index_change_photo_old = 0;
    public View item_photo_old = null;
    HorizontalScrollView layoutHorizontalScrollView;
    LinearLayout layoutListPhoto;
    RelativeLayout layoutPhoto;
    ArrayList<String> listPathPhoto;
    LinearLayout mLinearLayout;
    PhotoEditorActivity mainActivity;
    View mainView;
    int pWHPhoto;

    public ListPhoto(final PhotoEditorActivity mainActivity, View mainView, final ArrayList<String> listPathPhoto, int pHLayoutBottom) {
        this.mainActivity = mainActivity;
        this.mainView = mainView;
        this.listPathPhoto = listPathPhoto;
        this.layoutPhoto = (RelativeLayout) mainView.findViewById(R.id.layoutPhoto);
        this.layoutListPhoto = (LinearLayout) mainView.findViewById(R.id.layoutListPhoto);
        this.layoutHorizontalScrollView = (HorizontalScrollView) mainView.findViewById(R.id.layoutHorizontalScrollView);
        ((TextView) mainView.findViewById(R.id.txtTotalPhoto)).setText(mainActivity.getResources().getString(R.string.text_total_photo, new Object[]{Integer.valueOf(listPathPhoto.size())}));
        this.pWHPhoto = (int) ((((float) pHLayoutBottom) / 100.0f) * 70.0f);
        this.layoutPhoto.getLayoutParams().height = pHLayoutBottom;
        this.layoutHorizontalScrollView.getLayoutParams().height = this.pWHPhoto;
        this.layoutListPhoto.getLayoutParams().height = this.pWHPhoto;
        UtilLib.getInstance().doBackGround(new IDoBackGround() {
            public void onDoBackGround(boolean isCancelled) {
                UtilLib.getInstance().handlerDoWork(new IHandler() {
                    public void doWork() {
                        ListPhoto.this.loadPhoto();
                    }
                });
            }

            public void onCompleted() {
                ListPhoto.this.layoutListPhoto.addView(ListPhoto.this.mLinearLayout);
                mainActivity.pathItemPhotoCurrent = (String) listPathPhoto.get(0);
                mainActivity.item_photo_ItemPhotoCurrent = ListPhoto.this.getItem_photo_old();
                mainActivity.indexItemPhotoCurrent = 0;
            }
        });
    }

    void loadPhoto() {
        this.mLinearLayout = (LinearLayout) View.inflate(this.mainActivity, R.layout.libphotoeditor_layout_linearlayout, null);
        int pWHCheck = (int) ((((float) this.pWHPhoto) / 100.0f) * 20.0f);
        int pWHOrder = (int) ((((float) this.pWHPhoto) / 100.0f) * 30.0f);
        for (int i = 0; i < this.listPathPhoto.size(); i++) {
            String path = (String) this.listPathPhoto.get(i);
            View item_photo = View.inflate(this.mainActivity, R.layout.libphotoeditor_item_photo, null);
            this.mLinearLayout.addView(item_photo);
            RelativeLayout root = (RelativeLayout) item_photo.findViewById(R.id.root);
            root.getLayoutParams().width = this.pWHPhoto;
            root.getLayoutParams().height = this.pWHPhoto;
            ImageView photo = (ImageView) item_photo.findViewById(R.id.photo);
            photo.getLayoutParams().width = this.pWHPhoto;
            photo.getLayoutParams().height = this.pWHPhoto;
            TextView txtOrder = (TextView) item_photo.findViewById(R.id.txtOrder);
            txtOrder.setText("" + (i + 1));
            txtOrder.getLayoutParams().width = pWHOrder;
            txtOrder.getLayoutParams().height = pWHOrder;
            txtOrder.setTextSize(0, ((float) pWHOrder) / 3.0f);
            ImageView check = (ImageView) item_photo.findViewById(R.id.check);
            check.getLayoutParams().width = pWHOrder;
            check.getLayoutParams().height = pWHOrder;
            Glide.with(this.mainActivity).load(path).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).override(this.pWHPhoto, this.pWHPhoto).animate(R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(R.drawable.libphotoeditor_icon_default).fallback(R.drawable.libphotoeditor_icon_default).placeholder(R.drawable.libphotoeditor_icon_default).into(photo);
            handleClick(item_photo, i);
            if (i == 0) {
                this.item_photo_old = item_photo;
            } else {
                check.setVisibility(View.GONE);
            }
        }
    }

    void handleClick(final View item_photo, final int index) {
        item_photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ListPhoto.this.item_photo_old != item_photo) {
                    ListPhoto.this.mainActivity.changePhoto(item_photo, index);
                }
            }
        });
    }

    public int getIndex_change_photo_old() {
        return this.index_change_photo_old;
    }

    public void setIndex_change_photo_old(int index_change_photo_old) {
        this.index_change_photo_old = index_change_photo_old;
    }

    public View getItem_photo_old() {
        return this.item_photo_old;
    }

    public void setItem_photo_old(View item_photo_old) {
        this.item_photo_old = item_photo_old;
    }
}
