package com.freelancer.videoeditor.util;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.vo.ItemFrame;

import java.util.ArrayList;

public class DefineFrame {
    public ArrayList<ItemFrame> getFrame(int type, float pW, float pH) {
        ArrayList<ItemFrame> mListItemFrames = new ArrayList();
        ArrayList<ItemFrame> arrayList;
        float tmpPW;
        float tmpPH;
        float tmpWH;
        float pXStart;
        float f;
        float mW;
        float mH;
        switch (type) {
            case 1:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                break;
            case 2:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH));
                break;
            case 3:
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH / 2.0f));
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW, pH / 2.0f));
                break;
            case 4:
                float tmpPW2 = pW / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, tmpPW2 * 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, tmpPW2 * 2.0f, 0.0f, tmpPW2, pH));
                break;
            case 5:
                tmpPW = pW / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, tmpPW, pH));
                mListItemFrames.add(new ItemFrame(1, tmpPW, 0.0f, tmpPW * 2.0f, pH));
                break;
            case 6:
                float tmpPH2 = pH / 3.0f;
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(0, 0.0f, 0.0f, pW, tmpPH2 * 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, tmpPH2 * 2.0f, pW, tmpPH2));
                break;
            case 7:
                tmpPH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, tmpPH));
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(1, 0.0f, tmpPH, pW, tmpPH * 2.0f));
                break;
            case 8:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                tmpWH = pW / 2.0f;
                pXStart = tmpWH / 10.0f;
                tmpPW = tmpWH - pXStart;
                mListItemFrames.add(new ItemFrame(1, pXStart, pXStart, tmpPW, tmpPW));
                break;
            case 9:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                tmpWH = pW / 2.0f;
                pXStart = tmpWH / 10.0f;
                tmpPW = tmpWH - pXStart;
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(1, tmpWH, pXStart, tmpPW, tmpPW));
                break;
            case 10:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                tmpWH = pW / 2.0f;
                pXStart = tmpWH / 10.0f;
                f = pXStart;
                tmpPW = tmpWH - pXStart;
                mListItemFrames.add(new ItemFrame(1, pXStart, tmpWH, tmpPW, tmpPW));
                break;
            case 11:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                tmpWH = pW / 2.0f;
                pXStart = tmpWH / 10.0f;
                f = pXStart;
                tmpPW = tmpWH - pXStart;
                mListItemFrames.add(new ItemFrame(1, tmpWH, tmpWH, tmpPW, tmpPW));
                break;
            case 12:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                pXStart = pW / 10.0f;
                f = pXStart;
                mListItemFrames.add(new ItemFrame(1, pXStart, f, pW - (2.0f * pXStart), (pH / 2.0f) - f));
                break;
            case 13:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                pXStart = pW / 10.0f;
                f = pXStart;
                mListItemFrames.add(new ItemFrame(1, pXStart, f, (pW / 2.0f) - pXStart, pH - (2.0f * f)));
                break;
            case 14:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                pXStart = pW / 10.0f;
                mListItemFrames.add(new ItemFrame(1, pXStart, pH / 2.0f, pW - (2.0f * pXStart), (pH / 2.0f) - pXStart));
                break;
            case 15:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH));
                pXStart = pW / 10.0f;
                f = pXStart;
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(1, pW / 2.0f, f, (pW / 2.0f) - pXStart, pH - (2.0f * f)));
                break;
            case 16:
                tmpPW = pW / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, tmpPW, pH));
                mListItemFrames.add(new ItemFrame(1, 0.0f + tmpPW, 0.0f, tmpPW, pH));
                mListItemFrames.add(new ItemFrame(2, 0.0f + (2.0f * tmpPW), 0.0f, tmpPW, pH));
                break;
            case 17 /*17*/:
                tmpPH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, tmpPH));
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(1, 0.0f, 0.0f + tmpPH, pW, tmpPH));
                arrayList = mListItemFrames;
                arrayList.add(new ItemFrame(2, 0.0f, 0.0f + (2.0f * tmpPH), pW, tmpPH));
                break;
            case R.styleable.Toolbar_titleMarginBottom /*18*/:
                tmpPW = pW / 3.0f;
                tmpPH = (pH / 10.0f) * 8.0f;
                float tmpPH1 = (pH - tmpPH) / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, tmpPW, tmpPH));
                mListItemFrames.add(new ItemFrame(1, 0.0f + tmpPW, tmpPH1, tmpPW, tmpPH));
                mListItemFrames.add(new ItemFrame(2, 0.0f + (2.0f * tmpPW), tmpPH1 * 2.0f, tmpPW, tmpPH));
                break;
