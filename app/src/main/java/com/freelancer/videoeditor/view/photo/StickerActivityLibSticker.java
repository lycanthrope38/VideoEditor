package com.freelancer.videoeditor.view.photo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.AppConstLibSticker;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.OnStickerClickListenerLibSticker;
import com.freelancer.videoeditor.util.TabIconIndicatorLibSticker;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.view.base.BaseActivityLibSticker;
import com.freelancer.videoeditor.vo.Data;
import com.freelancer.videoeditor.vo.ListSticker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class StickerActivityLibSticker extends BaseActivityLibSticker implements OnStickerClickListenerLibSticker {
    public static final int ORIGIN_HEIGHT_SCREEN = 1920;
    private static final int REQUEST_WRITE_STORAGE = 1111;
    private static final String TAG = "StickerActivityLibSticker";
    private String[] COLORS_ITEM = new String[]{"#faf9f9", "#f5f4f4"};
    private String FIRST_TAB_NAME = "";
    private List<ListSticker> arrStickers;
    private ImageView buttonBackHome;
    private Data mData;
    private boolean mIsSortTab = false;
    private String mJsonCached;
    private StickerClick mStickerDataSelected;
    private TabIconIndicatorLibSticker mTabPageIndicator;
    private ViewPager mViewPager;
    private TextView textTitle;
    private String urlApiSticker = "";

    private class ListStickerAdapter extends FragmentPagerAdapter {
        public ListStickerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return ListStickerFragmentLibSticker.newInstance((ListSticker) StickerActivityLibSticker.this.arrStickers.get(position), position % 2 == 0 ? R.color.lib_sticker_bg_sticker_common : R.color.lib_sticker_bg_sticker_common_1, StickerActivityLibSticker.this.COLORS_ITEM);
        }

        public CharSequence getPageTitle(int position) {
            return ((ListSticker) StickerActivityLibSticker.this.arrStickers.get(position)).getName();
        }

        public int getCount() {
            return StickerActivityLibSticker.this.arrStickers.size();
        }
    }

    private class StickerClick {
        private ListSticker data;
        private int position;

        public StickerClick(int position, ListSticker data) {
            this.position = position;
            this.data = data;
        }

        public int getPosition() {
            return this.position;
        }

        public ListSticker getData() {
            return this.data;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_sticker_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.urlApiSticker = bundle.getString(AppConstLibSticker.BUNDLE_KEY_URL_STICKER, "");
            this.COLORS_ITEM = bundle.getStringArray(AppConstLibSticker.BUNDLE_KEY_COLOR_ITEMS);
        } else {
            finish();
        }
        this.buttonBackHome = (ImageView) findViewById(R.id.image_back);
        this.textTitle = (TextView) findViewById(R.id.text_action_bar_title);
        this.mIsSortTab = getIntent().getExtras().getBoolean(AppConst.BUNDLE_KEY_IS_SORT_TAB, false);
        if (this.mIsSortTab) {
            this.FIRST_TAB_NAME = getIntent().getExtras().getString(AppConstLibSticker.BUNDLE_KEY_FIRST_TAB_NAME, "");
        }
        this.mViewPager = (ViewPager) findViewById(R.id.pager_sticker);
        this.mTabPageIndicator = (TabIconIndicatorLibSticker) findViewById(R.id.indicator_sticker);
        this.mViewPager.setOffscreenPageLimit(3);
        this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                StickerActivityLibSticker.this.setPageTitle(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        resizeLayout();
        CacheCounter.getInstance().countOpenApp(AppConstLibSticker.SHARE_CACHE_STICKER_COUNTER, 10);
        if (!CacheCounter.getInstance().IsReload()) {
            loadDataFromCache();
        } else if (UtilLib.getInstance().haveNetworkConnection(this)) {
            getListStickers();
        } else {
            loadDataFromCache();
        }
        this.buttonBackHome.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StickerActivityLibSticker.this.setResult(0);
                StickerActivityLibSticker.this.finish();
            }
        });
    }

    private void loadDataFromCache() {
        this.mJsonCached = SharePrefUtils.getString(AppConstLibSticker.SHARE_CACHE_STICKER, "");
        if (!TextUtils.isEmpty(this.mJsonCached)) {
            Timber.d(TAG, "LOAD [STICKERS] FROM CACHED");
            this.arrStickers = ((Data) new Gson().fromJson(this.mJsonCached, Data.class)).getListCateSticker();
            setStickerAdapter();
        } else if (UtilLib.getInstance().haveNetworkConnection(this)) {
            getListStickers();
        } else {
            Toast.makeText(this, R.string.lib_sticker_message_not_connect_network, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setPageTitle(int position) {
        if (this.arrStickers != null && !this.arrStickers.isEmpty()) {
            this.textTitle.setText(((ListSticker) this.arrStickers.get(position)).getName());
        }
    }

    public int getTaskId() {
        return super.getTaskId();
    }

    private void resizeLayout() {
        int heightTabBottom = (ExtraUtils.getDisplayInfo(this).heightPixels * 210) / ORIGIN_HEIGHT_SCREEN;
        this.mTabPageIndicator.getLayoutParams().height = heightTabBottom;
        this.mTabPageIndicator.setLayoutHeight(heightTabBottom);
    }

    private void setStickerAdapter() {
        this.arrStickers = changeIndexForTab();
        if (this.arrStickers != null && !this.arrStickers.isEmpty()) {
            setPageTitle(0);
            this.mViewPager.setAdapter(new ListStickerAdapter(getSupportFragmentManager()));
            this.mTabPageIndicator.setFolderIcon(getStickerTabIcon());
            this.mTabPageIndicator.setViewPager(this.mViewPager);
            this.mTabPageIndicator.setVisibility(0);
        }
    }

    private List<ListSticker> changeIndexForTab() {
        List<ListSticker> arr = this.arrStickers;
        if (arr != null && !arr.isEmpty() && this.mIsSortTab) {
            String firstTabName = this.FIRST_TAB_NAME;
            int count = arr.size();
            for (int i = 0; i < count; i++) {
                ListSticker ls = (ListSticker) arr.get(i);
                if (ls.getFolder().equalsIgnoreCase(firstTabName)) {
                    arr.remove(i);
                    arr.add(0, ls);
                    break;
                }
            }
        }
        return arr;
    }

    private String[] getStickerTabIcon() {
        ArrayList<String> folderIcon = new ArrayList();
        for (ListSticker item : this.arrStickers) {
            folderIcon.add(item.getIcon());
        }
        return (String[]) folderIcon.toArray(new String[folderIcon.size()]);
    }

    public void onStickerClick(Object data, int position) {
        if (data instanceof ListSticker) {
            this.mStickerDataSelected = new StickerClick(position + 1, (ListSticker) data);
            if (UtilLib.getInstance().isPermissionAllow(this, REQUEST_WRITE_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                saveStickerToLocal();
            }
        }
    }

    private void saveStickerToLocal() {
        ExtraUtils.createFolder(AppConstLibSticker.ROOT_SAVE_STICKER);
        UtilLib.getInstance().showLoadingWithMessage(this, getString(R.string.message_download));
        Glide.with(this).load(String.format(Locale.getDefault(), AppConstLibSticker.URL_IMAGE_STICKER, new Object[]{this.mStickerDataSelected.getData().getFolder(), Integer.valueOf(this.mStickerDataSelected.getPosition())})).asBitmap().into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                UtilLib.getInstance().hideLoading();
                String targetPath = String.format(Locale.getDefault(), AppConstLibSticker.SAVE_STICKER_PATH, new Object[]{StickerActivityLibSticker.this.mStickerDataSelected.getData().getFolder(), Integer.valueOf(StickerActivityLibSticker.this.mStickerDataSelected.getPosition())});
                if (ExtraUtils.saveBitmapToPNG(resource, targetPath)) {
                    StickerActivityLibSticker.this.finishActivityAndReturn(targetPath);
                } else {
                    T.show(R.string.lib_sticker_error_save_image);
                }
            }

            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                UtilLib.getInstance().hideLoading();
                T.show(R.string.lib_sticker_message_not_connect_network);
            }

            public void onLoadCleared(Drawable placeholder) {
                UtilLib.getInstance().hideLoading();
            }
        });
    }

    private void finishActivityAndReturn(String url) {
        Intent intent = new Intent();
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_STICKER_PATH, url);
        setResult(-1, intent);
        finish();
    }

    private void cacheData(String json) {
        SharePrefUtils.putString(AppConstLibSticker.SHARE_CACHE_STICKER, json);
    }

    private void getListStickers() {
        L.d(TAG, "LIST STICKERS SIZE: STARTING.... ");
        UtilLib.getInstance().showLoading(this);
        getWebService().getListSticker(this.urlApiSticker, getLocale()).enqueue(new NetworkCallback<NetResponse<Data>>() {
            public void onSuccess(NetResponse<Data> response) {
                UtilLib.getInstance().hideLoading();
                StickerActivityLibSticker.this.mData = (Data) response.getData();
                StickerActivityLibSticker.this.arrStickers = StickerActivityLibSticker.this.mData.getListCateSticker();
                StickerActivityLibSticker.this.cacheData(new Gson().toJson(StickerActivityLibSticker.this.mData));
                StickerActivityLibSticker.this.setStickerAdapter();
            }

            public void onFailed(NetworkError error) {
                UtilLib.getInstance().hideLoading();
                L.d(StickerActivityLibSticker.TAG, "LIST STICKERS FAILED: " + error.getMessage());
                StickerActivityLibSticker.this.mJsonCached = SharePrefUtils.getString(AppConstLibSticker.SHARE_CACHE_STICKER, "");
                if (TextUtils.isEmpty(StickerActivityLibSticker.this.mJsonCached)) {
                    T.show(R.string.lib_sticker_message_not_connect_network);
                    StickerActivityLibSticker.this.finish();
                    return;
                }
                L.d(StickerActivityLibSticker.TAG, "LOAD STICKERS FROM CACHED");
                StickerActivityLibSticker.this.arrStickers = ((Data) new Gson().fromJson(StickerActivityLibSticker.this.mJsonCached, Data.class)).getListCateSticker();
                StickerActivityLibSticker.this.setStickerAdapter();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_WRITE_STORAGE) {
            return;
        }
        if (UtilLib.getInstance().isPermissionAllow(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            saveStickerToLocal();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            showDenyDialog("android.permission.WRITE_EXTERNAL_STORAGE", REQUEST_WRITE_STORAGE);
        } else {
            openAppSettings();
        }
    }

    private void showDenyDialog(final String permission, final int requestCode) {
        UtilLib.getInstance().showDenyDialog((Activity) this, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UtilLib.getInstance().requestPermission(StickerActivityLibSticker.this, permission, requestCode);
            }
        }, null);
    }

    private void openAppSettings() {
        UtilLib.getInstance().showRememberDialog(this, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ExtraUtils.openAppSettings(StickerActivityLibSticker.this, StickerActivityLibSticker.this.getPackageName());
            }
        }, null);
    }
}
