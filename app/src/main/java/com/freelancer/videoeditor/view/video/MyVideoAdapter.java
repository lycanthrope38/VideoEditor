package com.BestPhotoEditor.FreeVideoEditor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.BestPhotoEditor.FreeVideoEditor.AppConst;
import com.BestPhotoEditor.FreeVideoEditor.R;
import com.BestPhotoEditor.FreeVideoEditor.activity.VideoSavedActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import mylibsutil.util.ExtraUtils;
import mylibsutil.util.T;

public class MyVideoAdapter extends Adapter<ViewHolder> {
    private int PH_ICON_PLAY;
    private int PH_ITEM_VIDEO;
    private int PH_LINE;
    private int PWH_BTN;
    Context context;
    ArrayList<File> listFile = new ArrayList();
    private LayoutInflater mLayoutInflater;
    String rootFolder;

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        ImageView bgClick;
        ImageView btnDelete;
        ImageView btnShare;
        ImageView iconPlay;
        RelativeLayout layoutImage;
        LinearLayout layoutVideo;
        ImageView line;
        ImageView thumbVideo;
        TextView txtDate;
        TextView txtDuration;
        TextView txtName;

        public ViewHolder(View view, int PH_ITEM_VIDEO, int PWH_BTN, int PH_LINE, int PH_ICON_PLAY) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.name);
            this.txtDuration = (TextView) view.findViewById(com.universalvideoview.R.id.duration);
            this.txtDate = (TextView) view.findViewById(R.id.date);
            this.thumbVideo = (ImageView) view.findViewById(R.id.thumbVideo);
            this.btnDelete = (ImageView) view.findViewById(com.piclistphotofromgallery.R.id.btnDelete);
            this.btnShare = (ImageView) view.findViewById(R.id.btnShare);
            this.line = (ImageView) view.findViewById(libs.photoeditor.R.id.line);
            this.iconPlay = (ImageView) view.findViewById(R.id.iconPlay);
            this.bgClick = (ImageView) view.findViewById(R.id.bgClick);
            this.layoutImage = (RelativeLayout) view.findViewById(R.id.layoutImage);
            this.layoutVideo = (LinearLayout) view.findViewById(R.id.layoutVideo);
            this.layoutVideo.getLayoutParams().height = PH_ITEM_VIDEO;
            this.layoutImage.getLayoutParams().width = PH_ITEM_VIDEO;
            this.layoutImage.getLayoutParams().height = PH_ITEM_VIDEO;
            this.line.getLayoutParams().height = PH_LINE;
            this.iconPlay.getLayoutParams().width = PH_ICON_PLAY;
            this.iconPlay.getLayoutParams().height = PH_ICON_PLAY;
            this.thumbVideo.getLayoutParams().width = PH_ITEM_VIDEO;
            this.thumbVideo.getLayoutParams().height = PH_ITEM_VIDEO;
            this.bgClick.getLayoutParams().width = PH_ITEM_VIDEO;
            this.bgClick.getLayoutParams().height = PH_ITEM_VIDEO;
            this.btnDelete.getLayoutParams().width = PWH_BTN;
            this.btnDelete.getLayoutParams().height = PWH_BTN;
            this.btnShare.getLayoutParams().width = PWH_BTN;
            this.btnShare.getLayoutParams().height = PWH_BTN;
        }
    }

    public MyVideoAdapter(Context context, String rootFolder) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.rootFolder = rootFolder;
        resetData();
        this.PH_ITEM_VIDEO = (int) ((((float) ExtraUtils.getDisplayInfo((Activity) context).heightPixels) / 100.0f) * 15.0f);
        this.PWH_BTN = (int) ((((float) this.PH_ITEM_VIDEO) / 100.0f) * 20.0f);
        this.PH_LINE = (int) ((((float) this.PH_ITEM_VIDEO) / 100.0f) * 2.0f);
        this.PH_ICON_PLAY = (int) ((((float) this.PH_ITEM_VIDEO) / 100.0f) * 30.0f);
    }

    void resetData() {
        File[] fileList = new File(this.rootFolder).listFiles();
        this.listFile.clear();
        for (File tmp : fileList) {
            if (tmp.getName().endsWith(".mp4") || tmp.getName().endsWith(".MP4")) {
                this.listFile.add(tmp);
            }
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.mLayoutInflater.inflate(R.layout.item_my_video, parent, false), this.PH_ITEM_VIDEO, this.PWH_BTN, this.PH_LINE, this.PH_ICON_PLAY);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final String pathVideo = ((File) this.listFile.get(position)).getAbsolutePath();
        long timeMillis = 0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(pathVideo);
            timeMillis = Long.parseLong(retriever.extractMetadata(9));
        } catch (Exception ex) {
            ex.printStackTrace();
            MediaPlayer mp = MediaPlayer.create(this.context, Uri.fromFile(new File(pathVideo)));
            if (mp != null) {
                timeMillis = (long) mp.getDuration();
                mp.release();
            }
        }
        long duration = timeMillis / 1000;
        long hours = duration / 3600;
        long seconds = duration - ((3600 * hours) + (60 * ((duration - (3600 * hours)) / 60)));
        final File file = new File(pathVideo);
        String name = file.getName();
        Date date = new Date(file.lastModified());
        SimpleDateFormat format = new SimpleDateFormat(Locale.getDefault().getLanguage().equals("vi") ? AppConst.FORMAT_DATE_VN : AppConst.FORMAT_DATE_DEFAULT, Locale.getDefault());
        Glide.with(this.context).load(pathVideo).into(holder.thumbVideo);
        holder.txtName.setText(name);
        holder.txtDuration.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)}));
        holder.txtDate.setText(format.format(date));
        holder.layoutImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyVideoAdapter.this.nextVideoSavedActivity(pathVideo);
            }
        });
        holder.btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (file.delete()) {
                    ExtraUtils.scanFile(MyVideoAdapter.this.context, file.getAbsolutePath());
                    MyVideoAdapter.this.resetData();
                    if (MyVideoAdapter.this.listFile.size() == 0) {
                        MyVideoAdapter.this.context.finish();
                        T.show(MyVideoAdapter.this.context.getResources().getString(R.string.message_no_video_my_video));
                        return;
                    }
                    T.show(MyVideoAdapter.this.context.getResources().getString(R.string.message_delete_video_my_video));
                    MyVideoAdapter.this.notifyDataSetChanged();
                }
            }
        });
        holder.btnShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExtraUtils.shareVideoViaIntent(MyVideoAdapter.this.context, pathVideo, true);
            }
        });
    }

    public int getItemCount() {
        return this.listFile.size();
    }

    private void nextVideoSavedActivity(String videoUrl) {
        Intent intent = new Intent(this.context, VideoSavedActivity.class);
        intent.putExtra(AppConst.BUNDLE_KEY_VIDEO_URL, videoUrl);
        intent.putExtra(AppConst.BUNDLE_KEY_VIDEO_OPEN_FROM_MY_VIDEO, true);
        intent.putExtra(AppConst.BUNDLE_KEY_HOME, false);
        this.context.startActivity(intent);
    }
}
