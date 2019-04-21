package com.lsj.videocompress.compress.info;

import android.annotation.TargetApi;
import android.media.MediaFormat;

/**
 * 功能：视频信息描述类
 * 描述：
 * Created by lishoajie on 2017/3/27.
 */
@TargetApi(16)
public class VideoSourceInfo {
    //调教方案：降低比特率的同时降低宽高，不容易出现马赛克
    public static final int DEFAULT_WIDTH = 450;
    public static final int DEFAULT_HEIGHT = 800;
    public static final int DEFAULT_BIT_RATE = VideoFormatHelper.getResolutionBitRate(VideoFormatHelper.RESOLUTION_450P);

    private VideoUtils.TrackResult mTrackResult;
    private String mSourcePath;
    private int mWidth;
    private int mHeight;
    private int mBitRate;
    private int mOrientationDegree;
    private long mDurationMicroSeconds;
    private MediaFormat mVideoMediaFormat;

    public VideoSourceInfo(String path) {
        mTrackResult = VideoUtils.getFirstVideoAndAudioTrack(path);
        mSourcePath = path;
        if (mTrackResult == null || mTrackResult.mVideoTrackFormat == null) {
            mWidth = DEFAULT_WIDTH;
            mHeight = DEFAULT_HEIGHT;
            mBitRate = DEFAULT_BIT_RATE;
            mOrientationDegree = 0;
            mDurationMicroSeconds = 1;
            return;
        }

        mVideoMediaFormat = mTrackResult.mVideoTrackFormat;
        mWidth = getInteger(MediaFormat.KEY_WIDTH, DEFAULT_WIDTH);
        mHeight = getInteger(MediaFormat.KEY_HEIGHT, DEFAULT_HEIGHT);
        mBitRate = getInteger(MediaFormat.KEY_BIT_RATE, DEFAULT_BIT_RATE);
        mDurationMicroSeconds = getLong(MediaFormat.KEY_DURATION, 1);
    }

    public VideoSourceInfo(String path, @VideoFormatHelper.ResolutionType int resolutionType) {
        this(path);
        mBitRate = VideoFormatHelper.getResolutionBitRate(resolutionType);

        Size size = VideoFormatHelper.refreshVideoInfo(mWidth, mHeight, resolutionType);

        if (size != null) {
            mWidth = size.getWidth();
            mHeight = size.getHeight();
        }

        mOrientationDegree = VideoUtils.getVideoRotation(path);
    }

    long getLong(String key, long defaultValue) {
        if (mVideoMediaFormat.containsKey(key)) {
            try {
                return mVideoMediaFormat.getLong(key);
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }

        } else {
            return defaultValue;
        }
    }

    int getInteger(String key, int defaultValue) {
        if (mVideoMediaFormat.containsKey(key)) {
            try {
                return mVideoMediaFormat.getInteger(key);
            } catch (Exception e) {
                e.printStackTrace();
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getBitRate() {
        return mBitRate;
    }

    public void setBitRate(int bitRate) {
        mBitRate = bitRate;
    }

    public int getOrientationDegree() {
        return mOrientationDegree;
    }

    public void setOrientationDegree(int orientationDegree) {
        this.mOrientationDegree = orientationDegree;
    }

    public long getDurationMicroSeconds() {
        return mDurationMicroSeconds;
    }

    public void setDurationMicroSeconds(long durationMicroSeconds) {
        mDurationMicroSeconds = durationMicroSeconds;
    }

    public long getDurationSeconds() {
        return mDurationMicroSeconds / 1000000;
    }

    public String getSourcePath() {
        return mSourcePath;
    }

    public VideoUtils.TrackResult getTrackResult() {
        return mTrackResult;
    }
}