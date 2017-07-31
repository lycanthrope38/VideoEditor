package com.freelancer.videoeditor.view.base;

import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.ArrayList;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class BaseObjectMainActivity {
    public ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlas = new ArrayList();
    public ArrayList<BuildableBitmapTextureAtlas> mListBuildableBitmapTextureAtlas = new ArrayList();
    public VertexBufferObjectManager mVertexBufferObjectManager;
    public PhotoEditorActivity mainActivity;

    public abstract void onAttach();

    public abstract void onLoadResource();

    public BaseObjectMainActivity(PhotoEditorActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mVertexBufferObjectManager = mainActivity.getVertexBufferObjectManager();
    }
}
