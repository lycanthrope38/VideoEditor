package com.freelancer.videoeditor.util.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader.TileMode;
import android.opengl.GLES20;

import com.freelancer.videoeditor.view.photo.PhotoEditorActivity;

import java.util.ArrayList;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

public abstract class RectangleBaseClipping extends Rectangle {
    ArrayList<BitmapTextureAtlas> mListBitmapTextureAtlas = new ArrayList();
    ArrayList<BuildableBitmapTextureAtlas> mListBuildableBitmapTextureAtlas = new ArrayList();
    VertexBufferObjectManager mVertexBufferObjectManager;
    PhotoEditorActivity mainActivity;

    abstract void onAttach();

    abstract void onLoadResource();

    public RectangleBaseClipping(PhotoEditorActivity mainActivity, float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
        this.mainActivity = mainActivity;
        this.mVertexBufferObjectManager = mainActivity.getVertexBufferObjectManager();
    }

    public IBitmapTextureAtlasSource getAtlasBitmap(int width, int height, Bitmap pBitmap) {
        final Bitmap bitmap = pBitmap;
        final int i = width;
        final int i2 = height;
        return new BaseBitmapTextureAtlasSourceDecorator(new EmptyBitmapTextureAtlasSource(width, height)) {
            protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
                this.mPaint.setAntiAlias(true);
                this.mPaint.setDither(true);
                this.mPaint.setShader(new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT));
                pCanvas.drawRect(0.0f, 0.0f, (float) i, (float) i2, this.mPaint);
            }

            public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
                throw new DeepCopyNotSupportedException();
            }
        };
    }

    protected void onManagedDraw(GLState pGLState, Camera pCamera) {
        boolean wasScissorTestEnabled = pGLState.enableScissorTest();
        int surfaceHeight = pCamera.getSurfaceHeight();
        float[] lowerLeftSurfaceCoordinates = convertLocalToSceneCoordinates(0.0f, 0.0f);
        int lowerLeftX = Math.round(lowerLeftSurfaceCoordinates[0]);
        int lowerLeftY = surfaceHeight - Math.round(lowerLeftSurfaceCoordinates[1]);
        float[] upperLeftSurfaceCoordinates = convertLocalToSceneCoordinates(0.0f, this.mHeight);
        int upperLeftX = Math.round(upperLeftSurfaceCoordinates[0]);
        int upperLeftY = surfaceHeight - Math.round(upperLeftSurfaceCoordinates[1]);
        float[] upperRightSurfaceCoordinates = convertLocalToSceneCoordinates(this.mWidth, this.mHeight);
        int upperRightX = Math.round(upperRightSurfaceCoordinates[0]);
        int upperRightY = surfaceHeight - Math.round(upperRightSurfaceCoordinates[1]);
        float[] lowerRightSurfaceCoordinates = convertLocalToSceneCoordinates(this.mWidth, 0.0f);
        int lowerRightX = Math.round(lowerRightSurfaceCoordinates[0]);
        int lowerRightY = surfaceHeight - Math.round(lowerRightSurfaceCoordinates[1]);
        int minClippingX = min(lowerLeftX, upperLeftX, upperRightX, lowerRightX);
        int maxClippingX = max(lowerLeftX, upperLeftX, upperRightX, lowerRightX);
        int minClippingY = min(lowerLeftY, upperLeftY, upperRightY, lowerRightY);
        GLES20.glScissor(minClippingX, minClippingY, maxClippingX - minClippingX, max(lowerLeftY, upperLeftY, upperRightY, lowerRightY) - minClippingY);
        try {
            super.onManagedDraw(pGLState, pCamera);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        pGLState.setScissorTestEnabled(wasScissorTestEnabled);
    }

    public int min(int x, int y, int m, int n) {
        int min = x;
        if (y < min) {
            min = y;
        }
        if (m < min) {
            min = m;
        }
        if (n < min) {
            return n;
        }
        return min;
    }

    public int max(int x, int y, int m, int n) {
        int min = x;
        if (y > min) {
            min = y;
        }
        if (m > min) {
            min = m;
        }
        if (n > min) {
            return n;
        }
        return min;
    }

    public float max(float x, float y, float m, float n) {
        float min = x;
        if (y > min) {
            min = y;
        }
        if (m > min) {
            min = m;
        }
        if (n > min) {
            return n;
        }
        return min;
    }
}
