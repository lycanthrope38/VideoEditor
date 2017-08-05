package com.freelancer.videoeditor.util;

import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.util.ManagerViewCenter.LIST_ITEM;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BaseList implements OnSeekBarChangeListener {
    final int PERCENT_HorizontalScrollView = 15;
    public int PHEIGHT_HorizontalScrollView = 0;
    String formatImage;
    boolean isShowLayoutSeekbar = false;
    private RelativeLayout itemNone;
    RelativeLayout itemSelected = null;
    View layoutBaseList;
    HorizontalScrollView layoutHorizontalScrollView;
    LinearLayout layoutList;
    RelativeLayout layoutRoot;
    RelativeLayout layoutSeekBar;
    LIST_ITEM list_item;
    ProgressBar loading;
    PhotoEditorActivity mainActivity;
    OnClickItemBaseList onClickItemBaseList;
    String pathFolderIamge;
    String prefixImage;
    RectangleFilter rectangleFilter;
    SeekBar seekBarAlpha;
    TextView txtAlpha;

    public BaseList(PhotoEditorActivity mainActivity, RelativeLayout layoutParent, String pathFolderIamge, String prefixImage, String formatImage, LIST_ITEM list_item) {
        this.mainActivity = mainActivity;
        this.pathFolderIamge = pathFolderIamge;
        this.prefixImage = prefixImage;
        this.formatImage = formatImage;
        this.list_item = list_item;
        this.layoutBaseList = View.inflate(mainActivity, R.layout.libphotoeditor_layout_base_list, null);
        layoutParent.addView(this.layoutBaseList);
        findID(this.layoutBaseList);
        iniSizeLayout();
        loadItem();
    }

    public void findID(View mView) {
        this.layoutRoot = (RelativeLayout) mView.findViewById(R.id.layoutRoot);
        this.layoutHorizontalScrollView = (HorizontalScrollView) mView.findViewById(R.id.layoutHorizontalScrollView);
        this.layoutList = (LinearLayout) mView.findViewById(R.id.layoutList);
        this.loading = (ProgressBar) mView.findViewById(R.id.loading);
        this.layoutSeekBar = (RelativeLayout) mView.findViewById(R.id.layoutSeekBar);
        this.seekBarAlpha = (SeekBar) mView.findViewById(R.id.seekBarAlpha);
        this.txtAlpha = (TextView) mView.findViewById(R.id.txtAlpha);
        this.seekBarAlpha.setOnSeekBarChangeListener(this);
        this.layoutSeekBar.setVisibility(View.GONE);
    }

    private void iniSizeLayout() {
        this.PHEIGHT_HorizontalScrollView = (ConfigScreen.SCREENHEIGHT / 100) * 15;
        this.layoutHorizontalScrollView.getLayoutParams().height = this.PHEIGHT_HorizontalScrollView;
        this.layoutRoot.getLayoutParams().height = this.PHEIGHT_HorizontalScrollView;
        this.layoutList.getLayoutParams().height = this.PHEIGHT_HorizontalScrollView;
    }

    private void loadItem() {
        final LinearLayout[] mLinearLayout = new LinearLayout[1];

        Observable.fromCallable(() -> {
            int totalItem = BaseList.this.getTotalItem(BaseList.this.prefixImage);
            mLinearLayout[0] = (LinearLayout) View.inflate(BaseList.this.mainActivity, R.layout.libphotoeditor_layout_linearlayout, null);
            int pHItem = BaseList.this.PHEIGHT_HorizontalScrollView;
            int pWItem = pHItem;
            BaseList.this.addItemNone(mLinearLayout[0], pHItem, pWItem);
            for (int i = 0; i < totalItem; i++) {
                RelativeLayout mRelativeLayout = (RelativeLayout) View.inflate(BaseList.this.mainActivity, R.layout.libphotoeditor_item_base_list, null);
                mLinearLayout[0].addView(mRelativeLayout);
                BaseList.this.setOnClickOnItem(mRelativeLayout, i + 1);
                BaseList.this.displayItem(mRelativeLayout, i + 1, pHItem, pWItem);
            }
            return "";
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    BaseList.this.loading.setVisibility(View.GONE);
                    BaseList.this.loading.startAnimation(AnimationUtils.loadAnimation(BaseList.this.mainActivity, R.anim.libphotoeditor_fade_out));
                    BaseList.this.layoutList.addView(mLinearLayout[0]);
                    BaseList.this.layoutList.startAnimation(AnimationUtils.loadAnimation(BaseList.this.mainActivity, R.anim.libphotoeditor_fade_in));
                });
    }

    private void addItemNone(LinearLayout mLinearLayout, int pWItem, int pHItem) {
        this.itemNone = (RelativeLayout) View.inflate(this.mainActivity, R.layout.libphotoeditor_item_none_base_list, null);
        this.itemSelected = this.itemNone;
        setOnClickOnItem(this.itemNone, -1);
        mLinearLayout.addView(this.itemNone);
        ImageView image_frame = (ImageView) this.itemNone.findViewById(R.id.image_frame);
        ImageView image_selected = (ImageView) this.itemNone.findViewById(R.id.image_selected);
        ImageView image_icon_check = (ImageView) this.itemNone.findViewById(R.id.image_icon_check);
        setSize(this.itemNone, image_frame, image_selected, image_icon_check, pWItem, pHItem);
    }

    private void setSize(RelativeLayout layoutItem, ImageView image_frame, ImageView image_selected, ImageView image_icon_check, int pWItem, int pHItem) {
        image_frame.getLayoutParams().width = pWItem;
        image_frame.getLayoutParams().height = pHItem;
        image_selected.getLayoutParams().width = pWItem;
        image_selected.getLayoutParams().height = pHItem;
        layoutItem.getLayoutParams().width = pWItem;
        layoutItem.getLayoutParams().height = pHItem;
        int pWHIconCheck = (int) ((((float) pHItem) / 100.0f) * 20.0f);
        image_icon_check.getLayoutParams().width = pWHIconCheck;
        image_icon_check.getLayoutParams().height = pWHIconCheck;
    }

    private void displayItem(RelativeLayout mRelativeLayout, int index, int pWItem, int pHItem) {
        final RelativeLayout relativeLayout = mRelativeLayout;
        final int i = pWItem;
        final int i2 = pHItem;
        final int i3 = index;
        UtilLib.getInstance().handlerDoWork(new IHandler() {
            public void doWork() {
                ImageView image_frame = (ImageView) relativeLayout.findViewById(R.id.image_frame);
                ImageView image_icon_check = (ImageView) relativeLayout.findViewById(R.id.image_icon_check);
                BaseList.this.setSize(relativeLayout, image_frame, (ImageView) relativeLayout.findViewById(R.id.image_selected), image_icon_check, i, i2);
                image_icon_check.setVisibility(View.GONE);
                Glide.with(BaseList.this.mainActivity).load(Uri.parse(BaseList.this.pathFolderIamge + "/" + BaseList.this.prefixImage + i3 + BaseList.this.formatImage)).asBitmap().override(i, i2).thumbnail(AppConst.ZOOM_MIN).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_frame);
            }
        });
    }

    private void setOnClickOnItem(final RelativeLayout mRelativeLayout, final int index) {
        mRelativeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UtilLib.getInstance().handlerDoWork(new IHandler() {
                    public void doWork() {
                        if (BaseList.this.itemSelected == null) {
                            BaseList.this.itemSelected = mRelativeLayout;
                            ((ImageView) BaseList.this.itemSelected.findViewById(R.id.image_icon_check)).setVisibility(View.VISIBLE);
                            if (BaseList.this.onClickItemBaseList == null) {
                                return;
                            }
                            if (index != -1) {
                                BaseList.this.onClickItemBaseList.OnItemClick(mRelativeLayout, index);
                            } else {
                                BaseList.this.onClickItemBaseList.OnItemNoneClick();
                            }
                        } else if (BaseList.this.itemSelected != mRelativeLayout) {
                            ((ImageView) BaseList.this.itemSelected.findViewById(R.id.image_icon_check)).setVisibility(View.GONE);
                            BaseList.this.itemSelected = mRelativeLayout;
                            ((ImageView) BaseList.this.itemSelected.findViewById(R.id.image_icon_check)).setVisibility(View.VISIBLE);
                            if (BaseList.this.onClickItemBaseList == null) {
                                return;
                            }
                            if (index != -1) {
                                BaseList.this.onClickItemBaseList.OnItemClick(mRelativeLayout, index);
                            } else {
                                BaseList.this.onClickItemBaseList.OnItemNoneClick();
                            }
                        }
                    }
                });
            }
        });
    }

    public void removeItemSelected() {
        if (this.itemSelected != null) {
            ((ImageView) this.itemSelected.findViewById(R.id.image_icon_check)).setVisibility(View.GONE);
            ((ImageView) this.itemNone.findViewById(R.id.image_icon_check)).setVisibility(View.VISIBLE);
            this.itemSelected = this.itemNone;
        }
    }

    private int getTotalItem(String folder) {
        try {
            return this.mainActivity.getAssets().list(folder).length;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setVisibleLayoutBaseList(final int visible, final boolean isAnimation) {
        Observable.fromCallable(() -> {
            BaseList.this.layoutBaseList.setVisibility(visible);
            if (BaseList.this.isShowLayoutSeekbar) {
                BaseList.this.layoutSeekBar.setVisibility(visible);
            }
            if (isAnimation) {
                Animation mAnimation = AnimationUtils.loadAnimation(BaseList.this.mainActivity, R.anim.libphotoeditor_fade_in);
                BaseList.this.layoutBaseList.startAnimation(mAnimation);
                if (BaseList.this.isShowLayoutSeekbar) {
                    BaseList.this.layoutSeekBar.startAnimation(mAnimation);
                }
            }
            return "";
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public void showLayoutSeekbar() {
        if (this.layoutSeekBar.getVisibility() == View.GONE && !this.isShowLayoutSeekbar) {
            this.layoutSeekBar.setVisibility(View.VISIBLE);
            this.isShowLayoutSeekbar = true;
        }
    }

    public void hideLayoutSeekbar() {
        if (this.layoutSeekBar.getVisibility() == View.VISIBLE && this.isShowLayoutSeekbar) {
            this.layoutSeekBar.setVisibility(View.GONE);
            this.isShowLayoutSeekbar = false;
        }
    }

   public void updateProgess() {
        this.seekBarAlpha.setProgress(RectangleFilter.publicAlpha);
        this.txtAlpha.setText(RectangleFilter.publicAlpha + " %");
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.rectangleFilter.updateAlpha(progress);
        this.txtAlpha.setText(progress + " %");
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public OnClickItemBaseList getOnClickItemBaseList() {
        return this.onClickItemBaseList;
    }

    public void setOnClickItemBaseList(OnClickItemBaseList onClickItemBaseList) {
        this.onClickItemBaseList = onClickItemBaseList;
    }

    public RectangleFilter getRectangleFilter() {
        return this.rectangleFilter;
    }

    public void setRectangleFilter(RectangleFilter rectangleFilter) {
        this.rectangleFilter = rectangleFilter;
    }
}
