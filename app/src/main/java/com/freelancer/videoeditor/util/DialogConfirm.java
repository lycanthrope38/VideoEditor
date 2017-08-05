package com.freelancer.videoeditor.util;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

public class DialogConfirm extends Dialog implements OnClickListener {
    PhotoEditorActivity mainActivity;
    OnDialogConfirm onDialogConfirm;
    String pathFile;

    public DialogConfirm(PhotoEditorActivity mainActivity, OnDialogConfirm onDialogConfirm) {
        super(mainActivity);
        this.mainActivity = mainActivity;
        this.onDialogConfirm = onDialogConfirm;
        getWindow().requestFeature(1);
        setContentView(R.layout.libphotoeditor_dialog_confirm);
        ((Button) findViewById(R.id.btnSave)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnNo)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnCancel)).setOnClickListener(this);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSave) {
            if (this.onDialogConfirm != null) {
                this.onDialogConfirm.OnYes();
            }
            dismiss();
        } else if (i == R.id.btnNo) {
            if (this.onDialogConfirm != null) {
                this.onDialogConfirm.OnNo();
            }
            dismiss();
        } else if (i == R.id.btnCancel) {
            dismiss();
        }
    }
}
