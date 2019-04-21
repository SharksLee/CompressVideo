package com.lsj.videocompress.compress;

import android.annotation.TargetApi;
import android.os.Looper;
import android.text.TextUtils;
import com.lsj.videocompress.compress.info.VideoFormatHelper;
import com.lsj.videocompress.compress.info.VideoSourceInfo;
import com.lsj.videocompress.compress.info.VideoUtils;
import com.lsj.videocompress.compress.video.InputSurface;
import com.lsj.videocompress.compress.video.OutputSurface;
import com.lsj.videocompress.compress.video.VideoCheckHelper;


import java.io.File;
import java.io.IOException;

/**
 * 视频压缩类
 * 调用 start()、{@link #cancel()}
 * POWERED BY lishoajie 2017-3-30
 */
@TargetApi(18)
public class CompressProcess extends Thread {
    private ProgressHelper mProgressHelper;
    private String mOutPutPath;
    private VideoSourceInfo mVideoSourceInfo;

    private ICompressListener mCompressListener;
    private boolean mIsRunning = false;
    private int mCurrentPercent = -1;
    private boolean mIsCancel;


    public CompressProcess(String inputPath, String outputPath) {
        this(inputPath, outputPath, VideoFormatHelper.RESOLUTION_450P);
    }

    public CompressProcess(String inputPath, String outputPath, @VideoFormatHelper.ResolutionType int resolutionType) {
        this.mOutPutPath = outputPath;

        Looper looper;

        if ((looper = Looper.myLooper()) != null) {
            this.mProgressHelper = new ProgressHelper(this, looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            this.mProgressHelper = new ProgressHelper(this, looper);
        } else {
            this.mProgressHelper = null;
        }
        mVideoSourceInfo = new VideoSourceInfo(inputPath, resolutionType);
    }


    @Override
    public void run() {
        mIsCancel = false;
        if (TextUtils.isEmpty(mVideoSourceInfo.getSourcePath())) return;

        if (TextUtils.isEmpty(mOutPutPath)) return;

        if (mVideoSourceInfo == null) return;

        if (mVideoSourceInfo.getWidth() <= 0 || mVideoSourceInfo.getHeight() <= 0)
            return;

        File file = new File(mOutPutPath);


        if (file.exists()) {
            //视频上传成功
            if (VideoUtils.isCompressedVideoComplete(mVideoSourceInfo.getSourcePath(), mOutPutPath)) {
                mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_UPDATE, 100, 0));
                mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_SUCCESS));
                return;
            }

            file.delete();
        }
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();

            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        super.run();
        InputSurface openGLHelper = null;
        OutputSurface surfaceHelper = null;
        DecodeHelper decodeHelper = null;
        EncoderHelper encoderHelper = null;
        try {
            if (this.mIsRunning) {
                return;
            }
            this.mIsRunning = true;

            mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_START));
            encoderHelper = new EncoderHelper();
            encoderHelper.a(mVideoSourceInfo);
            (openGLHelper = new InputSurface(encoderHelper.mMediaCodec.createInputSurface())).makeCurrent();
            (surfaceHelper = new OutputSurface()).crateProgram("#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 textureCoordinate;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, textureCoordinate);\n}\n");
            (decodeHelper = new DecodeHelper(surfaceHelper.getSurface(), mVideoSourceInfo)).initVideo();
            decodeHelper.start();
            encoderHelper.init(mOutPutPath);
            VideoCheckHelper var6 = new VideoCheckHelper("CompressProcess");

            while (!decodeHelper.isFinished() && mIsRunning && !mIsCancel) {
                decodeHelper.next();
                long mCurrentDuration;
                if ((mCurrentDuration = decodeHelper.getVideoDuration()) >= 0L && var6.compare(mCurrentDuration)) {
                    surfaceHelper.awaitNewImage();
                    surfaceHelper.drawFrame();
                    encoderHelper.writeMediaMuxer();
                    openGLHelper.setPresentationTime(mCurrentDuration * 1000L);
                    openGLHelper.swapBuffers();

                    if (mProgressHelper != null) {
                        int percent = (int) (100.0f * mCurrentDuration / mVideoSourceInfo.getDurationMicroSeconds());
                        if (percent != mCurrentPercent) {
                            mCurrentPercent = percent;
                            mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_UPDATE, percent, 0));
                        }
                    }
                }
            }

            decodeHelper.writeAllAudioData(encoderHelper.getMediaMuxer(), encoderHelper.getAudioIndex());

            if (mProgressHelper != null && mIsRunning) {
                if (mIsCancel) {
                    mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_CANCEL));
                } else {
                    mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_UPDATE, 100, 0));
                    mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_SUCCESS));
                }
            }

            mIsRunning = false;
        } catch (Exception e1) {
            e1.printStackTrace();
            if (mProgressHelper != null) {
                mProgressHelper.sendMessage(mProgressHelper.obtainMessage(ProgressHelper.CODE_FAIL));
            }
        } finally {
            try {
                if (surfaceHelper != null) {
                    surfaceHelper.release();
                }
                if (openGLHelper != null) {
                    openGLHelper.release();
                }
                if (encoderHelper != null) {
                    encoderHelper.release();
                }
                if (decodeHelper != null) {
                    decodeHelper.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCompressListener(ICompressListener compressListener) {
        mCompressListener = compressListener;
    }

    private void commandListener(ActionType actionType) {
        commandListener(actionType, 0);
    }

    private void commandListener(ActionType actionType, int param) {
        if (mCompressListener == null) return;

        switch (actionType) {
            case start:
                mCompressListener.onStart();
                break;
            case progress:
                mCompressListener.onProgress(param);
                break;
            case success:
                mCompressListener.onSuccess();
                break;
            case fail:
                mCompressListener.onFail();
                break;
            case cancel:
                mCompressListener.onCanceled();
                break;
        }
    }

    public void onStart() {
        commandListener(ActionType.start);
    }

    public void onProgress(int progress) {
        commandListener(ActionType.progress, progress);
    }

    public void onCompleted() {
        commandListener(ActionType.success);
    }

    public void onFail() {
        commandListener(ActionType.fail);
    }

    public void onCanceled() {
        commandListener(ActionType.cancel);
    }

    public void cancel() {
        mIsCancel = true;
    }

    public VideoSourceInfo getVideoInfo() {
        return mVideoSourceInfo;
    }

    private enum ActionType {
        start,
        progress,
        success,
        fail,
        cancel
    }
}
