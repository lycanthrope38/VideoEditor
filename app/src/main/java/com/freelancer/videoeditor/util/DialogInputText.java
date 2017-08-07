package com.freelancer.videoeditor.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;


import yuku.ambilwarna.AmbilWarnaDialog;

public class DialogInputText extends Dialog implements OnClickListener {
    int color = -1;
    EditText edtInput;
    GridView gridView;
    IBitmap mIBitmap;
    PhotoEditorActivity mainActivity;
    int pH = (ConfigScreen.SCREENHEIGHT / 4);
    int pW = (ConfigScreen.SCREENWIDTH / 8);
    ScrollView scrollView;
    int size = 24;
    TextView txtSize;
    View viewColor;

    public DialogInputText(PhotoEditorActivity mainActivity, IBitmap mIBitmap) {
        super(mainActivity);
        this.mIBitmap = mIBitmap;
        this.mainActivity = mainActivity;
        getWindow().requestFeature(1);
        setContentView(R.layout.dialog_input_text);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.scrollView.getLayoutParams().height = this.pH;
        this.viewColor = findViewById(R.id.viewColor);
        this.viewColor.setBackgroundColor(-1);
        this.txtSize = (TextView) findViewById(R.id.txtSize);
        ((Button) findViewById(R.id.btnApply)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnNo)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnDown)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnUp)).setOnClickListener(this);
        RelativeLayout btnColor = (RelativeLayout) findViewById(R.id.btnColor);
        btnColor.setOnClickListener(this);
        btnColor.getLayoutParams().width = this.pW;
        btnColor.getLayoutParams().height = this.pW;
        RelativeLayout btnB = (RelativeLayout) findViewById(R.id.btnB);
        btnB.setOnClickListener(this);
        btnB.getLayoutParams().width = this.pW;
        btnB.getLayoutParams().height = this.pW;
        RelativeLayout btnI = (RelativeLayout) findViewById(R.id.btnI);
        btnI.setOnClickListener(this);
        btnI.getLayoutParams().width = this.pW;
        btnI.getLayoutParams().height = this.pW;
        RelativeLayout btnBI = (RelativeLayout) findViewById(R.id.btnBI);
        btnBI.setOnClickListener(this);
        btnBI.getLayoutParams().width = this.pW;
        btnBI.getLayoutParams().height = this.pW;
        RelativeLayout btnN = (RelativeLayout) findViewById(R.id.btnN);
        btnN.setOnClickListener(this);
        btnN.getLayoutParams().width = this.pW;
        btnN.getLayoutParams().height = this.pW;
        RelativeLayout btnLe = (RelativeLayout) findViewById(R.id.btnLe);
        btnLe.setOnClickListener(this);
        btnLe.getLayoutParams().width = this.pW;
        btnLe.getLayoutParams().height = this.pW;
        RelativeLayout btnCe = (RelativeLayout) findViewById(R.id.btnCe);
        btnCe.setOnClickListener(this);
        btnCe.getLayoutParams().width = this.pW;
        btnCe.getLayoutParams().height = this.pW;
        RelativeLayout btnRi = (RelativeLayout) findViewById(R.id.btnRi);
        btnRi.setOnClickListener(this);
        btnRi.getLayoutParams().width = this.pW;
        btnRi.getLayoutParams().height = this.pW;
        this.edtInput = (EditText) findViewById(R.id.edtInput);
        this.edtInput.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.gridView = findViewById(R.id.gridView);
        this.gridView.getLayoutParams().height = this.pH;
        this.gridView.setAdapter(new FontAdapter(mainActivity, this.edtInput));
        getWindow().setSoftInputMode(5);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnApply) {
            onApply();
            dismiss();
        } else if (i == R.id.btnNo) {
            dismiss();
        } else if (i == R.id.btnB) {
            this.edtInput.setTypeface(this.edtInput.getTypeface(), 1);
        } else if (i == R.id.btnI) {
            this.edtInput.setTypeface(this.edtInput.getTypeface(), 2);
        } else if (i == R.id.btnBI) {
            this.edtInput.setTypeface(this.edtInput.getTypeface(), 3);
        } else if (i == R.id.btnN) {
            this.edtInput.setTypeface(null);
        } else if (i == R.id.btnLe) {
            this.edtInput.setGravity(3);
        } else if (i == R.id.btnCe) {
            this.edtInput.setGravity(17);
        } else if (i == R.id.btnRi) {
            this.edtInput.setGravity(5);
        } else if (i == R.id.btnColor) {
            showDialogSelectColor(this.mainActivity);
        } else if (i == R.id.btnDown) {
            if (this.size - 1 > 0) {
                this.size--;
                this.txtSize.setText("" + this.size);
                this.edtInput.setTextSize((float) this.size);
            }
        } else if (i == R.id.btnUp) {
            this.size++;
            this.txtSize.setText("" + this.size);
            this.edtInput.setTextSize((float) this.size);
        }
    }

    public void onApply() {
        if (this.edtInput.getText().toString().length() != 0) {
            ExtraUtils.setBackgroundDrawable(this.edtInput, null);
            this.edtInput.setCursorVisible(false);
            this.edtInput.setSelectAllOnFocus(false);
            this.edtInput.setSelected(false);
            this.edtInput.clearComposingText();
            Bitmap mBitmap = Bitmap.createBitmap(this.edtInput.getWidth(), this.edtInput.getHeight(), Config.ARGB_8888);
            Canvas mCanvas = new Canvas(mBitmap);
            this.edtInput.layout(0, 0, this.edtInput.getWidth(), this.edtInput.getHeight());
            this.edtInput.draw(mCanvas);
            this.mIBitmap.onCompleted(mBitmap);
        }
    }

    public void showDialogSelectColor(Context mContext) {

        new AmbilWarnaDialog(mContext, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                DialogInputText.this.color = color;
                    DialogInputText.this.edtInput.setTextColor(color);
                    DialogInputText.this.viewColor.setBackgroundColor(color);
            }
        }).show();
//        if (this.mColorPickerDialog == null) {
//            this.mColorPickerDialog = new ColorPickerDialog(mContext, this.color);
//            this.mColorPickerDialog.setAlphaSliderVisible(true);
//            this.mColorPickerDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
//                public void onColorChanged(int color) {
//                    DialogInputText.this.color = color;
//                    DialogInputText.this.edtInput.setTextColor(color);
//                    DialogInputText.this.viewColor.setBackgroundColor(color);
//                }
//            });
//        }
//        this.mColorPickerDialog.show();
    }
}
