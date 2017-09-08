package com.freelancer.videoeditor.view.pick;

import com.freelancer.videoeditor.vo.Audio;
import com.freelancer.videoeditor.vo.Item;

/**
 * Created by ThongLe on 9/8/2017.
 */

public interface OnPickListener {
    interface OnAlbum {
        void OnItemAlbumClick(int i);
    }

    interface OnAudioClickListener {
        void onPlayAudio(Audio audio);
    }
    interface OnListAlbum {
        void OnItemListAlbumClick(Item item);
    }

}
