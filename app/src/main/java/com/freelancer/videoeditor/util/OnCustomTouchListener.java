package com.freelancer.videoeditor.util;

import android.view.MotionEvent;
import android.view.View;

public interface OnCustomTouchListener {
    void OnCustomTouchDown(View view, MotionEvent motionEvent);

    void OnCustomTouchMoveOut(View view, MotionEvent motionEvent);

    void OnCustomTouchOther(View view, MotionEvent motionEvent);

    void OnCustomTouchUp(View view, MotionEvent motionEvent);
}
