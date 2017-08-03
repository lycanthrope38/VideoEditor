package com.freelancer.videoeditor.view.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.util.AsyncTaskLoader;
import com.freelancer.videoeditor.config.ConfigScreen;
import com.freelancer.videoeditor.util.HandlerTools;
import com.freelancer.videoeditor.util.IClose;

import java.io.File;
import java.util.ArrayList;
import timber.log.Timber;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public abstract class BaseGame extends SimpleBaseGameActivity {
    public AsyncTaskLoader asyncTaskLoader;
    public boolean isMultiTouch = false;
    public Camera mCamera;
    public Context mContext;
    public EngineOptions mEngineOptions;
    public HUD mHud;
    public VertexBufferObjectManager mVertexBufferObjectManager;
    public Scene mainScene;
    public FrameLayout mainView;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        this.asyncTaskLoader = new AsyncTaskLoader();
        this.mVertexBufferObjectManager = getVertexBufferObjectManager();
    }

    public EngineOptions onCreateEngineOptions() {
        iniConfigScreen();
        this.mHud = new HUD();
        Timber.d("ConfigScreen.SCREENWIDTH = " + ConfigScreen.SCREENWIDTH + " ConfigScreen.SCREENHEIGHT = " + ConfigScreen.SCREENHEIGHT);
        this.mCamera = new Camera(0.0f, 0.0f, (float) ConfigScreen.SCREENWIDTH, (float) ConfigScreen.SCREENHEIGHT);
        if (ConfigScreen.mRatioResolutionPolicy != null) {
            this.mEngineOptions = new EngineOptions(true, ConfigScreen.mScreenOrientation, ConfigScreen.mRatioResolutionPolicy, this.mCamera);
        } else {
            this.mEngineOptions = new EngineOptions(true, ConfigScreen.mScreenOrientation, new FillResolutionPolicy(), this.mCamera);
        }
        this.mEngineOptions.getAudioOptions().setNeedsSound(true);
        this.mEngineOptions.getAudioOptions().setNeedsMusic(true);
        this.mEngineOptions.getTouchOptions().setNeedsMultiTouch(this.isMultiTouch);
        this.mEngineOptions.getRenderOptions().setDithering(true);
        this.mEngineOptions.getRenderOptions().setMultiSampling(true);
        return this.mEngineOptions;
    }

    protected void onCreateResources() {
    }

    protected Scene onCreateScene() {
        this.mainScene = new Scene();
        this.mCamera.setHUD(this.mHud);
        return this.mainScene;
    }

    public void iniConfigScreen() {
        ConfigScreen.ini(this);
    }

    public Entity removeEntity(final Entity mEntity) {
        this.mEngine.runOnUpdateThread(new Runnable() {
            public void run() {
                BaseGame.this.remove(mEntity);
            }
        });
        return null;
    }

    public Entity removeEntity(final Entity mEntity, final IClose mIClose) {
        this.mEngine.runOnUpdateThread(new Runnable() {
            public void run() {
                BaseGame.this.remove(mEntity);
                if (mIClose != null) {
                    mIClose.onClose();
                }
            }
        });
        return null;
    }

    private Entity remove(Entity mEntity) {
        if (mEntity != null) {
            try {
                EngineLock engineLock = this.mEngine.getEngineLock();
                engineLock.lock();
                mEntity.clearEntityModifiers();
                mEntity.clearUpdateHandlers();
                mEntity.detachSelf();
                mEntity.dispose();
                engineLock.unlock();
            } catch (Exception e) {
                Timber.e("remove e = " + e.toString());
            }
        }
        return null;
    }

    public void clearEntity(final Sprite mSprite, final IClose mClose) {
        this.mEngine.runOnUpdateThread(new Runnable() {
            public void run() {
                if (mSprite != null) {
                    mSprite.clearEntityModifiers();
                    mSprite.setRotation(0.0f);
                    mSprite.setAlpha(HandlerTools.ROTATE_R);
                    mSprite.setScale(HandlerTools.ROTATE_R);
                    if (mClose != null) {
                        mClose.onClose();
                    }
                }
            }
        });
    }

    public void deleteAllChild(Entity mEntity) {
        for (int i = 0; i < mEntity.getChildCount(); i++) {
            removeEntity((Entity) mEntity.getChildByIndex(i));
        }
    }

    public boolean loadBg(String path, String name, int width, int height) {
        try {
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
            BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), width, height, TextureOptions.BILINEAR);
            ITextureRegion trBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, name, 0, 0);
            mBitmapTextureAtlas.load();
            this.mainScene.attachChild(new Sprite(((float) (ConfigScreen.SCREENWIDTH / 2)) - (trBg.getWidth() / 2.0f), ((float) (ConfigScreen.SCREENHEIGHT / 2)) - (trBg.getHeight() / 2.0f), trBg, getVertexBufferObjectManager()));
            return true;
        } catch (Exception e) {
            Timber.e("loadBg e = " + e.toString());
            return false;
        }
    }

    public ITextureRegion loadTextureRegion(String path, String name, int width, int height, ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlas) {
        try {
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
            BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(getTextureManager(), width, height, TextureOptions.BILINEAR);
            ITextureRegion trBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, name, 0, 0);
            mBitmapTextureAtlas.load();
            mListBitmapTextureAtlas.add(mBitmapTextureAtlas);
            return trBg;
        } catch (Exception e) {
            Timber.e("loadTextureRegion e = " + e.toString());
            return null;
        }
    }

    public ITextureRegion loadTextureRegion(String path, int width, int height, ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlas) {
        ITextureRegion iTextureRegion = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                BitmapTextureAtlas texture = new BitmapTextureAtlas(getTextureManager(), width, height, TextureOptions.BILINEAR);
                FileBitmapTextureAtlasSource fileTextureSource = FileBitmapTextureAtlasSource.create(f);
                getTextureManager().loadTexture(texture);
                mListBitmapTextureAtlas.add(texture);
                iTextureRegion = TextureRegionFactory.createFromSource(texture, fileTextureSource, 0, 0, false);
            }
        } catch (Exception e) {
            Timber.e("loadTextureRegion e = " + e.toString());
        }
        return iTextureRegion;
    }

    public TiledTextureRegion loadTiledTextureRegion(String path, String name, int width, int height, int colum, int row, ArrayList<BuildableBitmapTextureAtlas> mListBuildableBitmapTextureAtlas) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
        BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), width + 10, height + 10, TextureOptions.BILINEAR);
        TiledTextureRegion mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, name, colum, row);
        try {
            mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder(0, 0, 1));
            mBitmapTextureAtlas.load();
            mListBuildableBitmapTextureAtlas.add(mBitmapTextureAtlas);
            return mTiledTextureRegion;
        } catch (TextureAtlasBuilderException e) {
            Timber.e("loadTiledTextureRegion e = " + e.toString());
            return null;
        }
    }

    public void detachSelfOnScene(final Sprite mRectangularShape) {
        this.mEngine.runOnUpdateThread(new Runnable() {
            public void run() {
                if (!mRectangularShape.detachSelf()) {
                    Timber.e("detachSelf = false");
                }
            }
        });
    }

    public void detachSelfOnScene(final Text mRectangularShape) {
        this.mEngine.runOnUpdateThread(new Runnable() {
            public void run() {
                if (!mRectangularShape.detachSelf()) {
                    Timber.e("detachSelf = false");
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        if (this.mEngine.isRunning()) {
            this.mEngine.stop();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!this.mEngine.isRunning()) {
            this.mEngine.start();
        }
    }

    @Override
    protected void onSetContentView() {
        super.onSetContentView();
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
        View v = View.inflate(this, R.layout.baselibsandengine_activity_main, null);
        this.mainView = v.findViewById(R.id.mainView_base);
        this.mainView.addView(this.mRenderSurfaceView, 0);
        setContentView(v);
    }



    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public Context getmContext() {
        return this.mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Scene getMainScene() {
        return this.mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public EngineOptions getmEngineOptions() {
        return this.mEngineOptions;
    }

    public void setmEngineOptions(EngineOptions mEngineOptions) {
        this.mEngineOptions = mEngineOptions;
    }

    public Camera getmCamera() {
        return this.mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

    public HUD getmHud() {
        return this.mHud;
    }

    public void setmHud(HUD mHud) {
        this.mHud = mHud;
    }

    public boolean isMultiTouch() {
        return this.isMultiTouch;
    }

    public void setMultiTouch(boolean isMultiTouch) {
        this.isMultiTouch = isMultiTouch;
    }
}
