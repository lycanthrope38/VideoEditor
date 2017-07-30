package com.freelancer.videoeditor.view.photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.AppConst;
import com.freelancer.videoeditor.config.ShareConstants;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.IHandler;
import com.freelancer.videoeditor.util.OnCapture;
import com.freelancer.videoeditor.util.OnClickItemBaseList;
import com.freelancer.videoeditor.util.OnDialogConfirm;
import com.freelancer.videoeditor.util.OnManagerViewCenter;
import com.freelancer.videoeditor.util.OnSetSpriteForTools;
import com.freelancer.videoeditor.util.OnToolsBlur;
import com.freelancer.videoeditor.util.OnViewTools;
import com.freelancer.videoeditor.util.UtilLib;
import com.freelancer.videoeditor.util.view.ManagerRectanglePhoto;
import com.freelancer.videoeditor.util.view.RectangleBaseClipping;
import com.freelancer.videoeditor.view.base.BaseGame;

import java.io.File;
import java.util.ArrayList;

import timber.log.Timber;


public class PhotoEditorActivity extends BaseGame implements OnRequestPermissionsResultCallback, OnManagerViewCenter, OnSetSpriteForTools, OnToolsBlur, OnViewTools {
    public static final String KEY_LIST_PATH_PHOTO = "KEY_LIST_PATH_PHOTO";
    public static final String KEY_PHOTO_EDITOR_DATA = "KEY_PHOTO_EDITOR_DATA";
    public static final int REQUEST_CODE_CROP = 10001;
    int REQUEST_CODE_SAVE = R.styleable.AppCompatTheme_seekBarStyle;
    final String TAG = "PhotoEditorActivity";
    DialogInputText dialogInputText;
    HandlerTools handlerTools = new HandlerTools();
    public int indexItemPhotoCurrent = -1;
    boolean isFinishResult = false;
    public boolean isSave = true;
    private boolean isSortTab;
    public View item_photo_ItemPhotoCurrent = null;
    private ArrayList<String> listPathPhoto;
    RectangleBaseClipping mRectangleMain;
    public ManagerRectanglePhoto managerRectanglePhoto;
    public ManagerViewCenter managerViewCenter;
    MyBroadcast myBroadcast;
    MyScreenCapture myScreenCapture;
    OnCapture onCaptureChangePhoto;
    private OnClickItemBaseList onClickItemBaseListBackground = new OnClickItemBaseList() {
        public void OnItemClick(View view, int index) {
            PhotoEditorActivity.this.rectangleBackground.load("http://139.59.241.64/App/Background/background" + index + AppConst.FORMAT_FILLTER, null);
        }

        public void OnItemNoneClick() {
            PhotoEditorActivity.this.rectangleBackground.removePhoto();
            PhotoEditorActivity.this.managerRectanglePhoto.getmRectanglePhotoSeleted().resetPhoto();
        }
    };
    private OnClickItemBaseList onClickItemBaseListBorder = new OnClickItemBaseList() {
        public void OnItemClick(View view, int index) {
            PhotoEditorActivity.this.rectangleBorder.load("http://139.59.241.64/App/Frames/VideoMaker/frame" + index + AppConst.FORMAT_FRAME, null);
        }

        public void OnItemNoneClick() {
            PhotoEditorActivity.this.rectangleBorder.removePhoto();
        }
    };
    private OnClickItemBaseList onClickItemBaseListFilter = new OnClickItemBaseList() {
        public void OnItemClick(View view, int index) {
            PhotoEditorActivity.this.rectangleFilter.load("http://139.59.241.64/App/Filter/filter" + index + AppConst.FORMAT_FILLTER, new OnLoadImageFromURL() {
                public void onCompleted(Bitmap mBitmap) {
                    UtilLib.getInstance().handlerDoWork(new IHandler() {
                        public void doWork() {
                            PhotoEditorActivity.this.managerViewCenter.getListFilter().showLayoutSeekbar();
                        }
                    });
                }

                public void onFail() {
                }
            });
        }

        public void OnItemNoneClick() {
            UtilLib.getInstance().handlerDoWork(new IHandler() {
                public void doWork() {
                    PhotoEditorActivity.this.managerViewCenter.getListFilter().hideLayoutSeekbar();
                    PhotoEditorActivity.this.rectangleFilter.removePhoto();
                }
            });
        }
    };
    private OnViewBottom onViewBottom = new OnViewBottom() {
        public void OnBorder() {
            PhotoEditorActivity.this.managerViewCenter.showList(LIST_ITEM.BORDER);
            L.e("OnViewBottom", "BORDER");
        }

        public void OnBackground() {
            PhotoEditorActivity.this.managerViewCenter.showList(LIST_ITEM.BACKGROUND);
            L.e("OnViewBottom", "OnBackground");
        }

        public void OnSticker() {
            PhotoEditorActivity.this.goStickerScreen(PhotoEditorActivity.this.photoEditorData.getUrlApiSticker(), PhotoEditorActivity.this.photoEditorData.getKeyFullBannerAdmob(), PhotoEditorActivity.this.photoEditorData.getKeyNativeAdmob());
            L.e("OnViewBottom", "OnSticker");
        }

        public void OnFilter() {
            PhotoEditorActivity.this.managerViewCenter.showList(LIST_ITEM.FILTER);
            L.e("OnViewBottom", "OnFilter");
        }

        public void OnText() {
            L.e("OnViewBottom", "OnText");
            PhotoEditorActivity.this.managerViewCenter.setVisibleLayoutCenter(8, false);
            if (PhotoEditorActivity.this.dialogInputText == null) {
                PhotoEditorActivity.this.dialogInputText = new DialogInputText(PhotoEditorActivity.this, new IBitmap() {
                    public void onCompleted(Bitmap mBitmap) {
                        if (mBitmap != null) {
                            PhotoEditorActivity.this.rectangleTextAndSticker.addItem(mBitmap, 3);
                            PhotoEditorActivity.this.isSave = false;
                            PhotoEditorActivity.this.isSaveChange();
                        }
                    }
                });
                PhotoEditorActivity.this.dialogInputText.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        PhotoEditorActivity.this.viewBottom.setUnCheckButtonBottom();
                    }
                });
            }
            PhotoEditorActivity.this.dialogInputText.show();
        }

        public void UnCheck() {
            PhotoEditorActivity.this.managerViewCenter.unCheck();
            L.e("OnViewBottom", "UnCheck");
        }
    };
    private OnViewTop onViewTop = new OnViewTop() {
        public void OnBack() {
            PhotoEditorActivity.this.done();
        }

        public void OnSave() {
            PhotoEditorActivity.this.onCaptureChangePhoto = null;
            PhotoEditorActivity.this.onCaptureChangePhoto = new OnCapture() {
                public void onSuccess(String pathItemSave) {
                    Glide.with(PhotoEditorActivity.this).load(pathItemSave).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).override(200, 200).animate(libs.photoeditor.R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(libs.photoeditor.R.drawable.libphotoeditor_icon_default).fallback(libs.photoeditor.R.drawable.libphotoeditor_icon_default).placeholder(libs.photoeditor.R.drawable.libphotoeditor_icon_default).into((ImageView) PhotoEditorActivity.this.viewBottom.listPhoto.item_photo_old.findViewById(libs.photoeditor.R.id.photo));
                    PhotoEditorActivity.this.loadPhotoForRectanglePhoto(PhotoEditorActivity.this.item_photo_ItemPhotoCurrent, PhotoEditorActivity.this.indexItemPhotoCurrent, pathItemSave);
                }

                public void onFail() {
                }
            };
            PhotoEditorActivity.this.savePhoto(PhotoEditorActivity.this.pathItemPhotoCurrent);
        }

        public void OnDone() {
            PhotoEditorActivity.this.done();
        }
    };
    private String packageNameCrop;
    String pathFileSave = BuildConfig.FLAVOR;
    public String pathItemPhotoCurrent = BuildConfig.FLAVOR;
    private PhotoEditorData photoEditorData;
    private String prefixOutImage;
    RectangleBorder rectangleBackground;
    RectangleBorder rectangleBorder;
    public RectangleFilter rectangleFilter;
    public RectangleTextAndSticker rectangleTextAndSticker;
    private String rootPathSavePhoto;
    Sprite spriteTools;
    int typeObject = -1;
    ViewBottom viewBottom;
    ViewTop viewTop;

    public abstract class MyBroadcast extends BroadcastReceiver {
        public abstract void handleResult(Bitmap bitmap);

        public void onReceive(Context context, Intent intent) {
            String path = intent.getExtras().getString("PATH", BuildConfig.FLAVOR);
            if (path.length() != 0) {
                handleResult(BitmapFactory.decodeFile(path));
            }
        }
    }

    public void iniConfigScreen() {
        super.iniConfigScreen();
        ConfigScreen.mScreenOrientation = ScreenOrientation.PORTRAIT_FIXED;
    }

    public EngineOptions onCreateEngineOptions() {
        setMultiTouch(true);
        super.onCreateEngineOptions();
        return this.mEngineOptions;
    }

    protected void onSetContentView() {
        super.onSetContentView();
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
        View v = View.inflate(this, libs.photoeditor.R.layout.libphotoeditor_activity_main, null);
        this.mainView = (FrameLayout) v.findViewById(libs.photoeditor.R.id.mainView);
        this.mainView.addView(this.mRenderSurfaceView, 0);
        this.layoutAdomb = (LinearLayout) v.findViewById(libs.photoeditor.R.id.layoutAdomb);
        setContentView(v);
    }

    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        overridePendingTransition(libs.photoeditor.R.anim.abc_fade_in, libs.photoeditor.R.anim.abc_fade_out);
        SharePrefUtils.init(this);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            this.photoEditorData = (PhotoEditorData) mBundle.getSerializable(KEY_PHOTO_EDITOR_DATA);
            if (this.photoEditorData == null) {
                finish();
                L.e("PhotoEditorActivity", "photoEditorData = NULL");
                return;
            }
            this.listPathPhoto = this.photoEditorData.getListPathPhoto();
            this.rootPathSavePhoto = this.photoEditorData.getPathFolderSaveTemp();
            this.prefixOutImage = this.photoEditorData.getPrefixOutImage();
            this.packageNameCrop = this.photoEditorData.getPackageNameCrop();
            String keyBannerAdmob = this.photoEditorData.getKeyBannerAdmob();
            String keyFullBannerAdmob = this.photoEditorData.getKeyFullBannerAdmob();

            L.e("PhotoEditorActivity", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            L.e("PhotoEditorActivity", "rootPathSavePhoto = " + this.rootPathSavePhoto);
            L.e("PhotoEditorActivity", "prefixOutImage = " + this.prefixOutImage);
            L.e("PhotoEditorActivity", "NUMBER_START_IMAGE = " + this.photoEditorData.getNumerStartImage());
            L.e("PhotoEditorActivity", "FORMAT_OUT_IMAGE = " + this.photoEditorData.getFormatOutImage());
            L.e("PhotoEditorActivity", "WIDTH_IMAGE = " + this.photoEditorData.getWidthImage());
            L.e("PhotoEditorActivity", "HEIGHT_IMAGE = " + this.photoEditorData.getHeightImage());
            L.e("PhotoEditorActivity", "ID_ADMOB_BANNER = " + keyBannerAdmob);
            L.e("PhotoEditorActivity", "ID_ADMOB_FULL_BANNER = " + keyFullBannerAdmob);
            L.e("PhotoEditorActivity", "ID_ADMOB_NATIVE_ADMOB = " + this.photoEditorData.getKeyNativeAdmob());
            L.e("PhotoEditorActivity", "URL_API_STICKER = " + this.photoEditorData.getUrlApiSticker());
            L.e("PhotoEditorActivity", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            if (this.listPathPhoto == null || this.rootPathSavePhoto.length() == 0 || this.prefixOutImage.length() == 0) {
                finish();
                return;
            } else if (this.listPathPhoto.size() == 0) {
                finish();
                return;
            } else {
                PH_BANNER = (int) ExtraUtils.convertDpToPixel(50.0f, this);
                if (!UtilLib.getInstance().haveNetworkConnection(this)) {
                    PH_BANNER = 0;
                }
                RelativeLayout layoutCenter = (RelativeLayout) this.mainView.findViewById(libs.photoeditor.R.id.layoutCenter);
                layoutCenter.getLayoutParams().height = ConfigScreen.SCREENWIDTH;
                RelativeLayout layoutContentCenter = (RelativeLayout) this.mainView.findViewById(libs.photoeditor.R.id.layoutContentCenter);
                layoutCenter.getLayoutParams().height = ConfigScreen.SCREENWIDTH;
                this.viewTop = new ViewTop(this, this.mainView);
                this.viewTop.setOnViewTop(this.onViewTop);
                this.isSave = true;
                isSaveChange();
                this.managerViewCenter = new ManagerViewCenter(this, layoutContentCenter);
                this.managerViewCenter.setOnClickItemBaseList(this.onClickItemBaseListBorder, this.onClickItemBaseListFilter, this.onClickItemBaseListBackground);
                this.managerViewCenter.setOnManagerViewCenter(this);
                this.managerViewCenter.listBlur.setOnToolsBlur(this);
                this.viewBottom = new ViewBottom(this, this.listPathPhoto);
                this.viewBottom.setOnViewBottom(this.onViewBottom);
                this.viewBottom.findID(this.mainView);
                return;
            }
        }
        finish();
        L.e("PhotoEditorActivity", "mBundle = NULL");
    }

    protected Scene onCreateScene() {
        super.onCreateScene();
        this.mainScene.setTouchAreaBindingOnActionDownEnabled(true);
        this.mainScene.setOnAreaTouchTraversalFrontToBack();
        this.mainScene.setBackground(new Background(0.0f, 0.0f, 0.0f));
        showStart();
        return this.mainScene;
    }

    void iniUI() {
        this.myScreenCapture = new MyScreenCapture();
        addRectangleMain();
        this.rectangleBackground = new RectangleBorder(this, 0.0f, 0.0f, this.mRectangleMain.getWidth(), this.mRectangleMain.getHeight(), this.mVertexBufferObjectManager);
        this.mRectangleMain.attachChild(this.rectangleBackground);
        this.managerRectanglePhoto = new ManagerRectanglePhoto(this);
        this.managerRectanglePhoto.onLoadResource();
        this.managerRectanglePhoto.addRectanglePhoto(this, this.mRectangleMain, 1);
        this.rectangleTextAndSticker = new RectangleTextAndSticker(this, 0.0f, 0.0f, this.mRectangleMain.getWidth(), this.mRectangleMain.getHeight(), this.mVertexBufferObjectManager);
        this.rectangleTextAndSticker.setOnSetSpriteForTools(this);
        this.rectangleTextAndSticker.onLoadResource();
        this.rectangleTextAndSticker.onAttach();
        this.mRectangleMain.attachChild(this.rectangleTextAndSticker);
        this.rectangleBorder = new RectangleBorder(this, 0.0f, 0.0f, this.mRectangleMain.getWidth(), this.mRectangleMain.getHeight(), this.mVertexBufferObjectManager);
        this.mRectangleMain.attachChild(this.rectangleBorder);
        this.rectangleFilter = new RectangleFilter(this, this.managerViewCenter.getListFilter(), 0.0f, 0.0f, this.mRectangleMain.getWidth(), this.mRectangleMain.getHeight(), this.mVertexBufferObjectManager);
        this.mRectangleMain.attachChild(this.rectangleFilter);
        this.managerViewCenter.getListFilter().setRectangleFilter(this.rectangleFilter);
    }

    public void showStart() {
        final IDoBackGround mIDoBackGround = new IDoBackGround() {
            public void onDoBackGround(boolean isCancelled) {
                PhotoEditorActivity.this.iniUI();
                PhotoEditorActivity.this.managerViewCenter.setVisibleLayoutCenter(8, false);
            }

            public void onCompleted() {
                UtilLib.getInstance().hideLoading();
                PhotoEditorActivity.this.managerRectanglePhoto.loadPhotoFromURI(Uri.fromFile(new File((String) PhotoEditorActivity.this.listPathPhoto.get(0))));
            }
        };
        runOnUiThread(new Runnable() {
            public void run() {
                UtilLib.getInstance().showLoading(PhotoEditorActivity.this);
                UtilLib.getInstance().doBackGround(mIDoBackGround);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("PhotoEditorActivity", "onActivityResult resultCode = " + resultCode);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0 || requestCode == 1) {
            this.managerRectanglePhoto.loadPhotoFromURI(data.getData());
        } else if (requestCode == REQUEST_CODE_CROP) {
            L.e("PhotoEditorActivity", "REQUEST_CODE_CROP = 10001");
            byte[] byteArray = data.getByteArrayExtra("BITMAP");
            L.e("PhotoEditorActivity", "byteArray = " + byteArray);
            this.managerRectanglePhoto.getmRectanglePhotoSeleted().reLoad(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        } else if (requestCode == R.styleable.AppCompatTheme_ratingBarStyleIndicator && data != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra(AppConstLibSticker.BUNDLE_KEY_STICKER_PATH));
            if (bitmap != null) {
                onStickerEmoticonClick(bitmap);
            }
        }
    }

    void addRectangleMain() {
        float pX = 0.0f;
        float pY = (float) ViewTop.PHEIGHT_TOP;
        float pW = (float) ConfigScreen.SCREENWIDTH;
        float pHView = (float) (((ConfigScreen.SCREENHEIGHT - ViewBottom.PHEIGHT_BOTTOM) - ViewTop.PHEIGHT_TOP) - PH_BANNER);
        float pH = (pW * 800.0f) / 800.0f;
        if (pH > pHView) {
            pH = pHView;
            pW = (pH * 800.0f) / 800.0f;
            pX = ((float) (ConfigScreen.SCREENWIDTH / 2)) - (pW / 2.0f);
        }
        this.mRectangleMain = new RectangleBorder(this, pX, pY, pW, pH, this.mVertexBufferObjectManager);
        this.mainScene.attachChild(this.mRectangleMain);
    }

    void caculatorRectangleCapture() {
        int pHeightCapture = (((ConfigScreen.SCREENHEIGHT - PH_BANNER) - ViewBottom.PHEIGHT_BOTTOM) - ViewTop.PHEIGHT_TOP) - ((((ConfigScreen.SCREENHEIGHT - PH_BANNER) - ViewBottom.PHEIGHT_BOTTOM) - ViewTop.PHEIGHT_TOP) - ((int) this.mRectangleMain.getHeight()));
        this.myScreenCapture.set((ConfigScreen.SCREENWIDTH - ((int) this.mRectangleMain.getWidth())) / 2, (ConfigScreen.SCREENHEIGHT - pHeightCapture) - ViewTop.PHEIGHT_TOP, (int) this.mRectangleMain.getWidth(), pHeightCapture);
    }

    public void savePhoto(String pathFile) {
        this.managerViewCenter.setVisibleLayoutCenter(8, false);
        this.viewBottom.setUnCheckButtonBottom();
        if (isPermissionAllow(this.REQUEST_CODE_SAVE, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            startSavePhoto();
        }
    }

    void startSavePhoto() {
        this.pathFileSave = this.rootPathSavePhoto + "/" + this.prefixOutImage + (this.photoEditorData.getNumerStartImage() + this.viewBottom.listPhoto.index_change_photo_old) + this.photoEditorData.getFormatOutImage();
        MyFile.ini(this);
        this.rectangleTextAndSticker.hideRectangBorderAndButtonDeleted();
        caculatorRectangleCapture();
        MyScreenCapture.SC_FORMAT_IMAGE_SAVE = FORMAT_IMAGE_SAVE.JPEG;
        MyScreenCapture.WIDTH_IMAGE = this.photoEditorData.getWidthImage();
        MyScreenCapture.HEIGHT_IMAGE = this.photoEditorData.getHeightImage();
        this.myScreenCapture.capture(this, this.pathFileSave, 0, new OnCapture() {
            public void onSuccess(final String pathFile) {
                PhotoEditorActivity.this.isSave = true;
                PhotoEditorActivity.this.isFinishResult = true;
                PhotoEditorActivity.this.listPathPhoto.set(PhotoEditorActivity.this.viewBottom.listPhoto.index_change_photo_old, PhotoEditorActivity.this.pathFileSave);
                Timber.e("PhotoEditorActivity", "listPathPhoto.get(0) = " + ((String) PhotoEditorActivity.this.listPathPhoto.get(0)));
                Timber.e("PhotoEditorActivity", "onSuccess = " + PhotoEditorActivity.this.pathFileSave);
                Timber.e("PhotoEditorActivity", "viewBottom.listPhoto.index_change_photo_old = " + PhotoEditorActivity.this.viewBottom.listPhoto.index_change_photo_old);
                UtilLib.getInstance().handlerDoWork(new IHandler() {
                    public void doWork() {
                        if (PhotoEditorActivity.this.onCaptureChangePhoto != null) {
                            PhotoEditorActivity.this.onCaptureChangePhoto.onSuccess(pathFile);
                        }
                    }
                });
                PhotoEditorActivity.this.isSaveChange();
            }

            public void onFail() {
                Timber.e("TAG", "savePhoto onFail");
                if (PhotoEditorActivity.this.onCaptureChangePhoto != null) {
                    PhotoEditorActivity.this.onCaptureChangePhoto.onFail();
                }
            }
        });
    }

    boolean isPermissionAllow(int requestCode, String permission) {
        if (AppUtils.isPermissionAllow(this, permission)) {
            return true;
        }
        AppUtils.requestPermission(this, permission, requestCode);
        return false;
    }

    public void onHideViewCenter() {
        this.viewBottom.setUnCheckButtonBottom();
    }

    public void onStickerEmoticonClick(Bitmap bitmap) {
        if (bitmap != null) {
            this.rectangleTextAndSticker.addItem(bitmap, 2);
            this.isSave = false;
            isSaveChange();
        }
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != this.REQUEST_CODE_SAVE) {
            return;
        }
        if (AppUtils.isPermissionAllow(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            startSavePhoto();
        } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
            showDenyDialog("android.permission.WRITE_EXTERNAL_STORAGE", this.REQUEST_CODE_SAVE);
        } else {
            openAppSettings();
        }
    }

    void showDenyDialog(final String permission, final int requestCode) {
        AppUtils.showDenyDialog(this, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.requestPermission(PhotoEditorActivity.this, permission, requestCode);
            }
        }, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    void openAppSettings() {
        AppUtils.showRememberDialog(this, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ExtraUtils.openAppSettings(PhotoEditorActivity.this, PhotoEditorActivity.this.getPackageName());
            }
        }, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void changePhoto(final View item_photo, final int index) {
        this.pathItemPhotoCurrent = (String) this.listPathPhoto.get(index);
        this.item_photo_ItemPhotoCurrent = item_photo;
        this.indexItemPhotoCurrent = index;
        String pathItemSave = (String) this.listPathPhoto.get(this.viewBottom.listPhoto.index_change_photo_old);
        if (this.isSave) {
            loadPhotoForRectanglePhoto(item_photo, index, this.pathItemPhotoCurrent);
            return;
        }
        this.onCaptureChangePhoto = new OnCapture() {
            public void onSuccess(String pathItemSave) {
                Glide.with(PhotoEditorActivity.this).load(pathItemSave).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).override(200, 200).animate(libs.photoeditor.R.anim.anim_fade_in).thumbnail(AppConst.ZOOM_MIN).error(libs.photoeditor.R.drawable.libphotoeditor_icon_default).fallback(libs.photoeditor.R.drawable.libphotoeditor_icon_default).placeholder(libs.photoeditor.R.drawable.libphotoeditor_icon_default).into((ImageView) PhotoEditorActivity.this.viewBottom.listPhoto.item_photo_old.findViewById(libs.photoeditor.R.id.photo));
                PhotoEditorActivity.this.loadPhotoForRectanglePhoto(item_photo, index, PhotoEditorActivity.this.pathItemPhotoCurrent);
            }

            public void onFail() {
            }
        };
        createDialogConfirm(item_photo, index, pathItemSave, this.pathItemPhotoCurrent);
    }

    void createDialogConfirm(View item_photo, int index, String pathItemSave, String pathItemClick) {
        final String str = pathItemSave;
        final View view = item_photo;
        final int i = index;
        final String str2 = pathItemClick;
        new DialogConfirm(this, new OnDialogConfirm() {
            public void OnYes() {
                PhotoEditorActivity.this.savePhoto(str);
            }

            public void OnNo() {
                PhotoEditorActivity.this.loadPhotoForRectanglePhoto(view, i, str2);
            }
        }).show();
    }

    void loadPhotoForRectanglePhoto(View item_photo, int index, String pathItemClick) {
        if (item_photo != null) {
            if (this.viewBottom.listPhoto.item_photo_old != null) {
                ((ImageView) this.viewBottom.listPhoto.item_photo_old.findViewById(libs.photoeditor.R.id.check)).setVisibility(8);
            }
            this.viewBottom.listPhoto.item_photo_old = item_photo;
            this.viewBottom.listPhoto.index_change_photo_old = index;
            this.pathItemPhotoCurrent = pathItemClick;
            L.e("PhotoEditorActivity", "loadPhotoForRectanglePhoto = " + pathItemClick);
            this.managerRectanglePhoto.loadPhotoFromURI(Uri.fromFile(new File(pathItemClick)));
            ((ImageView) this.viewBottom.listPhoto.item_photo_old.findViewById(libs.photoeditor.R.id.check)).setVisibility(0);
            this.managerViewCenter.setVisibleLayoutCenter(8, false);
            this.viewBottom.setUnCheckButtonBottom();
            this.managerViewCenter.removeItemSelected();
            this.managerViewCenter.getListFilter().hideLayoutSeekbar();
            this.rectangleFilter.removePhoto();
            this.rectangleBorder.removePhoto();
            this.rectangleBackground.removePhoto();
            this.rectangleTextAndSticker.removeAllSticker();
            this.isSave = true;
            isSaveChange();
        }
    }

    public void isSaveChange() {
        this.viewTop.isSaveChange(this.isSave);
    }

    public void onBackPressed() {
        if (this.managerViewCenter.isVisible()) {
            this.managerViewCenter.setVisibleLayoutCenter(8, true);
        } else {
            done();
        }
    }

    public void onResume() {
        super.onResume();
    }

    private void done() {
        if (UtilLib.getInstance().getRandomIndex(0, 2) == 0 && this.isFinishResult) {
            PhotoEditorActivity.this.resultData();
        } else if (this.isFinishResult) {
            resultData();
        } else if (this.isSave) {
            setResult(0);
            finish();
        } else {
            createDialogConfirm(this.item_photo_ItemPhotoCurrent, this.indexItemPhotoCurrent, this.pathItemPhotoCurrent, this.pathItemPhotoCurrent);
        }
    }

    private void resultData() {
        Timber.d("PhotoEditorActivity", "RESULT ACTIVITY: " + this.listPathPhoto);
        Intent intent = new Intent();
        intent.putStringArrayListExtra(KEY_LIST_PATH_PHOTO, this.listPathPhoto);
        setResult(-1, intent);
        finish();
    }

    private void goStickerScreen(String urlApiSticker, String keyFullBannerAmob, String keyNativeAdmob) {
        Intent intent = new Intent(this, StickerActivityLibSticker.class);
        intent.putExtra(AppConst.BUNDLE_KEY_IS_SORT_TAB, this.isSortTab);
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_ADMOB_APP_ID, this.ADMOB_APP_ID);
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_URL_STICKER, urlApiSticker);
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_FULL_BANNER_ADMOB, keyFullBannerAmob);
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_NATIVE_ADMOB, keyNativeAdmob);
        intent.putExtra(AppConstLibSticker.BUNDLE_KEY_COLOR_ITEMS, AppConst.STICKER_COLOR_DEFAULT);
        startActivityForResult(intent, R.styleable.AppCompatTheme_ratingBarStyleIndicator);
    }

    public void onCrop() {
        if (this.spriteTools == null) {
            return;
        }
        if (UtilLib.getInstance().appInstalledOrNot(this.packageNameCrop, this)) {
            checkPhotoCropVersion();
            return;
        }
        installPhotoCrop(true);
        Timber.e("PhotoEditorActivity", "Not install");
    }

    private void installPhotoCrop(boolean isShowAlert) {
        if (isShowAlert) {
            Builder builder = new Builder(this);
            builder.setTitle(libs.photoeditor.R.string.dialog_title_download_crop);
            builder.setMessage(libs.photoeditor.R.string.dialog_message_download_crop);
            builder.setPositiveButton(libs.photoeditor.R.string.dialog_confirm_text_btn_yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    UtilLib.getInstance().showDetailApp(PhotoEditorActivity.this, PhotoEditorActivity.this.packageNameCrop);
                }
            });
            builder.setNegativeButton(libs.photoeditor.R.string.dialog_confirm_text_btn_no, null);
            builder.create().show();
            return;
        }
        UtilLib.getInstance().showDetailApp((Activity) this, this.packageNameCrop);
    }

    private void checkPhotoCropVersion() {
        try {
            if (getPackageManager().getPackageInfo(this.packageNameCrop, 0).versionCode < this.photoEditorData.getCurrentCropVersion()) {
                T.show(libs.photoeditor.R.string.message_update_photo_crop, 1);
                installPhotoCrop(false);
                return;
            }
            nextCropActivity(this.managerRectanglePhoto.getmRectanglePhotoSeleted().getUriPathFile());
            Timber.e("PhotoEditorActivity", "nextCropActivity");
        } catch (NameNotFoundException e) {
            installPhotoCrop(true);
        }
    }

    void nextCropActivity(Uri mCropImageUri) {
        if (this.myBroadcast == null) {
            this.myBroadcast = new MyBroadcast() {
                public void handleResult(Bitmap bitmap) {
                    Timber.e("PhotoEditorActivity", "myBroadcast reload bitmap");
                    if (bitmap != null) {
                        PhotoEditorActivity.this.managerRectanglePhoto.getmRectanglePhotoSeleted().reLoad(bitmap);
                        PhotoEditorActivity.this.isSave = false;
                        PhotoEditorActivity.this.isSaveChange();
                        return;
                    }
                    Timber.e("PhotoEditorActivity", "Get bitmap null");
                }
            };
            IntentFilter intentFilter = new IntentFilter("libs.photoeditor.ui.activity.COMPLETED");
            intentFilter.setPriority(999);
            registerReceiver(this.myBroadcast, intentFilter);
        }
        Intent intent = new Intent(this.photoEditorData.getActionIntentFilterPhotoCrop());
        intent.putExtra(ShareConstants.MEDIA_URI, mCropImageUri);
        intent.putExtra("isOtherApp", true);
        intent.setFlags(268435456);
        sendBroadcast(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.myBroadcast != null) {
            unregisterReceiver(this.myBroadcast);
        }
    }

    public void onFlipV() {
        if (this.spriteTools != null) {
            boolean z;
            Sprite sprite = this.spriteTools;
            if (this.spriteTools.isFlippedHorizontal()) {
                z = false;
            } else {
                z = true;
            }
            sprite.setFlippedHorizontal(z);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onFlipH() {
        if (this.spriteTools != null) {
            boolean z;
            Sprite sprite = this.spriteTools;
            if (this.spriteTools.isFlippedVertical()) {
                z = false;
            } else {
                z = true;
            }
            sprite.setFlippedVertical(z);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onZoomIn(int action) {
        if (this.spriteTools != null) {
            this.handlerTools.zoom(this, this.spriteTools, this.typeObject, action, HandlerTools.ZOOM_IN);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onZoomOut(int action) {
        if (this.spriteTools != null) {
            this.handlerTools.zoom(this, this.spriteTools, this.typeObject, action, HandlerTools.ZOOM_OUT);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onRotateL(int action) {
        if (this.spriteTools != null) {
            this.handlerTools.rotate(this, this.spriteTools, this.typeObject, action, HandlerTools.ROTATE_L);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onRotateR(int action) {
        if (this.spriteTools != null) {
            this.handlerTools.rotate(this, this.spriteTools, this.typeObject, action, HandlerTools.ROTATE_R);
            this.isSave = false;
            isSaveChange();
        }
    }

    public void onShowHide(int visible) {
        if (visible == 0) {
            this.viewBottom.setVisibleLayoutBottom(8, true);
        } else {
            this.viewBottom.setVisibleLayoutBottom(0, true);
        }
    }

    public void onSetSpriteForTools(Sprite mSprite, int type) {
        this.spriteTools = mSprite;
        this.typeObject = type;
        this.viewBottom.viewTools.setVisibleLayoutTools(0, true, type);
    }

    public void onDeleteSprite() {
        this.viewBottom.viewTools.setVisibleLayoutTools(8, true, 1);
        this.spriteTools = null;
        this.isSave = false;
        isSaveChange();
    }

    public void OnBorderClick(int type) {
        L.e("BLUR", "OnBorderClick");
        this.managerRectanglePhoto.getmRectanglePhotoSeleted().OnBorderClick(type);
    }

    public void OnSeekBarChange(int progress) {
        L.e("BLUR", "OnBorderClick");
        this.managerRectanglePhoto.getmRectanglePhotoSeleted().resizeBorder(progress);
    }
}