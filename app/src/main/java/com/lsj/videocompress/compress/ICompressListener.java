package com.lsj.videocompress.compress;

/**
 * 功能：
 * 描述：
 * Created by lishoajie on 2017/3/25.
 */

public interface ICompressListener {
    void onStart();

    /**
     * @param percent 0~100
     */
    void onProgress(int percent);


    void onSuccess();

    void onFail();

    void onCanceled();
}
