package com.freelancer.videoeditor.view.photo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.OnToolListener;
import com.freelancer.videoeditor.view.pick.DetailAlbumAdapter;
import com.freelancer.videoeditor.view.pick.OnListAlbum;
import com.freelancer.videoeditor.vo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThongLe on 8/5/2017.
 */

public class StickerAdapter  extends ArrayAdapter<String> {

    Context context;
    List<String> data = new ArrayList();
    int layoutResourceId;
    OnToolListener.OnStickerClick onListAlbum;
    int pHeightItem = 0;

    static class RecordHolder {
        ImageView click;
        ImageView imageItem;
        RelativeLayout layoutRoot;

        RecordHolder() {
        }
    }

    public StickerAdapter(Context context, int layoutResourceId, List<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.pHeightItem = ExtraUtils.getDisplayInfo((Activity) context).widthPixels / 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StickerAdapter.RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new StickerAdapter.RecordHolder();
            holder.imageItem = row.findViewById(R.id.imageItem);
            row.setTag(holder);
        } else {
            holder = (StickerAdapter.RecordHolder) row.getTag();
        }
        Glide.with(this.context).load(data.get(position)).into(holder.imageItem);
        row.setOnClickListener(v -> {
            if (StickerAdapter.this.onListAlbum != null) {
                StickerAdapter.this.onListAlbum.OnItemListStickerClick(data.get(position));
            }
        });
        return row;
    }

    public OnToolListener.OnStickerClick getOnListAlbum() {
        return this.onListAlbum;
    }

    public void setOnListAlbum(OnToolListener.OnStickerClick onListAlbum) {
        this.onListAlbum = onListAlbum;
    }
}
