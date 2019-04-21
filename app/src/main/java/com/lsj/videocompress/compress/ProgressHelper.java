package com.lsj.videocompress.compress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 进度条提示
 */
final class ProgressHelper extends Handler {
    public static final int CODE_START = 3301;
    public static final int CODE_UPDATE = 3302;
    public static final int CODE_SUCCESS = 3303;
    public static final int CODE_FAIL = 3304;
    public static final int CODE_CANCEL = 3305;
    private final WeakReference<CompressProcess> mCompressManagerRef;

    public ProgressHelper(CompressProcess var1, Looper var3) {
        super(var3);
        this.mCompressManagerRef = new WeakReference<>(var1);
    }

    public final void handleMessage(Message var1) {
        CompressProcess compressProcess;
        if ((compressProcess = this.mCompressManagerRef.get()) == null) {
            return;

        }
        switch (var1.what) {
            case CODE_START:
                compressProcess.onStart();
                break;
            case CODE_UPDATE:
                compressProcess.onProgress(var1.arg1);
                break;
            case CODE_SUCCESS:
                compressProcess.onCompleted();
                break;
            case CODE_FAIL:
                compressProcess.onFail();
                break;
            case CODE_CANCEL:
                compressProcess.onCanceled();
                break;
        }

    }
}
