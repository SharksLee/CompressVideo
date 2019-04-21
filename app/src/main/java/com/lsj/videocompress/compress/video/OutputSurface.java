package com.lsj.videocompress.compress.video;

/**
 * 功能：
 * 描述：
 * Created by lishoajie on 2017/3/24.
 */

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.view.Surface;


@TargetApi(17)
public final class OutputSurface implements OnFrameAvailableListener {
    private final Object mFrameSyncObject;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private boolean mFrameAvailable;
    private TextureRender mTextureRender;

    public OutputSurface() {
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mFrameSyncObject = new Object();
        this.mTextureRender = new TextureRender();
        this.mTextureRender.surfaceCreated();
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    public final void release() {
        if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.mEGLDisplay);
        }

        this.mSurface.release();
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public final Surface getSurface() {
        return this.mSurface;
    }

    public final void crateProgram(String var1) {
        this.mTextureRender.createProgram(var1);
    }

    public final void awaitNewImage() {
        final int TIMEOUT_MS = 10000;
        synchronized (mFrameSyncObject) {
            while (!mFrameAvailable) {
                try {
                    // Wait for onFrameAvailable() to signal us.  Use a timeout to avoid
                    // stalling the test if it doesn't arrive.
                    mFrameSyncObject.wait(TIMEOUT_MS);
                    if (!mFrameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException ie) {
                    // shouldn't happen
                    throw new RuntimeException(ie);
                }
            }
            mFrameAvailable = false;
        }
        // Latch the data.
        TextureRender.checkGlError("before updateTexImage");
        mSurfaceTexture.updateTexImage();
    }

    public final void drawFrame() {
        this.mTextureRender.drawFrame(this.mSurfaceTexture);
    }

    public final void onFrameAvailable(SurfaceTexture var1) {
        synchronized (this.mFrameSyncObject) {
            var1.getTimestamp();
            this.mFrameAvailable = true;
            this.mFrameSyncObject.notifyAll();
        }
    }
}
