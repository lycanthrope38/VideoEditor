package com.freelancer.videoeditor.util;


import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;

public class HandlerTools {
    public static final float ROTATE_L = -1.0f;
    public static final float ROTATE_R = 1.0f;
    public static final float ZOOM_IN = 0.01f;
    public static final float ZOOM_OUT = -0.01f;
    TimerHandler timerHandler;

    public void zoom(PhotoEditorActivity mainActivity, Sprite mSprite, int typeObject, int action, float zoom) {
        if (mSprite != null) {
            if (action == 0) {
                if (this.timerHandler != null) {
                    mainActivity.getMainScene().unregisterUpdateHandler(this.timerHandler);
                    this.timerHandler = null;
                }
                if (this.timerHandler == null) {
                    final Sprite sprite = mSprite;
                    final float f = zoom;
                    final int i = typeObject;
                    final PhotoEditorActivity photoEditorActivity = mainActivity;
                    this.timerHandler = new TimerHandler(ZOOM_IN, true, new ITimerCallback() {
                        public void onTimePassed(TimerHandler pTimerHandler) {
                            if (sprite.getScaleX() < AppConst.ZOOM_MAX && f == HandlerTools.ZOOM_IN) {
                                sprite.setScale(sprite.getScaleX() + f);
                            }
                            if (sprite.getScaleX() > AppConst.ZOOM_MIN && f == HandlerTools.ZOOM_OUT) {
                                sprite.setScale(sprite.getScaleX() + f);
                            }
                            if (i == 2) {
                                photoEditorActivity.rectangleTextAndSticker.updateScaleBtnDelete();
                            }
                        }
                    });
                    mainActivity.getMainScene().registerUpdateHandler(this.timerHandler);
                }
            } else if (action == 1 && this.timerHandler != null) {
                mainActivity.getMainScene().unregisterUpdateHandler(this.timerHandler);
                this.timerHandler = null;
            }
        }
    }

    public void rotate(PhotoEditorActivity mainActivity, final Sprite mSprite, int typeObject, int action, final float rotate) {
        if (mSprite != null) {
            if (action == 0) {
                if (this.timerHandler != null) {
                    mainActivity.getMainScene().unregisterUpdateHandler(this.timerHandler);
                    this.timerHandler = null;
                }
                if (this.timerHandler == null) {
                    this.timerHandler = new TimerHandler(ZOOM_IN, true, new ITimerCallback() {
                        public void onTimePassed(TimerHandler pTimerHandler) {
                            mSprite.setRotation(mSprite.getRotation() + rotate);
                        }
                    });
                    mainActivity.getMainScene().registerUpdateHandler(this.timerHandler);
                }
            } else if (action == 1 && this.timerHandler != null) {
                mainActivity.getMainScene().unregisterUpdateHandler(this.timerHandler);
                this.timerHandler = null;
            }
        }
    }
}
