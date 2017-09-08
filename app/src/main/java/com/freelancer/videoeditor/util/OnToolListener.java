package com.freelancer.videoeditor.util;

import com.freelancer.videoeditor.view.video.Action;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by ThongLe on 9/8/2017.
 */

public interface OnToolListener {
    interface OnToolsBlur {
        void OnBorderClick(int i);

        void OnSeekBarChange(int i);
    }
    interface OnToolBoxListener {
        void onPassData(Action action, Object obj);
    }
    interface OnStickerClick {
        void OnItemListStickerClick(String item);
    }
    interface OnSetSpriteForTools {
        void onDeleteSprite();

        void onSetSpriteForTools(Sprite sprite, int i);
    }

}
