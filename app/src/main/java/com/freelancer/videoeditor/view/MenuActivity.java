package com.freelancer.videoeditor.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.util.AppUtils;
import com.freelancer.videoeditor.util.IDoBackGround;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.view.base.BaseGame;
import com.freelancer.videoeditor.view.pick.PickImageExtendsActivity;
import com.freelancer.videoeditor.view.video.MyVideoActivity;
import com.freelancer.videoeditor.view.video.VideoEditorActivity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MenuActivity extends BaseGame {
    public static final int REQUEST_CODE_NEXT_MY_VIDEO = 101;
    private static final String TAG = "MenuActivity";
    private final int REQUEST_PICK_IMG = 1001;
    private LinearLayout layoutTop;


    private ArrayList<String> pathList;


    @Override
    protected void onSetContentView() {
        super.onSetContentView();
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        UtilLib.getInstance().requestAllPermission(this, AppConst.LIST_PERMISSION_REQUEST);
        Bundle bundle = getIntent().getExtras();
    }

    @OnClick(R.id.ln_create_video)
    public void pickImage() {
        Intent mIntent = new Intent(this, PickImageExtendsActivity.class);
        mIntent.putExtra(PickImageExtendsActivity.KEY_LIMIT_MAX_IMAGE, 60);
        mIntent.putExtra(PickImageExtendsActivity.KEY_LIMIT_MIN_IMAGE, 3);
        mIntent.putExtra(PickImageExtendsActivity.KEY_ACTION, 2);
        startActivityForResult(mIntent, R.styleable.AppCompatTheme_ratingBarStyleSmall);
    }

    @OnClick(R.id.ln_my_studio)
    public void myVideos() {
        File mFile = new File(AppConst.OUT_VIDEO_FOLDER);
        if (!mFile.exists()) {
            mFile.mkdirs();
            Toast.makeText(mContext, getResources().getString(R.string.message_no_video_my_video), Toast.LENGTH_SHORT).show();
        } else if (mFile.listFiles().length == 0) {
            Toast.makeText(mContext, getResources().getString(R.string.message_no_video_my_video), Toast.LENGTH_SHORT).show();
        }else{
            MyVideoActivity.startActivity(this);
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        setMultiTouch(true);
        this.mHud = new HUD();
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        ConfigScreen.SCREENHEIGHT = display.getHeight();
        ConfigScreen.SCREENWIDTH = display.getWidth();
        AppConst.WIDTH_IMAGE = ConfigScreen.SCREENWIDTH;
        AppConst.HEIGHT_IMAGE = ConfigScreen.SCREENWIDTH;
        Timber.e("TAG", "ConfigScreen.SCREENWIDTH = " + ConfigScreen.SCREENWIDTH + " ConfigScreen.SCREENHEIGHT = " + ConfigScreen.SCREENHEIGHT);
        this.mCamera = new Camera(0.0f, 0.0f, (float) ConfigScreen.SCREENWIDTH, (float) ConfigScreen.SCREENHEIGHT);
        ConfigScreen.mRatioResolutionPolicy = new RatioResolutionPolicy((float) ConfigScreen.SCREENWIDTH, (float) ConfigScreen.SCREENHEIGHT);
        ConfigScreen.mScreenOrientation = ScreenOrientation.PORTRAIT_FIXED;
        this.mEngineOptions = new EngineOptions(true, ConfigScreen.mScreenOrientation, ConfigScreen.mRatioResolutionPolicy, this.mCamera);
        this.mEngineOptions.getAudioOptions().setNeedsSound(true);
        this.mEngineOptions.getAudioOptions().setNeedsMusic(true);
        this.mEngineOptions.getTouchOptions().setNeedsMultiTouch(this.isMultiTouch);
        this.mEngineOptions.getRenderOptions().setDithering(true);
        this.mEngineOptions.getRenderOptions().setMultiSampling(true);
        return this.mEngineOptions;
    }

    @Override
    protected Scene onCreateScene() {
        super.onCreateScene();
        this.mainScene.setTouchAreaBindingOnActionDownEnabled(true);
        this.mainScene.setOnAreaTouchTraversalFrontToBack();
        return this.mainScene;
    }

    @Override
    protected void onCreateResources() {
        super.onCreateResources();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == R.styleable.AppCompatTheme_ratingBarStyleSmall) {
            this.pathList = data.getExtras().getStringArrayList(PickImageExtendsActivity.KEY_DATA_RESULT);
            Timber.d(TAG, "LIST IMG: " + (this.pathList != null ? this.pathList.toString() : "ARR NULL"));
            if (this.pathList != null && !this.pathList.isEmpty()) {
                startScaleBitmap(this.pathList);
            }
        }
    }

    private void gotoVideoEditor(ArrayList<String> pathList) {
        Intent i = new Intent(this, VideoEditorActivity.class);
        i.putStringArrayListExtra(AppConst.BUNDLE_KEY_LIST_IMG_PICK, pathList);
        startActivity(i);
    }

    private void startScaleBitmap(final ArrayList<String> pathList) {
        UtilLib.getInstance().showLoadingProgress(this, getResources().getString(R.string.message_create_photo_for_video));

        Observable.fromCallable(() -> {
            try {
                MenuActivity.this.scaleListBitmap(pathList, AppConst.WIDTH_IMAGE, AppConst.HEIGHT_IMAGE, AppConst.OUT_IMAGE_TEMP_FOLDER, AppConst.PREFIX_OUT_IMAGE);
            } catch (Exception e) {
                Timber.e(MenuActivity.TAG, "startScaleBitmap e = " + e.toString());
            }
            return "";
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    UtilLib.getInstance().hideLoadingDownload();
                    if (pathList == null || pathList.isEmpty()) {
                        Timber.e(MenuActivity.TAG, "pathList is NULL");
                    } else {
                        MenuActivity.this.gotoVideoEditor(pathList);
                    }
                });
    }

    private void scaleListBitmap(ArrayList<String> pathList, int pWScale, int pHScale, String pathFolder, String perFixName) throws Exception, OutOfMemoryError {
        if (pathList.size() == 0) {
            throw new Exception("pathList size != 0");
        }
        File mFile = new File(pathFolder);
        AppUtils.deleteFolder(mFile);
        if (!mFile.isDirectory()) {
            mFile.mkdirs();
        }
        float TOTAL_PHOTO = (float) pathList.size();
        for (int i = 0; ((float) i) < TOTAL_PHOTO; i++) {
            Bitmap mBitmap;
            String pathFile = (String) pathList.get(i);
            Options options = new Options();
            options.inScaled = false;
            try {
                mBitmap = UtilLib.getInstance().getResizedBitmap(cropCenterBitmap(autoRotateBitmap(BitmapFactory.decodeFile(pathFile, options), pathFile)), pHScale, pWScale);
            } catch (OutOfMemoryError e) {
                mBitmap = null;
            }
            if (mBitmap != null) {
                AppUtils.saveBitmap(mBitmap, pathFolder + "/" + perFixName + (i + 1) + AppConst.FORMAT_FILLTER);
            }
            UtilLib.getInstance().updateDialogProgress((int) ((((float) i) / TOTAL_PHOTO) * 100.0f));
        }
    }

    public Bitmap autoRotateBitmap(Bitmap mBitmap, String pathFile) {
        try {
            int rotation = new ExifInterface(pathFile).getAttributeInt("Orientation", 1);
            if (rotation == 6) {
                return UtilLib.getInstance().rotateBitmap(mBitmap, 90.0f);
            }
            if (rotation == 3) {
                return UtilLib.getInstance().rotateBitmap(mBitmap, 180.0f);
            }
            if (rotation == 8) {
                return UtilLib.getInstance().rotateBitmap(mBitmap, 270.0f);
            }
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return mBitmap;
        }
    }

    public Bitmap cropCenterBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        if (bitmap.getWidth() > bitmap.getHeight()) {
            return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - bitmap.getHeight()) / 2, 0, bitmap.getHeight(), bitmap.getHeight(), matrix, true);
        } else if (bitmap.getWidth() >= bitmap.getHeight()) {
            return bitmap;
        } else {
            return Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - bitmap.getWidth()) / 2, bitmap.getWidth(), bitmap.getWidth(), matrix, true);
        }
    }



}
