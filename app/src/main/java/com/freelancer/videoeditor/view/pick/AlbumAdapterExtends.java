package com.freelancer.videoeditor.view.pick;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.vo.Item;

import java.util.ArrayList;

public class AlbumAdapterExtends extends ArrayAdapter<Item> {
    private ArrayList<Item> data = new ArrayList();
    private int heightAdsNativePX = 0;
    private int layoutResourceId;
    private OnAlbum onItem;
    private int pHeightItem = 0;
    private int pWHIconNext = 0;
    private PickImageExtendsActivity pickImageExtendsActivity;
    private RecordHolder recordHolderAds = null;

    static class RecordHolder {
        public ImageView iconNext;
        public ImageView imageItem;
        public RelativeLayout layoutItem;
        public RelativeLayout layoutRoot;
        public TextView txtPath;
        public TextView txtTitle;

        RecordHolder() {
        }
    }

    public AlbumAdapterExtends(PickImageExtendsActivity pickImageExtendsActivity, int layoutResourceId, ArrayList<Item> data) {
        super(pickImageExtendsActivity, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.pickImageExtendsActivity = pickImageExtendsActivity;
        this.data = data;
        this.pHeightItem = ExtraUtils.getDisplayInfo(pickImageExtendsActivity).heightPixels / 8;
        this.pWHIconNext = this.pHeightItem / 4;
        this.heightAdsNativePX = (int) ExtraUtils.convertDpToPixel(80.0f, pickImageExtendsActivity);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RecordHolder holder;
        if (convertView == null) {
            convertView = this.pickImageExtendsActivity.getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.name_album);
            holder.txtPath = (TextView) convertView.findViewById(R.id.path_album);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.icon_album);
            holder.iconNext = (ImageView) convertView.findViewById(R.id.iconNext);
            holder.layoutRoot = (RelativeLayout) convertView.findViewById(R.id.layoutRoot);
            holder.layoutItem = (RelativeLayout) convertView.findViewById(R.id.layoutItem);
            holder.layoutRoot.getLayoutParams().height = this.pHeightItem;
            holder.imageItem.getLayoutParams().width = this.pHeightItem;
            holder.imageItem.getLayoutParams().height = this.pHeightItem;
            holder.iconNext.getLayoutParams().width = this.pWHIconNext;
            holder.iconNext.getLayoutParams().height = this.pWHIconNext;
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }

        holder.layoutItem.setVisibility(View.VISIBLE);
        Item item = (Item) this.data.get(position);
        holder.txtTitle.setText(item.getName());
        holder.txtPath.setText(item.getPathFolder());
        Glide.with(this.pickImageExtendsActivity).load(item.getPathFile()).asBitmap().override(200, 200).animate(R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(R.drawable.piclist_icon_default).fallback(R.drawable.piclist_icon_default).placeholder(R.drawable.piclist_icon_default).into(holder.imageItem);
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AlbumAdapterExtends.this.onItem != null) {
                    AlbumAdapterExtends.this.onItem.OnItemAlbumClick(position);
                }
            }
        });

        return convertView;
    }

    public OnAlbum getOnItem() {
        return this.onItem;
    }

    public void setOnItem(OnAlbum onItem) {
        this.onItem = onItem;
    }
}
