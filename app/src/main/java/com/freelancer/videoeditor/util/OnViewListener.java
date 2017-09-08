package com.freelancer.videoeditor.util;

import android.graphics.Bitmap;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by ThongLe on 9/8/2017.
 */

public interface OnViewListener {
     interface OnViewTools {
        void onFlipH();

        void onFlipV();

        void onRotateL(int i);

        void onRotateR(int i);

        void onShowHide(int i);

        void onZoomIn(int i);

        void onZoomOut(int i);
    }

    interface OnViewTop {
        void OnBack();

        void OnDone();

        void OnSave();
    }

    interface OnViewBottom {
        void OnBackground();

        void OnBorder();

        void OnFilter();

        void OnSticker();

        void OnText();

        void UnCheck();
    }
    interface OnManagerViewCenter {
        void onHideViewCenter();
    }

    interface IBitmap {
        void onCompleted(Bitmap bitmap);
    }
    interface IButtonSprite {
        Sprite onClick(Sprite sprite);

        Sprite onDown(Sprite sprite);

        Sprite onMove(Sprite sprite);

        Sprite onMoveOut(Sprite sprite);

        Sprite onUp(Sprite sprite);
    }

    interface IOnResourceReady {
        void OnResourceReady(Bitmap bitmap);

        void onLoadFailed();
    }

    interface OnDialogConfirm {
        void OnNo();

        void OnYes();
    }

}
