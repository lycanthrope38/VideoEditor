package com.freelancer.videoeditor.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ThongLe on 9/8/2017.
 */

public interface OnClickListener {
    interface OnCapture {
        void onFail();

        void onSuccess(String str);
    }

    interface OnClickItemBaseList {
        void OnItemClick(View view, int i);

        void OnItemNoneClick();
    }
    interface OnCustomClickListener {
        void OnCustomClick(View view, MotionEvent motionEvent);
    }

    interface OnCustomTouchListener {
        void OnCustomTouchDown(View view, MotionEvent motionEvent);

        void OnCustomTouchMoveOut(View view, MotionEvent motionEvent);

        void OnCustomTouchOther(View view, MotionEvent motionEvent);

        void OnCustomTouchUp(View view, MotionEvent motionEvent);
    }
    interface OnRecyclerClickListener {
        void onItemClicked(int i, View view, Object obj);
    }

}
