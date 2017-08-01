package com.freelancer.videoeditor.view.pick;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.freelancer.videoeditor.BuildConfig;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.FileUtils;
import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.IDoBackGround;
import com.freelancer.videoeditor.util.IHandler;
import com.freelancer.videoeditor.util.OnCustomTouchListener;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;
import com.freelancer.videoeditor.vo.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PickImageExtendsActivity extends Activity implements OnClickListener, OnAlbum, OnListAlbum, com.freelancer.videoeditor.util.OnCustomClickListener {
    public static final int ACTION_PICK_IMAGE = 1;
    public static final int ACTION_PIC_COLLAGE = 0;
    public static final int ACTION_PIC_VIDEO = 2;
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    public static final String KEY_PATH_IMAGE_RESULT = "KEY_PATH_IMAGE_RESULT";
    private String CAPTURE_IMAGE_FILE_PROVIDER;
    private final int LIMIT_IMAGE_MAX_DEFAULT = 9;
    private final int LIMIT_IMAGE_MIN_DEFAULT = ACTION_PICK_IMAGE;
    public String PATH_FILE_SAVE_TEMP = "Temp";
    private final int REQUEST_CODE_CAMERA = R.styleable.AppCompatTheme_autoCompleteTextViewStyle;
    private final String TAG = "PickImageExtendsActivity";
    private int actionPick = ACTION_PIC_COLLAGE;
    private AlbumAdapterExtends adapterExtends;
    private ImageView btnBack;
    private LinearLayout btnPicCamera;
    private ArrayList<Item> dataAlbum = new ArrayList();
    private ArrayList<Item> dataListPhoto = new ArrayList();
    private GridView gridViewAlbum;
    private GridView gridViewListAlbum;
    private HorizontalScrollView horizontalScrollView;
    private RelativeLayout layoutBottom;
    private LinearLayout layoutDetailAlbum;
    private LinearLayout layoutListImage;
    private LinearLayout layoutListItemSelect;
    private int limitImageMax = 9;
    private int limitImageMin = ACTION_PICK_IMAGE;
    private DetailAlbumAdapter listAlbumAdapter;
    ArrayList<Item> listItemSelect = new ArrayList();
    private int pWHBtnDelete;
    private int pWHItemSelected;
    private ArrayList<String> pathList = new ArrayList();
    private AlertDialog sortDialog;
    private TextView txtMessageSelectImage;
    private TextView txtTitle;
    private TextView txtTotalImage;

    private class GetItemAlbum extends AsyncTask<Void, Void, String> {
        private GetItemAlbum() {
        }

        protected String doInBackground(Void... params) {
            Uri uri = Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[PickImageExtendsActivity.ACTION_PIC_VIDEO];
            projection[PickImageExtendsActivity.ACTION_PIC_COLLAGE] = "_data";
            projection[PickImageExtendsActivity.ACTION_PICK_IMAGE] = "bucket_display_name";
            Cursor cursor = PickImageExtendsActivity.this.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index_data = cursor.getColumnIndexOrThrow("_data");
                while (cursor.moveToNext()) {
                    String pathFile = cursor.getString(column_index_data);
                    File file = new File(pathFile);
                    if (file.exists()) {
                        boolean check = PickImageExtendsActivity.this.checkFile(file);
                        if (!PickImageExtendsActivity.this.Check(file.getParent(), PickImageExtendsActivity.this.pathList) && check) {
                            PickImageExtendsActivity.this.pathList.add(file.getParent());
                            PickImageExtendsActivity.this.dataAlbum.add(new Item(file.getParentFile().getName(), pathFile, file.getParent()));
                        }
                    }
                }
                cursor.close();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            PickImageExtendsActivity.this.gridViewAlbum.setAdapter(adapterExtends);
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        String pathAlbum;

        GetItemListAlbum(String pathAlbum) {
            this.pathAlbum = pathAlbum;
        }

        protected String doInBackground(Void... params) {
            File file = new File(this.pathAlbum);
            if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                int length = listFile.length;
                for (int i = PickImageExtendsActivity.ACTION_PIC_COLLAGE; i < length; i += PickImageExtendsActivity.ACTION_PICK_IMAGE) {
                    File fileTmp = listFile[i];
                    if (fileTmp.exists()) {
                        boolean check = PickImageExtendsActivity.this.checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            PickImageExtendsActivity.this.dataListPhoto.add(new Item(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress();
                        }
                    }
                }
            }
            return BuildConfig.FLAVOR;
        }

        protected void onPostExecute(String result) {
            try {
                Collections.sort(PickImageExtendsActivity.this.dataListPhoto, new Comparator<Item>() {
                    public int compare(Item lhs, Item rhs) {
                        File fileI = new File(lhs.getPathFolder());
                        File fileJ = new File(rhs.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                        }
                        return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
                    }
                });
            } catch (Exception e) {
            }
            PickImageExtendsActivity.this.listAlbumAdapter.notifyDataSetChanged();
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    @SuppressLint({"LongLogTag"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(ACTION_PICK_IMAGE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.piclist_activity_album_extends);
        this.CAPTURE_IMAGE_FILE_PROVIDER = getPackageName() + ".fileprovider";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.limitImageMax = bundle.getInt(KEY_LIMIT_MAX_IMAGE, 9);
            this.limitImageMin = bundle.getInt(KEY_LIMIT_MIN_IMAGE, ACTION_PICK_IMAGE);
            this.actionPick = bundle.getInt(KEY_ACTION, ACTION_PIC_COLLAGE);
            if (this.limitImageMin > this.limitImageMax) {
                finish();
            }
            if (this.limitImageMin < 0) {
                finish();
            }
            Log.e("PickImageExtendsActivity", "limitImageMin = " + this.limitImageMin);
            Log.e("PickImageExtendsActivity", "limitImageMax = " + this.limitImageMax);
        }
        this.pWHItemSelected = (int) ((((float) ((int) ((((float) ExtraUtils.getDisplayInfo(this).heightPixels) / 100.0f) * 14.0f))) / 100.0f) * 98.0f);
        this.pWHBtnDelete = (int) ((((float) this.pWHItemSelected) / 100.0f) * 25.0f);
        this.txtTitle = findViewById(R.id.txtTitle);
        this.txtMessageSelectImage = findViewById(R.id.txtMessageSelectImage);
        this.txtMessageSelectImage.setOnClickListener(this);
        if (this.actionPick == 0) {
            String string = "Select image";
            Object[] objArr = new Object[ACTION_PIC_VIDEO];
            objArr[ACTION_PIC_COLLAGE] = this.limitImageMin;
            objArr[ACTION_PICK_IMAGE] = this.limitImageMax;
            this.txtMessageSelectImage.setText(String.format(string, objArr));
        } else if (this.actionPick == ACTION_PIC_VIDEO) {
            this.txtMessageSelectImage.setText(getResources().getString(R.string.text_message_select_image_for_video));
        }
        this.gridViewListAlbum = findViewById(R.id.gridViewDetailAlbum);
        this.txtTotalImage = findViewById(R.id.txtTotalImage);
        this.btnBack = findViewById(R.id.btnBack);
        UtilLib.getInstance().setOnCustomTouchViewScaleNotOther(btnBack, this);
        this.layoutListImage = findViewById(R.id.layoutListImage);
        UtilLib.getInstance().setOnCustomTouchViewScaleNotOther((ImageView) findViewById(R.id.btnSort), this);
        UtilLib.getInstance().setOnCustomTouchViewScaleNotOther((LinearLayout) findViewById(R.id.btnDone), this);
        final ImageView icon_camera = findViewById(R.id.icon_camera);
        this.btnPicCamera = findViewById(R.id.btnPicCamera);
        UtilLib.getInstance().setOnCustomTouchView(this.btnPicCamera, new OnCustomTouchListener() {
            private void setScale(float scale) {
                icon_camera.setScaleX(scale);
                icon_camera.setScaleY(scale);
            }

            public void OnCustomTouchDown(View v, MotionEvent event) {
                setScale(0.9f);
            }

            public void OnCustomTouchMoveOut(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
            }

            public void OnCustomTouchUp(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
                PickImageExtendsActivity.this.requestCameraPermission();
            }

            public void OnCustomTouchOther(View v, MotionEvent event) {
                setScale(HandlerTools.ROTATE_R);
            }
        });
        this.layoutListItemSelect = findViewById(R.id.layoutListItemSelect);
        this.layoutDetailAlbum = findViewById(R.id.layoutDetailAlbum);
        this.horizontalScrollView = findViewById(R.id.horizontalScrollView);
        this.gridViewAlbum = findViewById(R.id.gridViewAlbum);
        try {
            Collections.sort(this.dataAlbum, (lhs, rhs) -> {
                File fileI = new File(lhs.getPathFolder());
                File fileJ = new File(rhs.getPathFolder());
                if (fileI.lastModified() > fileJ.lastModified()) {
                    return -1;
                }
                if (fileI.lastModified() < fileJ.lastModified()) {
                    return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                }
                return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
            });
        } catch (Exception e) {
        }
        this.adapterExtends = new AlbumAdapterExtends(this, R.layout.piclist_row_album_extends, this.dataAlbum);
        this.adapterExtends.setOnItem(this);
//        if (instance.isPermissionAllow(this, PhotoEditorActivity.REQUEST_CODE_CROP, "android.permission.READ_EXTERNAL_STORAGE")) {
            new GetItemAlbum().execute();
//        }
        updateTxtTotalImage();
        this.layoutBottom = findViewById(R.id.layoutBottom);
        this.layoutBottom.getLayoutParams().height = this.pWHItemSelected;
        if (this.actionPick == 0 || this.actionPick == ACTION_PIC_VIDEO) {
            this.btnPicCamera.getLayoutParams().height = ACTION_PIC_COLLAGE;
        } else if (this.actionPick == ACTION_PICK_IMAGE) {
            this.layoutBottom.getLayoutParams().height = ACTION_PIC_COLLAGE;
        }
    }

    private boolean Check(String a, ArrayList<String> list) {
        return !list.isEmpty() && list.contains(a);
    }

    public void showDialogSortAlbum() {
        CharSequence[] items = getResources().getStringArray(R.array.array_sort_value);
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.text_title_dialog_sort_by_album));
        Log.e("TAG", "showDialogSortAlbum");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
//                if (PickImageExtendsActivity.this.indexRowAdsNative != -1) {
//                    PickImageExtendsActivity.this.dataAlbum.remove(PickImageExtendsActivity.this.indexRowAdsNative);
//                }
                switch (item) {
                    case PickImageExtendsActivity.ACTION_PIC_COLLAGE /*0*/:
                        Collections.sort(PickImageExtendsActivity.this.dataAlbum, new Comparator<Item>() {
                            public int compare(Item lhs, Item rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
                        PickImageExtendsActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by NAME");
                        break;
                    case 1:
                        UtilLib.getInstance().showLoading(PickImageExtendsActivity.this);
                        UtilLib.getInstance().doBackGround(new IDoBackGround() {
                            public void onDoBackGround(boolean isCancelled) {
                                Collections.sort(PickImageExtendsActivity.this.dataAlbum, (lhs, rhs) -> {
                                    File fileI = new File(lhs.getPathFolder());
                                    File fileJ = new File(rhs.getPathFolder());
                                    long totalSizeFileI = PickImageExtendsActivity.getFolderSize(fileI);
                                    long totalSizeFileJ = PickImageExtendsActivity.getFolderSize(fileJ);
                                    if (totalSizeFileI > totalSizeFileJ) {
                                        return -1;
                                    }
                                    if (totalSizeFileI < totalSizeFileJ) {
                                        return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                                    }
                                    return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
                                });
                            }

                            public void onCompleted() {
                                PickImageExtendsActivity.this.refreshGridViewAlbum();
                                UtilLib.getInstance().hideLoading();
                            }
                        });
                        Log.e("TAG", "showDialogSortAlbum by Size");
                        break;
                    case 2:
                        Collections.sort(PickImageExtendsActivity.this.dataAlbum, new Comparator<Item>() {
                            public int compare(Item lhs, Item rhs) {
                                File fileI = new File(lhs.getPathFolder());
                                File fileJ = new File(rhs.getPathFolder());
                                if (fileI.lastModified() > fileJ.lastModified()) {
                                    return -1;
                                }
                                if (fileI.lastModified() < fileJ.lastModified()) {
                                    return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                                }
                                return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
                            }
                        });
                        PickImageExtendsActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by Date");
                        break;
                }
                PickImageExtendsActivity.this.sortDialog.dismiss();
            }
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewAlbum() {
        this.adapterExtends = new AlbumAdapterExtends(this, R.layout.piclist_row_album_extends, this.dataAlbum);
        this.adapterExtends.setOnItem(this);
        this.gridViewAlbum.setAdapter(adapterExtends);
        this.gridViewAlbum.setVisibility(View.GONE);
        this.gridViewAlbum.setVisibility(View.VISIBLE);
    }

    public void showDialogSortListAlbum() {
        CharSequence[] items = getResources().getStringArray(R.array.array_sort_value);
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.text_title_dialog_sort_by_photo));
        builder.setSingleChoiceItems(items, -1, (dialog, item) -> {
            switch (item) {
                case 0:
                    try {
                        Collections.sort(PickImageExtendsActivity.this.dataListPhoto, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
                        PickImageExtendsActivity.this.refreshGridViewListAlbum();
                        break;
                    } catch (Exception e) {
                        break;
                    }
                case 1:
                    try {
                        UtilLib.getInstance().showLoading(PickImageExtendsActivity.this);
                        UtilLib.getInstance().doBackGround(new IDoBackGround() {
                            public void onDoBackGround(boolean isCancelled) {
                                Collections.sort(PickImageExtendsActivity.this.dataListPhoto, (lhs, rhs) -> {
                                    File fileI = new File(lhs.getPathFolder());
                                    File fileJ = new File(rhs.getPathFolder());
                                    long totalSizeFileI = PickImageExtendsActivity.getFolderSize(fileI);
                                    long totalSizeFileJ = PickImageExtendsActivity.getFolderSize(fileJ);
                                    if (totalSizeFileI > totalSizeFileJ) {
                                        return -1;
                                    }
                                    if (totalSizeFileI < totalSizeFileJ) {
                                        return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                                    }
                                    return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
                                });
                            }

                            public void onCompleted() {
                                PickImageExtendsActivity.this.refreshGridViewListAlbum();
                                UtilLib.getInstance().hideLoading();
                            }
                        });
                        break;
                    } catch (Exception e2) {
                        break;
                    }
                case PickImageExtendsActivity.ACTION_PIC_VIDEO /*2*/:
                    try {
                        Collections.sort(PickImageExtendsActivity.this.dataListPhoto, new Comparator<Item>() {
                            public int compare(Item lhs, Item rhs) {
                                File fileI = new File(lhs.getPathFolder());
                                File fileJ = new File(rhs.getPathFolder());
                                if (fileI.lastModified() > fileJ.lastModified()) {
                                    return -1;
                                }
                                if (fileI.lastModified() < fileJ.lastModified()) {
                                    return PickImageExtendsActivity.ACTION_PICK_IMAGE;
                                }
                                return PickImageExtendsActivity.ACTION_PIC_COLLAGE;
                            }
                        });
                        PickImageExtendsActivity.this.refreshGridViewListAlbum();
                        break;
                    } catch (Exception e3) {
                        break;
                    }
            }
            PickImageExtendsActivity.this.sortDialog.dismiss();
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewListAlbum() {
        this.listAlbumAdapter = new DetailAlbumAdapter(this,R.layout.item_pick_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
        this.gridViewListAlbum.setVisibility(View.GONE);
        this.gridViewListAlbum.setVisibility(View.VISIBLE);
    }

    public static long getFolderSize(File directory) {
        long length = 0;
        if (directory == null) {
            return 0;
        }
        if (!directory.exists()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            int length2 = files.length;
            for (int i = ACTION_PIC_COLLAGE; i < length2; i += ACTION_PICK_IMAGE) {
                File file = files[i];
                if (file.isFile()) {
                    boolean isCheck = false;
                    for (int k = ACTION_PIC_COLLAGE; k < AppConst.FORMAT_IMAGE.size(); k += ACTION_PICK_IMAGE) {
                        if (file.getName().endsWith((String) AppConst.FORMAT_IMAGE.get(k))) {
                            isCheck = true;
                            break;
                        }
                    }
                    if (isCheck) {
                        length++;
                    }
                }
            }
        }
        return length;
    }

    public void OnItemAlbumClick(int position) {
        showListAlbum(this.dataAlbum.get(position).getPathFolder());
    }

    public void OnItemListAlbumClick(Item item) {
        if (this.actionPick != 0 && this.actionPick != ACTION_PIC_VIDEO) {
            String pathFile = item.getPathFile();
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_PATH_IMAGE_RESULT, pathFile);
            setResult(-1, returnIntent);
            finish();
        } else if (this.listItemSelect.size() < this.limitImageMax) {
            addItemSelect(item);
        } else {
            String string = getResources().getString(R.string.text_message_limit_pick_image);
            Object[] objArr = new Object[ACTION_PICK_IMAGE];
            objArr[ACTION_PIC_COLLAGE] = this.limitImageMax;
//            T.show(String.format(string, objArr));
        }
    }

    void addItemSelect(final Item item) {
        if (this.txtMessageSelectImage.getVisibility() == View.VISIBLE) {
            this.txtMessageSelectImage.setVisibility(View.GONE);
            this.layoutListImage.setVisibility(View.VISIBLE);
        }
        item.setId(this.listItemSelect.size());
        this.listItemSelect.add(item);
        updateTxtTotalImage();
        final View viewItemSelected = View.inflate(this, R.layout.piclist_item_selected_extends, null);
        ImageView imageItem = viewItemSelected.findViewById(R.id.imageItem);
        ImageView btnDelete = viewItemSelected.findViewById(R.id.btnDelete);
        viewItemSelected.findViewById(R.id.layoutRoot).getLayoutParams().height = this.pWHItemSelected;
        imageItem.getLayoutParams().width = this.pWHItemSelected;
        imageItem.getLayoutParams().height = this.pWHItemSelected;
        btnDelete.getLayoutParams().width = this.pWHBtnDelete;
        btnDelete.getLayoutParams().height = this.pWHBtnDelete;
        Glide.with(this).load(item.getPathFile()).asBitmap().override(this.pWHItemSelected, this.pWHItemSelected).animate(R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(R.drawable.piclist_icon_default).fallback(R.drawable.piclist_icon_default).placeholder(R.drawable.piclist_icon_default).into(imageItem);
        btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PickImageExtendsActivity.this.layoutListItemSelect.removeView(viewItemSelected);
                PickImageExtendsActivity.this.listItemSelect.remove(item);
                PickImageExtendsActivity.this.updateTxtTotalImage();
            }
        });
        UtilLib.getInstance().handlerDoWork(new IHandler() {
            public void doWork() {
                PickImageExtendsActivity.this.layoutListItemSelect.addView(viewItemSelected);
                viewItemSelected.startAnimation(AnimationUtils.loadAnimation(PickImageExtendsActivity.this,R.anim.abc_fade_in));
                PickImageExtendsActivity.this.sendScroll();
            }
        });
    }

    void updateTxtTotalImage() {
        String string = getResources().getString(R.string.text_images_extends);
        Object[] objArr = new Object[ACTION_PIC_VIDEO];
        objArr[ACTION_PIC_COLLAGE] = this.listItemSelect.size();
        objArr[ACTION_PICK_IMAGE] = this.limitImageMax;
//        this.txtTotalImage.setText(String.format(string, objArr));
        if (this.txtMessageSelectImage.getVisibility() == View.GONE && this.listItemSelect.size() == 0) {
            this.txtMessageSelectImage.setVisibility(View.VISIBLE);
            this.layoutListImage.setVisibility(View.GONE);
        }
    }

    private void sendScroll() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        PickImageExtendsActivity.this.horizontalScrollView.fullScroll(66);
                    }
                });
            }
        }).start();
    }

    void showListAlbum(String pathAlbum) {
        File file = new File(pathAlbum);
        if (file != null && file.exists()) {
            this.txtTitle.setText(file.getName());
            this.listAlbumAdapter = new DetailAlbumAdapter(this, R.layout.item_pick_album, this.dataListPhoto);
            this.listAlbumAdapter.setOnListAlbum(this);
            this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
            this.layoutDetailAlbum.setVisibility(View.VISIBLE);
            new GetItemListAlbum(pathAlbum).execute();
        }
    }

    private void done(ArrayList<String> listString) {
        Intent mIntent = new Intent();
        setResult(-1, mIntent);
        mIntent.putStringArrayListExtra(KEY_DATA_RESULT, listString);
        finish();
    }

    ArrayList<String> getListString(ArrayList<Item> listItemSelect) {
        ArrayList<String> listString = new ArrayList();
        for (int i = ACTION_PIC_COLLAGE; i < listItemSelect.size(); i += ACTION_PICK_IMAGE) {
            listString.add(listItemSelect.get(i).getPathFile());
        }
        return listString;
    }

    public void OnCustomClick(View v, MotionEvent event) {
        if (v.getId() ==R.id.btnSort) {
            if (this.layoutDetailAlbum.getVisibility() == View.GONE) {
                showDialogSortAlbum();
            } else {
                showDialogSortListAlbum();
            }
        } else if (v.getId() == R.id.btnBack) {
            onBackPressed();
        } else if (v.getId() == R.id.btnDone) {
            ArrayList<String> listString = getListString(this.listItemSelect);
            if (listString.size() >= this.limitImageMin) {
                done(listString);
            } else {
                Toast.makeText(this, getResources().getString(R.string.message_click_button_done_not_image), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View v) {
    }


    private boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(FileUtils.HIDDEN_PREFIX) || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;
        for (int k = ACTION_PIC_COLLAGE; k < AppConst.FORMAT_IMAGE.size(); k += ACTION_PICK_IMAGE) {
            if (name.endsWith((String) AppConst.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        UtilLib.getInstance().getClass();
//        if (requestCode == PhotoEditorActivity.REQUEST_CODE_CROP) {
//            if (UtilLib.getInstance().isPermissionAllow(this, "android.permission.READ_EXTERNAL_STORAGE")) {
//                new GetItemAlbum().execute();
//            } else if (shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
//                UtilLib.getInstance().getClass();
//                UtilLib.getInstance().showDenyDialog(this, "android.permission.READ_EXTERNAL_STORAGE", PhotoEditorActivity.REQUEST_CODE_CROP, true);
//            } else {
//                UtilLib.getInstance().openAppSettings(this, true);
//            }
//        } else if (requestCode != R.styleable.AppCompatTheme_autoCompleteTextViewStyle) {
//        } else {
//            if (UtilLib.getInstance().isPermissionAllow(this, "android.permission.CAMERA")) {
//                pickCamera(R.styleable.AppCompatTheme_autoCompleteTextViewStyle);
//            } else if (shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
//                UtilLib.getInstance().showDenyDialog(this, "android.permission.CAMERA", R.styleable.AppCompatTheme_autoCompleteTextViewStyle, false);
//            } else {
//                UtilLib.getInstance().openAppSettings(this, true);
//            }
//        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == R.styleable.AppCompatTheme_autoCompleteTextViewStyle) {
            File path = new File(getFilesDir(), this.PATH_FILE_SAVE_TEMP);
            if (!path.exists()) {
                path.mkdirs();
            }
            File imageFile = new File(path, "image.jpg");
            revokeUriPermission(Uri.fromFile(imageFile), 3);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_PATH_IMAGE_RESULT, imageFile.getAbsolutePath());
            setResult(-1, returnIntent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestCameraPermission() {
        if (UtilLib.getInstance().isPermissionAllow(this, "android.permission.CAMERA")) {
            pickCamera(R.styleable.AppCompatTheme_autoCompleteTextViewStyle);
        } else {
            UtilLib.getInstance().requestPermission(this, "android.permission.CAMERA", R.styleable.AppCompatTheme_autoCompleteTextViewStyle);
        }
    }

    public void pickCamera(int requestCode) {
        File path = new File(getFilesDir(), this.PATH_FILE_SAVE_TEMP);
        if (!path.exists()) {
            path.mkdirs();
        }
        Uri imageUri = FileProvider.getUriForFile(this, this.CAPTURE_IMAGE_FILE_PROVIDER, new File(path, "image.jpg"));
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", imageUri);
//        for (ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REQUEST)) {
//            grantUriPermission(resolveInfo.activityInfo.packageName, imageUri, 3);
//        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onBackPressed() {
        if (this.layoutDetailAlbum.getVisibility() == View.VISIBLE) {
            this.dataListPhoto.clear();
            this.listAlbumAdapter.notifyDataSetChanged();
            this.layoutDetailAlbum.setVisibility(View.GONE);
            this.txtTitle.setText(getResources().getString(R.string.text_title_activity_album_extends));
            return;
        }
        super.onBackPressed();
    }
}
