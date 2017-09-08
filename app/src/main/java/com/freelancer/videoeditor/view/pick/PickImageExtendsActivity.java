package com.freelancer.videoeditor.view.pick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.FileUtils;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.vo.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class PickImageExtendsActivity extends AppCompatActivity implements OnClickListener, OnAlbum, OnListAlbum, com.freelancer.videoeditor.util.OnClickListener.OnCustomClickListener{
    public static final int ACTION_PICK_IMAGE = 1;
    public static final int ACTION_PIC_COLLAGE = 0;
    public static final int ACTION_PIC_VIDEO = 2;
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    public static final String KEY_PATH_IMAGE_RESULT = "KEY_PATH_IMAGE_RESULT";
    private String CAPTURE_IMAGE_FILE_PROVIDER;
    public String PATH_FILE_SAVE_TEMP = "Temp";
    private final int REQUEST_CODE_CAMERA = 1221;

    @BindView(R.id.gridViewDetailAlbum)
    GridView gridViewListAlbum;
    @BindView(R.id.txtMessageSelectImage)
    TextView txtMessageSelectImage;
    @BindView(R.id.txtTotalImage)
    TextView txtTotalImage;
    @BindView(R.id.layoutBottom)
    RelativeLayout layoutBottom;
    @BindView(R.id.gridViewAlbum)
    GridView gridViewAlbum;
    @BindView(R.id.horizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.layoutDetailAlbum)
    LinearLayout layoutDetailAlbum;
    @BindView(R.id.layoutListImage)
    LinearLayout layoutListImage;
    @BindView(R.id.layoutListItemSelect)
    LinearLayout layoutListItemSelect;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int actionPick = ACTION_PIC_COLLAGE;
    private AlbumAdapterExtends adapterExtends;
    private ArrayList<Item> dataAlbum = new ArrayList();
    private ArrayList<Item> dataListPhoto = new ArrayList();
    private ArrayList<String> pathList = new ArrayList();
    ArrayList<Item> listItemSelect = new ArrayList();
    private int limitImageMax = 9;
    private int limitImageMin = ACTION_PICK_IMAGE;
    private DetailAlbumAdapter listAlbumAdapter;
    private int pWHBtnDelete;
    private int pWHItemSelected;
    private AlertDialog sortDialog;


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
                        boolean check = checkFile(file);
                        if (!Check(file.getParent(), pathList) && check) {
                            pathList.add(file.getParent());
                            dataAlbum.add(new Item(file.getParentFile().getName(), pathFile, file.getParent()));
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
                for (int i = 0; i < length; i += 1) {
                    File fileTmp = listFile[i];
                    if (fileTmp.exists()) {
                        boolean check = checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            dataListPhoto.add(new Item(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress();
                        }
                    }
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                //sort by time modify
                Collections.sort(PickImageExtendsActivity.this.dataListPhoto, new Comparator<Item>() {
                    public int compare(Item lhs, Item rhs) {
                        File fileI = new File(lhs.getPathFolder());
                        File fileJ = new File(rhs.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return 1;
                        }
                        return 0;
                    }
                });
            } catch (Exception ignored) {
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
        setContentView(R.layout.activity_album_extends);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        this.toolbar.setTitle(getResources().getString(R.string.text_title_activity_album_extends));

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
        pWHItemSelected = (int) ((((float) ((int) ((((float) ExtraUtils.getDisplayInfo(this).heightPixels) / 100.0f) * 14.0f))) / 100.0f) * 98.0f);
        pWHBtnDelete = (int) ((((float) this.pWHItemSelected) / 100.0f) * 25.0f);
        txtMessageSelectImage.setOnClickListener(this);
        if (this.actionPick == 0) {
            String string = "Select image";
            Object[] objArr = new Object[2];
            objArr[0] = this.limitImageMin;
            objArr[1] = this.limitImageMax;
            this.txtMessageSelectImage.setText(String.format(string, objArr));
        } else if (this.actionPick == ACTION_PIC_VIDEO) {
            this.txtMessageSelectImage.setText(getResources().getString(R.string.text_message_select_image_for_video));
        }
        UtilLib.getInstance().setOnCustomTouchViewScaleNotOther(findViewById(R.id.btnDone), this);

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
        this.adapterExtends = new AlbumAdapterExtends(this, R.layout.item_album_extends, this.dataAlbum);
        this.adapterExtends.setOnItem(this);
        new GetItemAlbum().execute();
        updateTxtTotalImage();
        this.layoutBottom.getLayoutParams().height = this.pWHItemSelected;
        if (this.actionPick == ACTION_PICK_IMAGE) {
            this.layoutBottom.getLayoutParams().height = 0;
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
        builder.setSingleChoiceItems(items, -1, (dialog, item) -> {
            switch (item) {
                case 0:
                    Collections.sort(dataAlbum, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
                    refreshGridViewAlbum();
                    Log.e("TAG", "showDialogSortAlbum by NAME");
                    break;
                case 1:
                    UtilLib.getInstance().showLoading(PickImageExtendsActivity.this);
                    Observable.fromCallable(() -> {
                        Collections.sort(dataAlbum, (lhs, rhs) -> {
                            File fileI = new File(lhs.getPathFolder());
                            File fileJ = new File(rhs.getPathFolder());
                            long totalSizeFileI = getFolderSize(fileI);
                            long totalSizeFileJ = getFolderSize(fileJ);
                            if (totalSizeFileI > totalSizeFileJ) {
                                return -1;
                            }
                            if (totalSizeFileI < totalSizeFileJ) {
                                return 1;
                            }
                            return 0;
                        });
                        return "";
                    }).subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(t -> {
                                PickImageExtendsActivity.this.refreshGridViewAlbum();
                                UtilLib.getInstance().hideLoading();
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
                                return 1;
                            }
                            return 0;
                        }
                    });
                    PickImageExtendsActivity.this.refreshGridViewAlbum();
                    Log.e("TAG", "showDialogSortAlbum by Date");
                    break;
            }
            PickImageExtendsActivity.this.sortDialog.dismiss();
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewAlbum() {
        this.adapterExtends = new AlbumAdapterExtends(this, R.layout.item_album_extends, this.dataAlbum);
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
                        Observable.fromCallable(() -> {
                            Collections.sort(dataListPhoto, (lhs, rhs) -> {
                                File fileI = new File(lhs.getPathFolder());
                                File fileJ = new File(rhs.getPathFolder());
                                long totalSizeFileI = getFolderSize(fileI);
                                long totalSizeFileJ = getFolderSize(fileJ);
                                if (totalSizeFileI > totalSizeFileJ) {
                                    return -1;
                                }
                                if (totalSizeFileI < totalSizeFileJ) {
                                    return 1;
                                }
                                return 0;
                            });
                            return "";
                        }).subscribeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    PickImageExtendsActivity.this.refreshGridViewListAlbum();
                                    UtilLib.getInstance().hideLoading();
                                });
                        break;
                    } catch (Exception e2) {
                        break;
                    }
                case 2:
                    try {
                        Collections.sort(dataListPhoto, (lhs, rhs) -> {
                            File fileI = new File(lhs.getPathFolder());
                            File fileJ = new File(rhs.getPathFolder());
                            if (fileI.lastModified() > fileJ.lastModified()) {
                                return -1;
                            }
                            if (fileI.lastModified() < fileJ.lastModified()) {
                                return 1;
                            }
                            return 0;
                        });
                        refreshGridViewListAlbum();
                        break;
                    } catch (Exception e3) {
                        break;
                    }
            }
            sortDialog.dismiss();
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
            for (int i = 0; i < length2; i += 1) {
                File file = files[i];
                if (file.isFile()) {
                    boolean isCheck = false;
                    for (int k = 0; k < AppConst.FORMAT_IMAGE.size(); k += 1) {
                        if (file.getName().endsWith(AppConst.FORMAT_IMAGE.get(k))) {
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



    @Override
    public void OnItemAlbumClick(int position) {
        showListAlbum(this.dataAlbum.get(position).getPathFolder());
    }

    @Override
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
            Object[] objArr = new Object[1];
            objArr[0] = this.limitImageMax;
            Toast.makeText(this, String.format(string, objArr), Toast.LENGTH_SHORT).show();
        }
    }

    void addItemSelect(final Item item) {
        if (this.txtMessageSelectImage.getVisibility() == View.VISIBLE) {
            this.txtMessageSelectImage.setVisibility(View.GONE);
        }
        item.setId(this.listItemSelect.size());
        this.listItemSelect.add(item);
        updateTxtTotalImage();
        final View viewItemSelected = View.inflate(this, R.layout.item_selected_extends, null);
        ImageView imageItem = viewItemSelected.findViewById(R.id.imageItem);
        ImageView btnDelete = viewItemSelected.findViewById(R.id.btnDelete);
        viewItemSelected.findViewById(R.id.layoutRoot).getLayoutParams().height = this.pWHItemSelected;
        imageItem.getLayoutParams().width = this.pWHItemSelected;
        imageItem.getLayoutParams().height = this.pWHItemSelected;
        btnDelete.getLayoutParams().width = this.pWHBtnDelete;
        btnDelete.getLayoutParams().height = this.pWHBtnDelete;
        Glide.with(this).load(item.getPathFile()).asBitmap().override(this.pWHItemSelected, this.pWHItemSelected).animate(R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(R.drawable.piclist_icon_default).fallback(R.drawable.piclist_icon_default).placeholder(R.drawable.piclist_icon_default).into(imageItem);
        btnDelete.setOnClickListener(v -> {
            PickImageExtendsActivity.this.layoutListItemSelect.removeView(viewItemSelected);
            PickImageExtendsActivity.this.listItemSelect.remove(item);
            PickImageExtendsActivity.this.updateTxtTotalImage();
        });
        Timber.tag("passedAddView").d("add");
        PickImageExtendsActivity.this.layoutListItemSelect.addView(viewItemSelected);
        viewItemSelected.startAnimation(AnimationUtils.loadAnimation(PickImageExtendsActivity.this,R.anim.abc_fade_in));
        PickImageExtendsActivity.this.sendScroll();
    }

    void updateTxtTotalImage() {
        String string = getResources().getString(R.string.text_images_extends);
        Object[] objArr = new Object[2];
        objArr[0] = this.listItemSelect.size();
        objArr[1] = this.limitImageMax;
        this.txtTotalImage.setText(String.format(string, objArr));
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
                        horizontalScrollView.fullScroll(66);
                    }
                });
            }
        }).start();
    }

    void showListAlbum(String pathAlbum) {
        File file = new File(pathAlbum);
        if (file != null && file.exists()) {
            toolbar.setTitle(file.getName());
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

    @Override
    public void OnCustomClick(View v, MotionEvent event) {
        if (v.getId() == R.id.btnDone) {
            ArrayList<String> listString = getListString(this.listItemSelect);
            if (listString.size() >= this.limitImageMin) {
                done(listString);
            } else {
                Toast.makeText(this, getResources().getString(R.string.message_click_button_done_not_image), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
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
        for (int k = 0; k < AppConst.FORMAT_IMAGE.size(); k += 1) {
            if (name.endsWith((String) AppConst.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pick_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort:
                if (this.layoutDetailAlbum.getVisibility() == View.GONE) {
                    showDialogSortAlbum();
                } else {
                    showDialogSortListAlbum();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == REQUEST_CODE_CAMERA) {
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


    @Override
    public void onBackPressed() {
        if (this.layoutDetailAlbum.getVisibility() == View.VISIBLE) {
            this.dataListPhoto.clear();
            this.listAlbumAdapter.notifyDataSetChanged();
            this.layoutDetailAlbum.setVisibility(View.GONE);
            this.toolbar.setTitle(getResources().getString(R.string.text_title_activity_album_extends));
            return;
        }
        super.onBackPressed();
    }
}
