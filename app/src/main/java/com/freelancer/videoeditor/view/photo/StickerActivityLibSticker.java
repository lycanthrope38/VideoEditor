package com.freelancer.videoeditor.view.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.OnToolListener;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ThongLe on 8/5/2017.
 */

public class StickerActivityLibSticker extends AppCompatActivity implements OnToolListener.OnStickerClick {
    @BindView(R.id.gridViewSticker)
    GridView mGridSticker;
    private StickerAdapter stickerAdapter;
    private List<String> dataListPhoto = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);
        ButterKnife.bind(this);
        String[] listThumbs = ExtraUtils.listAssetFiles(this, AppConst.FOLDER_STICKER);
        int i = 0;
        if (listThumbs == null || listThumbs.length <= 0) {
            Toast.makeText(this, "No Thumbs", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Collections.sort(Arrays.asList(listThumbs), new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        int length = listThumbs.length;
        while (i < length) {
            this.dataListPhoto.add(AppConst.FULL_ASSET_FOLDER_STICKER + File.separator + listThumbs[i]);
            i++;
        }

        this.stickerAdapter = new StickerAdapter(this, R.layout.item_sticker, this.dataListPhoto);
        this.stickerAdapter.setOnListAlbum(this);
        this.mGridSticker.setAdapter(this.stickerAdapter);
    }


    @Override
    public void OnItemListStickerClick(String item) {
        finishActivityAndReturn(item);
    }

    @OnClick(R.id.image_back)
    public void onBackClick(){
        finish();
    }

    private void finishActivityAndReturn(String url) {
        Intent intent = new Intent();
        intent.putExtra(AppConst.BUNDLE_KEY_STICKER_PATH, url);
        setResult(-1, intent);
        finish();
    }
}
