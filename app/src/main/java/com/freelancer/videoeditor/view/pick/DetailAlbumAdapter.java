package com.freelancer.videoeditor.view.pick;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.vo.Item;

import java.util.ArrayList;

public class DetailAlbumAdapter extends ArrayAdapter<Item> {
    Context context;
    ArrayList<Item> data = new ArrayList();
    int layoutResourceId;
    OnListAlbum onListAlbum;
    int pHeightItem = 0;

    static class RecordHolder {
        ImageView click;
        ImageView imageItem;
        RelativeLayout layoutRoot;

        RecordHolder() {
        }
    }

    public DetailAlbumAdapter(Context context, int layoutResourceId, ArrayList<Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.pHeightItem = ExtraUtils.getDisplayInfo((Activity) context).widthPixels / 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.imageItem = (ImageView) row.findViewById(R.id.imageItem);
            holder.click = (ImageView) row.findViewById(R.id.click);
            holder.layoutRoot = (RelativeLayout) row.findViewById(R.id.layoutRoot);
            holder.layoutRoot.getLayoutParams().height = this.pHeightItem;
            holder.imageItem.getLayoutParams().width = this.pHeightItem;
            holder.imageItem.getLayoutParams().height = this.pHeightItem;
            holder.click.getLayoutParams().width = this.pHeightItem;
            holder.click.getLayoutParams().height = this.pHeightItem;
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        final Item item = (Item) this.data.get(position);
        Glide.with(this.context).load(item.getPathFile()).asBitmap().override(200, 200).animate(R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(R.drawable.piclist_icon_default).fallback(R.drawable.piclist_icon_default).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);
        row.setOnClickListener(v -> {
            if (DetailAlbumAdapter.this.onListAlbum != null) {
                DetailAlbumAdapter.this.onListAlbum.OnItemListAlbumClick(item);
            }
        });
        return row;
    }

    public OnListAlbum getOnListAlbum() {
        return this.onListAlbum;
    }

    public void setOnListAlbum(OnListAlbum onListAlbum) {
        this.onListAlbum = onListAlbum;
    }
}
