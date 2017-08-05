package com.freelancer.videoeditor.util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ManagerViewCenter implements OnClickListener {
    RelativeLayout layoutCenter;
    BaseList listBackground;
    public ListBlur listBlur;
    BaseList listBorder;
    BaseList listFilter;
    LIST_ITEM list_item_selected = null;
    PhotoEditorActivity mainActivity;
    OnManagerViewCenter onManagerViewCenter;

    public enum LIST_ITEM {
        BORDER,
        FILTER,
        BACKGROUND,
        STICKER
    }

    public ManagerViewCenter(PhotoEditorActivity mainActivity, RelativeLayout layoutCenter) {
        this.mainActivity = mainActivity;
        this.layoutCenter = layoutCenter;
        layoutCenter.setOnClickListener(this);
        this.listBorder = new BaseList(mainActivity, layoutCenter, AppConst.ASSETS_PATH_FOLDER_BORDER, AppConst.PREFIX_BORDER, AppConst.FORMAT_FRAME, LIST_ITEM.BORDER);
        this.listFilter = new BaseList(mainActivity, layoutCenter, AppConst.ASSETS_PATH_FOLDER_FILTER, AppConst.PREFIX_FILTER, AppConst.FORMAT_FILLTER, LIST_ITEM.FILTER);
        this.listBackground = new BaseList(mainActivity, layoutCenter, AppConst.ASSETS_PATH_FOLDER_BACKGROUND, AppConst.PREFIX_THUMB_BACKGROUND, AppConst.FORMAT_FILLTER, LIST_ITEM.BACKGROUND);
        this.listBlur = new ListBlur(mainActivity, layoutCenter);
    }

    public void showList(LIST_ITEM listitem) {
        hideAll();
        if (this.list_item_selected == null || this.list_item_selected != listitem) {
            this.list_item_selected = listitem;
            show(this.list_item_selected);
            return;
        }
        unCheck();
    }

    public void unCheck() {
        hideAll();
        this.list_item_selected = null;
        setVisibleLayoutCenter(8, true);
    }

    void hideAll() {
        this.listBorder.setVisibleLayoutBaseList(View.GONE, false);
        this.listBackground.setVisibleLayoutBaseList(View.GONE, false);
        this.listFilter.setVisibleLayoutBaseList(View.GONE, false);
        this.listBlur.setVisibleLayoutBorder(View.GONE, false);
    }

    void show(LIST_ITEM listitem) {
        if (listitem == LIST_ITEM.BORDER) {
            this.listBorder.setVisibleLayoutBaseList(View.VISIBLE, true);
        } else if (listitem == LIST_ITEM.BACKGROUND) {
            this.listBlur.setVisibleLayoutBorder(View.VISIBLE, true);
            this.mainActivity.managerRectanglePhoto.getmRectanglePhotoSeleted().addPhotoBlur();
        } else if (listitem == LIST_ITEM.FILTER) {
            this.listFilter.setVisibleLayoutBaseList(View.VISIBLE, true);
        }
        setVisibleLayoutCenter(0, false);
    }

    public void setVisibleLayoutCenter(final int visible, final boolean isAnimation) {
        Observable.fromCallable(() -> {
            ManagerViewCenter.this.layoutCenter.setVisibility(visible);
            if (!isAnimation) {
                return "";
            }
            if (visible == View.VISIBLE) {
                ManagerViewCenter.this.layoutCenter.startAnimation(AnimationUtils.loadAnimation(ManagerViewCenter.this.mainActivity, R.anim.libphotoeditor_fade_in));
            } else if (visible == View.GONE) {
                ManagerViewCenter.this.layoutCenter.startAnimation(AnimationUtils.loadAnimation(ManagerViewCenter.this.mainActivity, R.anim.libphotoeditor_fade_out));
                if (ManagerViewCenter.this.onManagerViewCenter != null) {
                    ManagerViewCenter.this.onManagerViewCenter.onHideViewCenter();
                }
            }
            return "";
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void setOnClickItemBaseList(OnClickItemBaseList onClickItemBaseListBorder, OnClickItemBaseList onClickItemBaseListFilter, OnClickItemBaseList onClickItemBaseListBackground) {
        this.listBorder.setOnClickItemBaseList(onClickItemBaseListBorder);
        this.listFilter.setOnClickItemBaseList(onClickItemBaseListFilter);
        this.listBackground.setOnClickItemBaseList(onClickItemBaseListBackground);
    }

    public void removeItemSelected() {
        this.listBorder.removeItemSelected();
        this.listFilter.removeItemSelected();
        this.listBackground.removeItemSelected();
    }

    public boolean isVisible() {
        if (this.layoutCenter.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        if (this.layoutCenter.getId() == v.getId()) {
            this.list_item_selected = null;
            setVisibleLayoutCenter(View.GONE, true);
            this.onManagerViewCenter.onHideViewCenter();
        }
    }

    public OnManagerViewCenter getOnManagerViewCenter() {
        return this.onManagerViewCenter;
    }

    public void setOnManagerViewCenter(OnManagerViewCenter onManagerViewCenter) {
        this.onManagerViewCenter = onManagerViewCenter;
    }

    public BaseList getListFilter() {
        return this.listFilter;
    }

    public void setListFilter(BaseList listFilter) {
        this.listFilter = listFilter;
    }

    public ListBlur getListBlur() {
        return this.listBlur;
    }

    public void setListBlur(ListBlur listBlur) {
        this.listBlur = listBlur;
    }
}
