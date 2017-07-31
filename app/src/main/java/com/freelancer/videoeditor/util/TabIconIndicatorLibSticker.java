package com.freelancer.videoeditor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.freelancer.videoeditor.R;

import java.util.Locale;

public class TabIconIndicatorLibSticker extends HorizontalScrollView implements PageIndicator {
    private static final CharSequence EMPTY_TITLE = "";
    private String[] mFolderIcons;
    private int mLayoutHeight;
    private OnPageChangeListener mListener;
    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private final OnClickListener mTabClickListener;
    private final IcsLinearLayoutLibSticker mTabLayout;
    private TabPageIndicator.OnTabReselectedListener mTabReselectedListener;
    private Runnable mTabSelector;
    private ViewPager mViewPager;

    public interface OnTabReselectedListener {
        void onTabReselected(int i);
    }

    private class TabView extends AppCompatTextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.vpiTabPageIndicatorStyle);
            setEllipsize(TruncateAt.MARQUEE);
            setSingleLine();
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec((int) (((float) TabIconIndicatorLibSticker.this.mMaxTabWidth) + (((float) TabIconIndicatorLibSticker.this.mMaxTabWidth) * 0.3f)), 1073741824), heightMeasureSpec);
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public void setLayoutHeight(int height) {
        this.mLayoutHeight = height;
    }

    public TabIconIndicatorLibSticker(Context context) {
        this(context, null);
    }

    public TabIconIndicatorLibSticker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLayoutHeight = 0;
        this.mTabClickListener = new OnClickListener() {
            public void onClick(View view) {
                TabView tabView = (TabView) view;
                int oldSelected = TabIconIndicatorLibSticker.this.mViewPager.getCurrentItem();
                int newSelected = tabView.getIndex();
                TabIconIndicatorLibSticker.this.mViewPager.setCurrentItem(newSelected);
                if (oldSelected == newSelected && TabIconIndicatorLibSticker.this.mTabReselectedListener != null) {
                    TabIconIndicatorLibSticker.this.mTabReselectedListener.onTabReselected(newSelected);
                }
            }
        };
        setHorizontalScrollBarEnabled(false);
        this.mTabLayout = new IcsLinearLayoutLibSticker(context, R.attr.vpiTabPageIndicatorStyle);
        addView(this.mTabLayout, new LayoutParams(-2, -1));
    }

    public void setOnTabReselectedListener(TabPageIndicator.OnTabReselectedListener listener) {
        this.mTabReselectedListener = listener;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded = widthMode == 1073741824;
        setFillViewport(lockedExpanded);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount <= 1 || !(widthMode == 1073741824 || widthMode == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = -1;
        } else if (childCount > 2) {
            this.mMaxTabWidth = (int) (((float) MeasureSpec.getSize(widthMeasureSpec)) * 0.16f);
        } else {
            this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
        }
        int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();
        if (lockedExpanded && oldWidth != newWidth) {
            setCurrentItem(this.mSelectedTabIndex);
        }
    }

    private void animateToTab(int position) {
        final View tabView = this.mTabLayout.getChildAt(position);
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
        this.mTabSelector = new Runnable() {
            public void run() {
                TabIconIndicatorLibSticker.this.smoothScrollTo(tabView.getLeft() - ((TabIconIndicatorLibSticker.this.getWidth() - tabView.getWidth()) / 2), 0);
                TabIconIndicatorLibSticker.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            post(this.mTabSelector);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
    }

    public void setFolderIcon(String[] folderIcons) {
        this.mFolderIcons = folderIcons;
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(this.mTabClickListener);
        tabView.setText(text);
        addIconToTab(String.format(Locale.getDefault(), AppConstLibSticker.URL_TAB_STICKER_ICON, new Object[]{this.mFolderIcons[index]}), tabView);
        this.mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, -1, HandlerTools.ROTATE_R));
    }

    private void addIconToTab(String url, final TabView tv) {
        Glide.with(getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    int tabHeight = TabIconIndicatorLibSticker.this.mLayoutHeight / 2;
                    Drawable drawable = new BitmapDrawable(TabIconIndicatorLibSticker.this.getContext().getResources(), resource);
                    drawable.setBounds(0, 0, tabHeight, tabHeight);
                    tv.setCompoundDrawables(null, drawable, null, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void onPageScrollStateChanged(int arg0) {
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(arg0);
        }
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (this.mListener != null) {
            this.mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (this.mListener != null) {
            this.mListener.onPageSelected(arg0);
        }
    }

    public void setViewPager(ViewPager view) {
        if (this.mViewPager != view) {
            if (this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener(null);
            }
            if (view.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.mViewPager = view;
            view.setOnPageChangeListener(this);
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        this.mTabLayout.removeAllViews();
        PagerAdapter adapter = this.mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (this.mSelectedTabIndex > count) {
            this.mSelectedTabIndex = count - 1;
        }
        setCurrentItem(this.mSelectedTabIndex);
        requestLayout();
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mSelectedTabIndex = item;
        this.mViewPager.setCurrentItem(item);
        int tabCount = this.mTabLayout.getChildCount();
        int i = 0;
        while (i < tabCount) {
            View child = this.mTabLayout.getChildAt(i);
            boolean isSelected = i == item;
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
            i++;
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }
}
