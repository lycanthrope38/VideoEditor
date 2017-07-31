package com.freelancer.videoeditor.util;

import org.andengine.entity.sprite.Sprite;

public interface IButtonSprite {
    Sprite onClick(Sprite sprite);

    Sprite onDown(Sprite sprite);

    Sprite onMove(Sprite sprite);

    Sprite onMoveOut(Sprite sprite);

    Sprite onUp(Sprite sprite);
}
