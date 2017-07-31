package com.freelancer.videoeditor.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.freelancer.videoeditor.config.ConfigScreen;

import java.io.IOException;
import java.util.ArrayList;

public class FontAdapter extends BaseAdapter {
    ArrayList<View> listView = new ArrayList();
    Context mContext;
    EditText mEditText;
    String[] mList;
    int pW;

    public FontAdapter(Context mContext, EditText mEditText) {
        this.mContext = mContext;
        this.mEditText = mEditText;
        this.pW = ConfigScreen.SCREENWIDTH / 4;
        try {
            this.mList = mContext.getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return this.mList.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (position >= this.listView.size()) {
            view = null;
        } else {
            view = (View) this.listView.get(position);
        }
        if (view != null) {
            return view;
        }
        view = View.inflate(this.mContext, R.layout.libphotoeditor_row_typeface, null);
        final Typeface mTypeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/" + this.mList[position]);
        ((TextView) view.findViewById(R.id.txt_name)).setTypeface(mTypeface);
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FontAdapter.this.mEditText.setTypeface(mTypeface);
            }
        });
        this.listView.add(view);
        return view;
    }
}
