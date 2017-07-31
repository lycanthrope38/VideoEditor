package com.freelancer.videoeditor.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.freelancer.videoeditor.R;

import java.util.Locale;

public class ColorPickerDialog extends Dialog implements OnClickListener, ColorPickerView.OnColorChangedListener {
    Button btnApply;
    Button btnCancel;
    private ColorPickerView mColorPicker;
    private ColorStateList mHexDefaultTextColor;
    private EditText mHexVal;
    private boolean mHexValueEnabled = false;
    private OnColorChangedListener mListener;
    private ColorPickerPanelView mNewColor;
    private ColorPickerPanelView mOldColor;

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    public ColorPickerDialog(Context context, int initialColor) {
        super(context);
        requestWindowFeature(1);
        init(initialColor);
    }

    private void init(int color) {
        getWindow().setFormat(1);
        setUp(color);
    }

    private void setUp(int color) {
        View layout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_color_picker, null);
        setContentView(layout);
        this.btnApply = (Button) layout.findViewById(R.id.btnApply);
        this.btnApply.setOnClickListener(this);
        this.btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        this.btnCancel.setOnClickListener(this);
        setTitle(R.string.dialog_color_picker);
        this.mColorPicker = (ColorPickerView) layout.findViewById(R.id.color_picker_view);
        this.mOldColor = (ColorPickerPanelView) layout.findViewById(R.id.old_color_panel);
        this.mNewColor = (ColorPickerPanelView) layout.findViewById(R.id.new_color_panel);
        this.mHexVal = (EditText) layout.findViewById(R.id.hex_val);
        this.mHexVal.setInputType(524288);
        this.mHexDefaultTextColor = this.mHexVal.getTextColors();
        this.mHexVal.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 6) {
                    return false;
                }
                ((InputMethodManager) v.getContext().getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
                String s = ColorPickerDialog.this.mHexVal.getText().toString();
                if (s.length() > 5 || s.length() < 10) {
                    try {
                        ColorPickerDialog.this.mColorPicker.setColor(ColorPickerPreference.convertToColorInt(s.toString()), true);
                        ColorPickerDialog.this.mHexVal.setTextColor(ColorPickerDialog.this.mHexDefaultTextColor);
                        return true;
                    } catch (IllegalArgumentException e) {
                        ColorPickerDialog.this.mHexVal.setTextColor(-65536);
                        return true;
                    }
                }
                ColorPickerDialog.this.mHexVal.setTextColor(-65536);
                return true;
            }
        });
        ((LinearLayout) this.mOldColor.getParent()).setPadding(Math.round(this.mColorPicker.getDrawingOffset()), 0, Math.round(this.mColorPicker.getDrawingOffset()), 0);
        this.mOldColor.setOnClickListener(this);
        this.mNewColor.setOnClickListener(this);
        this.mColorPicker.setOnColorChangedListener(this);
        this.mOldColor.setColor(color);
        this.mColorPicker.setColor(color, true);
    }

    public void onColorChanged(int color) {
        this.mNewColor.setColor(color);
        if (this.mHexValueEnabled) {
            updateHexValue(color);
        }
    }

    public void setHexValueEnabled(boolean enable) {
        this.mHexValueEnabled = enable;
        if (enable) {
            this.mHexVal.setVisibility(0);
            updateHexLengthFilter();
            updateHexValue(getColor());
            return;
        }
        this.mHexVal.setVisibility(8);
    }

    public boolean getHexValueEnabled() {
        return this.mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible()) {
            this.mHexVal.setFilters(new InputFilter[]{new LengthFilter(9)});
            return;
        }
        this.mHexVal.setFilters(new InputFilter[]{new LengthFilter(7)});
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            this.mHexVal.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            this.mHexVal.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        this.mHexVal.setTextColor(this.mHexDefaultTextColor);
    }

    public void setAlphaSliderVisible(boolean visible) {
        this.mColorPicker.setAlphaSliderVisible(visible);
        if (this.mHexValueEnabled) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    public boolean getAlphaSliderVisible() {
        return this.mColorPicker.getAlphaSliderVisible();
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.mListener = listener;
    }

    public int getColor() {
        return this.mColorPicker.getColor();
    }

    public void onClick(View v) {
        if ((v.getId() == R.id.new_color_panel || v.getId() == R.id.btnApply) && this.mListener != null) {
            this.mListener.onColorChanged(this.mNewColor.getColor());
        }
        dismiss();
    }

    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("old_color", this.mOldColor.getColor());
        state.putInt("new_color", this.mNewColor.getColor());
        return state;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mOldColor.setColor(savedInstanceState.getInt("old_color"));
        this.mColorPicker.setColor(savedInstanceState.getInt("new_color"), true);
    }
}
