package com.lsj.videocompress.compress.video;

/**
 * 功能：
 * 描述：
 * Created by lishoajie on 2017/3/24.
 */


import android.util.Log;

public final class VideoCheckHelper {
    private final String mTag;
    private long mCurrent = -1L;

    public VideoCheckHelper(String tag) {
        this.mTag = tag;
    }

    public final boolean compare(long value) {
        if (value > this.mCurrent) {
            this.mCurrent = value;
            return true;
        } else {
            Log.w(this.mTag, "before encode video. when checking PTS is error. last Pts >= currentPTS. engore this frame.!!");
            return false;
        }
    }
}
