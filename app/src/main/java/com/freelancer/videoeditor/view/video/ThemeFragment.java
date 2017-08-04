package com.freelancer.videoeditor.view.video;

import android.app.Activity;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.OnRecyclerClickListener;
import com.freelancer.videoeditor.util.OnToolBoxListener;
import com.freelancer.videoeditor.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import net.margaritov.preference.colorpicker.BuildConfig;

public class ThemeFragment extends BaseFragment implements OnRecyclerClickListener {
    private static final int DIMEN_PADDING = 2131296461;
    private static final String TAG = "ThemeFragment";
    private int THUMB_SIZE_HEIGHT = 450;
    private int THUMB_SIZE_WIDTH = 450;
    private ListBorderAdapter mAdapter;
    private int mColumnWith = 0;
    private ArrayList<String> mDataFrame;
    private LinearLayoutManager mLayoutManager;
    private OnToolBoxListener mListener;
    @BindView(R.id.recycler_list_border)
    RecyclerView mRecyclerView;
    private View mRootView;

    public static ThemeFragment newInstance() {
        return newInstance(null);
    }

    public static ThemeFragment newInstance(Bundle data) {
        ThemeFragment fragment = new ThemeFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnToolBoxListener) activity;
            int screenWith = ExtraUtils.getDisplayInfo(getActivity()).widthPixels;
//            this.mColumnWith = Math.round((((float) (screenWith / 6)) - (((float) getResources().getDimensionPixelSize(DIMEN_PADDING)) / 2.0f)) + ExtraUtils.convertDpToPixel(HandlerTools.ROTATE_R, getActivity()));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " must be implemented OnToolBoxListener");
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_toolbox_theme, container, false);
        ButterKnife.bind(this, this.mRootView);
        return this.mRootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        init();
        fillListBorder();
    }

    private void init() {
        try {
            generateThumb();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setOriginSize(String assetPath) throws IOException {
        Options options = ExtraUtils.getOriginSizeOfImage(getContext(), assetPath);
        this.THUMB_SIZE_WIDTH = options.outWidth;
        this.THUMB_SIZE_HEIGHT = options.outHeight;
        Timber.d(TAG, "ORIGIN WIDTH: " + this.THUMB_SIZE_WIDTH);
        Timber.d(TAG, "ORIGIN HEIGHT: " + this.THUMB_SIZE_HEIGHT);
    }

    private void generateThumb() throws Exception {
        int i = 0;
        this.mDataFrame = new ArrayList();
        String[] listThumbs = ExtraUtils.listAssetFiles(getActivity(), AppConst.FOLDER_THEME);
        if (listThumbs == null || listThumbs.length <= 0) {
            Toast.makeText(getActivity(), "No Thumbs" , Toast.LENGTH_SHORT).show();

            return;
        }
        String path = AppConst.FULL_ASSET_FOLDER_BORDER_NAME + File.separator + listThumbs[0];
        Collections.sort(Arrays.asList(listThumbs), new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", BuildConfig.FLAVOR);
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        int length = listThumbs.length;
        while (i < length) {
            this.mDataFrame.add(AppConst.FULL_ASSET_FOLDER_BORDER_NAME + File.separator + listThumbs[i]);
            i++;
        }
    }

    private void fillListBorder() {
        if (this.mDataFrame != null && !this.mDataFrame.isEmpty()) {
            this.mAdapter = new ListBorderAdapter(getActivity(), this.mDataFrame);
//            this.mAdapter.setColumnWith(this.mColumnWith);
//            this.mAdapter.setColumnSpace(getResources().getDimensionPixelSize(DIMEN_PADDING));
            this.mAdapter.setOnItemClickListener(this);
            this.mRecyclerView.setAdapter(this.mAdapter);
        }
    }

    public void onItemClicked(int position, View viewClicked, Object obj) {
        if (this.mListener != null) {
            this.mListener.onPassData(Action.ADD_OVERLAY_BORDER, obj);
        }
    }
}