//            case R.styleable.Toolbar_titleMargins /*19*/:
//                tmpPW = pW / 3.0f;
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, tmpPW, pH));
//                f = pH / 10.0f;
//                arrayList = mListItemFrames;
//                arrayList.add(new ItemFrame(1, 0.0f + tmpPW, f, tmpPW, pH - f));
//                f = (pH / 10.0f) * 2.0f;
//                arrayList = mListItemFrames;
//                arrayList.add(new ItemFrame(2, 0.0f + (2.0f * tmpPW), f, tmpPW, pH - f));
//                break;
//            case HttpEngine.MAX_FOLLOW_UPS /*20*/:
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
//                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
//                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, pH));
//                break;
            case R.styleable.Toolbar_buttonGravity /*21*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                break;
            case R.styleable.Toolbar_collapseIcon /*22*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                break;
            case R.styleable.Toolbar_collapseContentDescription /*23*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW, pH / 2.0f));
                break;
            case R.styleable.Toolbar_navigationIcon /*24*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.Toolbar_navigationContentDescription /*25*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.Toolbar_logoDescription /*26*/:
                mListItemFrames.add(new ItemFrame(1, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(0, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, pH));
                break;
            case R.styleable.Toolbar_titleTextColor /*27*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.Toolbar_subtitleTextColor /*28*/:
                mListItemFrames.add(new ItemFrame(1, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(0, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, pH));
                break;
            case R.styleable.AppCompatTheme_actionModeBackground /*29*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeSplitBackground /*30*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 4.0f, pH));
                mListItemFrames.add(new ItemFrame(2, (pW / 4.0f) * 2.0f, 0.0f, pW / 4.0f, pH));
                mListItemFrames.add(new ItemFrame(3, (pW / 4.0f) * 3.0f, 0.0f, pW / 4.0f, pH));
                break;
            case R.styleable.AppCompatTheme_actionModeCloseDrawable /*31*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 4.0f, pW, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, (pH / 4.0f) * 2.0f, pW, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 4.0f) * 3.0f, pW, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeCutDrawable /*32*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeCopyDrawable /*33*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, 0.0f, pW / 2.0f, pH));
                break;
            case R.styleable.AppCompatTheme_actionModePasteDrawable /*34*/:
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, pW / 2.0f, pH));
                mListItemFrames.add(new ItemFrame(0, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeSelectAllDrawable /*35*/:
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, pW, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(0, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeShareDrawable /*36*/:
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(0, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeFindDrawable /*37*/:
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(0, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, (pH / 3.0f) * 2.0f, pW, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /*38*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 3.0f) * 2.0f, pW, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_actionModePopupWindowStyle /*39*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceLargePopupMenu /*40*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 2.0f, (pW / 3.0f) * 2.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu /*41*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearancePopupMenuHeader /*42*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_dialogTheme /*43*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_dialogPreferredPadding /*44*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                break;
            case R.styleable.AppCompatTheme_listDividerAlertDialog /*45*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                break;
            case R.styleable.AppCompatTheme_actionDropDownStyle /*46*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, (pW / 3.0f) * 2.0f, pH / 3.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                break;
            case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /*47*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_spinnerDropDownItemStyle /*48*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_homeAsUpIndicator /*49*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, (pH / 3.0f) * 2.0f, pW, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_actionButtonStyle /*50*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_buttonBarStyle /*51*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_buttonBarButtonStyle /*52*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_selectableItemBackground /*53*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                break;
            case R.styleable.AppCompatTheme_selectableItemBackgroundBorderless /*54*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_borderlessButtonStyle /*55*/:
                mListItemFrames.add(new ItemFrame(0, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_dividerVertical /*56*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_dividerHorizontal /*57*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_activityChooserViewStyle /*58*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_toolbarStyle /*59*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_toolbarNavigationButtonStyle /*60*/:
                mListItemFrames.add(new ItemFrame(0, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_popupMenuStyle /*61*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_popupWindowStyle /*62*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_editTextColor /*63*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_editTextBackground /*64*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 4.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, (pW / 2.0f) + (pW / 4.0f), pH / 2.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_imageButtonStyle /*65*/:
                mListItemFrames.add(new ItemFrame(0, pW / 2.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, (pH / 2.0f) + (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceSearchResultTitle /*66*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, pW / 2.0f, (pH / 2.0f) + (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceSearchResultSubtitle /*67*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, (pW / 2.0f) + (pW / 4.0f), 0.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_textColorSearchUrl /*68*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, (pW / 2.0f) - (mW / 2.0f), 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 2.0f) - mW, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_searchViewStyle /*69*/:
                mW = pW / 3.0f;
                mListItemFrames.add(new ItemFrame(0, (pW / 2.0f) - (mW / 2.0f), pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 2.0f) - mW, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_listPreferredItemHeight /*70*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, (pH / 2.0f) - (mH / 2.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, (pH / 2.0f) - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW - mW, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_listPreferredItemHeightSmall /*71*/:
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, pW - (pW / 3.0f), (pH / 2.0f) - (mH / 2.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, (pH / 2.0f) - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_listPreferredItemHeightLarge /*72*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, mW, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW - mW, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, mW, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, pW - mW, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_listPreferredItemPaddingLeft /*73*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW - mW, mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, mW, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, mW, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_listPreferredItemPaddingRight /*74*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - mH, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 2.0f, pH - mH, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_dropDownListViewStyle /*75*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, mW, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_listPopupWindowStyle /*76*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, mW, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceListItem /*77*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, mW, mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW - mW, mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - mH, pW, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_textAppearanceListItemSmall /*78*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, (2.0f * pW) / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, (3.0f * pW) / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 4.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, (2.0f * pW) / 4.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, (3.0f * pW) / 4.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_panelBackground /*79*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, mW, pH - mH, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(7, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_panelMenuListWidth /*80*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 2.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 4.0f), pH / 2.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_panelMenuListTheme /*81*/:
                mW = pW / 3.0f;
                mH = pH / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW - (pW / 3.0f), pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 3.0f), pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_listChoiceBackgroundIndicator /*82*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 4.0f), 0.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 4.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - (pH / 4.0f), pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 4.0f, pH - (pH / 4.0f), pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 2.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_colorPrimary /*83*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 4.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, 0.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 4.0f, 0.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 4.0f, pH / 2.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 4.0f), pH - (pH / 4.0f), pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, pW - (pW / 4.0f), pH / 2.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 4.0f), 0.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_colorPrimaryDark /*84*/:
                mW = (2.0f * pW) / 3.0f;
                mH = (2.0f * pH) / 3.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, mW / 2.0f, 0.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 4.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, mW / 2.0f, pH / 4.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 2.0f, mW, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW - (pW / 3.0f), pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 3.0f), pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_colorAccent /*85*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 2.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_colorControlNormal /*86*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW - (pW / 4.0f), 0.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, 0.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW / 4.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 2.0f, pH / 2.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 4.0f), pH / 2.0f, pW / 4.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_colorControlActivated /*87*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 4.0f), 0.0f, pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 4.0f, pH / 4.0f, pW / 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 4.0f), pH / 4.0f, pW / 4.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - (pH / 4.0f), pW / 4.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 4.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (pW / 4.0f), pH - (pH / 4.0f), pW / 4.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_colorControlHighlight /*88*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 4.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 2.0f, pH / 2.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, 0.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(8, pW / 2.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_colorButtonNormal /*89*/:
                mW = (3.0f * pW) / 4.0f;
                mH = (3.0f * pH) / 4.0f;
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, mW / 2.0f, 0.0f, mW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, mW, 0.0f, pW / 4.0f, mH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 4.0f, mH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 4.0f, pH / 4.0f, (2.0f * mW) / 3.0f, (2.0f * mH) / 3.0f));
                mListItemFrames.add(new ItemFrame(5, mW, mH / 2.0f, pW / 4.0f, mH / 2.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - (mH / 2.0f), pW / 4.0f, mH / 2.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 4.0f, pH - (pH / 4.0f), mH / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (mW / 2.0f), pH - (pH / 4.0f), mH / 2.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_colorSwitchThumbNormal /*90*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(1, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 3.0f), pH / 4.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 2.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), pH / 2.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 3.0f, pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (pW / 3.0f), pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_controlBackground /*91*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, 0.0f, pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 3.0f, pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (pW / 3.0f), pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_colorBackgroundFloating /*92*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH / 3.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(6, pW / 4.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(7, pW / 2.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (pW / 4.0f), pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_alertDialogStyle /*93*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(6, pW - (pW / 3.0f), pH / 4.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(7, pW - (pW / 3.0f), pH / 2.0f, pW / 3.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(8, pW - (pW / 3.0f), pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
                break;
            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /*94*/:
                mListItemFrames.add(new ItemFrame(1, 0.0f, 0.0f, pW, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(0, pW / 2.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_alertDialogCenterButtons /*95*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
                break;
            case R.styleable.AppCompatTheme_alertDialogTheme /*96*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                break;
            case R.styleable.AppCompatTheme_textColorAlertDialogListItem /*97*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(4, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /*98*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, (pW / 3.0f) * 2.0f, (pH / 3.0f) * 2.0f));
                mListItemFrames.add(new ItemFrame(1, (pW / 3.0f) * 2.0f, 0.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(2, (pW / 3.0f) * 2.0f, pH / 3.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, (pH / 3.0f) * 2.0f, pW / 3.0f, pH / 3.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, (pH / 3.0f) * 2.0f, (pW / 3.0f) * 2.0f, pH / 3.0f));
                break;
            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /*99*/:
                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 4.0f, pW, pH / 2.0f));
                mListItemFrames.add(new ItemFrame(3, 0.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                mListItemFrames.add(new ItemFrame(4, pW / 2.0f, pH - (pH / 4.0f), pW / 2.0f, pH / 4.0f));
                break;
//            case StatusLine.HTTP_CONTINUE /*100*/:
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
//                mListItemFrames.add(new ItemFrame(1, pW / 2.0f, 0.0f, pW / 2.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(2, 0.0f, (pH / 3.0f) * 2.0f, pW / 2.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(3, pW / 2.0f, pH / 3.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
//                break;
//            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /*101*/:
//                mW = pW / 3.0f;
//                mH = pH / 3.0f;
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(1, 0.0f, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(2, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(3, mH, 0.0f, pW / 3.0f, pH));
//                mListItemFrames.add(new ItemFrame(4, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(5, pW - mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(6, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                break;
//            case R.styleable.AppCompatTheme_buttonStyle /*102*/:
//                mW = pW / 3.0f;
//                mH = pH / 3.0f;
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH));
//                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(3, mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(4, pW - mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(5, mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(6, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                break;
//            case R.styleable.AppCompatTheme_buttonStyleSmall /*103*/:
//                mW = pW / 3.0f;
//                mH = pH / 3.0f;
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(1, 0.0f, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(2, mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(3, pW - mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(4, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(5, mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(6, pW - mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                break;
//            case R.styleable.AppCompatTheme_checkboxStyle /*104*/:
//                mW = pW / 3.0f;
//                mH = pH / 3.0f;
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, mW, mH));
//                mListItemFrames.add(new ItemFrame(1, mW, 0.0f, mW, mH));
//                mListItemFrames.add(new ItemFrame(2, pW - mW, 0.0f, pW / 3.0f, pH));
//                mListItemFrames.add(new ItemFrame(3, 0.0f, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(4, mW, mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - mH, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(6, mW, pH - mH, pW / 3.0f, pH / 3.0f));
//                break;
//            case R.styleable.AppCompatTheme_checkedTextViewStyle /*105*/:
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(1, pW / 4.0f, 0.0f, pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(2, pW / 2.0f, 0.0f, pW / 2.0f, (pH / 3.0f) * 2.0f));
//                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 3.0f, pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(4, pW / 4.0f, pH / 3.0f, pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(5, 0.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(6, pW / 4.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(7, pW / 2.0f, pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(8, pW - (pW / 4.0f), pH - (pH / 3.0f), pW / 4.0f, pH / 3.0f));
//                break;
//            case R.styleable.AppCompatTheme_editTextStyle /*106*/:
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(1, pW / 3.0f, 0.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(2, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(3, 0.0f, pH / 4.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, pH / 4.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(5, pW - (pW / 3.0f), pH / 4.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(6, 0.0f, pH / 2.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(7, pW / 3.0f, pH / 2.0f, (pW / 3.0f) * 2.0f, pH / 2.0f));
//                mListItemFrames.add(new ItemFrame(8, 0.0f, pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
//                break;
//            case R.styleable.AppCompatTheme_radioButtonStyle /*107*/:
//                mListItemFrames.add(new ItemFrame(0, 0.0f, 0.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(1, 0.0f, pH / 4.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(2, 0.0f, pH / 2.0f, pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(3, 0.0f, pH - (pH / 4.0f), pW / 3.0f, pH / 4.0f));
//                mListItemFrames.add(new ItemFrame(4, pW / 3.0f, 0.0f, pW / 3.0f, pH / 2.0f));
//                mListItemFrames.add(new ItemFrame(5, pW / 3.0f, pH / 2.0f, pW / 3.0f, pH / 2.0f));
//                mListItemFrames.add(new ItemFrame(6, pW - (pW / 3.0f), 0.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(7, pW - (pW / 3.0f), pH / 3.0f, pW / 3.0f, pH / 3.0f));
//                mListItemFrames.add(new ItemFrame(8, pW - (pW / 3.0f), pH - (pH / 3.0f), pW / 3.0f, pH / 3.0f));
//                break;
        }
        return mListItemFrames;
    }
}
