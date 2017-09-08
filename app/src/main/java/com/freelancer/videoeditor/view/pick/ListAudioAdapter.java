package com.freelancer.videoeditor.view.pick;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.vo.Audio;

import java.util.List;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class ListAudioAdapter  extends BaseAdapter {
    private Context mContext;
    private List<Audio> mData;
    private LayoutInflater mInflater;
    private OnPickListener.OnAudioClickListener mListener;
    private int oldPosSelected = -1;

    static class ViewHolder {
        RadioButton radioButton;
        TextView textAudioName;

        ViewHolder() {
        }
    }

    public ListAudioAdapter(Activity context, List<Audio> listAudio) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = listAudio;
        this.mListener = (OnPickListener.OnAudioClickListener) context;
    }

    public void removeAudioSelected() {
        if (this.oldPosSelected != -1) {
            ((Audio) this.mData.get(this.oldPosSelected)).setChecked(false);
            notifyDataSetChanged();
            this.oldPosSelected = -1;
        }
    }

    public int getCount() {
        return this.mData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.item_list_audio, parent, false);
            holder = new ViewHolder();
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_choose_audio);
            holder.textAudioName = (TextView) convertView.findViewById(R.id.text_item_audio_name);
            convertView.setTag(holder);
        }
        final Audio item = (Audio) this.mData.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.radioButton.setOnClickListener(v -> {
            if (position != ListAudioAdapter.this.oldPosSelected) {
                ListAudioAdapter.this.setRadioCheck(position, item);
            }
        });
        holder.textAudioName.setText(item.getName());
        ViewHolder finalHolder = holder;
        holder.textAudioName.setOnClickListener(v -> {
            if (!finalHolder.radioButton.isChecked()) {
                ListAudioAdapter.this.setRadioCheck(position, item);
            }
        });
        holder.radioButton.setChecked(item.isChecked());
        return convertView;
    }

    private void setRadioCheck(int position, Audio item) {
        this.mData.get(position).setChecked(true);
        if (this.oldPosSelected != -1) {
            this.mData.get(this.oldPosSelected).setChecked(false);
        }
        this.oldPosSelected = position;
        playAudio(item);
        notifyDataSetChanged();
    }

    private void playAudio(Audio audio) {
        if (this.mListener != null) {
            this.mListener.onPlayAudio(audio);
        }
    }
}
