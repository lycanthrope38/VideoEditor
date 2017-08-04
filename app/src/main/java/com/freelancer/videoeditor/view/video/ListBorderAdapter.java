package com.freelancer.videoeditor.view.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.OnRecyclerClickListener;

import java.util.List;

public class ListBorderAdapter extends RecyclerView.Adapter<ListBorderAdapter.ViewHolder> {
    private List<String> mAppInfo;
    private Bitmap mBitmapGirl;
    private int mColumnSpace = 0;
    private int mColumnWith = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnRecyclerClickListener mListener;

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private ImageView bkg;
        private RelativeLayout container;
        private ImageView girl;
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);
            this.container = (RelativeLayout) view.findViewById(R.id.container);
            this.girl = (ImageView) view.findViewById(R.id.thumb_girl);
            this.icon = (ImageView) view.findViewById(R.id.thumb_border);
            this.bkg = (ImageView) view.findViewById(R.id.thumb_bkg);
        }
    }

    public ListBorderAdapter(Context context, List<String> appInfo) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mAppInfo = appInfo;
    }

    public void setBitmapGirl(Bitmap bitmap) {
        this.mBitmapGirl = bitmap;
    }

    public void setColumnWith(int columnWith) {
        this.mColumnWith = columnWith;
    }

    public void setColumnSpace(int space) {
        this.mColumnSpace = space;
    }

    public void setOnItemClickListener(OnRecyclerClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.mInflater.inflate(R.layout.item_list_border_horizontal, parent, false));
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        ((LayoutParams) holder.container.getLayoutParams()).width = this.mColumnWith;
//        holder.container.setPadding(this.mColumnSpace, 0, this.mColumnSpace, 0);
        final String item = this.mAppInfo.get(position);
        holder.container.setOnClickListener(v -> {
            if (ListBorderAdapter.this.mListener != null) {
                ListBorderAdapter.this.mListener.onItemClicked(holder.getAdapterPosition(), holder.container, item);
            }
        });
        if (this.mBitmapGirl == null || item.contains("none")) {
            holder.icon.setAlpha(HandlerTools.ROTATE_R);
            holder.girl.setImageBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.mylibsutil_bg_null));
            holder.bkg.setImageResource(position % 2 == 0 ?R.drawable.bkg_theme : R.drawable.bkg_theme_two);
        } else {
            holder.icon.setAlpha(HandlerTools.ROTATE_R);
            holder.girl.setImageBitmap(this.mBitmapGirl);
            holder.bkg.setBackgroundColor(0);
        }
        ExtraUtils.displayImage(this.mContext, holder.icon, item);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return this.mAppInfo.size();
    }
}
