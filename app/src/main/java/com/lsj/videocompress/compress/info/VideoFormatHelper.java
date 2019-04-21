package com.lsj.videocompress.compress.info;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 功能：视频格式帮助类
 * 描述：MediaCodec支持16:9格式的分辨率
 * Created by lishoajie on 2017/3/27.
 */

public class VideoFormatHelper {

    /**
     * 450*800
     */
    public static final int RESOLUTION_450P = 1;

    /**
     * 720*1280
     */
    public static final int RESOLUTION_720P = 2;
    /**
     * 1080*1920
     */
    public static final int RESOLUTION_1080P = 3;

    /**
     * 1440*2560
     */
    public static final int RESOLUTION_2K = 4;

    /**
     * @param videoWidth     宽度
     * @param videoHeight    高度
     * @param resolutionType 期望的分辨率
     * @return
     */
    public static Size refreshVideoInfo(int videoWidth, int videoHeight, @ResolutionType int resolutionType) {
        if (videoWidth <= 0 || videoHeight <= 0) return null;

        Size size = getResolutionSize(resolutionType);

        //当前分辨率比预期的分辨率低，则找一个当前最近的分辨率
        if (Math.min(videoWidth, videoHeight) < size.getShortValue()) {
            size = getClosedResolutionType(videoWidth, videoHeight);
        }
        size.swapByRatio(videoWidth, videoHeight);
        return size;
    }

    /**
     * 根据宽、高拿到距离最近的分辨率
     *
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static Size getClosedResolutionType(int width, int height) {
        int result = width * height;

        //找到差值最小的
        Size closedSize = getResolutionSize(RESOLUTION_450P);
        int closedDiff = -1;

        for (int i = 1; i <= 4; i++) {
            Size size = getResolutionSize(i);
            int curDiff = Math.abs(size.getPixelsSize() - result);

            if (closedDiff == -1 || curDiff < closedDiff) {
                closedSize = size;
                closedDiff = curDiff;
            }
        }

        return closedSize;
    }


    public static Size getResolutionSize(@ResolutionType int resolutionType) {
        switch (resolutionType) {
            case RESOLUTION_2K:
                return new Size(1440, 2560);
            case RESOLUTION_1080P:
                return new Size(1080, 1920);
            case RESOLUTION_720P:
                return new Size(720, 1080);
            default:
                return new Size(450, 800);
        }
    }

    public static int getResolutionBitRate(@ResolutionType int resolutionType) {
        switch (resolutionType) {
            case RESOLUTION_2K:
                return 50 * 100000;
            case RESOLUTION_1080P:
                return 30 * 100000;
            case RESOLUTION_720P:
                return 20 * 100000;
            default:
                return 15 * 100000;
        }
    }

    /**
     * 分辨率的类型
     */
    @IntDef({RESOLUTION_450P, RESOLUTION_720P, RESOLUTION_1080P, RESOLUTION_2K})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResolutionType {

    }
}
