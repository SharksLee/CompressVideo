package com.lsj.videocompress.compress.video;

/**
 * 功能：
 * 描述：
 * Created by lishoajie on 2017/3/24.
 */

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.util.Log;
import android.view.Surface;

@TargetApi(18)
public final class InputSurface {
    private static final int EGL_RECORDABLE_ANDROID = 0x3142;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private Surface mSurface;

    public InputSurface(Surface surface) {
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        if (surface == null) {
            throw new NullPointerException();
        } else {
            this.mSurface = surface;
            this.mEGLDisplay = EGL14.eglGetDisplay(0);
            if (this.mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
                throw new RuntimeException("unable to get EGL14 display");
            } else {
                int[] version = new int[2];
                if (!EGL14.eglInitialize(this.mEGLDisplay, version, 0, version, 1)) {
                    this.mEGLDisplay = null;
                    throw new RuntimeException("unable to initialize EGL14");
                } else {
                    version = new int[]{EGL14.EGL_RED_SIZE, 8,
                            EGL14.EGL_GREEN_SIZE, 8,
                            EGL14.EGL_BLUE_SIZE, 8,
                            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                            EGL_RECORDABLE_ANDROID, 1,
                            EGL14.EGL_NONE};
                    EGLConfig[] var3 = new EGLConfig[1];
                    int[] var4 = new int[1];
                    if (!EGL14.eglChooseConfig(this.mEGLDisplay, version, 0, var3, 0, var3.length, var4, 0)) {
                        Log.e("InputSurface", "unable to find RGB888+recordable ES2 EGL config");
                    } else {
                        version = new int[]{EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                                EGL14.EGL_NONE};
                        this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, var3[0], EGL14.EGL_NO_CONTEXT, version, 0);
                        checkEglError("eglCreateContext");
                        if (this.mEGLContext == null) {
                            throw new RuntimeException("null context");
                        } else {
                            version = new int[]{EGL14.EGL_NONE};
                            this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, var3[0], this.mSurface, version, 0);
                            checkEglError("eglCreateWindowSurface");
                            if (this.mEGLSurface == null) {
                                Log.e("InputSurface", "surface was null");
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for EGL errors.
     */
    private void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
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
        this.mSurface = null;
    }

    public final void makeCurrent() {
        if (!EGL14.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public final boolean swapBuffers() {
        return EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
    }

    public final void setPresentationTime(long var1) {
        EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, var1);
    }
}
