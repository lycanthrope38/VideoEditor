package com.freelancer.videoeditor.util;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.vo.ItemFrame;

import java.util.ArrayList;

public class DefineFrame {
    public ArrayList<ItemFrame> getFrame(int type, float pW, float pH) {
        ArrayList<ItemFrame> mListItemFrames = new ArrayList();
        switch (type) {
            case 1:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                break;
        }
        return mListItemFrames;
    }
}
